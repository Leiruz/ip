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

    public void markDone() { isDone = true; }
    public void markNotDone() { isDone = false; }
}
