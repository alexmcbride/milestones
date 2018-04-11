package wpd2.coursework1.servlet;

import org.apache.commons.lang.time.DateUtils;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.viewmodel.MilestonesViewModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectDetailsServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_details.vm";

    @Override
    protected void doGet() throws IOException {
        if (!authorize()) return;

        int id = getRouteId();

        // Get project
        Project project = Project.find(id);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Check user has permission
        User user = userManager.getUser();
        boolean readonly = false;
        if (!project.isOwnedBy(user)) {
            // Check user has permission to view in read-only mode.
            readonly = project.isReadOnly(user);
            if (!readonly) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // get milestones
        List<Milestone> milestones;
        milestones = Milestone.findAll(project.getId());


        Date date = new Date();
        MilestonesViewModel model = new MilestonesViewModel(project, readonly);


        Date datePlusSeven = DateUtils.addDays(date, 7);
        model.setCurrentDatePlusSeven(datePlusSeven);


        model.setDoneMilestones(new ArrayList<>());
        model.setLateMilestones(new ArrayList<>());
        model.setCurrentMilestones(new ArrayList<>());
        model.setUpcomingMilestones(new ArrayList<>());


        model.setProject(project);

        for (Milestone milestone : milestones) {
            // if is complete add to complete list
            if (milestone.isComplete()) {
                model.addDoneMilestone(milestone);
                continue;
            }
            // if is late put in late list
            if (milestone.getDue().before(date) && (!milestone.isComplete())) {
                model.addLateMilestone(milestone);
                continue;
            }
            // if is within 7 days put in current list
            if (milestone.getDue().before(datePlusSeven) && (milestone.getDue().after(date)) && (!milestone.isComplete())) {
                model.addCurrentMilestone(milestone);
                continue;
            }
            // if is within 7 days put in upcoming list
            if (milestone.getDue().after(datePlusSeven) && (!milestone.isComplete())) {
                model.addUpcomingMilestone(milestone);
                continue;
            }

        }

        // model.setDoneMilestones();


        // Render the view.
        view(TEMPLATE_FILE, model);
    }

    protected void doPost() throws IOException {

        // mark milestone as done
        // In finished code user would come from login.
        if (!authorize()) return;
        int milestoneId = Integer.valueOf(request.getParameter("milestoneId"));
        Milestone milestone = Milestone.find(milestoneId);
        int projectId = milestone.getProjectId();
        Project project = Project.find(projectId);


        String formType = request.getParameter("formType");

        if(formType.equals("markedDoneForm")) {



            milestone.setComplete(true);




            // Check if project is valid.
            if (milestone.isValid()) {
                // Save project to database.
                milestone.update();
            }

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + projectId);

            //view(TEMPLATE_FILE, project);
        }
        else
        {

            milestone.setComplete(false);


            // Check if project is valid.
            if (milestone.isValid()) {
                // Save project to database.
                milestone.update();
            }

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + projectId);

            //view(TEMPLATE_FILE, project);

        }

    }
}