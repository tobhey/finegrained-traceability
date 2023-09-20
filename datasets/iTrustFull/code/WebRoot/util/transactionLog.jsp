<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<html>
<head>
<title>FOR TESTING PURPOSES ONLY</title>
</head>
<body>
<h1>Test Utilities</h1>
<table border=1>
	<tr>
		<th>Date</th>
		<th>ID</th>
		<th>Type</th>
		<th>User MID</th>
		<th>Secondary MID</th>
		<th>Extra Info</th>
	</tr>
	<%
		List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getAllTransactions();
		for (TransactionBean t : list) {
	%>
	<tr>
		<td><%=t.getTimeLogged()%></td>
		<td><%=t.getTransactionID()%></td>
		<td><%=t.getTransactionType().getDescription()%></td>
		<td><%=t.getLoggedInMID()%></td>
		<td><%=t.getSecondaryMID()%></td>
		<td><%=t.getAddedInfo()%></td>
	</tr>
	<%
	}
	%>
</table>
<br /><br />
<h1><a href="/iTrust">Back to iTrust</a></h1>
</body>
</html>
