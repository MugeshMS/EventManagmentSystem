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

public class FetchAD extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Event Details</title><link rel='stylesheet' href='total.css'></head><body>");
        out.println("<h1>Event Details</h1>");
        out.println("<table id='common'><thead><tr>"
                + "<th>Event Number</th><th>Event Name</th><th>Organizer</th><th>Fee</th><th>Venue</th><th>Date</th>"
                + "</tr></thead><tbody>");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");
        String sql = "SELECT event_number, event_name, organizer, fee, venue, event_date FROM events ORDER BY event_number";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("event_number") + "</td>");
                out.println("<td>" + rs.getString("event_name") + "</td>");
                out.println("<td>" + rs.getString("organizer") + "</td>");
                out.println("<td>" + rs.getString("fee") + "</td>");
                out.println("<td>" + rs.getString("venue") + "</td>");
                out.println("<td>" + rs.getString("event_date") + "</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td colspan='6' style='color: red;'>Error fetching data: " + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody></table>");
        out.println("<div style='text-align: center; margin-top: 20px;'>");
        out.println("<a href='Registration.html'><button id='sub'>Go to Booking</button></a>");
        out.println("</div>");
        out.println("<div style='text-align: center; margin-top: 20px;'>");
        out.println("<a href='DeleteEvent.html'><button id='sub'>Delete Event</button></a>");
        out.println("</div></body></html>");
    }
}
