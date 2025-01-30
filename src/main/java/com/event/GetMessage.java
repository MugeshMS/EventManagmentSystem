package com.event;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMessage  extends HttpServlet{
	String message;
	String name;
	String email;
	 String number;
	 String subject;
	 GetMessage obj = null;
	 
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
		
		 name = req.getParameter("name");      // "name" matches the name attribute in the HTML
         email = req.getParameter("email");    // "email" matches the name attribute in the HTML
         number = req.getParameter("number");  // "number" matches the name attribute in the HTML
         subject = req.getParameter("subject"); // "subject" matches the name attribute in the HTML
         message = req.getParameter("message"); // "message" matches the name attribute in the HTML
       
        // Output the data (for testing purposes)
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
      
		try {
			upMessage();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        out.println("<html><body>");
        out.println("<h1>Form Data Received</h1>");
        out.println("<p>Name: " + (name != null ? name : "Not Provided") + "</p>");
        out.println("<p>Email: " + (email != null ? email : "Not Provided") + "</p>");
        out.println("<p>Phone Number: " + (number != null ? number : "Not Provided") + "</p>");
        out.println("<p>Subject: " + (subject != null ? subject : "Not Provided") + "</p>");
        out.println("<p>Message: " + (message != null ? message : "Not Provided") + "</p>");
        out.println("<h4>Data Updated</h4>");
        out.println("</body></html>");
        RequestDispatcher dis = req.getRequestDispatcher("Alogin.html");
  
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

		String query = "insert into message values("+"'" + name +"','"+email+"','"+number+"','"+subject+"','"+message+"');";
		Connection con = DriverManager.getConnection(url,username,pass);
		Statement st = con.createStatement();
		st.executeUpdate(query);
		
		con.close();
}
}
		
	