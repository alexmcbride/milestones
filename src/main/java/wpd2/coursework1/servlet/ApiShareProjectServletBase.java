package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.SharedProject;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiShareProjectServletBase extends BaseJsonServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doPost(request, response);

        try {
            int userId = Integer.valueOf(request.getParameter("userId"));
            int projectId = Integer.valueOf(request.getParameter("projectId"));

            if (userId == userManager.getUserId()) {
                json(new JsonResponse("You cannot share a project with yourself"));
                return;
            }

            User user = User.find(userId);
            Project project = Project.find(projectId);

            if (!authorize(project)) return;

            SharedProject sharedProject = SharedProject.find(user, project);
            if (sharedProject != null) {
                json(new JsonResponse("This project has already been shared with " + user.getUsername()));
            }
            else {
                sharedProject = new SharedProject();
                sharedProject.create(project, user);
                json(new UserResponse(user.getUsername(),
                        user.getId(),
                        "This project has been shared with " + user.getUsername()));
            }
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private class UserResponse extends JsonResponse {
        private String username;
        private int id;

        UserResponse(String username, int id, String message) {
            super(true, message);
            this.username = username;
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public int getId() {
            return id;
        }
    }
}
