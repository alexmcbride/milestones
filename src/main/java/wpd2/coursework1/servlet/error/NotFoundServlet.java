package wpd2.coursework1.servlet.error;

import wpd2.coursework1.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet fired off by the 404 not found custom error.
 */
public class NotFoundServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "errors/not_found.vm";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        view(TEMPLATE_FILE, null);
    }
}
