package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;
import wpd2.coursework1.util.FlashHelper;
import wpd2.coursework1.viewmodel.UserPWResetEmailViewModel;

import java.io.IOException;

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
            String rToken = getRequest().getSession().getAttribute("ReturnedToken").toString();

            //if the user retreived using email is not null
            if (User.findByToken(rToken) != null) {
                //create user with the token
                User user = User.findByToken(rToken);
                //and set the input password to it.
                user.setPassword(getRequest().getParameter("password").toCharArray());

                //if user update is successful redirect user to log in screen
                if (user.update()) {
                    flash.message("Password reset");
                    getResponse().sendRedirect("/users/login");
                    return;
                }
            }
        }

        flash.message("Password not reset for some reason", FlashHelper.WARNING);
        // Display the form with validation errors.
        view(TEMPLATE_FILE, null);
    }
}
