package ella;

/**
 * Represents supported task types and their short codes used in display/storage.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String shortCode;

    TaskType(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * Returns the display tag (e.g., "[T]").
     *
     * @return Type tag string.
     */
    public String tag() {
        return "[" + shortCode + "]";
    }

    /**
     * Returns the short code used for storage (e.g., "T").
     *
     * @return Short code.
     */
    public String shortCode() {
        return shortCode;
    }
}
