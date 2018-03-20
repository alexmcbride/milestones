package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserRegisterServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Display the form.
        view(response, TEMPLATE_FILE, User.empty());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = User.create(request.getParameter("email"), request.getParameter("password"));

        // Check if user is valid.
        if (user.isValid()) {
/*            // Hash a password for the first time
            String hashed = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt());

            user.setPassword(hashed);*/

            // Save user to database.
            user.create();

            // Always redirect after post.
            response.sendRedirect("/users/login");

            return;
        }

        // Display the form with validation errors.
        view(response, TEMPLATE_FILE, user);
    }
}
