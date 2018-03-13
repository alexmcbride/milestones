package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateProjectServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "create_project.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Display the form.
        view(response, TEMPLATE_FILE, new Project());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Project project = new Project();
        project.setName(request.getParameter("name"));

        // Check if project is valid.
        if (project.isValid()) {
            // Save project to session.
            project.save(request.getSession());

            // Always redirect after post.
            response.sendRedirect("/projects");

            return;
        }

        // Display the form with validation errors.
        view(response, TEMPLATE_FILE, project);
    }
}
