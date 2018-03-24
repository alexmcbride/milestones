package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

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

        User user = User.find(getRequest().getParameter("email"));
//        user.setEmail(getRequest().getParameter("email"));
//        user.setPassword((getRequest().getParameter("password")).toCharArray());
        loginCount = loginCount+1;

        if (user == null) {
            // USER NOT FOUND
            return;
        }

/*            //check if username or email already exist first
            if(user.usernameExists(getRequest().getParameter("username"))){*/

                // find user with the same email.
//                if (user.emailExists(getRequest().getParameter("email"))) {

                    // Check that an unencrypted password matches one that has

                    if (user.authorize((getRequest().getParameter("password")).toCharArray())) {
                        // Always redirect to project.

                        getRequest().getSession().setAttribute("LoggedInEmail", user.getId());

                        loginCount =0;
                        getResponse().sendRedirect("/projects");

                    }
//                }

            if(loginCount==3){
                loginCount=0;
                getResponse().sendRedirect("/users/register");
                return;
                }

        // Display the form with validation errors.
        user.isValid();
        view(TEMPLATE_FILE, user);
   // }
    }
}
