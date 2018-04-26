package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

import java.util.UUID;

/*
 * Class to help manage a user, login in and out etc.
 */
public class UserManager {
    public static final String KEY_USER = "user";
    private final SessionWrapper session;

    /**
     * Creates a new UserManager.
     *
     * @param session the user's current session.
     */
    public UserManager(SessionWrapper session) {
        this.session = session;
    }

    /**
     * Gets the user from the session.
     *
     * @return a user object.
     */
    public User getUser() {
        return (User)session.getAttribute(KEY_USER);
    }

    /**
     * Gets the ID of the currently logged in user, or 0 if not logged in.
     *
     * @return the user ID.
     */
    public int getUserId() {
        User user = getUser();
        if (user != null) {
            return user.getId();
        }
        return 0;
    }

    /**
     * Gets if the user is logged in.
     *
     * @return true if the user is logged in.
     */
    @Deprecated
    public boolean getLoggedIn() {
        return getUser() != null;
    }

    /**
     * Gets if the user is logged in.
     *
     * @return true if the user is logged in.
     */
    public boolean isLoggedIn() {
        return getUser() != null;
    }

    /**
     * Gets the logged in user's email address.
     *
     * @return the email address or null.
     */
    public String getEmail() {
        User user = getUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    /**
     * Gets the logged in user's username.
     *
     * @return the username or null.
     */
    public String getUsername() {
        User user = getUser();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    /**
     * Logs the user in by saving them to the session.
     *
     * @param user the user to login.
     */
    public void login(User user) {
        session.setAttribute(KEY_USER, user);
    }

    /**
     * Removes a user from the session.
     */
    public void logout() {
        session.removeAttribute(KEY_USER);
    }

    /**
     * Generates a email activation token.
     *
     * @return the token.
     */
    public String generateEmailToken() {
        return UUID.randomUUID().toString();
    }
}
