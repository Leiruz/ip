package ella;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String USAGE_FIND = "Use: find <keyword>  (e.g., find report)";
    private static final String USAGE_MARK = "Use: mark <number>  (e.g., mark 2)";
    private static final String USAGE_UNMARK = "Use: unmark <number>  (e.g., unmark 2)";
    private static final String USAGE_DELETE = "Use: delete <number>  (e.g., delete 3)";
    private static final String USAGE_TODO = "Use: todo <description>  (e.g., todo read book)";
    private static final String USAGE_DEADLINE =
            "Use: deadline <desc> /by <date>\nExample: deadline submit report /by 2019-10-15";
    private static final String USAGE_EVENT =
            "Use: event <desc> /from <start> /to <end>\nExample: event project meeting /from Mon 2pm /to Mon 4pm";

    // Flexible delimiter patterns (case-insensitive, variable spacing)
    private static final Pattern DEADLINE_PATTERN =
            Pattern.compile("(?i)^(.*)\\s+/by\\s+(.*)$");
    private static final Pattern EVENT_PATTERN =
            Pattern.compile("(?i)^(.*)\\s+/from\\s+(.*)\\s+/to\\s+(.*)$");

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
        CommandParts parts = splitCommand(trimmed);

        switch (parts.command) {
        case "bye":
            return "👋 Bye! I’ll be here when you’re ready to be productive again.";
        case "list":
            return listTasks(taskList);
        case "find":
            return handleFind(taskList, parts.args);
        case "delete":
            return handleDelete(taskList, storage, parts.args);
        case "mark":
            return handleMark(taskList, storage, parts.args);
        case "unmark":
            return handleUnmark(taskList, storage, parts.args);
        case "todo":
            return handleTodo(taskList, storage, parts.args);
        case "deadline":
            return handleDeadline(taskList, storage, parts.args);
        case "event":
            return handleEvent(taskList, storage, parts.args);
        default:
            throw new EllaException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    private static CommandParts splitCommand(String trimmedInput) {
        String[] tokens = trimmedInput.split("\\s+", 2);
        String cmd = tokens[0].toLowerCase();
        String args = tokens.length == 2 ? tokens[1].trim() : "";
        return new CommandParts(cmd, args);
    }

    private static String handleFind(TaskList taskList, String args) throws EllaException {
        if (args.isEmpty()) {
            throw new EllaException(USAGE_FIND);
        }
        return findTasks(taskList, args);
    }

    private static String handleDelete(TaskList taskList, Storage storage, String args) throws EllaException {
        int idx = parseStrictOneBasedIndex(args, USAGE_DELETE);
        Task removed = removeTaskAt(taskList, idx);
        save(storage, taskList);
        return "🧹 Poof! Removed this task:\n"
                + "  " + removed + "\n"
                + "Now you have " + taskList.size() + " tasks left.";
    }

    private static String handleMark(TaskList taskList, Storage storage, String args) throws EllaException {
        int idx = parseStrictOneBasedIndex(args, USAGE_MARK);
        Task t = taskAt(taskList, idx);
        t.markDone();
        save(storage, taskList);
        return "✅ Nice. Marked as done:\n  " + t;
    }

    private static String handleUnmark(TaskList taskList, Storage storage, String args) throws EllaException {
        int idx = parseStrictOneBasedIndex(args, USAGE_UNMARK);
        Task t = taskAt(taskList, idx);
        t.markNotDone();
        save(storage, taskList);
        return "↩️ Alright, back to “in progress”:\n  " + t;
    }

    private static String handleTodo(TaskList taskList, Storage storage, String args) throws EllaException {
        if (args.isEmpty()) {
            throw new EllaException("A todo needs a description 😅\n" + USAGE_TODO);
        }
        Task t = new Todo(args);
        taskList.add(t);
        save(storage, taskList);
        return buildAddResponse(taskList, t);
    }

    private static String handleDeadline(TaskList taskList, Storage storage, String args) throws EllaException {
        if (args.isEmpty()) {
            throw new EllaException(USAGE_DEADLINE);
        }

        Matcher m = DEADLINE_PATTERN.matcher(args);
        if (!m.matches()) {
            throw new EllaException("Deadline needs `/by` 😅\n" + USAGE_DEADLINE);
        }

        String desc = m.group(1).trim();
        String by = m.group(2).trim();

        if (desc.isEmpty()) {
            throw new EllaException("A deadline needs a description 😅\n" + USAGE_DEADLINE);
        }
        if (by.isEmpty()) {
            throw new EllaException("A deadline needs a /by date 😅\n" + USAGE_DEADLINE);
        }

        Task t = new Deadline(desc, by);
        taskList.add(t);
        save(storage, taskList);
        return buildAddResponse(taskList, t);
    }

    private static String handleEvent(TaskList taskList, Storage storage, String args) throws EllaException {
        if (args.isEmpty()) {
            throw new EllaException(USAGE_EVENT);
        }

        Matcher m = EVENT_PATTERN.matcher(args);
        if (!m.matches()) {
            throw new EllaException("Event needs `/from` and `/to` 😅\n" + USAGE_EVENT);
        }

        String desc = m.group(1).trim();
        String from = m.group(2).trim();
        String to = m.group(3).trim();

        if (desc.isEmpty()) {
            throw new EllaException("An event needs a description 😅\n" + USAGE_EVENT);
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new EllaException("Event /from and /to values cannot be empty 😅\n" + USAGE_EVENT);
        }

        Task t = new Event(desc, from, to);
        taskList.add(t);
        save(storage, taskList);
        return buildAddResponse(taskList, t);
    }

    private static String buildAddResponse(TaskList taskList, Task t) {
        return "✨ Got it! Added this task:\n"
                + "  " + t + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.";
    }

    private static String listTasks(TaskList taskList) {
        if (taskList.size() == 0) {
            return "📭 Your list is empty for now.\nAdd one with `todo ...`, `deadline ...`, or `event ...`.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("📋 Here’s what you’ve got:\n");
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(i + 1).append(". ").append(taskList.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private static String findTasks(TaskList taskList, String keyword) {
        String keyLower = keyword.toLowerCase();

        StringBuilder sb = new StringBuilder();
        sb.append("🔎 Matching tasks:\n");

        int matchCount = 0;
        for (int i = 0; i < taskList.size(); i++) {
            Task t = taskList.get(i);
            if (t.toString().toLowerCase().contains(keyLower)) {
                matchCount++;
                // show original index so user can mark/delete easily
                sb.append(matchCount).append(". (").append(i + 1).append(") ").append(t).append("\n");
            }
        }

        if (matchCount == 0) {
            return "🙈 I couldn’t find anything matching `" + keyword + "`.";
        }

        return sb.toString().trim();
    }

    /**
     * Parses a strict one-based index.
     * Rejects empty strings, non-numbers, and extra tokens (e.g., "2 abc").
     */
    private static int parseStrictOneBasedIndex(String args, String usage) throws EllaException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new EllaException("Please provide a task number.\n" + usage);
        }
        if (!trimmed.matches("\\d+")) {
            throw new EllaException("That doesn’t look like a valid task number 😅\n" + usage);
        }
        return Integer.parseInt(trimmed);
    }

    private static Task taskAt(TaskList list, int oneBasedIndex) throws EllaException {
        if (list.size() == 0) {
            throw new EllaException("There are no tasks yet.\nAdd one first (e.g., todo <description>).");
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range.\nUse a number between 1 and " + list.size() + ".");
        }
        return list.get(oneBasedIndex - 1);
    }

    private static Task removeTaskAt(TaskList list, int oneBasedIndex) throws EllaException {
        if (list.size() == 0) {
            throw new EllaException("There are no tasks to delete yet.");
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range.\nUse a number between 1 and " + list.size() + ".");
        }
        return list.remove(oneBasedIndex - 1);
    }

    private static void save(Storage storage, TaskList taskList) throws EllaException {
        try {
            storage.saveLines(taskList.toStorageLines());
        } catch (Exception e) {
            throw new EllaException("I couldn’t save your tasks to disk 😭");
        }
    }

    private static class CommandParts {
        private final String command;
        private final String args;

        private CommandParts(String command, String args) {
            this.command = command;
            this.args = args;
        }
    }
}
