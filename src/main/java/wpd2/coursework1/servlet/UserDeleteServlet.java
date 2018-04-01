package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import java.io.IOException;

public class UserDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_delete.vm";

    @Override
    protected void doGet() throws IOException {
        User user = new User();
        /*      System.err.println("###before login check");*/
        if (request.getSession().getAttribute("user") != null) {
            /*      System.err.println("###logged in");*/
            //get user on the session
            user = (User) request.getSession().getAttribute("user");
            //get the user from database using logged in user's email
            user = User.find(user.getEmail());
        }

        /*        System.err.println("###after login check");*/
        view(TEMPLATE_FILE, user);
    }


    @Override
    protected void doPost() throws IOException {
        User user = new User();
        //get id of existing user
        User loggedinUserData = (User) request.getSession().getAttribute("user");
        //get user from database using the user's id
        user = User.find(loggedinUserData.getId());
        //if successfully delete the user
        if (user.delete()) {
            //remove this account from session
            request.getSession().removeAttribute("user");
            //redirect to main
            response.sendRedirect("/projects");
            return;
        }
        // if fails continues to show delete user view
        view(TEMPLATE_FILE, user);
    }
}


