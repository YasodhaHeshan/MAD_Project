package com.example.busbook.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends Worker {

    private static final String TAG = "EmailSender";

    public EmailSender(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String email = getInputData().getString("email");
        String subject = getInputData().getString("subject");
        String message = getInputData().getString("message");
        String accessToken = getInputData().getString("accessToken");

        try {
            String senderEmail = "your_email@gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(senderEmail, accessToken);
                }
            });

            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Failed to send email", e);
            return Result.failure();
        }
    }

    public static void sendEmail(Context context, String email, String subject, String message, String accessToken) {
        Data data = new Data.Builder()
                .putString("email", email)
                .putString("subject", subject)
                .putString("message", message)
                .putString("accessToken", accessToken)
                .build();

        OneTimeWorkRequest emailRequest = new OneTimeWorkRequest.Builder(EmailSender.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance(context).enqueue(emailRequest);
    }
}