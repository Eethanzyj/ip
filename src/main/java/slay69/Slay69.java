package slay69;

import java.io.IOException;

import slay69.storage.Storage;
import slay69.task.Deadline;
import slay69.task.Event;
import slay69.task.Task;
import slay69.task.TaskList;
import slay69.task.Todo;
import slay69.ui.Ui;

/**
 * Runs the Slay69 chatbot and manages the user's tasks.
 */
public class Slay69 {
    public static void main(String[] args) {
        try (Ui ui = new Ui()) {
            ui.showGreeting();

            Storage storage = new Storage();
            TaskList tasks;

            try {
                tasks = new TaskList(storage.load());
            } catch (IOException | Slay69Exception e) {
                ui.showLoadingError(e.getMessage());
                return;
            }

            boolean isRunning = true;

            while (isRunning && ui.hasNextCommand()) {
                String input = ui.readCommand();
                ui.showLine();

                try {
                    if (input.equals("bye")) {
                        ui.showGoodbye();
                        isRunning = false;
                    } else {
                        executeCommand(input, tasks, ui);

                        // Save successful changes, including deletion.
                        if (!input.equals("list")) {
                            storage.save(tasks.getTasks());
                        }
                    }
                } catch (Slay69Exception e) {
                    ui.showError(e.getMessage());
                } catch (IOException e) {
                    ui.showSavingError(e.getMessage());
                }

                ui.showLine();
            }
        }
    }

    /**
     * Executes one command, updating the task list when needed.
     *
     * @throws Slay69Exception if the command or its arguments are invalid
     */
    private static void executeCommand(String input, TaskList tasks,
                                       Ui ui)
            throws Slay69Exception {
        if (input.isEmpty()) {
            throw new Slay69Exception("Please enter a command.");
        }

        if (input.contains("|")) {
            throw new Slay69Exception(
                    "Please avoid '|'; it is reserved for saving tasks.");
        }

        String[] inputParts = input.split("\\s+", 2);
        String command = inputParts[0];
        String arguments = inputParts.length > 1
                ? inputParts[1].trim()
                : "";

        switch (command) {
        case "list":
            requireNoArguments(command, arguments);
            ui.showTasks(tasks.getTasks());
            break;
        case "mark":
            updateTask(arguments, tasks, true, ui);
            break;
        case "unmark":
            updateTask(arguments, tasks, false, ui);
            break;
        case "delete":
            deleteTask(arguments, tasks, ui);
            break;
        case "todo":
            addTask(createTodo(arguments), tasks, ui);
            break;
        case "deadline":
            addTask(createDeadline(arguments), tasks, ui);
            break;
        case "event":
            addTask(createEvent(arguments), tasks, ui);
            break;
        case "bye":
            throw new Slay69Exception(
                    "The bye command don't have arguments okay?! Try: bye");
        default:
            throw new Slay69Exception(
                    "What is this command?! "
                            + "Try: todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    private static Todo createTodo(String description)
            throws Slay69Exception {
        if (description.isBlank()) {
            throw new Slay69Exception(
                    "A todo needs a description laaaa. Try: todo read book");
        }

        return new Todo(description);
    }

    private static Deadline createDeadline(String arguments)
            throws Slay69Exception {
        if (arguments.isBlank()) {
            throw new Slay69Exception(
                    "A deadline needs a description laaaa. "
                            + "Try: deadline return book /by Sunday");
        }

        String[] parts = arguments.split("/by", 2);
        if (parts.length < 2) {
            throw new Slay69Exception(
                    "Please laaaaa, a deadline needs '/by'. "
                            + "Try: deadline return book /by Sunday");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new Slay69Exception(
                    "Hello, a deadline needs a description before '/by'.");
        }

        if (by.isEmpty()) {
            throw new Slay69Exception(
                    "???, a deadline needs a date or time after '/by' right?!");
        }

        return new Deadline(description, by);
    }

    private static Event createEvent(String arguments)
            throws Slay69Exception {
        if (arguments.isBlank()) {
            throw new Slay69Exception(
                    "Okay lor, an event don't need a description hor. "
                            + "Try: event meeting /from Monday /to Tuesday");
        }

        String[] fromParts = arguments.split("/from", 2);
        if (fromParts.length < 2) {
            throw new Slay69Exception(
                    "Huh, An event needs '/from'. "
                            + "Try: event meeting /from Monday /to Tuesday");
        }

        String description = fromParts[0].trim();
        if (description.isEmpty()) {
            throw new Slay69Exception(
                    "Haiz, An event needs a description before '/from'.");
        }

        String[] toParts = fromParts[1].split("/to", 2);
        if (toParts.length < 2) {
            throw new Slay69Exception(
                    "Bruh, An event needs '/to'. "
                            + "Try: event meeting /from Monday /to Tuesday");
        }

        String from = toParts[0].trim();
        String to = toParts[1].trim();

        if (from.isEmpty()) {
            throw new Slay69Exception(
                    "Srsly, an event needs a start time after '/from'.");
        }

        if (to.isEmpty()) {
            throw new Slay69Exception(
                    "Walao eh, an event needs an end time after '/to'.");
        }

        return new Event(description, from, to);
    }

    /**
     * Changes the completion status of the task selected by its displayed number.
     *
     * @throws Slay69Exception if the task number is missing, invalid, or out of range
     */
    private static void updateTask(String arguments, TaskList tasks,
                                   boolean shouldBeDone, Ui ui)
            throws Slay69Exception {
        String command = shouldBeDone ? "mark" : "unmark";
        int taskIndex = parseTaskIndex(arguments, tasks.size(), command);
        Task task = tasks.get(taskIndex);

        if (shouldBeDone) {
            task.markAsDone();
        } else {
            task.markAsUndone();
        }

        ui.showTaskStatusChanged(task, shouldBeDone);
    }

    /**
     * Removes the selected task and prints it together with the remaining count.
     * The list automatically shifts subsequent tasks to fill the gap.
     *
     * @throws Slay69Exception if the task number is missing, invalid, or out of range
     */
    private static void deleteTask(String arguments, TaskList tasks,
                                   Ui ui)
            throws Slay69Exception {
        int taskIndex = parseTaskIndex(arguments, tasks.size(), "delete");
        Task removedTask = tasks.delete(taskIndex);

        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Converts a user-visible task number into a valid zero-based list index.
     *
     * @throws Slay69Exception if the number is missing, invalid, or out of range
     */
    private static int parseTaskIndex(String arguments, int taskCount,
                                      String command)
            throws Slay69Exception {
        if (arguments.isBlank()) {
            throw new Slay69Exception(
                    "Please provide a task number. Try: " + command + " 1");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new Slay69Exception(
                    "'" + arguments + "' is not a valid task number.");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new Slay69Exception(
                    "Task " + taskNumber + " does not exist. "
                            + "You currently have " + taskCount + " tasks.");
        }

        return taskIndex;
    }

    private static void addTask(Task task, TaskList tasks, Ui ui) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void requireNoArguments(String command, String arguments)
            throws Slay69Exception {
        if (!arguments.isEmpty()) {
            throw new Slay69Exception(
                    "The " + command + " command does not accept arguments.");
        }
    }

}
