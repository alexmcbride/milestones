package wpd2.coursework1.util;

import java.util.UUID;

/*
 * Protects against CSRF by using a synchronizer token. We generate a token and store it in
 * the user's session, unique to that session. Each time we create a form we store this token
 * in a hidden input, which we then check on submit. This prevents people spoofing the form.
 * https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)_Prevention_Cheat_Sheet
 */
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
        if (token == null || !token.equals(tokenToCheck)) {
            String message = "Request blocked due to anti-forgery token check";
            throw new AntiForgeryException(message);
        }
    }

    public class AntiForgeryException extends RuntimeException {
        public AntiForgeryException(String message) {
            super(message);
        }
    }
}
