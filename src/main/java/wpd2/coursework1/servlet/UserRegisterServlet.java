package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;
import java.io.IOException;

public class UserRegisterServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register.vm";

    @Override
    protected void doGet() throws IOException {
        User user = new User();
        // Display the empty form.
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost() throws IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password").toCharArray());

        if (user.isValid()) {
            // Save user to database.
            user.create();
            flash.message("User account registered");
            response.sendRedirect("/users/login");
            return;
        }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}