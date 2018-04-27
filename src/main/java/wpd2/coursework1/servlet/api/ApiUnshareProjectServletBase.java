package wpd2.coursework1.servlet.api;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.SharedProject;
import wpd2.coursework1.model.User;
import wpd2.coursework1.servlet.BaseJsonServlet;
import wpd2.coursework1.servlet.JsonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiUnshareProjectServletBase extends BaseJsonServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        int userId = Integer.valueOf(request.getParameter("userId"));
        int projectId = Integer.valueOf(request.getParameter("projectId"));

        Project project = Project.find(projectId);
        if (!authorize(project)) return;

        SharedProject sharedProject = SharedProject.find(userId, projectId);
        if (sharedProject == null) {
            json(new JsonResponse("The shared project was not found"));
        }
        else {
            User user = User.find(userId);
            sharedProject.delete();
            json(new UnshareResponse("The project has been unshared with " + html.encode(user.getUsername()), userId));
        }
    }

    private class UnshareResponse extends JsonResponse {
        private int id;

        UnshareResponse(String message, int id) {
            super(true, message);
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
