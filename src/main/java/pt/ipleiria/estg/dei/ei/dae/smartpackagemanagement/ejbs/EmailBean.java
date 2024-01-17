package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.ejbs;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "EmailEJB")
public class EmailBean {
    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;
    private static final Logger logger = Logger.getLogger("EmailBean.logger");

    public void send(String to, String subject, String text) {
        Thread emailJob = new Thread(() -> {
            Message message = new MimeMessage(mailSession);
            Date timestamp = new Date();
            try {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to, false));
                message.setSubject(subject);
                message.setText(text);
                message.setSentDate(timestamp);
                Transport.send(message);
            } catch (MessagingException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
        emailJob.start();
    }
}