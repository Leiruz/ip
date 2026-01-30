package ella;
<<<<<<< HEAD

/**
 * Represents supported task types and their short codes used in display/storage.
 */
=======
>>>>>>> branch-Level-9
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String shortCode;

    TaskType(String shortCode) {
        this.shortCode = shortCode;
    }

<<<<<<< HEAD
    /**
     * Returns the display tag (e.g., "[T]").
     *
     * @return Type tag string.
     */
=======
>>>>>>> branch-Level-9
    public String tag() {
        return "[" + shortCode + "]";
    }

<<<<<<< HEAD
    /**
     * Returns the short code used for storage (e.g., "T").
     *
     * @return Short code.
     */
=======
>>>>>>> branch-Level-9
    public String shortCode() {
        return shortCode;
    }
}
