package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProjectDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_delete.vm";

    @Override
    protected void doGet() throws IOException {
        if (!authorize()) return;

        Project project = Project.find(getRouteId());

        if (!authorize(project)) return;

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
