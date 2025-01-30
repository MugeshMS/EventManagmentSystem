package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteEvent extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get the event number from the request
        String eventNumber = request.getParameter("event_number");

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "Mysql087");

            // DELETE query
            String query = "DELETE FROM events WHERE event_number = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, eventNumber);

            // Execute the delete query
            int rowsAffected = pstmt.executeUpdate();

            // Display feedback to the user
            if (rowsAffected > 0) {
                out.println("<h2 style='color:green;'>Event with Event Number " + eventNumber + " has been deleted successfully.</h2>");
            } else {
                out.println("<h2 style='color:red;'>No event found with the given Event Number.</h2>");
            }

            // Close connection
            con.close();
        } catch (Exception e) {
            out.println("<h2 style='color:red;'>Error: " + e.getMessage() + "</h2>");
        }
        out.println("<link rel=\"stylesheet\" href=\"total.css\">");
        out.println("<div style='text-align: center; margin-top: 20px;'>");
        out.println("<form action='fetchAD' method = 'post'>");
        out.println("<button id='sub'>Go to Events</button>");
        out.println("</form>");
        out.println("</div>");
    }
}