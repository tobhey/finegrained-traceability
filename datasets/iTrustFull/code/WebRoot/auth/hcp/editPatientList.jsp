<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.EditMonitoringListAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Patient List";
%>

<%@include file="/header.jsp" %>

<%
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
String addOrRemove = "Add";
if (pidString == null || 1 > pidString.length() || "false".equals(request.getParameter("confirmAction"))) {
	session.removeAttribute("pid");
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/editPatientList.jsp");
   	return;
}
//else {
//	session.removeAttribute("pid");
//}
	
EditMonitoringListAction action = new EditMonitoringListAction(prodDAO,loggedInMID.longValue());
long pid = Long.parseLong(pidString);
String patientName = action.getPatientName(pid);
if (action.isPatientInList(pid)) {
	addOrRemove = "Remove";
}
String confirm = "";
boolean conf_bool = false;

if ("true".equals(request.getParameter("confirmAction"))) {
	if(addOrRemove.equals("Add")) {
		conf_bool = action.addToList(pid);
		if(conf_bool)
			confirm = "Patient " + patientName + " Added";
	} else {
		conf_bool = action.removeFromList(pid);
		if(conf_bool)
			confirm = "Patient " + patientName + " Removed";
	}
	
	session.removeAttribute("pid");
	
}

if (!"".equals(confirm)) {
%>
	<div align=center>
		<span class="iTrustMessage"><%=confirm%></span>
	</div>
<%
} else {
%>

<br />

<form action="editPatientList.jsp" method="post">
	<input type="hidden" name="confirmAction" value="true"></input>
	<input type="submit" value="<%=addOrRemove %> <%=patientName %>">
	<input type="submit" value="Choose Different Patient" onClick="javascript:differentPatient();">
</form>

<script type="text/javascript">

function differentPatient() {
	document.getElementsByName("confirmAction")[0].value="false";
	document.forms[0].submit();
}

</script>

<%
}
%>
<br />
<br />
<br />

<%@include file="/footer.jsp" %>
