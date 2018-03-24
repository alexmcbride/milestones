package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import java.io.IOException;

public class UserPwResetEmailSentServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_pw_reset_email_sent.vm";

    @Override
    protected void doGet() throws IOException {
        String text = "anytext";
        // Display the form.
        view(TEMPLATE_FILE, text);
    }
}
