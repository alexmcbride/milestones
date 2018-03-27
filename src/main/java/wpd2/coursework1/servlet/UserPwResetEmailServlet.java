package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.util.EmailService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class UserPwResetEmailServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_pw_reset_email.vm";

    @Override
    protected void doGet() throws IOException {
        // Display the form.
        view(TEMPLATE_FILE, new Project());
    }

    @Override
    protected void doPost() throws IOException {
        String email = getRequest().getParameter("email");

        EmailService emailservice = new EmailService();

        String sbj = "Milestone Project Password Reset";
        String token = generateEmailToken();
        String msg = "Please Reset your password from here <a href='http://localhost:9000/users/pw_reset?token='"+token+"'>Reset My Password</a>";

        if(User.find(getRequest().getParameter("email")) != null) {

                if (emailservice.SendEmailUsingGMailSMTP(email, sbj, msg)) {
                    getResponse().sendRedirect("/users/pw_reset_email_sent");
                    User user = User.find(email);
                    user.setResetToken(token);
                    user.update();
                    /*HttpSession session = getRequest().getSession();
                    session.setAttribute(token, email);*/

                    return;
                }

        }else {
            String msg1 = "Not a valid email address.";
            view(TEMPLATE_FILE, msg1);
        }

    }

    public String generateEmailToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }
}
