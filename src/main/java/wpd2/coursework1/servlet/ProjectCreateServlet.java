package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class ProjectCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_create.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Display the form.
        view(response, TEMPLATE_FILE, Project.empty());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Project project = Project.create(request.getParameter("name"));

        // Check if project is valid.
        if (project.isValid()) {
            // Save project to database.
            project.create();

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + project.getId());

            return;
        }

        // Display the form with validation errors.
        view(response, TEMPLATE_FILE, project);
    }
}
