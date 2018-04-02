package wpd2.coursework1.util;

import javax.servlet.http.HttpSession;

/*
 * Wrapper to make HttpSession testable.
 */
public class SessionWrapper {
    private final HttpSession session;

    public SessionWrapper(HttpSession session) {
        this.session = session;
    }

    public Object getAttribute(String s) {
        return session.getAttribute(s);
    }

    public void removeAttribute(String s) {
        session.removeAttribute(s);
    }

    public void setAttribute(String s, Object o) {
        session.setAttribute(s, o);
    }
}