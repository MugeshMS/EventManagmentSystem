
// //        final String senderEmail = "eventbookmy0@gmail.com"; // Replace with your email
// //        final String senderPassword = "gnsy nrps tjmu wtad"; // Replace with app password
// //
// package com.event;

// import java.io.IOException;
// import java.io.PrintWriter;
// import java.sql.*;
// import java.util.Properties;
// import java.util.Random;

// import javax.mail.*;
// import javax.mail.internet.*;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// public class SendEmailServlet extends HttpServlet {
//     private static final long serialVersionUID = 1L;

//     protected void doPost(HttpServletRequest req, HttpServletResponse res)
//             throws ServletException, IOException {
//         String eventNo = req.getParameter("enum");
//         String eventName = req.getParameter("ename");
//         String cardNumber = req.getParameter("cardno");
//         String expDate = req.getParameter("edate");
//         String cvv = req.getParameter("cvv");
//         String mail = req.getParameter("mail");
//         String holder = req.getParameter("cname");
//         String refID = "" + (1000 + new Random().nextInt(9000));

//         // save transaction and fetch event details
//         EventDetails ed = fetchEventAndStoreTransaction(eventNo, eventName, cardNumber, expDate, cvv, holder, refID, mail);

//         // send email using SMTP credentials from environment variables
//         String senderEmail = System.getenv("SMTP_USER");
//         String senderPass = System.getenv("SMTP_PASS");

//         if (senderEmail == null || senderPass == null) {
//             res.getWriter().println("<h3 style='color:red;'>SMTP credentials not set in environment variables.</h3>");
//             return;
//         }

//         Properties properties = new Properties();
//         properties.put("mail.smtp.auth", "true");
//         properties.put("mail.smtp.starttls.enable", "true");
//         properties.put("mail.smtp.host", "smtp.gmail.com");
//         properties.put("mail.smtp.port", "587");

//         Session session = Session.getInstance(properties, new Authenticator() {
//             protected PasswordAuthentication getPasswordAuthentication() {
//                 return new PasswordAuthentication(senderEmail, senderPass);
//             }
//         });

//         try {
//             Message message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(senderEmail));
//             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
//             message.setSubject("Evently - Booking Confirmation");

//             StringBuilder emailContent = new StringBuilder();
//             emailContent.append("<html><body style='color:#111;background:#fff;padding:20px;'>");
//             emailContent.append("<h1>Thank you for booking in Evently</h1>");
//             emailContent.append("<h3>Ticket Details</h3>");
//             emailContent.append("<p>Event Number: " + ed.eventNumber + "</p>");
//             emailContent.append("<p>Event Name: " + ed.eventName + "</p>");
//             emailContent.append("<p>Organizer: " + ed.organizer + "</p>");
//             emailContent.append("<p>Fees: " + ed.fee + "</p>");
//             emailContent.append("<p>Venue: " + ed.venue + "</p>");
//             emailContent.append("<p>Date: " + ed.eventDate + "</p>");
//             emailContent.append("<p>RefID: " + refID + "</p>");
//             emailContent.append("</body></html>");

//             message.setContent(emailContent.toString(), "text/html");
//             Transport.send(message);

//             res.setContentType("text/html");
//             PrintWriter out = res.getWriter();
//             out.println("<link rel=\"stylesheet\" href=\"total.css\">");
//             out.println("<div style='text-align:center;margin-top:20px;'>");
//             out.println("<h3>You will receive a mail shortly, " + holder + "</h3>");
//             out.println("</div>");

//         } catch (MessagingException e) {
//             e.printStackTrace();
//             res.getWriter().println("<h3>Failed to send email: " + e.getMessage() + "</h3>");
//         }
//     }

//     private EventDetails fetchEventAndStoreTransaction(String eventNo, String eventName, String cardNumber, String expDate,
//                                                        String cvv, String holder, String refID, String mail) {
//         EventDetails ed = new EventDetails();

//         try {
//             Class.forName("org.postgresql.Driver");
//         } catch (ClassNotFoundException ignored) {}

//         String url = System.getenv("DB_URL");
//         String user = System.getenv("DB_USER");
//         String pass = System.getenv("DB_PASS");

//         String selectSql = "SELECT event_number, event_name, organizer, fee, venue, event_date FROM events WHERE event_number = ?";
//         String insertSql = "INSERT INTO transactions(event_number, event_name, card_number, exp_date, cvv, holder, ref_id, mail) VALUES (?,?,?,?,?,?,?,?)";

//         try (Connection con = DriverManager.getConnection(url, user, pass);
//              PreparedStatement sel = con.prepareStatement(selectSql);
//              PreparedStatement ins = con.prepareStatement(insertSql)) {

//             sel.setString(1, eventNo);
//             try (ResultSet rs = sel.executeQuery()) {
//                 if (rs.next()) {
//                     ed.eventNumber = rs.getString("event_number");
//                     ed.eventName = rs.getString("event_name");
//                     ed.organizer = rs.getString("organizer");
//                     ed.fee = rs.getString("fee");
//                     ed.venue = rs.getString("venue");
//                     ed.eventDate = rs.getString("event_date");
//                 }
//             }

