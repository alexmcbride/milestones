package wpd2.coursework1.util;

import javax.servlet.http.HttpSession;

/*
 * Helper wrapper to make HttpSession class testable.
 */
public class SessionWrapper {
    private final HttpSession session;

    /**
     * Creates a new SessionWrapper
     *
     * @param session the user's session.
     */
    public SessionWrapper(HttpSession session) {
        this.session = session;
    }

    /**
     * Gets an attribute from the session.
     *
     * @param s the attribute to get.
     * @return the result.
     */
    public Object getAttribute(String s) {
        return session.getAttribute(s);
    }

    /**
     * Remove attribute from session.
     *
     * @param s the attribute to remove
     */
    public void removeAttribute(String s) {
        session.removeAttribute(s);
    }

    /**
     * Set an attribute in the session.
     *
     * @param s the attribute to set
     * @param o the value to set it with
     */
    public void setAttribute(String s, Object o) {
        session.setAttribute(s, o);
    }
}