
package wpd2.coursework1.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;


public class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    public static final  String PLAIN_TEXT_UTF_8 = "text/html; charset=UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    protected void issue(String mimeType, int returnCode, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType);
        response.setStatus(returnCode);
    }

    protected void cache(HttpServletResponse response, int seconds) {
        if (seconds > 0) {
            response.setHeader("Pragma", "Public");
            response.setHeader("Cache-Control", "public, no-transform, max-age=" + seconds);
        }
    }

    protected void view(HttpServletResponse response, String template, Object object) throws IOException {
        VelocityRenderer renderer = new VelocityRenderer();
        renderer.render(response, template, object);
        issue(PLAIN_TEXT_UTF_8, 200, response);
    }

    protected void notFound(HttpServletResponse response) throws IOException {
        issue(PLAIN_TEXT_UTF_8, 404, response);
    }

    protected void badRequest(HttpServletResponse response) throws IOException {
        issue(PLAIN_TEXT_UTF_8, 400, response);
    }
}
