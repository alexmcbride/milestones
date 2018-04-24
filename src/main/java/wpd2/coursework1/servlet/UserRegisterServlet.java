
package wpd2.coursework1.servlet;

import wpd2.coursework1.helper.FlashHelper;
import wpd2.coursework1.model.TempUser;
import wpd2.coursework1.model.User;
import wpd2.coursework1.util.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UserRegisterServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_register.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        User user = new User();
/*        if(request.getParameter("token") != null){
            String token = request.getParameter("token").replace("'","");
            user = User.findByToken(token);
        }
        user.setUsername(null);*/

        // Display the empty form.
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        /*User user = User.find(request.getParameter("email"));*/
        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password").toCharArray());
        EmailService emailservice = new EmailService();
        TempUser tempuser = new TempUser();
        String email = getRequest().getParameter("email");
        String sbj = "Milestone Project Email Authentication";
        String token = generateEmailToken();
        String msg = "Please Register from here <a href='http://localhost:9000/users/login?token='"+token+"'>Register my account</a>";
        tempuser.setToken(token);
/*        tempuser.setUser(user);*/
        tempuser.setUsername(request.getParameter("username"));
        tempuser.setEmail(request.getParameter("email"));
        tempuser.setPassword(request.getParameter("password"));
        if (user.isValid()) {
            tempuser.create();
            emailservice.SendEmailUsingGMailSMTP(email, sbj, msg);
            // Save user to database.
            //user.update();
//            flash.message("Please check you email to complete registration", FlashHelper.INFO);
            response.sendRedirect(response.encodeURL("/users/pw_reset_email_sent"));
            return;
        }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }

    public String generateEmailToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }
}