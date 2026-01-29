public class Task {
    protected final String description;
    protected boolean isDone;
    protected final TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    protected String statusIcon() {
        return "[" + (isDone ? "X" : " ") + "]";
    }

    @Override
    public String toString() {
        return type.tag() + statusIcon() + " " + description;
    }

    public void markDone() { 
        isDone = true; 
    }
    public void markNotDone() { 
        isDone = false; 
    }

    public String toStorageString() {
        return type.shortCode() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

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
