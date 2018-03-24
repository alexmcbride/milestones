package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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
        user.setUsername(getRequest().getParameter("username"));
        user.setEmail(getRequest().getParameter("email"));
        user.setPassword(getRequest().getParameter("password").toCharArray());

        if(user.isValid())
            //check if username or email already exist first
            if(!user.usernameExists(getRequest().getParameter("username"))){

                // find user with the same email.
                if (!user.emailExists(getRequest().getParameter("email"))) {

                    // Save user to database.
                    user.create();
                    getResponse().sendRedirect("/users/login");
                    return;

                }
            }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
