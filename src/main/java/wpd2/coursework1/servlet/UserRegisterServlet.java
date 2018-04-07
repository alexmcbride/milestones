
package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;
import java.io.IOException;

public class UserRegisterServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register.vm";

    @Override
    protected void doGet() throws IOException {
        User user = new User();
        if(request.getParameter("token") != null){
            String token = request.getParameter("token").replace("'","");
            user = User.findByToken(token);
        }
        user.setUsername(null);

        // Display the empty form.
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost() throws IOException {
        User user = User.find(request.getParameter("email"));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password").toCharArray());

        if (user.isValid()) {
            // Save user to database.
            user.update();
            flash.message("User account registered");
            response.sendRedirect("/users/login");
            return;
        }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }

}