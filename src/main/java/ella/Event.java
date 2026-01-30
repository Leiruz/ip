package ella;
<<<<<<< HEAD

/**
 * Represents an event with a start and end time (stored as strings).
 */
=======
>>>>>>> branch-Level-9
public class Event extends Task {
    private final String from;
    private final String to;

<<<<<<< HEAD
    /**
     * Creates an event task.
     *
     * @param description Event description.
     * @param from Start time.
     * @param to End time.
     */
=======
>>>>>>> branch-Level-9
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

<<<<<<< HEAD
    /**
     * Returns the user-facing string representation of the event.
     *
     * @return Formatted event string.
     */
=======
>>>>>>> branch-Level-9
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

<<<<<<< HEAD
    /**
     * Converts this event into a storage-friendly string.
     *
     * @return Storage string representation.
     */
=======
>>>>>>> branch-Level-9
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + from + " | " + to;
    }
}
