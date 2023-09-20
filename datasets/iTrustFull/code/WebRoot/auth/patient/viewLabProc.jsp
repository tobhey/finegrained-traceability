<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Laboratory Procedures";
%>

<%@include file="/header.jsp" %>

<%
ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, loggedInMID.longValue());
List<LabProcedureBean> proc = action.getLabs();
int id = 0;

if(request.getParameter("id") != null) {
	try {
		id = Integer.parseInt(request.getParameter("id"));
	} catch (NumberFormatException nfe) {
		response.sendRedirect("calendar.jsp");
	}
}

LabProcedureBean bean = null;

for(LabProcedureBean b : proc) {
	if(b.getPid() == id) {
		bean = b;
		break;
	}
}

if(bean == null) {
	response.sendRedirect("calendar.jsp");
}
%>

<br />
<table  class="fTable" align=center>
	<tr>
		<th colspan="11">Lab Procedures</th>
	</tr>

	<tr class="subHeader">
  		<td>Lab Code</td>
		<td>Status</td>
		<td>Commentary</td>
		<td>Results</td>
		<td>OfficeVisitID</td>
		<td>Updated Date</td>
  	</tr>
	<tr>
		<td ><%=bean.getLoinc()%></td>
		<td ><%=bean.getStatus()%></td>
		<td ><%=bean.getCommentary()%></td>
		<td ><%=bean.getResults()%></td>
		<td ><%=bean.getOvID()%></td>
		<td ><%=bean.getTimestamp()%></td>
	</tr>
</table>
<br /><br />

<%@include file="/footer.jsp" %>
