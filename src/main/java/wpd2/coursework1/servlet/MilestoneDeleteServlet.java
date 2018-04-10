package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MilestoneDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_delete.vm";

    @Override
    protected void doGet() throws IOException {
        if (!authorize()) return;

        Milestone milestone = Milestone.find(getRouteId());
        if (milestone == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!authorize(milestone)) return;

        view(TEMPLATE_FILE, milestone);
    }

    @Override
    protected void doPost() throws IOException {
        int id = getRouteId();
        Milestone milestone = Milestone.find(id);

        Project project = Project.find(milestone.getProjectId());

        if (milestone.isValid()) {
            // Save project to database.
            milestone.delete();

            // Always redirect after post.
            response.sendRedirect("/projects/details/" + project.getId());

            return;
        }

        view(TEMPLATE_FILE, milestone);
    }
}
