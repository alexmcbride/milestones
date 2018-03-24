package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

import javax.servlet.http.HttpSession;

public class LoginManager {
    private final HttpSession session;

    public LoginManager(HttpSession session) {
        this.session = session;
    }

    public boolean isLoggedIn() {
        return session.getAttribute("user") != null;
    }

    public void logout() {
        session.removeAttribute("user");
    }

    public void login(User user) {
        session.setAttribute("user", user);
    }

    public User getUser() {
        return (User)session.getAttribute("user");
    }
}
