package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StoreLogin  extends HttpServlet{
	String cusername;
	String pass1;
	String pass2;
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException  {
		cusername = req.getParameter("Pusername");
		pass1 = req.getParameter("Ppassword");
		pass2 = req.getParameter("Cpassword");
		
		if(pass1.equals(pass2)) {
			try {
				upMessage();
				RequestDispatcher dis = req.getRequestDispatcher("ParticipantEvent.html");
				dis.forward(req, res);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			PrintWriter out = res.getWriter();
			out.println("Confirm password Mismatch");
		}
	}
	public  void upMessage() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/event";
		String username = "root";
		String pass = "Mysql087";
		try {
		    // Load the MySQL driver class
		    Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}

		String query = "insert into users values("+"'" + cusername +"','"+pass2+"');";
		Connection con = DriverManager.getConnection(url,username,pass);
		Statement st = con.createStatement();
		st.executeUpdate(query);
		
		con.close();
}

}
