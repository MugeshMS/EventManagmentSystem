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
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String eventNumber = request.getParameter("event_number");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        String sql = "DELETE FROM events WHERE event_number = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, eventNumber);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                out.println("<h2 style='color:green;'>Event with Event Number " + eventNumber + " has been deleted successfully.</h2>");
            } else {
                out.println("<h2 style='color:red;'>No event found with the given Event Number.</h2>");
            }
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
