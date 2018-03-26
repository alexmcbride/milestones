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
        //if token from the link user clicked is saved in the session
        if(getRequest().getSession().getAttribute("ReturnedToken") != null){
            //save the string to rToken session
            String rToken = getRequest().getSession().getAttribute("ReturnedToken").toString();
            //if rtoken successfully stored in the session
            if(getRequest().getSession().getAttribute(rToken) != null){
                //if the email retrieved with returned session is not null
                if(getRequest().getSession().getAttribute(rToken) != null) {
                    //if the user retreived using email is not null
                    if (User.find(getRequest().getSession().getAttribute(rToken).toString()) != null) {
                        //create user with the email
                        User user = User.find(getRequest().getSession().getAttribute(rToken).toString());
                        //and set the input password to it.
                        user.setPassword(getRequest().getParameter("password").toCharArray());
                        //if user update is successful redirect user to log in screen
                        if (user.updatePassword()) {
                            getResponse().sendRedirect("users/login");
                            return;
                        }
                    }
                }
        }
        String msg = "Error password did not get updated.";
        // Display the form with validation errors.
        view(TEMPLATE_FILE, msg);
        }
    }
}
