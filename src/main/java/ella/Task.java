package ella;
<<<<<<< HEAD

/**
 * Represents a task with a description, completion status, and a task type.
 */
=======
>>>>>>> branch-Level-9
public class Task {
    protected final String description;
    protected boolean isDone;
    protected final TaskType type;

<<<<<<< HEAD
    /**
     * Creates a task with the given description and type.
     *
     * @param description Task description.
     * @param type Task type.
     */
=======
>>>>>>> branch-Level-9
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

<<<<<<< HEAD
    /**
     * Returns the status icon string for this task.
     *
     * @return "[X]" if done, otherwise "[ ]".
     */
=======
>>>>>>> branch-Level-9
    protected String statusIcon() {
        return "[" + (isDone ? "X" : " ") + "]";
    }

<<<<<<< HEAD
    /**
     * Returns the user-facing string representation of the task.
     *
     * @return Formatted task string.
     */
=======
>>>>>>> branch-Level-9
    @Override
    public String toString() {
        return type.tag() + statusIcon() + " " + description;
    }

<<<<<<< HEAD
    /**
     * Marks the task as done.
     */
=======
>>>>>>> branch-Level-9
    public void markDone() {
        isDone = true;
    }

<<<<<<< HEAD
    /**
     * Marks the task as not done.
     */
=======
>>>>>>> branch-Level-9
    public void markNotDone() {
        isDone = false;
    }

<<<<<<< HEAD
    /**
     * Converts this task into a storage-friendly string.
     *
     * @return Storage string representation.
     */
=======
>>>>>>> branch-Level-9
    public String toStorageString() {
        return type.shortCode() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

<<<<<<< HEAD
    /**
     * Parses a task from a storage line.
     *
     * @param line A line from the save file.
     * @return The parsed task.
     * @throws EllaException If the line is corrupted or cannot be parsed.
     */
=======
>>>>>>> branch-Level-9
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
