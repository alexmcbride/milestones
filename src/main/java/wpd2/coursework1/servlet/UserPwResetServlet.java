package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.viewmodel.UserPWResetEmailViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class UserPwResetServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_pw_reset.vm";

    @Override
    protected void doGet() throws IOException {
        if(getRequest().getParameter("token") != null){
            String token = getRequest().getParameter("token");
            getRequest().getSession().setAttribute("ReturnedToken",token);
        }
        UserPWResetEmailViewModel password = new UserPWResetEmailViewModel();

                // Display the form.
        view(TEMPLATE_FILE, password);
    }

    @Override
    protected void doPost() throws IOException {
        if(getRequest().getSession().getAttribute("ReturnedToken") != null){
            String rToken = getRequest().getSession().getAttribute("ReturnedToken").toString();
            if(getRequest().getSession().getAttribute(rToken) != null){
                String email = getRequest().getSession().getAttribute(rToken).toString();

            User user = new User();
            if(user.find(email) != null);
            user.setEmail(getRequest().getParameter("email"));

            if(user.isValid()){
                user.update();
                getResponse().sendRedirect("users/login");
                return;
            }
        }
        String msg = "Error password did not get updated.";
        // Display the form with validation errors.
        view(TEMPLATE_FILE, msg);
        }
    }
}
