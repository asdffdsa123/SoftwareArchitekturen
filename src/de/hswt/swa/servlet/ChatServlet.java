package de.hswt.swa.servlet;


/*
 * Created on 20.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author lesske
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ChatServlet extends HttpServlet {

	private  HashMap messages;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		messages = new HashMap();
	}

	public void destroy() {
		
	}
	
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, java.io.IOException {
		String action = req.getParameter("aktion");
		if(action == null){
			res.getWriter().println("No action");
			return;
		}
		if (action.equals("enter")) {
			String name = req.getParameter("name");
			req.setAttribute("loginName", name);
			req.setAttribute("text", null);
			messages.put(name, new Vector());
	
		}
		
		if (action.equals("sende")) {
			HttpSession session = req.getSession();
			String user = (String)session.getAttribute("user.name");
			Set keys = messages.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				String key  = (String) it.next();
				Vector next = (Vector)messages.get(key);
				next.add(new String(user + " > " + req.getParameter("in")));
			}
			
			if (session.getAttribute("user.name") != null) {
				Vector texts = (Vector) messages.get(session.getAttribute("user.name"));
				req.setAttribute("text", texts);
				
			}
		}
		ServletContext con = getServletContext();
		RequestDispatcher dispatcher = con.getRequestDispatcher("Chat.jsp");
		
		dispatcher.forward(req, res);
		
	}
		
	
}
