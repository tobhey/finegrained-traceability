<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateICDCodeListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain ICD Codes";
%>

<%@include file="/header.jsp" %>

<%
String headerMessage = "Viewing Current ICD Codes";
	
UpdateICDCodeListAction icdUpdater = new UpdateICDCodeListAction(prodDAO, loggedInMID.longValue());

if (request.getParameter("add") != null || request.getParameter("update") != null) {
	try {
		DiagnosisBean diag = 
			new DiagnosisBean(request.getParameter("code"), request.getParameter("description"), request.getParameter("classification"));
		headerMessage = (request.getParameter("add") != null)?icdUpdater.addICDCode(diag):icdUpdater.updateInformation(diag);
	} catch(FormValidationException e) {
%>
		<div align=center>
			<span class="iTrustError"><%=e.getMessage() %></span>
		</div>
<%
		headerMessage = "Validation Errors";
	}
	
}
String headerColor = (headerMessage.indexOf("Error") > -1) ? "#ffcccc" : "#00CCCC";
%>

<div align=center>
<form name="mainForm" method="post">
<input type="hidden" id="updateID" name="updateID" value="" />
<input type="hidden" id="oldDescrip" name="oldDescrip" value="" />
<script type="text/javascript">
	function fillUpdate(code) {
		document.getElementById("code").value = code;
		document.getElementById("description").value = unescape(document.getElementById("UPD" + code).value);
		if ("yes" == document.getElementById("CLASS"+code).value) {
			document.getElementById("classification").checked = "checked";
		}
		else {
			document.getElementById("classification").checked = "";
		}
		
	}
</script>

<br />

<span class="iTrustMessage"><%=headerMessage %></span>

<br />
<br />

<table class="fTable" align="center">
<tr>
	<th colspan="3">Update ICD Code List</th>
</tr>
<tr class="subHeader">
		<th>Code</th>
		<th>Chronic?</th>
		<th>Description</th>
</tr>
	<tr>
		<td><input type="text" name="code" id="code" size="8" maxlength="8" /></td>
		<td><input type="checkbox" name="classification" id="classification" value="yes" /></td>
		<td><input type="text" name="description" id="description" size="40" maxlength="100" /></td>
	</tr>
</table>
<br />
<input type="submit" name="add" value="Add Code" />
<input type="submit" name="update" value="Update Code" />
<br />
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="3">Current ICD Codes</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Chronic?</th>
		<th>Description</th>
	</tr>
<%
	List<DiagnosisBean> codeList = prodDAO.getICDCodesDAO().getAllICDCodes();
	String tempCode = "";
	String tempDescrip = "";
	String tempClass = "";
	String escapedDescrip = "";
	for (DiagnosisBean codeEntry : codeList) {
		tempCode = codeEntry.getICDCode() + "";
		tempDescrip = codeEntry.getDescription();
		tempClass = codeEntry.getClassification();
		escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
%>
<tr>
	<td align="center"><%=tempCode%></td>
	<td align="center"><%=tempClass%></td>
	<td align="center">
		<a href="javascript:void(0)" onclick="fillUpdate('<%=tempCode %>')"><%=tempDescrip%></a>
		<input type="hidden" id="UPD<%=tempCode%>" name="UPD<%=tempCode%>" value="<%=escapedDescrip%>" />
		<input type="hidden" id="CLASS<%=tempCode%>" name="CLASS<%=tempCode%>" value="<%=tempClass%>" />
	</td>
</tr>

<% } %>

</table>
</form>
</div>
<br />


<%@include file="/footer.jsp" %>
