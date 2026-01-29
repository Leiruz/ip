public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + by;
    }
}
