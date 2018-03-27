package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

import java.util.List;

/*
 * A testable version of a Session that returns various stuff
 */
class TestableSession extends SessionWrapper {
    public User user;
    public List<FlashHelper.FlashMessage> flashMessages;

    public TestableSession() {
        super(null);
    }

    @Override
    public Object getAttribute(String s) {
        if (user != null) {
            return user;
        }

        if (flashMessages != null) {
            return flashMessages;
        }

        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {
        if (s.equals("flash-messages")) {
            flashMessages = (List< FlashHelper.FlashMessage>)o;
        }
    }

    @Override
    public void removeAttribute(String s) {
        if (s.equals("flash-messages")) {
            flashMessages = null;
        }
    }
}
