package wpd2.coursework1.util;

import javax.servlet.http.HttpSession;
import java.util.UUID;

public class AntiForgeryHelper {
    private static final String KEY_FORGERY_TOKEN = "keyForgeryToken";
    private final HttpSession session;

    public AntiForgeryHelper(HttpSession session) {
        this.session = session;
    }

    public String generateToken() {
        String token = UUID.randomUUID().toString();
        session.setAttribute(KEY_FORGERY_TOKEN, token);
        return token;
    }

    public void checkToken(String tokenToCheck) {
        String token = (String)session.getAttribute(KEY_FORGERY_TOKEN);
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
