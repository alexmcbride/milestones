package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;
import wpd2.coursework1.util.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UserRegisterEmailServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register_email.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        User user = new User();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        EmailService emailservice = new EmailService();
        User user = new User();
        String email = getRequest().getParameter("email");
        String sbj = "Milestone Project Email Authentication";
        String token = generateEmailToken();
        String msg = "Please Register from here <a href='http://localhost:9000/users/register?token='"+token+"'>Register my account</a>";
        user.setUsername("unauthrisedUser");
        user.setEmail(email);
        user.setResetToken(token);
        if(user.isValid()){
            emailservice.SendEmailUsingGMailSMTP(email, sbj, msg);
            user.create();
            getResponse().sendRedirect(response.encodeURL("/users/pw_reset_email_sent"));
            return;
        }else {
            /*flash.message("Email is not valid", FlashHelper.WARNING);*/
            view(TEMPLATE_FILE, user);
        }
    }

    public String generateEmailToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }
}