package wpd2.coursework1.util;

import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;

import java.io.IOException;

import static junit.framework.TestCase.*;

public class EmailServiceTest {
    @Test
    public void testSendEmailUsingGMailSMTP() {
        TestableTransportWrapper transport = new TestableTransportWrapper();
        EmailService em = new EmailServiceImpl(transport);

        assertTrue(em.SendEmailUsingGMailSMTP("mietta25@gmail.com","test","test message"));
        assertEquals(transport.message, "test message");
        assertEquals(transport.subject, "test");
        assertEquals(transport.email, "mietta25@gmail.com");
    }

    /**
     * A testable version of the transport wrapper, that stores the last message sent.
     */
    private class TestableTransportWrapper extends MailTransportWrapper {
        String email;
        String subject;
        String message;

        @Override
        public void send(Message message) throws MessagingException, IOException {
            this.message = (String)message.getContent();
            this.subject = message.getSubject();
            this.email = message.getAllRecipients()[0].toString();
        }
    }
}