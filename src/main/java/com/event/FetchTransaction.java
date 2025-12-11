package com.event;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchTransaction extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String eventNo = req.getParameter("ename");
        String eventName = req.getParameter("enum");
        String cardNumber = req.getParameter("cardno");
        String expDate = req.getParameter("edate");
        String cvv = req.getParameter("cvv");
        String mail = req.getParameter("mail");
        String holder = req.getParameter("cname");
        String refID = "" + (1000 + new Random().nextInt(9000));

        try {
            insertTransaction(eventNo, eventName, cardNumber, expDate, cvv, holder, refID, mail);
            // You may want to redirect or forward to confirmation page
        } catch (SQLException e) {
            e.printStackTrace();
            res.getWriter().println("<h3>Error storing transaction: " + e.getMessage() + "</h3>");
        }
    }

    private void insertTransaction(String eventNo, String eventName, String cardNumber, String expDate, String cvv,
                                   String holder, String refID, String mail) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        // specify column list to avoid issues
        String sql = "INSERT INTO transaction(event_number, event_name, card_number, exp_date, cvv, holder, ref_id, mail) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, eventNo);
            pst.setString(2, eventName);
            pst.setString(3, cardNumber);
            pst.setString(4, expDate);
            pst.setString(5, cvv);
            pst.setString(6, holder);
            pst.setString(7, refID);
            pst.setString(8, mail);
            pst.executeUpdate();
        }
    }
}
