package wpd2.coursework1.servlet.error;

import com.google.common.net.HttpHeaders;
import wpd2.coursework1.helper.FlashHelper;
import wpd2.coursework1.servlet.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet that redirects unauthorised users to the login page.
 */
public class UnauthorizedServlet extends BaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        // Try get return URL from session, if it's been set.
        HttpSession session = request.getSession();
        String returnUrl = (String)session.getAttribute("returnUrl");
        if (returnUrl == null) {
            returnUrl = "";
        }
        else {
            returnUrl = "?returnUrl=" + returnUrl;
            session.removeAttribute("returnUrl");
        }

        flash.message("You do not have permission to view that page", FlashHelper.WARNING);
        response.sendRedirect(response.encodeURL("/users/login" + returnUrl));
    }
}