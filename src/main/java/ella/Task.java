package ella;

/**
 * Represents a task with a description, completion status, and a task type.
 */
public class Task {
    protected final String description;
    protected boolean isDone;
    protected final TaskType type;

    /**
     * Creates a task with the given description and type.
     *
     * @param description Task description.
     * @param type Task type.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Returns the status icon string for this task.
     *
     * @return "[X]" if done, otherwise "[ ]".
     */
    protected String statusIcon() {
        return "[" + (isDone ? "X" : " ") + "]";
    }

    /**
     * Returns the user-facing string representation of the task.
     *
     * @return Formatted task string.
     */
    @Override
    public String toString() {
        return type.tag() + statusIcon() + " " + description;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markNotDone() {
        isDone = false;
    }

    /**
     * Converts this task into a storage-friendly string.
     *
     * @return Storage string representation.
     */
    public String toStorageString() {
        return type.shortCode() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Parses a task from a storage line.
     *
     * @param line A line from the save file.
     * @return The parsed task.
     * @throws EllaException If the line is corrupted or cannot be parsed.
     */
    public static Task fromStorageString(String line) throws EllaException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new EllaException("Corrupted save file line: " + line);
        }

        String typeCode = parts[0].trim();
        String doneCode = parts[1].trim();
        String desc = parts[2].trim();

        Task task;
        switch (typeCode) {
        case "T":
            task = new Todo(desc);
            break;
        case "D":
            if (parts.length < 4) {
                throw new EllaException("Corrupted deadline line: " + line);
            }
            task = new Deadline(desc, parts[3].trim());
            break;
        case "E":
            if (parts.length < 5) {
                throw new EllaException("Corrupted event line: " + line);
            }
            task = new Event(desc, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new EllaException("Unknown task type in save file: " + typeCode);
        }

        if (doneCode.equals("1")) {
            task.markDone();
        } else if (!doneCode.equals("0")) {
            throw new EllaException("Invalid done flag in save file: " + doneCode);
        }

        return task;
    }
}
