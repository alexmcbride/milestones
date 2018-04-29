package wpd2.coursework1.util;

/**
 * Service used when sending an email.
 */
public interface EmailService {
    /**
     * Sends an email
     *
     * @param email the email to send the message to
     * @param Sbj the subject for the email
     * @param Msg the message contents for the email
     * @return true if the email was sent
     */
    boolean SendEmailUsingGMailSMTP(String email, String Sbj, String Msg);
}
