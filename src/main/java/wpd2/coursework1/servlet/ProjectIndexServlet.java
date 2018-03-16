package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProjectIndexServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_list.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get list of projects.
        List<Project> projects = Project.loadAll();

        // Render the view.
        view(response, TEMPLATE_FILE, projects);
    }
}
