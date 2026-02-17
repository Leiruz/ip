package ella;

/**
 * Parses user input and executes the corresponding operations on the task list.
 * Returns user-facing messages that should be shown by the UI.
 */
public class Parser {

    private static final String MESSAGE_EMPTY_COMMAND =
            "Give me something to work with 😄 (e.g., `list`, `todo ...`, `deadline ...`)";

    private static final String MESSAGE_UNKNOWN_COMMAND =
            "Hmm… I don't recognise that command 🤔\n\n"
                    + "Try one of these:\n"
                    + "• todo <desc>\n"
                    + "• deadline <desc> /by <date>\n"
                    + "• event <desc> /from <start> /to <end>\n"
                    + "• list\n"
                    + "• find <keyword>\n"
                    + "• mark <num> / unmark <num>\n"
                    + "• delete <num>\n"
                    + "• bye";

    /**
     * Handles a single user command.
     *
     * @param input The full user input line.
     * @param taskList The current task list.
     * @param storage The storage component used to persist task changes.
     * @return A message to be displayed to the user.
     * @throws EllaException If the command is invalid or cannot be processed.
     */
    public static String handle(String input, TaskList taskList, Storage storage) throws EllaException {
        if (input == null || input.trim().isEmpty()) {
            throw new EllaException(MESSAGE_EMPTY_COMMAND);
        }

        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("bye")) {
            return "👋 Bye! I’ll be here when you’re ready to be productive again.";
        }

        if (trimmed.equalsIgnoreCase("list")) {
            return listTasks(taskList);
        }

        String lower = trimmed.toLowerCase();

        if (lower.startsWith("find")) {
            String keyword = trimmed.substring(4).trim();
            if (keyword.isEmpty()) {
                throw new EllaException("Use: find <keyword>  (e.g., find report)");
            }
            return findTasks(taskList, keyword);
        }

        if (lower.startsWith("delete")) {
            int idx = parseTaskNumber(trimmed, "delete");
            Task removed = removeTaskAt(taskList, idx);
            save(storage, taskList);
            return "🧹 Poof! Removed this task:\n"
                    + "  " + removed + "\n"
                    + "Now you have " + taskList.size() + " tasks left.";
        }

        if (lower.startsWith("mark")) {
            int idx = parseTaskNumber(trimmed, "mark");
            Task t = taskAt(taskList, idx);
            t.markDone();
            save(storage, taskList);
            return "✅ Nice. Marked as done:\n  " + t;
        }

        if (lower.startsWith("unmark")) {
            int idx = parseTaskNumber(trimmed, "unmark");
            Task t = taskAt(taskList, idx);
            t.markNotDone();
            save(storage, taskList);
            return "↩️ Alright, back to “in progress”:\n  " + t;
        }

        if (lower.startsWith("todo")) {
            String desc = trimmed.substring(4).trim();
            if (desc.isEmpty()) {
                throw new EllaException("A todo needs a description 😅\nUse: todo <description>");
            }
            Task t = new Todo(desc);
            taskList.add(t);
            save(storage, taskList);
            return buildAddResponse(taskList, t);
        }

        if (lower.startsWith("deadline")) {
            String rest = trimmed.substring("deadline".length()).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new EllaException("Use: deadline <desc> /by <date>\n"
                        + "Example: deadline submit report /by 2019-10-15");
            }

            String desc = parts[0].trim();
            String by = parts[1].trim();

            if (desc.isEmpty()) {
                throw new EllaException("A deadline needs a description 😅");
            }
            if (by.isEmpty()) {
                throw new EllaException("A deadline needs a /by date 😅");
            }

