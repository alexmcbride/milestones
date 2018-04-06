package wpd2.coursework1.servlet;

public class JsonResponse {
    private boolean success;
    private String message;

    public JsonResponse(String message) {
        this(false, message);
    }

    public JsonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
