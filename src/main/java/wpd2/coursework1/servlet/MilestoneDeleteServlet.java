package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MilestoneDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_delete.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        int id = getRouteId();
        Milestone milestone = Milestone.find(id);
        if (milestone == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!authorize(milestone)) return;

        view(TEMPLATE_FILE, milestone);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        int id = getRouteId();
        Milestone milestone = Milestone.find(id);

        Project project = Project.find(milestone.getProjectId());

        if (milestone.isValid()) {
            // Save project to database.
            milestone.delete();

            flash.message("Milestone '" + milestone.getName() + "' deleted");

            // Always redirect after post.
            response.sendRedirect(response.encodeURL("/projects/details/" + project.getId()));

            return;
        }

        view(TEMPLATE_FILE, milestone);
    }
}
