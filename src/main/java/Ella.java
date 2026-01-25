import java.util.Scanner;

public class Ella {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {

        String logo =
                "███████ ██      ██       █████  \n"
              + "██      ██      ██      ██   ██ \n"
              + "█████   ██      ██      ███████ \n"
              + "██      ██      ██      ██   ██ \n"
              + "███████ ███████ ███████ ██   ██ \n";
        System.out.println(logo);

        Scanner sc = new Scanner(System.in);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        printBox("Hello! I'm Ella\nWhat can I do for you?");

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                printBox("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equalsIgnoreCase("list")) {
                if (taskCount == 0) {
                    printBox("(no tasks yet)");
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Here are the tasks in your list:\n");
                    for (int i = 0; i < taskCount; i++) {
                        sb.append(i + 1).append(".").append(tasks[i]).append("\n");
                    }
                    printBox(sb.toString().trim());
                }
                continue;
            }

            if (input.toLowerCase().startsWith("mark ")) {
                Integer idx = parseIndex(input, "mark");
                if (idx == null) {
                    printBox("Please use: mark <task number>");
                    continue;
                }
                if (idx < 1 || idx > taskCount) {
                    printBox("Task number out of range.");
                    continue;
                }

                Task t = tasks[idx - 1];
                t.markDone();
                printBox("Nice! I've marked this task as done:\n  " + t);
                continue;
            }

            if (input.toLowerCase().startsWith("unmark ")) {
                Integer idx = parseIndex(input, "unmark");
                if (idx == null) {
                    printBox("Please use: unmark <task number>");
                    continue;
                }
                if (idx < 1 || idx > taskCount) {
                    printBox("Task number out of range.");
                    continue;
                }

                Task t = tasks[idx - 1];
                t.markNotDone();
                printBox("OK, I've marked this task as not done yet:\n  " + t);
                continue;
            }

            // Otherwise: treat as "add task"
            if (taskCount >= MAX_TASKS) {
                printBox("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
                continue;
            }

            tasks[taskCount] = new Task(input);
            taskCount++;
            printBox("added: " + input);
        }

        sc.close();
    }

    private static void printBox(String message) {
        System.out.println(LINE);
        for (String line : message.split("\n")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }

    private static Integer parseIndex(String input, String commandWord) {
        String rest = input.substring(commandWord.length()).trim(); // after "mark"/"unmark"
        if (rest.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(rest);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Extension A-Classes: use a class to represent a task
    static class Task {
        private final String description;
        private boolean isDone;

        Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        void markDone() {
            this.isDone = true;
        }

        void markNotDone() {
            this.isDone = false;
        }

        @Override
        public String toString() {
            return "[" + (isDone ? "X" : " ") + "] " + description;
        }
    }
}
