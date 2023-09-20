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
			<h1>iTrust is Down</h1>
			The iTrust system is not available at this time.<br />
			<br />
			<br />
			<span >
				If you are developing on iTrust, you are
				not connecting to the database properly. Check your
				iTrust/WebRoot/META-INF/context.xml, and restart Tomcat to fix this.
			</span> 
			<br />
			<br />
			<a href="/iTrust">Log back in.</a>
		</div>

	</body>
</html>

