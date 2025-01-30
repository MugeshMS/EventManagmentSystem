package com.event;

import java.io.IOException;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateEvent  extends HttpServlet{
	String eventNo ;
	String eventName;
	String coordi ;
	String fee ;
	String venue ;
	String date;
	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		 eventNo = req.getParameter("EventNo");
		 eventName = req.getParameter("EventName");
		 coordi  = req.getParameter("coordinatorName");
		 fee = req.getParameter("fee");
		 venue = req.getParameter("venue");
		 date = req.getParameter("date");
		
		try {
			upMessage();
			PrintWriter out = res.getWriter();
			//out.println("Event Added");
			RequestDispatcher dis = req.getRequestDispatcher("/fetchAD");
			dis.forward(req,res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public  int upMessage() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/event";
		String username = "root";
		String pass = "Mysql087";
		try {
		    // Load the MySQL driver class
		    Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}

		String query = "insert into events values("+"'" + eventNo +"','"+eventName+"','"+coordi+"','"+fee+"','"+venue+"','"+ date +"');";

		
		Connection con = DriverManager.getConnection(url,username,pass);
		Statement st = con.createStatement();
		st.executeUpdate(query);
		
		con.close();
		return 1;
}

}
