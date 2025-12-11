package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String number = req.getParameter("number");
        String subject = req.getParameter("subject");
        String message = req.getParameter("message");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            insertMessage(name, email, number, subject, message);
            out.println("<h4>Data Updated</h4>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error saving message: " + e.getMessage() + "</h3>");
        }

        // optional redirect
        // RequestDispatcher dis = req.getRequestDispatcher("Alogin.html");
        // dis.forward(req, res);
    }

    private void insertMessage(String name, String email, String number, String subject, String message) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        String sql = "INSERT INTO message(name, email, number, subject, message) VALUES (?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, number);
            pst.setString(4, subject);
            pst.setString(5, message);
            pst.executeUpdate();
        }
    }
}
