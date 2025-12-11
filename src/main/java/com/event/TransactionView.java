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
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Event Bookings</title><link rel='stylesheet' href='total.css'></head><body>");
        out.println("<h1>Event Bookings</h1>");
        out.println("<table id='common'><thead><tr>"
                + "<th>Event Name</th><th>Event No</th><th>Card No</th><th>ExpDate</th><th>CVV</th><th>Name</th><th>RefID</th>"
                + "</tr></thead><tbody>");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        String sql = "SELECT event_name, event_number, card_number, exp_date, cvv, holder, ref_id FROM transaction ORDER BY ref_id";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("event_name") + "</td>");
                out.println("<td>" + rs.getString("event_number") + "</td>");
                out.println("<td>" + rs.getString("card_number") + "</td>");
                out.println("<td>" + rs.getString("exp_date") + "</td>");
                out.println("<td>" + rs.getString("cvv") + "</td>");
                out.println("<td>" + rs.getString("holder") + "</td>");
                out.println("<td>" + rs.getString("ref_id") + "</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td colspan='7' style='color: red;'>Error fetching data: " + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody></table></body></html>");
    }
}
