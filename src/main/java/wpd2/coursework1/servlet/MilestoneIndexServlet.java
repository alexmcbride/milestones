package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import java.io.IOException;
import java.util.List;

public class MilestoneIndexServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_index.vm";

    @Override
    protected void doGet() throws IOException {
        // Get list of projects.
        User user = userManager.getUser();
        List<Project> projects = Project.findAll(user);

        // Render the view.
        view(TEMPLATE_FILE, projects);
    }
}
