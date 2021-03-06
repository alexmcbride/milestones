package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.SharedProject;
import wpd2.coursework1.model.User;
import wpd2.coursework1.viewmodel.MilestonesViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ProjectDetailsServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_details.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        int id = getRouteId();

        // Get project
        Project project = Project.find(id);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Figure out if this user can view the project.
        User user = userManager.getUser();
        boolean readonly = true;
        boolean authorized = false;

        // If project is open, let anyone view it.
        if (project.isPubliclyViewable()) {
            authorized = true;
            readonly = true;

            // This belongs to me, so let me edit it.
            if (userManager.isLoggedIn() && project.isOwnedBy(user)) {
                readonly = false;
            }
        }
        else if (userManager.isLoggedIn()) {
            // This project has been shared with me.
            SharedProject sharedProject = SharedProject.find(user, project);
            if (sharedProject != null) {
                authorized = true;
                readonly = true;

                sharedProject.setViewed(new Date());
                sharedProject.update();
            }
            // I'm logged in and this is my project.
            else if (project.isOwnedBy(user)) {
                authorized = true;
                readonly = false;
            }
        }

        if (!authorized) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // get milestones view model
        MilestonesViewModel model = new MilestonesViewModel(project, readonly);

        List<Milestone> milestones = Milestone.findAll(project.getId());
        for (Milestone milestone : milestones) {
            if (milestone.isComplete()) {
                model.addDoneMilestone(milestone);
            }
            else if (milestone.isLate()) {
                model.addLateMilestone(milestone);
            }
            else if (milestone.isCurrentWeek()) {
                model.addCurrentMilestone(milestone);
            }
            else if (milestone.isUpcoming()) {
                model.addUpcomingMilestone(milestone);
            }
        }

        // Render the view.
        view(TEMPLATE_FILE, model);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        if (!authorize()) return;

        int milestoneId = Integer.valueOf(request.getParameter("milestoneId"));

        // Check if project is valid.
        Milestone milestone = Milestone.find(milestoneId);
        milestone.toggleComplete();
        milestone.update();

        if (milestone.isComplete()) {
            flash.message("Set milestone '" + milestone.getName() + "' to complete");
        }
        else {
            flash.message("Set milestone '" + milestone.getName() + "' to incomplete");
        }

        // Always redirect after post.
        response.sendRedirect("/projects/details/" + milestone.getProjectId());
    }
}