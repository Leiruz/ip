package ella;

/**
 * Represents exceptions specific to the Ella chatbot application.
 */
public class EllaException extends Exception {

    /**
     * Creates an EllaException with the given message.
     *
     * @param message Explanation of the error.
     */
    public EllaException(String message) {
        super(message);
    }
}
