package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserLogoutServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_logout.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        if (!authorize()) return;

        User user = userManager.getUser();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        if (!authorize()) return;

        userManager.logout();
        flash.message("You are logged out");
        response.sendRedirect(response.encodeURL("/projects"));
    }
}

