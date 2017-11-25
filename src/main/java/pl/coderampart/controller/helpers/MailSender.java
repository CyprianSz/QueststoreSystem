package pl.coderampart.controller.helpers;

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
    private String messageTitle;
    private boolean debug;
    private Properties properties;

    public MailSender() {
        this.host = "smtp.gmail.com";
        this.port = "587";
        this.senderEmailAddress = "queststore.coderampart@gmail.com";
        this.senderEmailPassword = "SzafraN777";
        this.messageTitle = "WELCOME TO QUESTSTORE !";
        properties = System.getProperties();
    }

    public boolean send(String receiverEmailAdress, String messageBody) {
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

    public String prepareMessage(String userName, String password) {
        return "Welcome on board " + userName + " ! " +
               "\n\nTo log in for the first time use this password: " + password +
               "\n\nOf course you can change it later." +
               "\n\nHope you'll enjoy and see you soon ! ";
    }
}