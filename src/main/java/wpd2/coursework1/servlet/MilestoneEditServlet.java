package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MilestoneEditServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_edit.vm";



    @Override
    protected void doGet() throws IOException {
        // Display the form.
        int id = Integer.valueOf(request.getParameter("id"));

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
        User user = User.dummyUser();


        // get milestone id
        int id = Integer.valueOf(request.getParameter("id"));

        // get milestone
        Milestone milestoneToUpdate = Milestone.find(id);


        milestoneToUpdate.setName(request.getParameter("name"));


        // Overly complex approach to getting date value from form
        String stringDueDate = String.valueOf(request.getParameter("due"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(stringDueDate, formatter);
        Date date1 = java.sql.Date.valueOf(date);
        milestoneToUpdate.setDue(date1);



        // Get project
        Project project = Project.find(milestoneToUpdate.getProjectId());


        // Check if project is valid.
        if (milestoneToUpdate.isValid()) {
            // Save project to database.
            milestoneToUpdate.update();

            // Always redirect after post.
            response.sendRedirect("/projects/details?id=" + project.getId());

            return;
        }


        view(TEMPLATE_FILE, milestoneToUpdate);
    }
}
