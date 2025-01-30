package com.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Random;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchTransaction extends HttpServlet{
	String eventNo ;
	String eventName;
	String cardNumber ;
	String expDate ;
	String cvv;
	String holder;
	String refID;
	String mail;
	public void doPost(HttpServletRequest req, HttpServletResponse res)  {
		eventNo = req.getParameter("ename");
		eventName = req.getParameter("enum");
		cardNumber = req.getParameter("cardno");
		expDate = req.getParameter("edate");
		cvv = req.getParameter("cvv");
		mail = req.getParameter("mail");
		holder = req.getParameter("cname");
		Random random = new Random();
		refID = ""+(1000+random.nextInt(9000));
		try {
			upMessage();
			//PrintWriter out = res.getWriter();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		String query = "insert into transaction values("+"'" + eventNo +"','"+eventName+"','"+cardNumber+"','"+expDate+"','"+cvv+"','"+holder+"','"+refID+"','"+mail+"');";
		Connection con = DriverManager.getConnection(url,username,pass);
		Statement st = con.createStatement();
		st.executeUpdate(query);
		
		con.close();
}

}
