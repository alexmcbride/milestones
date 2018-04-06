package wpd2.coursework1.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wpd2.coursework1.helper.*;
import wpd2.coursework1.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    private static final  String RESPONSE_HTML = "text/html; charset=UTF-8";

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected UserManager userManager;
    protected AntiForgeryHelper antiForgeryHelper;
    protected FlashHelper flash;
    protected PrettyTimeHelper prettyTimeHelper;
    protected HtmlHelper html;

    protected int loginCount = 0;

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }

    private void checkAntiForgeryToken() {
        String token = request.getParameter("antiForgeryToken");
        antiForgeryHelper.checkToken(token);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        SessionWrapper session = new SessionWrapper(request.getSession());
        this.userManager = new UserManager(session);
        this.antiForgeryHelper = new AntiForgeryHelper(session);
        this.flash = new FlashHelper(session);
        this.html = new HtmlHelper();
        this.prettyTimeHelper = new PrettyTimeHelper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);

        doGet();
    }

    protected void doGet() throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);

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
        VelocityRenderer renderer = new VelocityRenderer();
        renderer.addContext("antiForgeryHelper", antiForgeryHelper);
        renderer.addContext("userManager", userManager);
        renderer.addContext("flash", flash);
        renderer.addContext("prettyTimeHelper", prettyTimeHelper);
        renderer.addContext("html", html);
        renderer.render(response, template, object);
        handleResponse(response, RESPONSE_HTML);
    }

    protected void handleResponse(HttpServletResponse response, String responseHtml) {
        response.setContentType(responseHtml);
        response.setStatus(200);
    }

    protected boolean authorize() throws IOException{
        if (userManager.isLoggedIn()) {
            return true;
        }
        response.sendRedirect("/users/login");
        return false;
    }
}