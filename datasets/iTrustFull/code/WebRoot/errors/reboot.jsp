<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>

<%
	session.invalidate();
%> 

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Authorization Error</title>
	</head>
	<body>
		<div align=center>
			<h1>Server Reboot!</h1>
			The server has been rebooted and you have lost your credentials.<br />

			<a href="/iTrust">Log back in.</a>
		</div>

	</body>
</html>
