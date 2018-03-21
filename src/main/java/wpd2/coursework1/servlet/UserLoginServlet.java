package wpd2.coursework1.servlet;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class UserLoginServlet extends BaseServlet {

    private static final String TEMPLATE_FILE = "user_login.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Display the form.
        User user = new User();
        view(TEMPLATE_FILE, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setUsername(request.getParameter("username"));
        user.setPassword((request.getParameter("password")).toCharArray());
        user.validate();
        //check if username or email already exist first
        if(user.usernameExists(request.getParameter("username"))){

            // find user with the same email.
            if (user.emailExists(request.getParameter("email"))) {

                        //get the password of the matched email
                        char[] retrivedUserPassword =(user.find(request.getParameter("email")).getPassword());
                        //decrypt the passward and check if it match

                        //check if password matches
                        char[] inputPassword = request.getParameter("password").toCharArray();

                        // Check that an unencrypted password matches one that has
                        // previously been hashed
                    /*   // gensalt's log_rounds parameter determines the complexity
                        // the work factor is 2**log_rounds, and the default is 10
                        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));*/
                        /*if (BCrypt.checkpw(inputPassword, userPassword)) {
                        System.out.println("It matches");*/
                        if(retrivedUserPassword == inputPassword){
                        // Always redirect to project.
                        user.create();
                        response.sendRedirect("/projects");
                        }
            }

        }
        // Display the form with validation errors.
        view(TEMPLATE_FILE, user);
    }
}
