package wpd2.coursework1.servlet;

import org.joda.time.DateTime;
import wpd2.coursework1.model.Project;

import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static java.time.LocalDate.*;
import static java.time.format.DateTimeFormatter.*;

public class MilestoneCreateServlet extends BaseServlet {
    private static final String TEMPLATE_FILE = "milestone_create.vm";

    @Override
    protected void doGet() throws IOException {
        // Display the form.
        int id = getRouteId();

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
        int id = getRouteId();

        // Get project
        Project project = Project.find(id);
        Milestone milestone = new Milestone();
        milestone.setName(request.getParameter("name"));


        // Overly complex approach to getting date value from form
        String stringDueDate = String.valueOf(request.getParameter("due"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(stringDueDate, formatter);
        Date date1 = java.sql.Date.valueOf(date);
        milestone.setDue(date1);


        // Check if project is valid.
        if (milestone.isValid()) {
            // Save project to database.
            milestone.create(project);

            // Always redirect after post.
            response.sendRedirect("/projects/details/" + project.getId());

            return;
        }


        view(TEMPLATE_FILE, milestone);
    }
}
