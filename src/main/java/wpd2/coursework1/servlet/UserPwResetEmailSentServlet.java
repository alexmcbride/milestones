package wpd2.coursework1.servlet;

import java.io.IOException;

public class UserPwResetEmailSentServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_pw_reset_email_sent.vm";

    @Override
    protected void doGet() throws IOException {
        view(TEMPLATE_FILE, null);
    }
}
