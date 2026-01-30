import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final LocalDate by;

    public Deadline(String description, String byRaw) throws EllaException {
        super(description, TaskType.DEADLINE);
        try {
            // expects yyyy-mm-dd e.g., 2019-10-15
            this.by = LocalDate.parse(byRaw.trim());
        } catch (DateTimeParseException e) {
            throw new EllaException("Invalid date format. Use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public String toStorageString() {
        // Store ISO format yyyy-mm-dd so it can be loaded back reliably
        return super.toStorageString() + " | " + by.toString();
    }
}
