package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProjectDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_delete.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        Project project = Project.find(getRouteId());

        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        if (!authorize(project)) return;

        view(TEMPLATE_FILE, project);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        int id = getRouteId();

        // Get project
        Project project = Project.find(id);
        project.delete();

        flash.message("Project '" + html.encode(project.getName()) + "' deleted");

        // Always redirect after post.
        response.sendRedirect(response.encodeURL("/projects"));

        view(TEMPLATE_FILE, project);
    }
}
