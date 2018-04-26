package wpd2.coursework1.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wpd2.coursework1.helper.*;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
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
    protected DateHelper dateHelper;
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
        this.html = new HtmlHelper(response);
        this.flash = new FlashHelper(session);
        this.dateHelper = new DateHelper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);

        checkAntiForgeryToken();
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
        renderer.addContext("dateHelper", dateHelper);
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
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    protected boolean authorize(Project project) throws IOException {
        if (authorize() && !project.isOwnedBy(userManager.getUser())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    protected boolean authorize(Milestone milestone) throws IOException {
        Project project = Project.find(milestone.getProjectId());
        return authorize(project);
    }

    protected int getRouteId() {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null) {
                return Integer.valueOf(pathInfo.substring(1));
            }
        }
        catch (NumberFormatException e) { /* Ignored */ }
        return 0;
    }
}