package wpd2.coursework1.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageBoardServlet extends BaseServlet {
    private static final String MESSAGE_BOARD_TEMPLATE = "mb.mustache";
    private MessageBoard messageBoard;

    public MessageBoardServlet() {
        messageBoard = new MessageBoard();
        messageBoard.setName("MB Name");
        messageBoard.getTopics().add("Hello");
        messageBoard.getTopics().add("World");
        messageBoard.getTopics().add("Yes");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        showView(response, MESSAGE_BOARD_TEMPLATE, messageBoard);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Create new message board
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        // Edit message board
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        // Delete message board
    }

    private void showView(HttpServletResponse response, String template, Object object) {
        MustacheRenderer renderer = new MustacheRenderer();
        String output = renderer.render(template, object);

        try {
            PrintWriter writer = response.getWriter();
            writer.write(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
