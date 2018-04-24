package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProjectUpdateServlet extends BaseServlet{
    private static final String TEMPLATE_FILE = "project_update.vm";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        try
        {
            int id = getRouteId();

            // Get project
            Project project = Project.find(id);

            // Check for 404 error.
            if (project == null) {
                getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (!authorize(project)) return;

            view(TEMPLATE_FILE, project);
        }
             catch (NumberFormatException e)
             {
                // Crappy request
                getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);


        int id = getRouteId();
        Project project = Project.find(id);
        //set name and id
        project.setName(getRequest().getParameter("name"));
        //update the project name
        if (project.isValid()) {
            project.update();

            // Always redirect after post.
            getResponse().sendRedirect(response.encodeURL("/projects"));

            return;
        }


        view(TEMPLATE_FILE, project);
    }




}
