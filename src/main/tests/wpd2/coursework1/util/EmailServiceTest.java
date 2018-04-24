package wpd2.coursework1.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {


    @Test
    void testSendEmailUsingGMailSMTP() {
        EmailService em = new EmailService();
        assertTrue(em.SendEmailUsingGMailSMTP("mietta25@gmail.com","test","test message"));
    }
}