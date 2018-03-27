package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import java.io.IOException;
import java.util.Date;

public class ProjectCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_create.vm";

    @Override
    protected void doGet() throws IOException {
        if (!authorize()) return;

        // Display the form.
        view(TEMPLATE_FILE, new Project());
    }

    @Override
    protected void doPost() throws IOException {
        if (!authorize()) return;

        Project project = new Project();
        project.setName(request.getParameter("name"));
        project.setCreated(new Date());

        // Check if project is valid.
        if (project.isValid()) {
            // Save project to database.
            User user = userManager.getUser();
            project.create(user);

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + project.getId());

            return;
        }

        view(TEMPLATE_FILE, project);
    }
}
