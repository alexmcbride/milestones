package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProjectDetailsServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_details.vm";

    @Override
    protected void doGet() throws IOException {
        try {
            int id = Integer.valueOf(request.getParameter("id"));

            // Get project
            Project project = Project.find(id);

            // Check for 404 error.
            if (project == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Render the view.
            view(TEMPLATE_FILE, project);
        }
        catch (NumberFormatException e) {
            // Crappy request
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
