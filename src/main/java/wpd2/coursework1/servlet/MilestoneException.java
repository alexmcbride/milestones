package wpd2.coursework1.servlet;

public class MilestoneException extends RuntimeException {
    public MilestoneException() {
        super();
    }

    public MilestoneException(String message) {
        super(message);
    }

    public MilestoneException(String message, Exception inner) {
        super(message, inner);
    }
}
