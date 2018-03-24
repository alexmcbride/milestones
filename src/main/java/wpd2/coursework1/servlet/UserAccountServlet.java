package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserAccountServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_account.vm";

    @Override
    protected void doGet() throws IOException {
        User user = new User();

        System.err.println("###before login check");

        if (getRequest().getSession().getAttribute("loggedInEmail") != null) {
            System.err.println("###logged in");
            int userId = Integer.valueOf(getRequest().getSession().getAttribute("loggedInId").toString());
            user.find(userId);
        }
        // Display the form.
        System.err.println("###after login check");
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost() throws IOException {
        User user = new User();
        user.setUsername(getRequest().getParameter("username"));
        user.setEmail(getRequest().getParameter("email"));
        user.setPassword(getRequest().getParameter("password").toCharArray());

        if(user.isValid()){
           user.update();
            }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
/*
=======
package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserAccountServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_account.vm";

    @Override
    protected void doGet() throws IOException {
        User user = new User();
        int userId = Integer.valueOf(request.getSession().getAttribute("loggedInId").toString());
        user.find(userId);
        // Display the form.
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password").toCharArray());

        if(user.isValid()){
           user.update();
            }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
>>>>>>> 642fa583b65fd829731dc4849fbb38d8140ebf3a
*/
