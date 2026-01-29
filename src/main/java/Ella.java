import java.util.Scanner;

public class Ella {
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        String logo =
                "EEEEEE  L      L        AAA   \n"
              + "E       L      L       A   A  \n"
              + "EEEEE   L      L       AAAAA  \n"
              + "E       L      L       A   A  \n"
              + "EEEEEE  LLLLL  LLLLL   A   A  \n";
        System.out.println(logo);

        Scanner sc = new Scanner(System.in);
        TaskList taskList = new TaskList();

        Storage storage = new Storage();

        // Load saved tasks
        try {
            for (String line : storage.loadLines()) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task t = Task.fromStorageString(line);
                taskList.add(t);
            }
        } catch (Exception e) {
            printBox("Warning: Could not load saved tasks. Starting fresh.");
        }

        printBox("Hello! I'm Ella\nWhat can I do for you?");

        while (true) {
            if (!sc.hasNextLine()) {
                break; // for redirected input
            }
            String input = sc.nextLine().trim();

            try {
                String response = handleInput(input, taskList, storage);
                if (response != null && !response.isEmpty()) {
                    printBox(response);
                }
                if (input.equalsIgnoreCase("bye")) {
                    break;
                }
            } catch (EllaException e) {
                // your own wording is encouraged
                printBox("Oops! " + e.getMessage());
            }
        }

        sc.close();
    }

    private static String handleInput(String input, TaskList taskList, Storage storage) throws EllaException{
        if (input.isEmpty()) {
            throw new EllaException("Please type a command.");
        }

        if (input.equalsIgnoreCase("bye")) {
            return "Bye. Hope to see you again soon!";
        }

        if (input.equalsIgnoreCase("list")) {
            if (taskList.size() == 0) {
                return "(no tasks yet)";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the tasks in your list:\n");
            for (int i = 0; i < taskList.size(); i++) {
                sb.append(i + 1).append(".").append(taskList.get(i)).append("\n");
            }
            return sb.toString().trim();
        }

        if (input.toLowerCase().startsWith("delete")) {
            int idx = parseTaskNumber(input, "delete");
            Task removed = removeTaskAt(taskList, idx);
            save(storage, taskList);
            return "Noted. I've removed this task:\n"
                    + "  " + removed + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        if (input.toLowerCase().startsWith("mark")) {
            int idx = parseTaskNumber(input, "mark");
            Task t = taskAt(taskList, idx);
            t.markDone();
            save(storage, taskList);
            return "Nice! I've marked this task as done:\n  " + t;
        }

        if (input.toLowerCase().startsWith("unmark")) {
            int idx = parseTaskNumber(input, "unmark");
            Task t = taskAt(taskList, idx);
            t.markNotDone();
            save(storage, taskList);
            return "OK, I've marked this task as not done yet:\n  " + t;
        }

        if (input.toLowerCase().startsWith("todo")) {
            String desc = input.substring(4).trim();
            if (desc.isEmpty()) {
                // Required example: todo with empty description
                throw new EllaException("The description of a todo cannot be empty.");
            }
            Task t = new Todo(desc);
            taskList.add(t);
            save(storage, taskList); 
            return "Got it. I've added this task:\n"
                    + "  " + t + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        if (input.toLowerCase().startsWith("deadline")) {
            String rest = input.substring("deadline".length()).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new EllaException("Deadline needs a /by. Use: deadline <desc> /by <when>");
            }
            String desc = parts[0].trim();
            String by = parts[1].trim();
            if (desc.isEmpty()) {
                throw new EllaException("The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new EllaException("The /by part of a deadline cannot be empty.");
            }
            Task t = new Deadline(desc, by);
            taskList.add(t);
            save(storage, taskList); 
            return "Got it. I've added this task:\n"
                    + "  " + t + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        if (input.toLowerCase().startsWith("event")) {
            String rest = input.substring("event".length()).trim();

            int fromIdx = rest.toLowerCase().indexOf(" /from ");
            int toIdx = rest.toLowerCase().indexOf(" /to ");

            if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
                throw new EllaException("Event needs /from and /to. Use: event <desc> /from <start> /to <end>");
            }

            String desc = rest.substring(0, fromIdx).trim();
            String from = rest.substring(fromIdx + " /from ".length(), toIdx).trim();
            String to = rest.substring(toIdx + " /to ".length()).trim();

            if (desc.isEmpty()) {
                throw new EllaException("The description of an event cannot be empty.");
            }
            if (from.isEmpty() || to.isEmpty()) {
                throw new EllaException("Event /from and /to values cannot be empty.");
            }

            Task t = new Event(desc, from, to);
            taskList.add(t);
            save(storage, taskList); 
            return "Got it. I've added this task:\n"
                    + "  " + t + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        throw new EllaException("I don't know what that means. \n\nTry: todo, deadline, event, list, mark, unmark, bye.");
    }

    private static int parseTaskNumber(String input, String command) throws EllaException {
        String rest = input.substring(command.length()).trim();
        if (rest.isEmpty()) {
            throw new EllaException("Please provide a task number. Use: " + command + " <number>");
        }
        try {
            return Integer.parseInt(rest);
        } catch (NumberFormatException e) {
            throw new EllaException("That doesn't look like a valid task number. Use: " + command + " <number>");
        }
    }

    private static Task taskAt(TaskList list, int oneBasedIndex) throws EllaException {
        if (list.size() == 0) {
            throw new EllaException("There are no tasks yet. Add one first (e.g., todo <description>).");
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range. Use a number between 1 and " + list.size() + ".");
        }
        return list.get(oneBasedIndex - 1);
    }

    private static Task removeTaskAt(TaskList list, int oneBasedIndex) throws EllaException {
        if (list.size() == 0) {
            throw new EllaException("There are no tasks to delete yet.");
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range. Use a number between 1 and " + list.size() + ".");
        }
        return list.remove(oneBasedIndex - 1);
    }

    private static void save(Storage storage, TaskList taskList) throws EllaException {
        try {
            storage.saveLines(taskList.toStorageLines());
        } catch (Exception e) {
            throw new EllaException("I couldn't save your tasks to disk.");
        }
    }



    private static void printBox(String message) {
        System.out.println(LINE);
        for (String line : message.split("\n")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }
}
