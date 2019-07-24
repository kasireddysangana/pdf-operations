package com.itworks.pdfoperations.service;

import com.itextpdf.text.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    Environment environment;

    private static Logger logger = LoggerFactory.getLogger(MailService.class);

    public String sendMail(String filePath, Document document, String emailId) throws MessagingException {
        logger.info("PDFService :: sendMail : Started...");
        Properties props = new Properties();
        String returnMessage = "PDF Created Successfully, but unable to Send Mail";


        props.put("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.host", environment.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", environment.getProperty("mail.smtp.port"));

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(environment.getProperty("mail.username"),
                                environment.getProperty("mail.password"));
                    }
                });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(environment.getProperty("mail.fromAddress")));

            message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(emailId));

            message.setSubject(environment.getProperty("mail.subject"));
            String body = environment.getProperty("mail.body");

            message.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(body);
            messageBodyPart1.setContent(body, "text/html; charset=utf-8");
            message.setContent(multipart);

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());

            messageBodyPart2.setFileName(fileName);

            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            Transport.send(message);
            returnMessage = "PDF Created and Mail Send Successfully;" +filePath;
            logger.info("PDFService :: sendMail : Success Response :"+returnMessage);
        return returnMessage;
    }
}
