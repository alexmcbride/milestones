package wpd2.coursework1.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService{
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
/*        props.setProperty("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");*/
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
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(Sbj);

            // Now set the actual message
            message.setText(Msg);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");
            return true;

        } catch (MessagingException e) { throw new RuntimeException(e);

        }

    }
}

