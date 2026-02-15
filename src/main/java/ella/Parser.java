package ella;

import java.util.List;

/**
 * Parses user input and executes the corresponding operations on the task list.
 * Returns user-facing messages that should be shown by the UI.
 */
public class Parser {

    private static final String MESSAGE_EMPTY_COMMAND = "Please type a command.";
    private static final String MESSAGE_UNKNOWN_COMMAND =
            "I don't know what that means.\n\nTry: todo, deadline, event, list, find, mark, unmark, bye.";

    private static final String MESSAGE_NO_TASKS_YET =
            "There are no tasks yet. Add one first (e.g., todo <description>).";
    private static final String MESSAGE_NO_TASKS_TO_DELETE = "There are no tasks to delete yet.";
    private static final String MESSAGE_NO_MATCHES = "No matching tasks found.";
    private static final String MESSAGE_LIST_EMPTY = "(no tasks yet)";

    private static final String KEYWORD_BY = " /by ";
    private static final String KEYWORD_FROM = " /from ";
    private static final String KEYWORD_TO = " /to ";

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
        assert taskList != null : "taskList must not be null";
        assert storage != null : "storage must not be null";

        if (input == null || input.trim().isEmpty()) {
            throw new EllaException(MESSAGE_EMPTY_COMMAND);
        }

        String trimmed = input.trim();
        ParsedInput parsed = parseInput(trimmed);

