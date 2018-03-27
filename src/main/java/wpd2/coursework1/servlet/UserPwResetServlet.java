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
        if(request.getParameter("token") != null){
            String token = request.getParameter("token");
            request.getSession().setAttribute("ReturnedToken",token);
        }
        UserPWResetEmailViewModel password = new UserPWResetEmailViewModel();

        // Display the form.
        view(TEMPLATE_FILE, password);
    }

    @Override
    protected void doPost() throws IOException {

        //if token value from the link user clicked is saved in the session
        if(getRequest().getSession().getAttribute("ReturnedToken") != null){
            //save the session value to rToken string
            String rToken = getRequest().getSession().getAttribute("ReturnedToken").toString().replace("'","");

            //if the user retreived using email is not null
            if (User.findbyToken(rToken) != null) {
                //create user with the token
                User user = User.findbyToken(rToken);
                //and set the input password to it.
                user.setPassword(getRequest().getParameter("password").toCharArray());

                //if user update is successful redirect user to log in screen
                if (user.update()) {
                    getResponse().sendRedirect("/users/login");
                    return;
                }
            }
        }

        String msg2 = "Error password did not get updated.";
        // Display the form with validation errors.
        view(TEMPLATE_FILE, msg2);
    }
}
