package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class UserRegisterServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*User user = new User();*/
        // Display the form.
        view(TEMPLATE_FILE, new User());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password").toCharArray());

        if(user.isValid())
            //check if username or email already exist first
            if(!user.usernameExists(request.getParameter("username"))){

                // find user with the same email.
                if (!user.emailExists(request.getParameter("email"))) {

                    // Save user to database.
                    user.create();
                    response.sendRedirect("/users/login");

                }
            }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
