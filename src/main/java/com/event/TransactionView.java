package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransactionView extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Include CSS file
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Event Details</title>");
        out.println("<link rel='stylesheet' href='total.css'>"); // Include your CSS file
        out.println("</head>");
        out.println("<body>");

        // Page Title
        out.println("<h1>Event Bookings</h1>");

        // Table structure
        out.println("<table id='common'>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th>Event Name</th>");
        out.println("<th>Event No</th>");
        out.println("<th>card No</th>");
        out.println("<th>ExpDate</th>");
        out.println("<th>CVV</th>");
        out.println("<th>Name</th>");
        out.println("<th>RefID</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "Mysql087");

            // Query to fetch data
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT event_name, event_no,cardno,expdate,cvv_no,name,refID FROM transaction");

            // Populate table rows with data
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("event_name") + "</td>");
                out.println("<td>" + rs.getString("event_no") + "</td>");
                out.println("<td>" + rs.getString("cardno") + "</td>");
                out.println("<td>" + rs.getString("expdate") + "</td>");
                out.println("<td>" + rs.getString("cvv_no") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("refID") + "</td>");
                out.println("</tr>");
            }

            // Close connection
            con.close();
        } catch (Exception e) {
            out.println("<tr><td colspan='6' style='color: red;'>Error fetching data: " + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody>");
        out.println("</table>");

        // Booking button
//        out.println("<div style='text-align: center; margin-top: 20px;'>");
//        out.println("<a href='Registration.html'>");
//        out.println("<button id='sub'>Go to Booking</button>");
//        out.println("</a>");
//        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}