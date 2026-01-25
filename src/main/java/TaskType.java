public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String shortCode;

    TaskType(String shortCode) {
        this.shortCode = shortCode;
    }

    public String tag() {
        return "[" + shortCode + "]";
    }
}
