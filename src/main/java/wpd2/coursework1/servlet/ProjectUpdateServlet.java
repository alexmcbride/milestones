package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProjectUpdateServlet extends BaseServlet{
    private static final String TEMPLATE_FILE = "project_update.vm";

    @Override
    protected void doGet() throws IOException
    {
        // Display the form.

        try
        {
            int id = Integer.valueOf(getRequest().getParameter("id"));

            // Get project
            Project project = Project.find(id);

            // Check for 404 error.
            if (project == null) {
                getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            view(TEMPLATE_FILE, project);
        }
             catch (NumberFormatException e)
             {
                // Crappy request
                getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
    }

    @Override
    protected void doPost() throws IOException {

        //create a project object
        Project prjct = new Project();
        //get id from the form
        String name = getRequest().getParameter("name");
        //get id from form
        int id = Integer.parseInt(getRequest().getParameter("id"));
        prjct.setId(id);

        //set name and id
        prjct.setName(name);
        //update the project name
        prjct.update();


        // Always redirect after post.
        getResponse().sendRedirect("/projects");


        view(TEMPLATE_FILE, prjct);
    }




}
