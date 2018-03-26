package wpd2.coursework1.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.util.AntiForgeryHelper;
import wpd2.coursework1.util.UserManager;
import wpd2.coursework1.util.VelocityRenderer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    public static final  String RESPONSE_HTML = "text/html; charset=UTF-8";
    public static final  String RESPONSE_PLAIN = "text/plain; charset=UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static final String RESPONSE_JSON = "Application/Json; charset=UTF-8\"";

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    public int loginCount = 0;

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }

    private void checkAntiForgeryToken() {
        String token = request.getParameter("antiForgeryToken");
        AntiForgeryHelper antiForgeryHelper = new AntiForgeryHelper(request.getSession());
        antiForgeryHelper.checkToken(token);
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

    protected void view(HttpServletResponse response, String template, Object object) throws IOException {
        handleView(response, template, object);
    }

    protected void view(String template, Object object) throws IOException {
        if (response == null) {
            throw new MilestoneException("Make sure you call super.doGet() or super.doPost() in your overridden methods");
        }

        handleView(response, template, object);
    }

    private void handleView(HttpServletResponse response, String template, Object object) throws IOException {
        AntiForgeryHelper antiForgeryHelper = new AntiForgeryHelper(request.getSession());
        UserManager userManager = new UserManager(request.getSession());
        VelocityRenderer renderer = new VelocityRenderer(antiForgeryHelper, userManager);
        renderer.render(response, template, object);
        response.setContentType(RESPONSE_HTML);
        response.setStatus(200);
    }

     /*Redirect to log in page when not logged in*/
    protected boolean Authenticate() throws IOException{
        if (getRequest().getSession().getAttribute("user") == null) {
            getResponse().sendRedirect("/users/login");
            return false;
        }
        return true;
    }

    protected void json(Object object) throws IOException {
        if (response == null) {
            throw new MilestoneException("Make sure you call super.doGet() or super.doPost() in your overridden methods");
        }

        json(response, object);
    }

    protected void json(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        response.getWriter().write(json);
        response.setContentType(RESPONSE_JSON);
        response.setStatus(200);
    }
}