package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;
import wpd2.coursework1.util.FlashHelper;


import java.io.IOException;

public class UserLoginServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_login.vm";

    @Override
    protected void doGet() throws IOException {
        // Display the form.
        User user = new User();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost() throws IOException {
        loginCount++;

        User user = User.find(request.getParameter("email"));

        if (user != null && user.authenticate(request.getParameter("password").toCharArray())) {
            request.getSession().setAttribute("user", user);
            loginCount = 0;
            // Always redirect to project.
            getResponse().sendRedirect("/projects");

            return;
        }
        else {
            user = new User();
            user.setEmail(request.getParameter("email"));
            flash.message("Email or password are incorrect", FlashHelper.WARNING);
        }

        if (loginCount == 3) {
            loginCount = 0;
            response.sendRedirect("/users/register");
            return;
        }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
