package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProjectUpdateServlet extends BaseServlet{
    private static final String TEMPLATE_FILE = "project_update.vm";

    @Override
    protected void doGet() throws IOException {
        // Display the form.

        Project prjct = new Project();
        prjct.find(getRequest().getParameter("id"));

        view(TEMPLATE_FILE, prjct);
    }

    @Override
    protected void doPost() throws IOException {

        Project prjct = new Project();
        String name = getRequest().getParameter("id");
        prjct.setName(name);
        prjct.validate();



        String id = getRequest().getParameter("id");

            return;


        view(TEMPLATE_FILE, project);
    }




}
