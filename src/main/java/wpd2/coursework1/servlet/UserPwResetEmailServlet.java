package wpd2.coursework1.servlet;

import wpd2.coursework1.helper.FlashHelper;
import wpd2.coursework1.model.User;
import wpd2.coursework1.util.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPwResetEmailServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_pw_reset_email.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        User user = new User();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        String email = request.getParameter("email");

        EmailService emailservice = new EmailService();

        String sbj = "Milestone Project Password Reset";
        String token = userManager.generateEmailToken();
        String msg = "Please Reset your password from here <a href='http://localhost:9002/users/pw_reset?token="+token+"'>Reset My Password</a>";

        User user = User.find(getRequest().getParameter("email"));
        if (user != null) {
            if (emailservice.SendEmailUsingGMailSMTP(email, sbj, msg)) {
                flash.message("Reset password email sent");

                user.setResetToken(token);
                user.update();

                response.sendRedirect(response.encodeURL("/users/pw_reset_email_sent"));
            }
        }
        else {
            flash.message("Not Valid Email address", FlashHelper.WARNING);
            view(TEMPLATE_FILE, user);
        }
    }
}

