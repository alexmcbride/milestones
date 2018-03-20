package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import java.io.IOException;
import java.util.List;

public class ProjectIndexServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_index.vm";

    @Override
    protected void doGet() throws IOException {
        // Get list of projects.
        List<Project> projects = Project.loadAll();

        // Render the view.
        view(TEMPLATE_FILE, projects);
    }
}
