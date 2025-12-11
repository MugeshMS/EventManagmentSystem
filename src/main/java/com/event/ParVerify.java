package com.event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParVerify extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String username1 = req.getParameter("Pausername");
        String pass1 = req.getParameter("word");

        if (username1 == null || username1.isEmpty() || pass1 == null || pass1.isEmpty()) {
            res.getWriter().println("Sorry, incorrect username or password.");
            return;
        }

        try {
            String stored = getPassword(username1);
            if (stored != null && pass1.equals(stored)) {
                RequestDispatcher dis = req.getRequestDispatcher("ParticipantEvent.html");
                dis.forward(req, res);
            } else {
                res.getWriter().println("Sorry, incorrect username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }

    private String getPassword(String username) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");
        String sql = "SELECT pass FROM users WHERE username = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("pass");
                }
            }
        }
        return null;
    }
}
