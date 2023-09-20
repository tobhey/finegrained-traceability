<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateCPTCodeListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.HtmlEncoder"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain CPT Codes";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "Viewing Current CPT Procedure Codes";
	
	UpdateCPTCodeListAction cptUpdater = new UpdateCPTCodeListAction(prodDAO, loggedInMID.longValue());
	String attribute = null;
	if (request.getParameter("add") != null || request.getParameter("update") != null) {
		try {
			if (null != request.getParameter("attribute")) {
				attribute = new String("immunization");
			}
			
			ProcedureBean proc = new ProcedureBean(request.getParameter("code"), request.getParameter("description"), attribute );
			headerMessage = (request.getParameter("add") != null) ? cptUpdater.addCPTCode(proc)	: cptUpdater.updateInformation(proc);
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

function fillUpdate(code) {
	document.getElementById("code").value = code;
	document.getElementById("description").value = unescape(document.getElementById("UPD" + code).value);
	document.getElementById("oldDescrip").value = unescape(document.getElementById("UPD" + code).value);
	if ("immunization" == document.getElementById("CLASS"+code).value) {
		document.getElementById("attribute").checked = "checked";
	}
	else {
		document.getElementById("attribute").checked = "";
	}
}
</script>

<div align="center">
<br />
<span class="iTrustMessage"><%=headerMessage %></span>
<br />
<br />

<form name="mainForm" action="editCPTProcedureCodes.jsp" method="post">
	<input type="hidden" id="updateID" name="updateID" value="" />
	<input type="hidden" id="oldDescrip" name="oldDescrip" value="" />
<table class="fTable" align="center">
	<tr>
		<td colspan="3">Update CPT Procedure Code List</td>
	</tr>
	<tr class="subHeader">
		<td>Code</td>
		<td>Description</td>
		<td>Immunization?</td>
	</tr>
	<tr>
		<td><input type="text" name="code" id="code" size="5" maxlength="5" /></td>
		<td><input type="text" name="description" id="description" size="60" maxlength="256" /></td>
		<td><input type="checkbox" name="attribute" id="attribute" value="yes"/></td> 
	</tr>
</table>
<input type="submit" name="add" value="Add Code" />
<input type="submit" name="update" value="Update Code" />

<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="3">Current CPT Procedure Codes</th>
	</tr>
	<tr class="subHeader">
		<td>Code</td>
		<td>Description</td>
		<td>Immunization?</td>
	</tr>
	<%
		List<ProcedureBean> codeList = prodDAO.getCPTCodesDAO().getAllCPTCodes();
		String tempCode = "";
		String tempDescrip = "";
		String escapedDescrip = "";
		String tempClass = "";
		for (ProcedureBean codeEntry : codeList) {
			tempCode = codeEntry.getCPTCode();
			tempDescrip = codeEntry.getDescription();
			tempClass = codeEntry.getAttribute();
			escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
	%>
		<tr>
			<td><%=tempCode %></td>
			<td><a href="javascript:void(0)" onclick="fillUpdate('<%=tempCode %>')"><%=HtmlEncoder.encode(tempDescrip)%></a>
				<input type="hidden" id="UPD<%=tempCode %>"	name="UPD<%=tempCode %>" value="<%=escapedDescrip %>" />
				<input type="hidden" id="CLASS<%=tempCode%>" name="CLASS<%=tempCode%>" value="<%=tempClass%>" />		
			</td>
			<td><%=("immunization".equals(codeEntry.getAttribute()))?"Yes":"No"%></td>
		</tr>
	<% } %>
</table>
</form>
</div>
<br />


<%@include file="/footer.jsp" %>
