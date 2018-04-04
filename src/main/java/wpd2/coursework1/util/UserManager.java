package wpd2.coursework1.util;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.SharedProject;
import wpd2.coursework1.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {
    public static final String KEY_USER = "user";
    private final SessionWrapper session;

    public UserManager(SessionWrapper session) {
        this.session = session;
    }

    public User getUser() {
        return (User)session.getAttribute(KEY_USER);
    }

    @Deprecated
    public boolean getLoggedIn() {
        return getUser() != null;
    }

    public boolean isLoggedIn() {
        return getUser() != null;
    }

    public String getEmail() {
        User user = getUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    public String getUsername() {
        User user = getUser();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public void login(User user) {
        session.setAttribute(KEY_USER, user);
    }

    public void logout() {
        session.removeAttribute(KEY_USER);
    }

    public String generateEmailToken() {
        return UUID.randomUUID().toString();
    }

    public List<Project> getSharedProjects() {
        List<Project> projects = new ArrayList<>();
        if (isLoggedIn()) {
            User user = getUser();
            List<SharedProject> sharedProjects = SharedProject.findAll(user);
            for (SharedProject sharedProject : sharedProjects) {
                Project project = Project.find(sharedProject.getProjectId());
                projects.add(project);
            }
            return projects;
        }
        return projects;
    }

    public List<User> getSharedUsers(Project project) {
        List<User> users = new ArrayList<>();
        List<SharedProject> sharedProjects = SharedProject.findAll(project);
        for (SharedProject sharedProject : sharedProjects) {
            User user = User.find(sharedProject.getUserId());
            users.add(user);
        }
        return users;
    }
}
