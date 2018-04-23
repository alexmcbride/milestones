package wpd2.coursework1.helper;

import java.util.UUID;

/*
 * Protects against CSRF by using a synchronizer token. We generate a token and store it unique
 * to that the user's session. Each time we create a form we store this token in a hidden input,
 * which we then check on form submit. This prevents people spoofing the form.
 * https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)_Prevention_Cheat_Sheet
 */
public class AntiForgeryHelper {
    public static final String KEY_FORGERY_TOKEN = "anti-forgery-token";
    private final SessionWrapper session;

    public AntiForgeryHelper(SessionWrapper session) {
        this.session = session;
    }

    /**
     * Get the current synchronizer token for this user's session.
     *
     * @return the token
     */
    public String getToken() {
        String token = (String)session.getAttribute(KEY_FORGERY_TOKEN);
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute(KEY_FORGERY_TOKEN, token);
        }
        return token;
    }

    /**
     * Throws an exception if the supplied token doesn't matches the one in the user's session.
     *
     * @param tokenToCheck the token to check
     */
    public void checkToken(String tokenToCheck) {
        String token = (String)session.getAttribute(KEY_FORGERY_TOKEN);
        if (token == null || !token.equals(tokenToCheck)) {
            String message = "Request blocked due to anti-forgery token check";
            throw new AntiForgeryException(message);
        }
    }


    /**
     * Wee custom exception.
     */
    public class AntiForgeryException extends RuntimeException {
        /**
         * Creates a new AntiForgeryException
         *
         * @param message the message
         */
        public AntiForgeryException(String message) {
            super(message);
        }
    }
}
