package wpd2.coursework1.servlet;

/**
 * Class to represent a JSON response, that can be serialised.
 */
public class JsonResponse {
    private boolean success;
    private String message;

    /**
     * Creates a new JsonResponse object.
     *
     * @param message the message to include.
     */
    public JsonResponse(String message) {
        this(false, message);
    }

    /**
     * Creates a new JsonResponse object.
     *
     * @param success a boolean indicating success of an action.
     * @param message the message to include.
     */
    public JsonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Gets if success is true.
     *
     * @return true if successful.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the message.
     *
     * @return the message.
     */
    public String getMessage() {
        return message;
    }
}
