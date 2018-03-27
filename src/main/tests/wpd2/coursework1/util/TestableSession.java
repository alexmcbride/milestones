package wpd2.coursework1.util;

import wpd2.coursework1.model.User;

import java.util.List;

/*
 * A testable version of a Session that returns various stuff
 */
class TestableSession extends SessionWrapper {
    public User user;
    public List<FlashHelper.FlashMessage> flashMessages;
    public String antiForgeryToken;

    public TestableSession() {
        super(null);
    }

    @Override
    public Object getAttribute(String s) {
        if (s.equals(User.KEY_USER)) {
            return user;
        }

        if (s.equals(FlashHelper.KEY_FLASH_MESSAGES)) {
            return flashMessages;
        }

        if (s.equals(AntiForgeryHelper.KEY_FORGERY_TOKEN)) {
            return antiForgeryToken;
        }

        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {
        if (s.equals(FlashHelper.KEY_FLASH_MESSAGES)) {
            flashMessages = (List< FlashHelper.FlashMessage>)o;
        }
        else if (s.equals(AntiForgeryHelper.KEY_FORGERY_TOKEN)) {
            antiForgeryToken = (String)o;
        }
    }

    @Override
    public void removeAttribute(String s) {
        if (s.equals(FlashHelper.KEY_FLASH_MESSAGES)) {
            flashMessages = null;
        }
        else if (s.equals(AntiForgeryHelper.KEY_FORGERY_TOKEN)) {
            antiForgeryToken = null;
        }
    }
}
