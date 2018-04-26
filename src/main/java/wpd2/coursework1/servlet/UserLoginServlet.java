package wpd2.coursework1.servlet;

import wpd2.coursework1.model.TempUser;
import wpd2.coursework1.model.User;
import wpd2.coursework1.helper.FlashHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserLoginServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_login.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        // Display the form.
        User user = new User();
        if(request.getParameter("token") != null){
            String token = request.getParameter("token").replace("'","");
            if(TempUser.findByToken(token) != null){
                TempUser tempuser = TempUser.findByToken(token);
                            /* user = tempuser.getUser();*/
                user.setUsername(tempuser.getUsername());
                user.setEmail(tempuser.getEmail());
                String dummy = "dummy";
                user.setPassword(dummy.toCharArray());
                user.setPasswordHash(tempuser.getPasswordHash());
                user.create();
                tempuser.delete();

                flash.message("Your account has been activated");
            }
        }

        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        loginCount++;

        User user = User.find(request.getParameter("email"));

        if (user != null && user.authenticate(request.getParameter("password").toCharArray())) {
            userManager.login(user);
            loginCount = 0;

            flash.message("You are logged in");
            if (user.getUnvisited() > 0) {
                flash.message("New projects have been shared with you!", FlashHelper.INFO);
            }

            // Always redirect to project.
            getResponse().sendRedirect(response.encodeURL("/projects"));

            return;
        }
        else {
            user = new User();
            user.setEmail(request.getParameter("email"));
            flash.message("Email or password are incorrect", FlashHelper.WARNING);
        }

        if (loginCount == 3) {
            loginCount = 0;
            response.sendRedirect(response.encodeURL("/users/register"));
            return;
        }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
