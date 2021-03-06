package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class MilestoneEditServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_edit.vm";

    @SuppressWarnings("Duplicates")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        int id = getRouteId();

        // Get milestone
        Milestone milestone = Milestone.find(id);
        if (milestone == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Make sure user has permission to view.
        if (!authorize(milestone)) return;

        view(TEMPLATE_FILE, milestone);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        // get milestone id
        int id = getRouteId();

        // get milestone
        Milestone milestoneToUpdate = Milestone.find(id);
        milestoneToUpdate.setName(request.getParameter("name"));
        milestoneToUpdate.setDue(request.getParameter("due"));
        milestoneToUpdate.setActual(request.getParameter("actual"));

        // Get project
        Project project = Project.find(milestoneToUpdate.getProjectId());

        // Check if project is valid.
        if (milestoneToUpdate.isValid()) {
            // Save project to database.
            milestoneToUpdate.update();

            flash.message("Milestone '" + milestoneToUpdate.getName() + "' edited");

            // Always redirect after post.
            response.sendRedirect(response.encodeURL("/projects/details/" + project.getId()));

            return;
        }

        view(TEMPLATE_FILE, milestoneToUpdate);
    }
}
