package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class ProjectCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_create.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Display the form.
        view(response, TEMPLATE_FILE, new Project());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setEmail("user@email.com");
        user.setPassword("password1".toCharArray());
        user.setUsername("user");
        user.create();

        Project project = new Project();
        project.setName(request.getParameter("name"));
        project.setCreated(new Date());

        // Check if project is valid.
        if (project.isValid()) {
            // Save project to database.
            project.create(user);

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + project.getId());

            return;
        }

        // Display the form with validation errors.
        view(response, TEMPLATE_FILE, project);
    }
}
