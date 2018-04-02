package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.util.EmailService;
import wpd2.coursework1.viewmodel.UserPWResetEmailViewModel;

import java.io.IOException;
import java.util.UUID;

public class UserEmailRegisterServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register_email.vm";

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
        String msg = "Please Reset your password from here <a href='http://localhost:9000/users/register?token='"+token+"'>Register my account</a>";

        if (emailservice.SendEmailUsingGMailSMTP(email, sbj, msg)) {
            getResponse().sendRedirect("/users/pw_reset_email_sent");
            User user = User.find(email);
            user.setEmail(email);
            user.setResetToken(token);
            user.update();
            return;
        }else {

            UserPWResetEmailViewModel model = new UserPWResetEmailViewModel();
            view(TEMPLATE_FILE, model);
        }
    }

    public String generateEmailToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }
}