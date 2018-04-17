package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;
import wpd2.coursework1.helper.FlashHelper;
import wpd2.coursework1.viewmodel.UserPWResetEmailViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPwResetServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_pw_reset.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        if(request.getParameter("token") != null){
            String token = request.getParameter("token").replace("'","");
            request.getSession().setAttribute("ReturnedToken",token);
        }
        User user = new User();

        // Display the form.
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);
        //if token value from the link user clicked is saved in the session
        if(getRequest().getSession().getAttribute("ReturnedToken") != null){
            //save the session value to rToken string
            String rToken = getRequest().getSession().getAttribute("ReturnedToken").toString().replace("'","");

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
