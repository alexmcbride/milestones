package wpd2.coursework1.servlet;

import wpd2.coursework1.util.JsonRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling JSON responses
 */
public class BaseJsonServlet extends BaseServlet {
    public static final String RESPONSE_JSON = "Application/Json; charset=UTF-8";

    /**
     * Render a JSON response.
     *
     * @param object an object to serialise as JSON
     * @throws IOException an exception...
     */
    protected void json(Object object) throws IOException {
        json(response, object);
    }

    /**
     * Render a JSON response.
     *
     * @param response the response object to render to
     * @param object an object to serialise as JSON
     * @throws IOException an exception...
     */
    protected void json(HttpServletResponse response, Object object) throws IOException {
        JsonRenderer renderer = new JsonRenderer();
        renderer.render(response, object);
        handleResponse(response, RESPONSE_JSON);
    }
}
