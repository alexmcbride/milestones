package wpd2.coursework1.servlet;

import org.apache.commons.lang.time.DateUtils;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.viewmodel.MilestonesViewModel;
import wpd2.coursework1.viewmodel.ProjectViewModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectDetailsServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_details.vm";

    @Override
    protected void doGet() throws IOException {


        User user = User.dummyUser();
        int id = Integer.valueOf(request.getParameter("id"));

        // Get project
        Project project = Project.find(id);


        // get milestones
        List<Milestone> milestones;
        milestones = Milestone.findAll(project.getId());


        Date date = new Date();
        MilestonesViewModel model = new MilestonesViewModel(project);


        Date datePlusSeven = DateUtils.addDays(date, 7);

        model.setCurrentDatePlusSeven(datePlusSeven);


        for (Milestone milestone : milestones) {
            // if is complete add to complete list
            if(milestone.isComplete()) {
                model.addDoneMilestone(milestone);
                continue;
            }
            // if is late put in late list
            if(milestone.getDue().before(date) && (milestone.isComplete()==false))
            {
                model.addLateMilestone(milestone);
                continue;
            }
            // if is within 7 days put in current list
            if(milestone.getDue().before(datePlusSeven) && (milestone.getDue().after(date))
                    && (milestone.isComplete()==false))
            {
                model.addCurrentMilestone(milestone);
                continue;
            }
            // if is within 7 days put in upcoming list
            if(milestone.getDue().after(datePlusSeven) && (milestone.isComplete()==false))
            {
                model.addUpcomingMilestone(milestone);
                continue;
            }

        }

        // model.setDoneMilestones();


        // Render the view.
        view(TEMPLATE_FILE, model);
    }
}