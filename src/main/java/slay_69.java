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
        String[] tasks = new String[MAX_TASKS];
        boolean[] isDone = new boolean[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (input.equals("list")) {
                // Level-2 & Level-3: Display stored tasks with done status
                for (int i = 0; i < taskCount; i++) {
                    String statusIcon = isDone[i] ? "[X]" : "[ ]";
                    System.out.println(" " + (i + 1) + "." + statusIcon + " " + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                // Level-3: Mark a task as done
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                if (taskIndex >= 0 && taskIndex < taskCount) {
                    isDone[taskIndex] = true;
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   [X] " + tasks[taskIndex]);
                }
            } else if (input.startsWith("unmark ")) {
                // Level-3: Mark a task as not done
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                if (taskIndex >= 0 && taskIndex < taskCount) {
                    isDone[taskIndex] = false;
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   [ ] " + tasks[taskIndex]);
                }
            } else {
                // Level-2: Add task to list
                tasks[taskCount] = input;
                isDone[taskCount] = false;
                taskCount++;
                System.out.println(" added: " + input);
            }

            System.out.println(line);
        }

        scanner.close();
    }
}