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

public class StoreLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String cusername = req.getParameter("Pusername");
        String pass1 = req.getParameter("Ppassword");
        String pass2 = req.getParameter("Cpassword");

        if (pass1 != null && pass1.equals(pass2)) {
            try {
                insertUser(cusername, pass2);
                RequestDispatcher dis = req.getRequestDispatcher("ParticipantEvent.html");
                dis.forward(req, res);
            } catch (SQLException e) {
                e.printStackTrace();
                res.getWriter().println("<h3>Error creating user: " + e.getMessage() + "</h3>");
            }
        } else {
            res.getWriter().println("Confirm password mismatch");
        }
    }

    private void insertUser(String username, String password) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");
        String sql = "INSERT INTO users(username, pass) VALUES (?,?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
        }
    }
}
