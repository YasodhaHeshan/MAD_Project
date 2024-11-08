package com.example.mad_project;

public class EmailContentGenerator {
    public static String generateBusTicketEmail(String userName, String bookingId, String busNumber, String departure, String destination, String date, String time, String seatNumber) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Bus Ticket Confirmation</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                ".container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                ".header { background-color: #4CAF50; color: #ffffff; padding: 10px 0; text-align: center; }" +
                ".content { padding: 20px; }" +
                ".content h2 { color: #333333; }" +
                ".ticket-details { margin: 20px 0; }" +
                ".ticket-details th, .ticket-details td { padding: 10px; text-align: left; }" +
                ".ticket-details th { background-color: #f4f4f4; }" +
                ".footer { text-align: center; padding: 10px; background-color: #f4f4f4; color: #333333; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<h1>Bus Ticket Confirmation</h1>" +
                "</div>" +
                "<div class=\"content\">" +
                "<h2>Dear " + userName + ",</h2>" +
                "<p>Thank you for booking your bus ticket with us. Here are your ticket details:</p>" +
                "<table class=\"ticket-details\">" +
                "<tr><th>Booking ID</th><td>" + bookingId + "</td></tr>" +
                "<tr><th>Bus Number</th><td>" + busNumber + "</td></tr>" +
                "<tr><th>Departure</th><td>" + departure + "</td></tr>" +
                "<tr><th>Destination</th><td>" + destination + "</td></tr>" +
                "<tr><th>Date</th><td>" + date + "</td></tr>" +
                "<tr><th>Time</th><td>" + time + "</td></tr>" +
                "<tr><th>Seat Number</th><td>" + seatNumber + "</td></tr>" +
                "</table>" +
                "<p>We hope you have a pleasant journey!</p>" +
                "<p>Best regards,</p>" +
                "<p>The Bus Booking Team</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; 2023 Bus Booking System. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}