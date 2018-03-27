package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

/*
 * A testable version of a Session that returns various stuff
 */
class TestableSession extends SessionWrapper {
    public User user;

    public TestableSession() {
        super(null);
    }

    @Override
    public Object getAttribute(String s) {
        if (user != null) {
            return user;
        }
        return null;
    }
}
