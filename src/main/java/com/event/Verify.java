package com.event;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Verify extends HttpServlet {
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		String aName = req.getParameter("Ausername");
		String pass = req.getParameter("Apassword");
		PrintWriter out = res.getWriter();
		if(aName.equals("mugesh") && pass.equals("101")) {
			RequestDispatcher dis = req.getRequestDispatcher("AdminEvent.html");
			dis.forward(req,res);
		}
		else {
		out.println("Sorry Wrong AdminId & Password");
		}
		
	}

}