        switch (parsed.command) {
        case "bye":
            return "Bye. Hope to see you again soon!";
        case "list":
            return listTasks(taskList);
        case "find":
            return handleFind(taskList, parsed.args);
        case "delete":
            return handleDelete(taskList, storage, parsed.args);
        case "mark":
            return handleMark(taskList, storage, parsed.args);
        case "unmark":
            return handleUnmark(taskList, storage, parsed.args);
        case "todo":
            return handleTodo(taskList, storage, parsed.args);
        case "deadline":
            return handleDeadline(taskList, storage, parsed.args);
        case "event":
            return handleEvent(taskList, storage, parsed.args);
        default:
            throw new EllaException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Splits the user input into a command word and the remaining arguments.
     */
    private static ParsedInput parseInput(String trimmed) {
        assert trimmed != null : "trimmed input must not be null";

        String[] parts = trimmed.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = (parts.length == 2) ? parts[1].trim() : "";
        return new ParsedInput(command, args);
    }

    private static String handleFind(TaskList taskList, String args) throws EllaException {
        assert taskList != null : "taskList must not be null";
        assert args != null : "args must not be null";

        if (args.isEmpty()) {
            throw new EllaException("Use: find <keyword>");
        }
        return findTasks(taskList, args);
    }

    private static String handleDelete(TaskList taskList, Storage storage, String args) throws EllaException {
        int oneBasedIndex = parseOneBasedIndex(args, "delete");

        Task removed = removeTaskAt(taskList, oneBasedIndex);
        save(storage, taskList);

        return "Noted. I've removed this task:\n"
                + "  " + removed + "\n"
                + "Now you have " + taskList.size() + " tasks in the list.";
    }

    private static String handleMark(TaskList taskList, Storage storage, String args) throws EllaException {
        int oneBasedIndex = parseOneBasedIndex(args, "mark");

        Task t = taskAt(taskList, oneBasedIndex);
        t.markDone();
        save(storage, taskList);

        return "Nice! I've marked this task as done:\n  " + t;
    }

    private static String handleUnmark(TaskList taskList, Storage storage, String args) throws EllaException {
        int oneBasedIndex = parseOneBasedIndex(args, "unmark");

        Task t = taskAt(taskList, oneBasedIndex);
        t.markNotDone();
        save(storage, taskList);

        return "OK, I've marked this task as not done yet:\n  " + t;
    }

    private static String handleTodo(TaskList taskList, Storage storage, String args) throws EllaException {
        if (args.isEmpty()) {
            throw new EllaException("The description of a todo cannot be empty.");
        }

        Task t = new Todo(args);
        taskList.add(t);
        save(storage, taskList);

        return buildAddResponse(taskList, t);
    }

    private static String handleDeadline(TaskList taskList, Storage storage, String args) throws EllaException {
        String[] parts = args.split(KEYWORD_BY, 2);
        if (parts.length < 2) {
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

        Task t = new Deadline(desc, by);
        taskList.add(t);
        save(storage, taskList);

        return buildAddResponse(taskList, t);
    }

    private static String handleEvent(TaskList taskList, Storage storage, String args) throws EllaException {
        String rest = args;

        int fromIdx = rest.toLowerCase().indexOf(KEYWORD_FROM);
        int toIdx = rest.toLowerCase().indexOf(KEYWORD_TO);

        if (fromIdx == -1 || toIdx == -1 || toIdx < fromIdx) {
            throw new EllaException("Event needs /from and /to. Use: event <desc> /from <start> /to <end>");
        }

        String desc = rest.substring(0, fromIdx).trim();
        String from = rest.substring(fromIdx + KEYWORD_FROM.length(), toIdx).trim();
        String to = rest.substring(toIdx + KEYWORD_TO.length()).trim();

        if (desc.isEmpty()) {
            throw new EllaException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new EllaException("Event /from and /to values cannot be empty.");
        }

        Task t = new Event(desc, from, to);
        taskList.add(t);
        save(storage, taskList);

        return buildAddResponse(taskList, t);
    }

    private static String buildAddResponse(TaskList taskList, Task t) {
        return "Got it. I've added this task:\n"
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
        assert taskList != null : "taskList must not be null";

        if (taskList.size() == 0) {
            return MESSAGE_LIST_EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
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
        assert taskList != null : "taskList must not be null";
        assert keyword != null : "keyword must not be null";

        // A-Streams is implemented inside TaskList.findMatching(...)
        List<Task> matches = taskList.findMatching(keyword);

        if (matches.isEmpty()) {
            return MESSAGE_NO_MATCHES;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");

        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(".").append(matches.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Parses a one-based task index from argument string.
     */
    private static int parseOneBasedIndex(String args, String command) throws EllaException {
        assert command != null && !command.isBlank() : "command must not be blank";
        assert args != null : "args must not be null";

        if (args.isEmpty()) {
            throw new EllaException("Please provide a task number. Use: " + command + " <number>");
        }

        try {
            return Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new EllaException("That doesn't look like a valid task number. Use: " + command + " <number>");
        }
    }

    /**
     * Gets a task by one-based index with range checking.
     */
    private static Task taskAt(TaskList list, int oneBasedIndex) throws EllaException {
        assert list != null : "TaskList must not be null";

        if (list.size() == 0) {
            throw new EllaException(MESSAGE_NO_TASKS_YET);
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range. Use a number between 1 and " + list.size() + ".");
        }

        int zeroBasedIndex = oneBasedIndex - 1;
        assert zeroBasedIndex >= 0 && zeroBasedIndex < list.size() : "index conversion should be within bounds";
        return list.get(zeroBasedIndex);
    }

    /**
     * Removes a task by one-based index with range checking.
     */
    private static Task removeTaskAt(TaskList list, int oneBasedIndex) throws EllaException {
        assert list != null : "TaskList must not be null";

        if (list.size() == 0) {
            throw new EllaException(MESSAGE_NO_TASKS_TO_DELETE);
        }
        if (oneBasedIndex < 1 || oneBasedIndex > list.size()) {
            throw new EllaException("Task number out of range. Use a number between 1 and " + list.size() + ".");
        }

        int zeroBasedIndex = oneBasedIndex - 1;
        assert zeroBasedIndex >= 0 && zeroBasedIndex < list.size() : "index conversion should be within bounds";
        return list.remove(zeroBasedIndex);
    }

    /**
     * Persists the current task list to disk.
     */
    private static void save(Storage storage, TaskList taskList) throws EllaException {
        assert storage != null : "storage must not be null";
        assert taskList != null : "taskList must not be null";

        try {
            storage.saveLines(taskList.toStorageLines());
        } catch (Exception e) {
            throw new EllaException("I couldn't save your tasks to disk.");
        }
    }

    /**
     * Simple value object for parsed input.
     */
    private static class ParsedInput {
        private final String command;
        private final String args;

        private ParsedInput(String command, String args) {
            this.command = command;
            this.args = args;
        }
    }
}
