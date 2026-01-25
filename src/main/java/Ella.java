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

            // -------- Level 4 add commands --------

            if (input.toLowerCase().startsWith("todo ")) {
                String desc = input.substring(4).trim();
                if (desc.isEmpty()) {
                    printBox("The description of a todo cannot be empty.");
                    continue;
                }

                Task newTask = new Todo(desc);
                taskCount = addTask(tasks, taskCount, newTask);
                if (taskCount != -1) {
                    printAddMessage(newTask, taskCount);
                }
                continue;
            }

            if (input.toLowerCase().startsWith("deadline ")) {
                String rest = input.substring("deadline".length()).trim();
                String[] parts = rest.split(" /by ", 2);

                if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    printBox("Please use: deadline <description> /by <when>");
                    continue;
                }

                Task newTask = new Deadline(parts[0].trim(), parts[1].trim());
                taskCount = addTask(tasks, taskCount, newTask);
                if (taskCount != -1) {
                    printAddMessage(newTask, taskCount);
                }
                continue;
            }

            if (input.toLowerCase().startsWith("event ")) {
                String rest = input.substring("event".length()).trim();

                int fromIdx = rest.toLowerCase().indexOf(" /from ");
                int toIdx = rest.toLowerCase().indexOf(" /to ");

                if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
                    printBox("Please use: event <description> /from <start> /to <end>");
                    continue;
                }

                String desc = rest.substring(0, fromIdx).trim();
                String from = rest.substring(fromIdx + " /from ".length(), toIdx).trim();
                String to = rest.substring(toIdx + " /to ".length()).trim();

                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    printBox("Please use: event <description> /from <start> /to <end>");
                    continue;
                }

                Task newTask = new Event(desc, from, to);
                taskCount = addTask(tasks, taskCount, newTask);
                if (taskCount != -1) {
                    printAddMessage(newTask, taskCount);
                }
                continue;
            }

            printBox("I don't understand. Try:\n"
                    + "  todo <description>\n"
                    + "  deadline <description> /by <when>\n"
                    + "  event <description> /from <start> /to <end>\n"
                    + "  list | mark N | unmark N | bye");
        }

        sc.close();
    }

    private static int addTask(Task[] tasks, int taskCount, Task newTask) {
        if (taskCount >= MAX_TASKS) {
            printBox("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            return -1;
        }
        tasks[taskCount] = newTask;
        return taskCount + 1;
    }

    private static void printAddMessage(Task task, int taskCount) {
        printBox("Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + taskCount + " tasks in the list.");
    }

    private static void printBox(String message) {
        System.out.println(LINE);
        for (String line : message.split("\n")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }

    private static Integer parseIndex(String input, String commandWord) {
        String rest = input.substring(commandWord.length()).trim();
        if (rest.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(rest);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
