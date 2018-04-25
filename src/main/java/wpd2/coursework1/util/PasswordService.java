package wpd2.coursework1.util;

public interface PasswordService {
    String hash(char[] password);

    boolean authenticate(char[] password, String token);
}
