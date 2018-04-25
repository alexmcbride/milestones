package wpd2.coursework1.util;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class EmailServiceTest {
    @Test
    public void testSendEmailUsingGMailSMTP() {
        EmailService em = new EmailService();
        assertTrue(em.SendEmailUsingGMailSMTP("mietta25@gmail.com","test","test message"));
    }
}