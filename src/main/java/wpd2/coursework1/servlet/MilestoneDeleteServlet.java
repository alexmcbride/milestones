package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MilestoneDeleteServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_delete.vm";



    @Override
    protected void doGet() throws IOException {
        // display the form used for deleting a milestone...
        int id = getRouteId();

        // Get milestone
        Milestone milestone = Milestone.find(id);

        // Check for 404 error.
        if (milestone == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }



        view(TEMPLATE_FILE, milestone);
    }

    @Override
    protected void doPost() throws IOException {

        // this method will run if the user has confirmed the deletion of a milestone

        User user = User.dummyUser();

        // get id of milestone
        int id = getRouteId();


        // Get milestone
        Milestone milestone = Milestone.find(id);

        // get project of milestone
        Project project = Project.find(milestone.getProjectId());


        // Check if project is valid.
        if (milestone.isValid()) {
            // Save project to database.
            milestone.delete();

            // Always redirect after post.
            response.sendRedirect("/projects/details/" + project.getId());

            return;
        }


        view(TEMPLATE_FILE, milestone);
    }
}
