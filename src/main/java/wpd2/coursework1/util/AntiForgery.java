package wpd2.coursework1.util;

import javax.servlet.http.HttpSession;
import java.util.UUID;

public class AntiForgery {
    private static final String KEY_FORGERY_TOKEN = "keyForgeryToken";
    private final HttpSession session;

    public AntiForgery(HttpSession session) {
        this.session = session;
    }

    /* Called by form_helper */
    public String generateForgeryToken() {
        String token = UUID.randomUUID().toString();
        session.setAttribute(KEY_FORGERY_TOKEN, token);
        return token;
    }

    public void checkForgeryToken(String tokenToCheck) {
        String token = (String)session.getAttribute(KEY_FORGERY_TOKEN);
        if (token != null && !token.equals(tokenToCheck)) {
            throw new AntiForgeryException();
        }
    }

    public class AntiForgeryException extends RuntimeException {
        /* Blank */
    }
}