            Task t = new Deadline(desc, by);
            taskList.add(t);
            save(storage, taskList);
            return buildAddResponse(taskList, t);
        }

        if (lower.startsWith("event")) {
            String rest = trimmed.substring("event".length()).trim();

            int fromIdx = rest.toLowerCase().indexOf(" /from ");
            int toIdx = rest.toLowerCase().indexOf(" /to ");

            if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
                throw new EllaException("Event needs /from and /to 😅\n"
                        + "Use: event <desc> /from <start> /to <end>");
            }

            String desc = rest.substring(0, fromIdx).trim();
            String from = rest.substring(fromIdx + " /from ".length(), toIdx).trim();
            String to = rest.substring(toIdx + " /to ".length()).trim();

            if (desc.isEmpty()) {
                throw new EllaException("An event needs a description 😅");
            }
            if (from.isEmpty() || to.isEmpty()) {
                throw new EllaException("Event /from and /to values cannot be empty 😅");
            }

            Task t = new Event(desc, from, to);
            taskList.add(t);
            save(storage, taskList);
            return buildAddResponse(taskList, t);
        }

        throw new EllaException(MESSAGE_UNKNOWN_COMMAND);
    }

    private static String buildAddResponse(TaskList taskList, Task t) {
        return "✨ Got it! Added this task:\n"
                + "  " + t + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.";
    }

    /**
     * Builds the listing string for all tasks.
     *
     * @param taskList The task list to display.
     * @return Formatted list output.
     */
    private static String listTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            return "📭 Your list is empty for now.\nAdd one with `todo ...`, `deadline ...`, or `event ...`.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("📋 Here’s what you’ve got:\n");
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(i + 1).append(".").append(taskList.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Finds tasks whose displayed text contains the given keyword (case-insensitive).
     *
     * @param taskList The task list to search.
     * @param keyword The keyword to search for.
     * @return Formatted search results.
     */
    private static String findTasks(TaskList taskList, String keyword) {
        String keyLower = keyword.toLowerCase();

        StringBuilder sb = new StringBuilder();
        sb.append("🔎 Matching tasks:\n");

        int matchCount = 0;
        for (int i = 0; i < taskList.size(); i++) {
            Task t = taskList.get(i);
            if (t.toString().toLowerCase().contains(keyLower)) {
                matchCount++;
                sb.append(matchCount).append(".").append(t).append("\n");
            }
        }

        if (matchCount == 0) {
            return "🙈 I couldn’t find anything matching `" + keyword + "`.";
        }

        return sb.toString().trim();
    }

    /**
     * Parses a one-based task number from a command like "mark 2".
     *
     * @param input Full input string.
     * @param command Command keyword (e.g., "mark").
     * @return One-based index the user provided.
     * @throws EllaException If the number is missing or invalid.
     */
    private static int parseTaskNumber(String input, String command) throws EllaException {
        String rest = input.substring(command.length()).trim();
        if (rest.isEmpty()) {
            throw new EllaException("Please provide a task number.\nUse: " + command + " <number>");
        }
        try {
            return Integer.parseInt(rest);
        } catch (NumberFormatException e) {
            throw new EllaException("That doesn’t look like a valid task number 😅\nUse: " + command + " <number>");
        }
    }

    /**
     * Gets a task by one-based index with range checking.
     *
     * @param list The task list.
     * @param oneBasedIndex The one-based task index.
     * @return The task at that index.
     * @throws EllaException If out of range or list empty.
     */
    private static Task taskAt(TaskList list, int oneBasedIndex) throws EllaException {
        if (list.size() == 0) {
            throw new EllaException("There are no tasks yet.\nAdd one first (e.g., todo <description>).");
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range.\nUse a number between 1 and " + list.size() + ".");
        }
        return list.get(oneBasedIndex - 1);
    }

    /**
     * Removes a task by one-based index with range checking.
     *
     * @param list The task list.
     * @param oneBasedIndex The one-based task index.
     * @return The removed task.
     * @throws EllaException If out of range or list empty.
     */
    private static Task removeTaskAt(TaskList list, int oneBasedIndex) throws EllaException {
        if (list.size() == 0) {
            throw new EllaException("There are no tasks to delete yet.");
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range.\nUse a number between 1 and " + list.size() + ".");
        }
        return list.remove(oneBasedIndex - 1);
    }

    /**
     * Persists the current task list to disk.
     *
     * @param storage Storage component.
     * @param taskList Task list to save.
     * @throws EllaException If saving fails.
     */
    private static void save(Storage storage, TaskList taskList) throws EllaException {
        try {
            storage.saveLines(taskList.toStorageLines());
        } catch (Exception e) {
            throw new EllaException("I couldn’t save your tasks to disk 😭");
        }
    }
}
