package com.example.mad_project;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "EmailService";
    private final String recipientEmail;
    private final String subject;
    private final String messageBody;
    private final String senderEmail;
    private final String senderPassword;

    public EmailService(String recipientEmail, String subject, String messageBody, String senderEmail, String senderPassword) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.messageBody = messageBody;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Set properties for Gmail SMTP server
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Create a new session with the email and password for authentication
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            // Compose the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send the message
            Transport.send(message);
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error sending email: ", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Log.i(TAG, "Email sent successfully");
        } else {
            Log.e(TAG, "Failed to send email");
        }
    }
}
