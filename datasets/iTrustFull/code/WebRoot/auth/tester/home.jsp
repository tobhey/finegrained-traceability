<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.beans.OperationalProfile"%>
<%@page import="java.text.NumberFormat"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Tester Home";
%>

<%@include file="/header.jsp" %>

<div style="text-align: center;">
<h1>Operational Profile</h1>
<%
	try {
		OperationalProfile op = prodDAO.getTransactionDAO().getOperationalProfile();
		
		NumberFormat formatter = NumberFormat.getPercentInstance();
		
%>

<table class="fancyTable" align="center" border=1 cellpadding=2 cellspacing=2>
	<tr>
		<th>Operation</th>
		<th colspan=2 >Total</th>
		<th colspan=2 >Patients Only</th>
		<th colspan=2 >Personnel Only</th>
	</tr>
<%
	int i = 1;
	for (TransactionType type : TransactionType.values()) {
%>
	<tr <%= (i++%2 == 0)?" class=\"alt\"":"" %>>
		<td align=left><%=type.getDescription()%></td>
		<td align=center><%=op.getTotalCount().get(type)%></td>
		<td align=center><%=formatter.format((double)op.getTotalCount().get(type) / op.getNumTotalTransactions())%></td>
		<td align=center><%=op.getPatientCount().get(type)%></td>
		<td align=center><%=formatter.format((double)op.getPatientCount().get(type) / op.getNumPatientTransactions())%></td>
		<td align=center><%=op.getPersonnelCount().get(type)%></td>
		<td align=center><%=formatter.format((double)op.getPersonnelCount().get(type) / op.getNumPersonnelTransactions())%></td>
	</tr>
<%
	}
%>
	<tr>
		<td><b>Totals</b></td>
		<td colspan=2 align=center><%=op.getNumTotalTransactions()%></td>
		<td colspan=2 align=center><%=op.getNumPatientTransactions()%></td>
		<td colspan=2 align=center><%=op.getNumPersonnelTransactions()%></td>
	</tr>
</table>

</div>

<%
	} catch (Exception e) {
%>
	<span >Exception Occurred</span>
	<br />
	<%=e.getMessage()%>
<%
e.printStackTrace();
}
%>

<%@include file="/footer.jsp" %>
