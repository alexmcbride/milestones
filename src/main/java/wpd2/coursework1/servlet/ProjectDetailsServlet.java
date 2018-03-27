package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.viewmodel.MilestonesViewModel;
import wpd2.coursework1.viewmodel.ProjectViewModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
        milestones = Milestone.findAll(project);
        MilestonesViewModel model = new MilestonesViewModel(project);

        for (Milestone milestone : milestones) {
            model.addMilestone(milestone);
        }

        // Render the view.
        view(TEMPLATE_FILE, model);
    }
}