package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;
import wpd2.coursework1.viewmodel.ProjectViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectIndexServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "project_index.vm";

    @Override
    protected void doGet() throws IOException {
        // In finished code user would come from login.
        User user = User.find(1);
        if (user == null) {
            user = new User();
            user.setUsername("user1");
            user.setEmail("user@email.com");
            user.setPassword("password1".toCharArray());
            user.create();
        }

        // Get list of projects.
        List<Project> projects = Project.findAll(user);

        // Make view models.
        List<ProjectViewModel> models = new ArrayList<>();
        for (Project project : projects) {
            models.add(new ProjectViewModel(project));
        }

        // Render the view.
        view(TEMPLATE_FILE, models);
    }
}
