package wpd2.coursework1.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPwResetEmailSentServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "user_pw_reset_email_sent.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        view(TEMPLATE_FILE, null);
    }
}
