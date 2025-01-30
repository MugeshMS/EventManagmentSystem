package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendEmailServlet extends HttpServlet {
	String eventNo ;
	String eventName;
	String cardNumber ;
	String expDate ;
	String cvv;
	String holder;
	String refID;
	String mail;
	
	String eve_NO;
	String eveName;
	String fee;
	String organizer;
	String venue;
	String date;
	
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    	eventName = req.getParameter("ename");
		eventNo = req.getParameter("enum");
		cardNumber = req.getParameter("cardno");
		expDate = req.getParameter("edate");
		cvv = req.getParameter("cvv");
		mail = req.getParameter("mail");
		holder = req.getParameter("cname");
		Random random = new Random();
		refID = ""+(1000+random.nextInt(9000));
        // Fetch email details from the HTML form
       String recipientEmail = mail;//request.getParameter("email"); // Get email from form
        String subject = "Book My Event"; // Mail subject
        String messageBody = "This is a test email from eventli.com."; // Mail body

        // Sender email credentials
        final String senderEmail = "eventbookmy0@gmail.com"; // Replace with your email
        final String senderPassword = "gnsy nrps tjmu wtac"; // Replace with app password
        try {
			upMessage(res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // SMTP properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Authenticate and create session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a mail message
        //	Message message = new MimeMessage(session);
        	Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail)); // Sender email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Recipient email
            message.setSubject(subject); // Subject
            message.setText(messageBody); // Body

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html><body style='background-image: url(\"https://wintertime.com.my/wp-content/uploads/2019/07/event-production-company-p2-entertainment-group-1.jpg\"); background-size: cover; background-position: center;color:white;'>");
            emailContent.append("<h1>Thank You for Booking in Evently</h1>");
            emailContent.append("<center><h4>Ticket Details</h4></center><br>");
            emailContent.append("<center><h4>Event Number = " + eve_NO + "</h4></center><br>");
            emailContent.append("<center><h4>Event Name = " + eveName + "</h4></center><br>");
            emailContent.append("<center><h4>Organizer = " + organizer + "</h4></center><br>");
            emailContent.append("<center><h4>FEES = " + fee + "</h4></center><br>");
            emailContent.append("<center><h4>Venue = " + venue + "</h4></center><br>");
            emailContent.append("<center><h4>Date = " + date + "</h4></center><br>");
            emailContent.append("<center><h4>RefID = " + refID + "</h4></center><br>");
            emailContent.append("</body></html>");

            message.setContent(emailContent.toString(), "text/html");
            Transport.send(message);

            // Send a response to the client
            res.setContentType("text/html");
          PrintWriter  out = res.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Event Details</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<link rel=\"stylesheet\" href=\"total.css\">");
            out.println("<div style='text-align: center; margin-top: 20px;'>");
            res.getWriter().println("<h3>you will receive a mail shortly " + holder + "</h3>");
            out.println("</div></body> </html>");
        } catch (MessagingException e) {
            e.printStackTrace();
            res.setContentType("text/html");
            res.getWriter().println("<h3>Failed to send email: " + e.getMessage() + "</h3>");
        }
    }

    public void upMessage(HttpServletResponse res) throws SQLException, IOException {
        String url = "jdbc:mysql://localhost:3306/event";
        String username = "root";
        String pass = "Mysql087";
        String get = "SELECT * FROM events WHERE event_number ='" + eventNo + "';";

      //  System.out.println("SQL Query: " + get); // Debugging query
        try {
            // Load the MySQL driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = DriverManager.getConnection(url, username, pass);

        try {
            PreparedStatement prep = con.prepareStatement(get);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                eve_NO = rs.getString("event_number");
                eveName = rs.getString("event_name");
                organizer = rs.getString("organizer");
                fee = rs.getString("fee");
                venue = rs.getString("venue");
                date = rs.getString("event_date");

                // Debugging data retrieved
            //    System.out.println("Event Details: " + eve_NO + ", " + eveName + ", " + organizer + ", " + fee + ", " + venue + ", " + date);
            } else {
                System.out.println("No data found for event number: " + eventNo);
                PrintWriter out = res.getWriter();
                out.println("<h1>No Such Event Found</h1>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            PrintWriter out = res.getWriter();
            out.println("<h1>Error while fetching event details: " + e.getMessage() + "</h1>");
        }

        String query = "INSERT INTO transaction VALUES('" + eventName + "','" + eventNo + "','" + cardNumber + "','" + expDate + "','" + cvv + "','" + holder + "','" + refID + "','" + mail + "');";
        Statement st = con.createStatement();
        st.executeUpdate(query);

        con.close();
    }
}
