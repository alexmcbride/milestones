package wpd2.coursework1.servlet;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.SharedProject;
import wpd2.coursework1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShareProjectServlet extends JsonServlet {
    protected void doPost() throws IOException {
//        try {
            int userId = Integer.valueOf(request.getParameter("userId"));
            int projectId = Integer.valueOf(request.getParameter("projectId"));

            if (userId == userManager.getUserId()) {
                view(new JsonResponse("You cannot share a project with yourself"));
                return;
            }

            User user = User.find(userId);
            Project project = Project.find(projectId);

            SharedProject sharedProject = SharedProject.find(user, project);
            if (sharedProject != null) {
                view(new JsonResponse("This project has already been shared with " + user.getUsername()));
            }
            else {
                sharedProject = new SharedProject();
                sharedProject.create(project, user);
                view(new UserResponse(user.getUsername(), user.getId()));
            }
//        }
//        catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//        }
    }

    private class UserResponse extends JsonResponse {
        private String username;
        private int id;

        public UserResponse(String username, int id) {
            super(true, null);
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
