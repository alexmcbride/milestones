package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

import java.util.UUID;

/*
 * Class to help manage a user, login in and out etc.
 */
public class UserManager {
    public static final String KEY_USER = "user";
    private final SessionWrapper session;

    public UserManager(SessionWrapper session) {
        this.session = session;
    }

    public User getUser() {
        return (User)session.getAttribute(KEY_USER);
    }

    public int getUserId() {
        User user = getUser();
        if (user != null) {
            return user.getId();
        }
        return 0;
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
}
