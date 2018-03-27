package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import java.io.IOException;

public class UserLogoutServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_logout.vm";

    @Override
    protected void doGet() throws IOException {
        User user = new User();
        /*      System.err.println("###before login check");*/
        if (getRequest().getSession().getAttribute("user") != null) {
            /*      System.err.println("###logged in");*/
            //get user on the session
            user = (User) getRequest().getSession().getAttribute("user");
            //get the user from database using logged in user's email
            user = User.find(user.getEmail());
        }

        /*        System.err.println("###after login check");*/
        view(TEMPLATE_FILE, user);
    }


    @Override
    protected void doPost() throws IOException {

        //get existing user
        User user = (User) getRequest().getSession().getAttribute("user");
        if (user != null) {
            getRequest().getSession().removeAttribute("user");
            //redirect to main
            getResponse().sendRedirect("/projects");
            return;
        }
        // if fails continues to show delete user view
        getResponse().sendRedirect("/projects");
        return;
    }
}


