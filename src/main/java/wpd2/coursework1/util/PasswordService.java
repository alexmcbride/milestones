package wpd2.coursework1.util;

/**
 * PasswordService for hashing password and authenticating users.
 */
public interface PasswordService {
    /**
     * Salts and hashes a password.
     *
     * @param password the password to hash.
     * @return the hashed password.
     */
    String hash(char[] password);

    /**
     * Authenticate a user.
     *
     * @param password the password submitted in the login form.
     * @param token the password hash from the database.
     * @return true if the passwords match.
     */
    boolean authenticate(char[] password, String token);
}
