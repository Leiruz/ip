public class Parser {

    public static String handle(String input, TaskList taskList, Storage storage) throws EllaException {
        if (input == null || input.trim().isEmpty()) {
            throw new EllaException("Please type a command.");
        }

        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("bye")) {
            return "Bye. Hope to see you again soon!";
        }

        if (trimmed.equalsIgnoreCase("list")) {
            return listTasks(taskList);
        }

        String lower = trimmed.toLowerCase();

        if (lower.startsWith("delete")) {
            int idx = parseTaskNumber(trimmed, "delete");
            Task removed = removeTaskAt(taskList, idx);
            save(storage, taskList);
            return "Noted. I've removed this task:\n"
                    + "  " + removed + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        if (lower.startsWith("mark")) {
            int idx = parseTaskNumber(trimmed, "mark");
            Task t = taskAt(taskList, idx);
            t.markDone();
            save(storage, taskList);
            return "Nice! I've marked this task as done:\n  " + t;
        }

        if (lower.startsWith("unmark")) {
            int idx = parseTaskNumber(trimmed, "unmark");
            Task t = taskAt(taskList, idx);
            t.markNotDone();
            save(storage, taskList);
            return "OK, I've marked this task as not done yet:\n  " + t;
        }

        if (lower.startsWith("todo")) {
            String desc = trimmed.substring(4).trim();
            if (desc.isEmpty()) {
                throw new EllaException("The description of a todo cannot be empty.");
            }
            Task t = new Todo(desc);
            taskList.add(t);
            save(storage, taskList);
            return "Got it. I've added this task:\n"
                    + "  " + t + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        if (lower.startsWith("deadline")) {
            String rest = trimmed.substring("deadline".length()).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                // If you already did Level-8, this message is correct.
                throw new EllaException("Use: deadline <desc> /by yyyy-mm-dd (e.g., 2019-10-15)");
            }

            String desc = parts[0].trim();
            String by = parts[1].trim();

            if (desc.isEmpty()) {
                throw new EllaException("The description of a deadline cannot be empty.");
            }
            if (by.isEmpty()) {
                throw new EllaException("The /by part of a deadline cannot be empty.");
            }

            Task t = new Deadline(desc, by); // Deadline validates date format (Level-8)
            taskList.add(t);
            save(storage, taskList);
            return "Got it. I've added this task:\n"
                    + "  " + t + "\n"
                    + "Now you have " + taskList.size() + " tasks in the list.";
        }

        if (lower.startsWith("event")) {
            String rest = trimmed.substring("event".length()).trim();

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

        throw new EllaException("I don't know what that means.\n\nTry: todo, deadline, event, list, mark, unmark, bye.");
    }

    private static String listTasks(TaskList taskList) {
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
}
