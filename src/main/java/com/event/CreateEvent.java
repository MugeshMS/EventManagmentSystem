package com.event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateEvent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String eventNo = req.getParameter("EventNo");
        String eventName = req.getParameter("EventName");
        String coordinator = req.getParameter("coordinatorName");
        String fee = req.getParameter("fee");
        String venue = req.getParameter("venue");
        String date = req.getParameter("date"); // expected yyyy-MM-dd from input

        try {
            insertEvent(eventNo, eventName, coordinator, fee, venue, date);
            RequestDispatcher dis = req.getRequestDispatcher("/fetchAD");
            dis.forward(req, res);
        } catch (SQLException e) {
            e.printStackTrace();
            res.setContentType("text/html");
            res.getWriter().println("<h3 style='color:red;'>Error adding event: " + e.getMessage() + "</h3>");
        }
    }

    private void insertEvent(String eventNo, String eventName, String coordinator, String fee, String venue, String date)
            throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) { }

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        String sql = "INSERT INTO events(event_number, event_name, organizer, fee, venue, event_date) VALUES (?,?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, eventNo);
            pst.setString(2, eventName);
            pst.setString(3, coordinator);
            pst.setString(4, fee);
            pst.setString(5, venue);
            // store as date string; if DB column is DATE, use java.sql.Date.valueOf(date) if date not null
            pst.setString(6, date);
            pst.executeUpdate();
        }
    }
}
