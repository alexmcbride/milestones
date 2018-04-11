package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.Milestone;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class MilestoneCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_create.vm";

    @Override
    protected void doGet() throws IOException {
        // Get Project
        int id = getRouteId();
        Project project = Project.find(id);

        if (!authorize(project)) return;

        // Check for 404 error.
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Milestone milestone = new Milestone();
        milestone.setDue(new Date());
        milestone.setProjectId(project.getId());
        view(TEMPLATE_FILE, milestone);
    }

    @Override
    protected void doPost() throws IOException {
        // Get project
        int id = getRouteId();
        Project project = Project.find(id);

        Milestone milestone = new Milestone();
        milestone.setName(request.getParameter("name"));
        milestone.setDue(request.getParameter("due"));

        if (milestone.isValid()) {
            milestone.create(project);

            // Always redirect after post.
            response.sendRedirect("/projects/details/" + project.getId());

            return;
        }

        milestone.setProjectId(project.getId());
        view(TEMPLATE_FILE, milestone);
    }
}
