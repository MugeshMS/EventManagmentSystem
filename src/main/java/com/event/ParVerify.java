package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParVerify extends HttpServlet {
    String username1;
    String pass1;
    String pass2 = null;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        username1 = req.getParameter("Pausername");
        pass1 = req.getParameter("word");
        PrintWriter out = res.getWriter();

        	if(username1 == null|| username1.isEmpty() ||pass1.isEmpty()|| pass1 == null) {
        		out.println(username1);
        		out.println("Sorry, incorrect username or password.");
        	
        		return;
        
        	}
        try {
            pass2 = upMessage();
            if (pass2 != null && pass1.equals(pass2)) {
            	RequestDispatcher dis = req.getRequestDispatcher("ParticipantEvent.html");
            	dis.forward(req, res);
            	} else {
                out.println("Sorry, incorrect username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println(e);
        }
        	
    }

    public String upMessage() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/event";
        String username = "root";
        String pass = "Mysql087";
        String cpass = null;
        String query = "select pass from users where username ='"+username1+"';";
        try {
		    // Load the MySQL driver class
		    Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}


        try {Connection con = DriverManager.getConnection(url, username, pass);
             Statement st = con.createStatement();
             ResultSet res = st.executeQuery(query); 

            // Move the cursor to the first row and fetch the password
            if (res.next()) {
                cpass = res.getString("pass");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow exception to handle it in the calling method
        }
        return cpass;
    }
}