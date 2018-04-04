package wpd2.coursework1.servlet;

import wpd2.coursework1.model.SharedProject;

import java.io.IOException;

public class UnshareProjectServlet extends JsonServlet {
    protected void doPost() throws IOException {
        int userId = Integer.valueOf(request.getParameter("userId"));
        int projectId = Integer.valueOf(request.getParameter("projectId"));

        SharedProject sharedProject = SharedProject.find(userId, projectId);
        if (sharedProject == null) {
            view(new JsonResponse("The shared project was not found"));
        }
        else {
            sharedProject.delete();
            view(new UnshareResponse("The project has been unshared", userId));
        }
    }

    private class UnshareResponse extends JsonResponse {
        private int id;

        public UnshareResponse(String message, int id) {
            super(true, message);
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
