<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.HtmlEncoder"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain Appointment Types";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "Viewing Current Appointment Types";
	
	EditApptTypeAction atEditor = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	if (request.getParameter("add") != null || request.getParameter("update") != null) {
		try {
			if(request.getParameter("name").equals("") || request.getParameter("duration").equals(""))
				headerMessage = "Please fill in all fields.";
			else {
				ApptTypeBean apptType = new ApptTypeBean(request.getParameter("name"), Integer.parseInt(request.getParameter("duration")));
				headerMessage = (request.getParameter("add") != null) ? atEditor.addApptType(apptType) : atEditor.editApptType(apptType);
			}
		} 
		catch(FormValidationException e) {
%>
	<div align=center>
		<span class="iTrustError"><%=e.getMessage() %></span>
	</div>
<%
			headerMessage = "Validation Errors";
		}
		
	}
	String headerColor = (headerMessage.indexOf("Error") > -1)
			? "#ffcccc"
			: "#00CCCC";
%>
<script type="text/javascript">

function fillUpdate(name) {
	document.getElementById("name").value = name;
	document.getElementById("duration").value = unescape(document.getElementById(name).value);
}
</script>

<div align="center">
<br />
<span class="iTrustMessage"><%=headerMessage %></span>
<br />
<br />

<form name="mainForm" action="editApptType.jsp" id="mainForm" method="post">
<table class="fTable" align="center">
	<tr>
		<td colspan="3">Update Appointment Type List</td>
	</tr>
	<tr class="subHeader">
		<td>Name</td>
		<td>Duration</td>
	</tr>
	<tr>
		<td><input type="text" name="name" id="name" size="30" maxlength="30" /></td>
		<td><input type="text" name="duration" id="duration" size="5" maxlength="5" /></td>
	</tr>
</table>
<input type="submit" name="add" id="add" value="Add Type" />
<input type="submit" name="update" id="update" value="Update Type" />

<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="3">Current Appointment Types</th>
	</tr>
	<tr class="subHeader">
		<td>Name</td>
		<td>Duration</td>
	</tr>
	<%
		List<ApptTypeBean> typeList = prodDAO.getApptTypeDAO().getApptTypes();
		int tempDuration = 0;
		String tempName = "";
		for (ApptTypeBean apptEntry : typeList) {
			tempDuration = apptEntry.getDuration();
			tempName = apptEntry.getName();
	%>
		<tr>
			<td><a href="javascript:void(0)" onclick="fillUpdate('<%=tempName %>')"><%=tempName %></a>
				<input type="hidden" id="<%=tempName%>" name="<%=tempName%>" value="<%=tempDuration%>" />		
			</td>
			<td><%=tempDuration %></td>
		</tr>
	<% } %>
</table>
</form>
</div>
<br />


<%@include file="/footer.jsp" %>
