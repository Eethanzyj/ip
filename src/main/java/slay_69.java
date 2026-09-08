import java.util.Scanner;

/**
 * Runs the Slay69 chatbot and manages the user's tasks.
 */
public class slay_69 {
    private static final int MAX_TASKS = 100;
    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        boolean isRunning = true;

        while (isRunning && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            System.out.println(LINE);

            try {
                if (input.equals("bye")) {
                    System.out.println(" Bye. Better do your work.");
                    isRunning = false;
                } else {
                    taskCount = executeCommand(input, tasks, taskCount);
                }
            } catch (Slay69Exception e) {
                System.out.println(" HUHHH!!! " + e.getMessage());
            }

            System.out.println(LINE);
        }

        scanner.close();
    }

    /**
     * Executes one command and returns the updated task count.
     *
     * @throws Slay69Exception if the command or its arguments are invalid
     */
    private static int executeCommand(String input, Task[] tasks, int taskCount)
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
            printTasks(tasks, taskCount);
            return taskCount;
        case "mark":
            updateTask(arguments, tasks, taskCount, true);
            return taskCount;
        case "unmark":
            updateTask(arguments, tasks, taskCount, false);
            return taskCount;
        case "todo":
            return addTask(createTodo(arguments), tasks, taskCount);
        case "deadline":
            return addTask(createDeadline(arguments), tasks, taskCount);
        case "event":
            return addTask(createEvent(arguments), tasks, taskCount);
        case "bye":
            throw new Slay69Exception(
                    "The bye command don't have arguments okay?! Try: bye");
        default:
            throw new Slay69Exception(
                    "What is this command?! "
                            + "Try: todo, deadline, event, list, mark, unmark, or bye.");
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
                    "Okay lor, an event don need a description hor. "
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

    private static void updateTask(String arguments, Task[] tasks,
                                   int taskCount, boolean shouldBeDone)
            throws Slay69Exception {
        String command = shouldBeDone ? "mark" : "unmark";
        int taskIndex = parseTaskIndex(arguments, taskCount, command);
        Task task = tasks[taskIndex];

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
     * Converts a user-visible task number into a valid zero-based array index.
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

    private static int addTask(Task task, Task[] tasks, int taskCount)
            throws Slay69Exception {
        if (taskCount >= MAX_TASKS) {
            throw new Slay69Exception(
                    "Your task list is full liao. It can hold at most "
                            + MAX_TASKS + " tasks.");
        }

        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;

        System.out.println(" Kk. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + updatedTaskCount
                + " tasks in the list.");

        return updatedTaskCount;
    }

    private static void printTasks(Task[] tasks, int taskCount) {
        System.out.println(" Here are the tasks in your list:");

        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
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
