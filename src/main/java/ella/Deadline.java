package ella;
<<<<<<< HEAD

=======
>>>>>>> branch-Level-9
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

<<<<<<< HEAD
/**
 * Represents a task that must be completed by a specific date.
 */
=======
>>>>>>> branch-Level-9
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final LocalDate by;

<<<<<<< HEAD
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
=======
    public Deadline(String description, String byRaw) throws EllaException {
        super(description, TaskType.DEADLINE);
        try {
            // expects yyyy-mm-dd e.g., 2019-10-15
>>>>>>> branch-Level-9
            this.by = LocalDate.parse(byRaw.trim());
        } catch (DateTimeParseException e) {
            throw new EllaException("Invalid date format. Use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

<<<<<<< HEAD
    /**
     * Returns the user-facing string representation, formatting the date.
     *
     * @return Formatted deadline string.
     */
=======
>>>>>>> branch-Level-9
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

<<<<<<< HEAD
    /**
     * Converts this deadline into a storage-friendly string.
     * Stores the date in ISO format yyyy-mm-dd.
     *
     * @return Storage string representation.
     */
    @Override
    public String toStorageString() {
=======
    @Override
    public String toStorageString() {
        // Store ISO format yyyy-mm-dd so it can be loaded back reliably
>>>>>>> branch-Level-9
        return super.toStorageString() + " | " + by.toString();
    }
}
