package wpd2.coursework1.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.io.IOException;

/**
 * Wrapper to make mail transport class testable.
 */
public class MailTransportWrapper {
    /**
     * Sends a message on the mail transport.
     *
     * @param message The message to send.
     * @throws MessagingException error
     * @throws IOException error
     */
    public void send(Message message) throws MessagingException, IOException {
        Transport.send(message);
    }
}
