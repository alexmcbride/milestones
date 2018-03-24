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
