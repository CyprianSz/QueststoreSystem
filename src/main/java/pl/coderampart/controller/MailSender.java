package pl.coderampart.controller;

import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private String host;
    private String port;
    private String senderEmailAddress;
    private String senderEmailPassword;
    private boolean debug;
    private Properties properties;

    public MailSender() {
        this.host = "smtp.gmail.com";
        this.port = "587";
        this.senderEmailAddress = "queststore.coderampart@gmail.com";
        this.senderEmailPassword = "SzafraN777";
        properties = System.getProperties();
    }

    public boolean send(String receiverEmailAdress, String messageTitle, String messageBody) {
        try {
            properties.setProperty("mail.smtp.host", this.host);
            properties.setProperty("mail.smtp.port", this.port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.debug", "true");
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance( properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmailAddress, senderEmailPassword);
                }
            });

            session.setDebug(debug);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmailAddress));

            InternetAddress[] addressTO = { new InternetAddress(receiverEmailAdress) };
            msg.setRecipients(Message.RecipientType.TO, addressTO);

            InternetAddress addressFROM = new InternetAddress(senderEmailAddress);
            msg.setFrom(addressFROM);

            msg.setSentDate(new Date());
            msg.setSubject(messageTitle);
            msg.setText(messageBody);

            Transport.send(msg);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}