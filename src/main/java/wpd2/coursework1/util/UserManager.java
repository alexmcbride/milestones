package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpSession;

public class UserManager {
    private final User user;

    public UserManager(HttpSession session) {
        // Change to user attribute in session.
        this.user = (User)session.getAttribute("user");;
    }

    public User getUser() {
        return user;
    }

    public boolean getLoggedIn() {
        return user != null;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getUsername() {
        return user.getUsername();
    }
}
