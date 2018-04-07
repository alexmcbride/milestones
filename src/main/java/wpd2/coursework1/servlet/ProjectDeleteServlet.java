package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
public class ProjectDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_delete.vm";


    @Override
    protected void doGet() throws IOException {

        int id = getRouteId();

        // Get project
        Project project = Project.find(id);
        // Check for 404 error.
        if (project == null) {
            getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        view(TEMPLATE_FILE, project);
    }

    @Override
    protected void doPost() throws IOException {
        int id = getRouteId();

        // Get project
        Project project = Project.find(id);

        project.delete();

        // Always redirect after post.
        getResponse().sendRedirect("/projects");

        view(TEMPLATE_FILE, project);
    }
}
