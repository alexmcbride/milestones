package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.viewmodel.ProjectListViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ProjectListServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_list.vm";

    // Helper for storing projects in session.
    private List<Project> loadProjects(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Project> projects = (List<Project>)session.getAttribute("projects");

        // Create new if doesn't exist.
        if (projects == null) {
            projects = Project.loadAll();
            session.setAttribute("projects", projects);
        }

        return projects;
    }

    // Helper for adding project to session.
    private void addProject(HttpServletRequest request, Project project) {
        List<Project> projects = loadProjects(request);
        projects.add(project);
        request.getSession().setAttribute("projects", projects);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get list of projects.
        List<Project> projects = loadProjects(request);

        // Render the view.
        view(response, TEMPLATE_FILE, new ProjectListViewModel(new Project(), projects));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Create project.
        Project project = new Project();
        project.setName(request.getParameter("name"));

        if (project.isValid()) {
            // Add to list in session.
            addProject(request, project);

            // Always redirect after post.
            response.sendRedirect("/projects");
        }
        else {
            // Render the view to display errors.
            List<Project> projects = loadProjects(request);
            view(response, TEMPLATE_FILE, new ProjectListViewModel(project, projects));
        }
    }
}
