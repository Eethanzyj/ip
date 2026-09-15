package slay69;

import java.util.ArrayList;
import java.util.Scanner;

import slay69.task.Deadline;
import slay69.task.Event;
import slay69.task.Task;
import slay69.task.Todo;

/**
 * Runs the Slay69 chatbot and manages the user's tasks.
 */
public class Slay69 {
    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        boolean isRunning = true;

        while (isRunning && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            System.out.println(LINE);

            try {
                if (input.equals("bye")) {
                    System.out.println(" Bye. Better do your work.");
                    isRunning = false;
                } else {
                    executeCommand(input, tasks);
                }
            } catch (Slay69Exception e) {
                System.out.println(" HUHHH!!! " + e.getMessage());
            }

            System.out.println(LINE);
        }

        scanner.close();
    }

    /**
     * Executes one command, updating the task list when needed.
     *
     * @throws Slay69Exception if the command or its arguments are invalid
     */
    private static void executeCommand(String input, ArrayList<Task> tasks)
            throws Slay69Exception {
        if (input.isEmpty()) {
            throw new Slay69Exception("Please enter a command.");
        }

        String[] inputParts = input.split("\\s+", 2);
        String command = inputParts[0];
        String arguments = inputParts.length > 1
                ? inputParts[1].trim()
                : "";

        switch (command) {
        case "list":
            requireNoArguments(command, arguments);
            printTasks(tasks);
            break;
        case "mark":
            updateTask(arguments, tasks, true);
            break;
        case "unmark":
            updateTask(arguments, tasks, false);
            break;
        case "delete":
            deleteTask(arguments, tasks);
            break;
        case "todo":
            addTask(createTodo(arguments), tasks);
            break;
        case "deadline":
            addTask(createDeadline(arguments), tasks);
            break;
        case "event":
            addTask(createEvent(arguments), tasks);
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
    private static void updateTask(String arguments, ArrayList<Task> tasks,
                                   boolean shouldBeDone)
            throws Slay69Exception {
        String command = shouldBeDone ? "mark" : "unmark";
        int taskIndex = parseTaskIndex(arguments, tasks.size(), command);
        Task task = tasks.get(taskIndex);

        if (shouldBeDone) {
            task.markAsDone();
            System.out.println(" Slayyyy! I've marked this task as done:");
        } else {
            task.markAsUndone();
            System.out.println(" When you want do, I've marked this task as not done yet:");
        }

        System.out.println("   " + task);
    }

    /**
     * Removes the selected task and prints it together with the remaining count.
     * The list automatically shifts subsequent tasks to fill the gap.
     *
     * @throws Slay69Exception if the task number is missing, invalid, or out of range
     */
    private static void deleteTask(String arguments, ArrayList<Task> tasks)
            throws Slay69Exception {
        int taskIndex = parseTaskIndex(arguments, tasks.size(), "delete");
        Task removedTask = tasks.remove(taskIndex);

        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
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

    private static void addTask(Task task, ArrayList<Task> tasks) {
        tasks.add(task);

        System.out.println(" Kk. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size()
                + " tasks in the list.");
    }

    private static void printTasks(ArrayList<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    private static void requireNoArguments(String command, String arguments)
            throws Slay69Exception {
        if (!arguments.isEmpty()) {
            throw new Slay69Exception(
                    "The " + command + " command does not accept arguments.");
        }
    }

    private static void printGreeting() {
        String logo = " ____  _            __    ___\n"
                + "/ ___|| | __ _ _   _/ /_  / _ \\\n"
                + "\\___ \\| |/ _` | | | | '_ \\| (_) |\n"
                + " ___) | | (_| | |_| | (_) \\__, |\n"
                + "|____/|_|\\__,_|\\__, |\\___/  /_/ \n"
                + "               |___/            \n";

        System.out.println("Hello from\n" + logo);
        System.out.println(LINE);
        System.out.println(" Wassup! I'm slay_69");
        System.out.println(" What you want?");
        System.out.println(LINE);
    }
}
