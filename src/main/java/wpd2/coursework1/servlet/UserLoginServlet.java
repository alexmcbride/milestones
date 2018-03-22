package wpd2.coursework1.servlet;

import com.sun.deploy.net.HttpRequest;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class UserLoginServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_login.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Display the form.
        User user = new User();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setUsername(request.getParameter("username"));
        user.setPassword((request.getParameter("password")).toCharArray());

        if(user.isValid())
            //check if username or email already exist first
            if(user.usernameExists(request.getParameter("username"))){

                // find user with the same email.
                if (user.emailExists(request.getParameter("email"))) {

                        // Check that an unencrypted password matches one that has

                        if(user.authorize((request.getParameter("password")).toCharArray())){
                        // Always redirect to project.
                        user.create();
                            HttpSession session = request.getSession();
                            session.setAttribute("LoggedInId",user.getId());
                        response.sendRedirect("/projects");
                        }
            }

        }
        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
