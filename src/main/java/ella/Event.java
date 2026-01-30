package ella;

/**
 * Represents an event with a start and end time (stored as strings).
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description Event description.
     * @param from Start time.
     * @param to End time.
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the user-facing string representation of the event.
     *
     * @return Formatted event string.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Converts this event into a storage-friendly string.
     *
     * @return Storage string representation.
     */
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + from + " | " + to;
    }
}