//             ins.setString(1, eventNo);
//             ins.setString(2, eventName);
//             ins.setString(3, cardNumber);
//             ins.setString(4, expDate);
//             ins.setString(5, cvv);
//             ins.setString(6, holder);
//             ins.setString(7, refID);
//             ins.setString(8, mail);
//             ins.executeUpdate();

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return ed;
//     }

//     private static class EventDetails {
//         String eventNumber = "";
//         String eventName = "";
//         String organizer = "";
//         String fee = "";
//         String venue = "";
//         String eventDate = "";
//     }
// }
package com.event;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendEmailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String eventNo = req.getParameter("enum");
        String eventName = req.getParameter("ename");
        String cardNumber = req.getParameter("cardno");
        String expDate = req.getParameter("edate");
        String cvv = req.getParameter("cvv");
        String mail = req.getParameter("mail");
        String holder = req.getParameter("cname");
        String refID = String.valueOf(1000 + new Random().nextInt(9000));

        // Save transaction & fetch event details
        EventDetails ed = fetchEventAndStoreTransaction(
                eventNo, eventName, cardNumber, expDate, cvv, holder, refID, mail
        );

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html><body style='font-family:Arial;'>");
        emailContent.append("<h2>Thank you for booking with Evently</h2>");
        emailContent.append("<p><b>Event Number:</b> ").append(ed.eventNumber).append("</p>");
        emailContent.append("<p><b>Event Name:</b> ").append(ed.eventName).append("</p>");
        emailContent.append("<p><b>Organizer:</b> ").append(ed.organizer).append("</p>");
        emailContent.append("<p><b>Fee:</b> ").append(ed.fee).append("</p>");
        emailContent.append("<p><b>Venue:</b> ").append(ed.venue).append("</p>");
        emailContent.append("<p><b>Date:</b> ").append(ed.eventDate).append("</p>");
        emailContent.append("<p><b>Reference ID:</b> ").append(refID).append("</p>");
        emailContent.append("</body></html>");

        try {
            sendEmailViaSendGrid(
                    mail,
                    "Evently - Booking Confirmation",
                    emailContent.toString()
            );

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h3 style='color:green;'>Booking successful! Email sent to " + mail + "</h3>");

        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("<h3 style='color:red;'>Failed to send email: " + e.getMessage() + "</h3>");
        }
    }

    // ================= SENDGRID EMAIL =================

    private void sendEmailViaSendGrid(String to, String subject, String htmlContent) throws Exception {

        String apiKey = System.getenv("SENDGRID_API_KEY");
        String fromEmail = System.getenv("MAIL_FROM");

        if (apiKey == null || fromEmail == null) {
            throw new RuntimeException("SendGrid environment variables not set");
        }

        URL url = new URL("https://api.sendgrid.com/v3/mail/send");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String payload = """
        {
          "personalizations": [{
            "to": [{ "email": "%s" }]
          }],
          "from": { "email": "%s" },
          "subject": "%s",
          "content": [{
            "type": "text/html",
            "value": "%s"
          }]
        }
        """.formatted(
                to,
                fromEmail,
                subject,
                htmlContent.replace("\"", "\\\"")
        );

        try (OutputStream os = con.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = con.getResponseCode();
        if (responseCode != 202) {
            throw new RuntimeException("SendGrid failed. HTTP code: " + responseCode);
        }
    }

    // ================= DATABASE =================

    private EventDetails fetchEventAndStoreTransaction(
            String eventNo, String eventName, String cardNumber,
            String expDate, String cvv, String holder,
            String refID, String mail) {

        EventDetails ed = new EventDetails();

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        String selectSql = """
            SELECT event_number, event_name, organizer, fee, venue, event_date
            FROM events WHERE event_number = ?
        """;

        String insertSql = """
            INSERT INTO transactions
            (event_number, event_name, card_number, exp_date, cvv, holder, ref_id, mail)
            VALUES (?,?,?,?,?,?,?,?)
        """;

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement sel = con.prepareStatement(selectSql);
             PreparedStatement ins = con.prepareStatement(insertSql)) {

            sel.setString(1, eventNo);
            ResultSet rs = sel.executeQuery();
            if (rs.next()) {
                ed.eventNumber = rs.getString("event_number");
                ed.eventName = rs.getString("event_name");
                ed.organizer = rs.getString("organizer");
                ed.fee = rs.getString("fee");
                ed.venue = rs.getString("venue");
                ed.eventDate = rs.getString("event_date");
            }

            ins.setString(1, eventNo);
            ins.setString(2, eventName);
            ins.setString(3, cardNumber);
            ins.setString(4, expDate);
            ins.setString(5, cvv);
            ins.setString(6, holder);
            ins.setString(7, refID);
            ins.setString(8, mail);
            ins.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ed;
    }

    // ================= MODEL =================

    private static class EventDetails {
        String eventNumber = "";
        String eventName = "";
        String organizer = "";
        String fee = "";
        String venue = "";
        String eventDate = "";
    }
}
