package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MilestoneIndexServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_index.vm";

    @Override
    protected void doGet() throws IOException {
        // In finished code this will come from login.
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

        // Render the view.
        view(TEMPLATE_FILE, projects);
    }
}
