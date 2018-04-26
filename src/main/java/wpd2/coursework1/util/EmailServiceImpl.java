package wpd2.coursework1.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Properties;

/**
 * Service used when sending an email.
 */
public class EmailServiceImpl implements EmailService {
    private final MailTransportWrapper transport;

    public EmailServiceImpl() {
        this(new MailTransportWrapper());
    }

    public EmailServiceImpl(MailTransportWrapper transport) {
        super();
        this.transport = transport;
    }

    @Override
    public boolean SendEmailUsingGMailSMTP(String email, String Sbj, String Msg) {
        boolean flag = false;
        // Recipient's email
        String to = email;//change accordingly

        // Sender's email ID needs to be mentioned
        String from = "ip3gcu@gmail.com";
        final String username = "ip3gcu@gmail.com";
        final String password = "jacobsmells";

        // Assuming sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", /*"465""25""805"*/"587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(Sbj);

            // Now set the actual message
            message.setText(Msg);

            // Send message
            transport.send(message);

            return true;

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

