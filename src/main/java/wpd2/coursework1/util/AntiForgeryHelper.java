package wpd2.coursework1.util;

import java.util.UUID;

public class AntiForgeryHelper {
    public static final String KEY_FORGERY_TOKEN = "anti-forgery-token";
    private final SessionWrapper session;

    public AntiForgeryHelper(SessionWrapper session) {
        this.session = session;
    }

    public String generateToken() {
        String token = (String)session.getAttribute(KEY_FORGERY_TOKEN);
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute(KEY_FORGERY_TOKEN, token);
        }
        return token;
    }

    public void checkToken(String tokenToCheck) {
        String token = (String)session.getAttribute(KEY_FORGERY_TOKEN);
        session.removeAttribute(KEY_FORGERY_TOKEN); // !important
        if (token == null || !token.equals(tokenToCheck)) {
            String message = "You must include the antiForgeryToken() form helper";
            throw new AntiForgeryException(message);
        }
    }

    public class AntiForgeryException extends RuntimeException {
        public AntiForgeryException(String message) {
            super(message);
        }
    }
}
