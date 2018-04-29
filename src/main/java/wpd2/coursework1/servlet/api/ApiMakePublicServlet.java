package wpd2.coursework1.servlet.api;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.servlet.BaseJsonServlet;
import wpd2.coursework1.servlet.JsonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiMakePublicServlet extends BaseJsonServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        try {
            int projectId = Integer.valueOf(request.getParameter("projectId"));
            Project project = Project.find(projectId);
            project.togglePubliclyViewable();
            project.update();

            String message = project.isPubliclyViewable() ?
                    "Project '" + html.encode(project.getName()) + "' is public, now anyone can view it!" :
                    "Project '" + html.encode(project.getName()) + "' is no longer public";

            json(new IsPublicResponse(message, project.isPubliclyViewable(), project.getId()));
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    class IsPublicResponse extends JsonResponse {
        private boolean publiclyViewable;
        private int projectId;

        IsPublicResponse(String message, boolean publiclyViewable, int projectId) {
            super(true, message);
            this.publiclyViewable = publiclyViewable;
            this.projectId = projectId;
        }

        public boolean isPubliclyViewable() {
            return publiclyViewable;
        }

        public int getProjectId() {
            return projectId;
        }
    }
}
