
package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAccountServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_account.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        if (!authorize()) return;

        User user = new User();

        System.err.println("###before login check");

        if (getRequest().getSession().getAttribute("user") != null) {
            System.err.println("###logged in");
            user = (User)getRequest().getSession().getAttribute("user");
            user = User.find(user.getEmail());
        }
        // Display the form.
        System.err.println("###after login check");
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        User user = new User();
        user.setUsername(getRequest().getParameter("username"));
        user.setEmail(getRequest().getParameter("email"));
        //get session user
        User loggedinUserData = (User)getRequest().getSession().getAttribute("user");
        //get id of existing user
        if(loggedinUserData != null) {
            //get the user with the session id
            user = User.find(loggedinUserData.getId());
            user.setId(loggedinUserData.getId());
            // set the passed updated data
            user.setUsername(getRequest().getParameter("username"));
            user.setEmail(getRequest().getParameter("email"));
            String password = getRequest().getParameter("password");
            if (password != null && password.trim().length() > 0){
                user.setPassword(getRequest().getParameter("password").toCharArray());
            }

            if (user.update()) {
                //update session to newly updated user detail
                getRequest().getSession().setAttribute("user", user);
                flash.message("Account details edited");
                getResponse().sendRedirect(response.encodeURL("/projects"));
                return;
            }
            // Display the form with validation errors.
        }
        view(TEMPLATE_FILE, user);
    }
}


