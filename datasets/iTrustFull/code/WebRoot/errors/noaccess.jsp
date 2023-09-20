<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>

<%
	session.invalidate();
%> 

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Server Rebooted</title>
	</head>
	<body>
		<div align=center>
			<h1>Authorization Error!</h1>
			You are not allowed to access this page because your role is invalid.
			For security reasons, you have been logged out.<br />
			<a href="/iTrust">Log back in.</a>
		</div>

	</body>
</html>
