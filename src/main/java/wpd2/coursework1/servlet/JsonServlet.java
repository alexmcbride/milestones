package wpd2.coursework1.servlet;

import wpd2.coursework1.util.JsonRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 Servlet for JSON responses
 */
public class JsonServlet extends BaseServlet {
    private static final String RESPONSE_JSON = "Application/Json; charset=UTF-8";

    @Override
    protected boolean authorize() throws IOException {
        if (userManager.isLoggedIn()) {
            return true;
        }
        // For JSON we don't want to redirect.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    protected void view(Object object) throws IOException {
        view(response, null, object);
    }

    @Override
    protected void view(HttpServletResponse response, String template, Object object) throws IOException {
        JsonRenderer renderer = new JsonRenderer();
        renderer.render(response, object);
        handleResponse(response, RESPONSE_JSON);
    }
}
