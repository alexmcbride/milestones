package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class UserLoginServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_login.vm";

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


/*            // gensalt's log_rounds parameter determines the complexity
            // the work factor is 2**log_rounds, and the default is 10
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));*/




            // find user with the same email.
            if (user.findUserByEmail(request.getParameter("email")).isValid()) {
                //check if the password matches
                String inputPassword = request.getParameter("password");
                String userPassword = user.getPassword();
// Check that an unencrypted password matches one that has
                // previously been hashed
                /*if (BCrypt.checkpw(inputPassword, userPassword)) {
                    System.out.println("It matches");
                    // Always redirect to project.
                    response.sendRedirect("/projects");
                }else{
                    // Display the empty form.
                    view(response, TEMPLATE_FILE, User.empty());
                }*/
            }
            else{
                // Display the empty form.
                view(response, TEMPLATE_FILE, User.empty());
            }
        }

        // Display the form with validation errors.
        view(response, TEMPLATE_FILE, user);
    }
}
