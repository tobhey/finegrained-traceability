
<%@page import="java.sql.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.FakeEmailDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@page import="java.util.List"%>
<html>
<head>
<title>Fake Emails Sent</title>
<style type="text/css">
body {
	margin: 4px;
	font-family: Arial;
	font-size: 0.8em;
}

.results {
	border-collapse: collapse;
}

.results tr th {
	font-size: 0.9em;
	padding: 0px 5px 0px 5px;
	background-color: Navy;
	color: White;
}

.results tr td {
	font-size: 0.8em;
	padding: 0px 5px 0px 5px;
}

.results tr th,.results tr td {
	border: 1px solid Gray;
}
</style>

</head>
<body>
<a href="/iTrust">Back to iTrust</a>
<h2>FOR TESTING PURPOSES ONLY</h2>
<%
List<Email> emails = DAOFactory.getProductionInstance().getFakeEmailDAO().getAllEmails();
%>
<b>Fake Emails</b>
<br />
<table class="results">
	<tr>
		<th>To List</th>
		<th>From</th>
		<th>Subject</th>
		<th>Body</th>
	</tr>
	<%
	for (Email email : emails) {
	%>
	<tr>
		<td><%=email.getToListStr() %></td>
		<td><%=email.getFrom() %></td>
		<td><%=email.getSubject() %></td>
		<td><%=email.getBody() %></td>
	</tr>
	<%
	}
	%>
</table>
<br />
<br />
</body>
</html>
