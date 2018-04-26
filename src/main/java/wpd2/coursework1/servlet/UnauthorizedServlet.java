package wpd2.coursework1.servlet;

import wpd2.coursework1.helper.FlashHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that redirects unauthorised users to the login page.
 */
public class UnauthorizedServlet extends BaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        flash.message("You do not have permission to view that page", FlashHelper.WARNING);

        response.sendRedirect(response.encodeURL("/users/login"));
    }
}