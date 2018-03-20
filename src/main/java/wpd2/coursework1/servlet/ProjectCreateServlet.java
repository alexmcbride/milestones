package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import java.io.IOException;
import java.util.Date;

public class ProjectCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_create.vm";

    @Override
    protected void doGet() throws IOException {
        // Display the form.
        view(TEMPLATE_FILE, new Project());
    }

    @Override
    protected void doPost() throws IOException {
        User user = new User();
        user.setEmail("user@email.com");
        user.setPassword("password1".toCharArray());
        user.setUsername("user");
        user.create();

        Project project = new Project();
        project.setName(getRequest().getParameter("name"));
        project.setCreated(new Date());

        // Check if project is valid.
        if (project.isValid()) {
            // Save project to database.
            project.create(user);

            // Always redirect after post.
            getResponse().sendRedirect("/projects/details?id=" + project.getId());

            return;
        }

        view(TEMPLATE_FILE, project);
    }
}
