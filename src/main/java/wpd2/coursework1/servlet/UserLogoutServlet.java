package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import java.io.IOException;

public class UserLogoutServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_logout.vm";

    @Override
    protected void doGet() throws IOException {
        if (!authorize()) return;

        User user = userManager.getUser();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost() throws IOException {
        if (!authorize()) return;

        userManager.logout();
        flash.message("You are logged out");
        getResponse().sendRedirect("/projects");
    }
}

