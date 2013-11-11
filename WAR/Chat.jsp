<html>
<head>
<title>Chat</title>
</head>
<body>
	<% 
	String param = request.getParameter("loginName");
	if(param == null){
		out.println("No auth");
	}else{
		out.println("Willkommen: " + param);
	}
	%>
</body>
</html>