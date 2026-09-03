import java.util.Scanner;

public class slay_69 {
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        String logo = " ____  _            __    ___\n"
                + "/ ___|| | __ _ _   _/ /_  / _ \\\n"
                + "\\___ \\| |/ _` | | | | '_ \\| (_) |\n"
                + " ___) | | (_| | |_| | (_) \\__, |\n"
                + "|____/|_|\\__,_|\\__, |\\___/  /_/ \n"
                + "               |___/            \n";

        String line = "____________________________________________________________";

        System.out.println("Hello from\n" + logo);
        System.out.println(line);
        System.out.println(" Hello! I'm slay_69");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (input.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                if (taskIndex >= 0 && taskIndex < taskCount) {
                    tasks[taskIndex].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[taskIndex]);
                }
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                if (taskIndex >= 0 && taskIndex < taskCount) {
                    tasks[taskIndex].markAsUndone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[taskIndex]);
                }
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                Task t = new Todo(description);
                tasks[taskCount] = t;
                taskCount++;

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + t);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ");
                Task t = new Deadline(parts[0], parts[1]);
                tasks[taskCount] = t;
                taskCount++;

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + t);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ");
                Task t = new Event(parts[0], parts[1], parts[2]);
                tasks[taskCount] = t;
                taskCount++;

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + t);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            }

            System.out.println(line);
        }

        scanner.close();
    }
}