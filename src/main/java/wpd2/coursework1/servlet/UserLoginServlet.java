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
    protected void doGet() throws IOException {
        // Display the form.
        User user = new User();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost() throws IOException {
        User user = new User();
        user.setEmail(getRequest().getParameter("email"));
        user.setPassword((getRequest().getParameter("password")).toCharArray());
        loginCount = loginCount+1;
               // find user with the same email.

                if (User.find(getRequest().getParameter("email")) != null) {
                    user = User.find(getRequest().getParameter("email"));
                    // Check that an unencrypted password matches one that has
                    if (user.authorize((getRequest().getParameter("password")).toCharArray())) {

                        getRequest().getSession().setAttribute("user", user);
                        loginCount =0;
                        // Always redirect to project.
                        getResponse().sendRedirect("/projects");
                        return;
                    }
                }
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
/*=======
package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
*/
