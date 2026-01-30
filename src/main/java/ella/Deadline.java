package ella;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final LocalDate by;

    /**
     * Creates a deadline task with a due date in yyyy-mm-dd format.
     *
     * @param description Deadline description.
     * @param byRaw Due date string in yyyy-mm-dd format.
     * @throws EllaException If the date cannot be parsed.
     */
    public Deadline(String description, String byRaw) throws EllaException {
        super(description, TaskType.DEADLINE);
        try {
            this.by = LocalDate.parse(byRaw.trim());
        } catch (DateTimeParseException e) {
            throw new EllaException("Invalid date format. Use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

    /**
     * Returns the user-facing string representation, formatting the date.
     *
     * @return Formatted deadline string.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Converts this deadline into a storage-friendly string.
     * Stores the date in ISO format yyyy-mm-dd.
     *
     * @return Storage string representation.
     */
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + by.toString();
    }
}
