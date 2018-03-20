package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import java.io.IOException;

public class ProjectCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_create.vm";

    @Override
    protected void doGet() throws IOException {
        // Display the form.
        view(TEMPLATE_FILE, Project.empty());
    }

    @Override
    protected void doPost() throws IOException {
        Project project = Project.create(getRequest().getParameter("name"));

        // Check if project is valid.
        if (project.isValid()) {
            // Save project to database.
            project.create();

            // Always redirect after post.
            getResponse().sendRedirect("/projects/details?id=" + project.getId());

            return;
        }

        // Display the form with validation errors.
        view(TEMPLATE_FILE, project);
    }
}
