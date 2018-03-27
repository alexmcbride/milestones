package wpd2.coursework1.servlet;

import org.joda.time.DateTime;
import wpd2.coursework1.model.Project;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class MilestoneCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_create.vm";



    @Override
    protected void doGet() throws IOException {
        // Display the form.
        int id = Integer.valueOf(request.getParameter("id"));

        // Get project
        Project project = Project.find(id);

        // Check for 404 error.
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }



        view(TEMPLATE_FILE, project);
    }

    @Override
    protected void doPost() throws IOException {
        User user = User.dummyUser();


        int id = Integer.valueOf(request.getParameter("id"));

        // Get project
        Project project = Project.find(id);
        Milestone milestone = new Milestone();
        milestone.setName(request.getParameter("name"));
        milestone.setDue(new Date());


        // Check if project is valid.
        if (milestone.isValid()) {
            // Save project to database.
            milestone.create(project);

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + project.getId());

            return;
        }


        view(TEMPLATE_FILE, milestone);
    }
}
