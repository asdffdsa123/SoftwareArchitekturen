package de.hswt.swa.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HalloWelt extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter("type");
		String html;
		if(type == null){
			html = calcGreetingPage();
		}
		else if(type.equals("date")){
			html = calcDatePage();
		}else if(type.equals("dir")){
			html = calcDirPage();
		}else{
			html = calcGreetingPage();
		}
		resp.getWriter().println(html);
	}
	
	private String calcDirPage(){
		StringBuilder b = new StringBuilder();
		b.append("<ul>");
		for(String file : new File("/").list()){
			b.append("<li>").append(file).append("</li>");
		}
		b.append("</ul>");
		return generateHTML("Dir", b.toString());
	}
	
	private String calcGreetingPage(){
		return generateHTML("Empty", "No parameter");
	}
	
	private String calcDatePage(){
		return generateHTML("Datum", new Date().toString());
	}

	private String generateHTML(String title, String body){
		return String.format("<html><head><title>%s</title></head><body>%s</body></html>", title, body);
	}
}
