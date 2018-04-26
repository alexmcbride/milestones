package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiMakePublicServlet extends BaseJsonServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        try {
            int projectId = Integer.valueOf(request.getParameter("projectId"));
            Project project = Project.find(projectId);
            project.toggleOpen();
            project.update();

            if (project.isOpen()) {
                json(new IsPublicResponse("Project '" + html.encode(project.getName()) + "' is now public", project.isOpen(), project.getId()));
            }
            else {
                json(new IsPublicResponse("Project '" + html.encode(project.getName()) + "' is no longer public", project.isOpen(), project.getId()));
            }
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    class IsPublicResponse extends JsonResponse {
        private boolean open;
        private int projectId;

        public IsPublicResponse(String message, boolean open, int projectId) {
            super(true, message);
            this.open = open;
            this.projectId = projectId;
        }

        public boolean isOpen() {
            return open;
        }

        public int getProjectId() {
            return projectId;
        }
    }
}
