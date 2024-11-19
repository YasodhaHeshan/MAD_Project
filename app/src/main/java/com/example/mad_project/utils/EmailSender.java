package com.example.mad_project.utils;

import android.util.Log;
import com.example.mad_project.utils.EmailContentGenerator;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private final String username;
    private final String password;

    public EmailSender() {
        this.username = "your@email.com";
        this.password = "user_password";
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        Session session = createSession();
        EmailContentGenerator.generateBusTicketEmail(
                "User Name", "12345", "Bus123", "City A",
                "City B", "2023-10-10", "10:00 AM", "12A"
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            Log.e("EmailSender", "Failed to send email", e);
            throw new MessagingException("Email sending failed", e);
        }
    }
}