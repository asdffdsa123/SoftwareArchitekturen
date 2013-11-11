<html>
<head>
<title></title>
</head>
<body>
<%@page import = "java.util.*" %>
<%
	String anfrage = request.getParameter("type");
	if(anfrage != null && anfrage.equals("date")){
		out.println(new Date().toString());
	}else{
		out.println("Unknown request");
	}
%>
</body>
</html>