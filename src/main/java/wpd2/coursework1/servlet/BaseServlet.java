
package wpd2.coursework1.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.util.AntiForgery;
import wpd2.coursework1.util.VelocityRenderer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    public static final  String HTML_TEXT_UTF_8 = "text/html; charset=UTF-8";
    public static final  String PLAIN_TEXT_UTF_8 = "text/plain; charset=UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AntiForgery antiForgery;

    protected AntiForgery getAntiForgery() {
        if (antiForgery == null) {
            antiForgery = new AntiForgery(request.getSession());
        }
        return antiForgery;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    protected void checkAntiForgeryToken() {
        String token = request.getParameter("antiForgeryToken");
        getAntiForgery().checkForgeryToken(token);
    }

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        doGet();
    }

    protected void doGet() throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        checkAntiForgeryToken();
        doPost();
    }

    protected void doPost() throws IOException {

    }

    protected void view(String template, Object object) throws IOException {
        VelocityRenderer renderer = new VelocityRenderer(getAntiForgery());
        renderer.render(response, template, object);
        response.setContentType(HTML_TEXT_UTF_8);
        response.setStatus(200);
    }
}
