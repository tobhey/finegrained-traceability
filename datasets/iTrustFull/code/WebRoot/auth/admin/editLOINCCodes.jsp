<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.LOINCbean"%>
<%--@page import="edu.ncsu.csc.itrust.action.UpdateLabProcListAction"--%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateLOINCListAction"%> 
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain LOINC Codes";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "Viewing Current LOINC Codes";
	
/*UpdateLabProcListAction icdUpdater =
		new UpdateLabProcListAction(prodDAO, loggedInMID.longValue());*/
UpdateLOINCListAction icdUpdater =
		new UpdateLOINCListAction(prodDAO, loggedInMID.longValue());		
	
	if (request.getParameter("add") != null || request.getParameter("update") != null) {
		try {
			LOINCbean diag =
				new LOINCbean();
				diag.setLabProcedureCode(request.getParameter("code"));
			diag.setComponent(request.getParameter("comp"));
			diag.setKindOfProperty(request.getParameter("kop"));
			diag.setTimeAspect(request.getParameter("time"));
			diag.setSystem(request.getParameter("system"));
			diag.setScaleType(request.getParameter("scale"));
			diag.setMethodType(request.getParameter("method"));
			
			headerMessage = (request.getParameter("add") != null)
					? icdUpdater.add(diag)
					: icdUpdater.updateInformation(diag);
		} catch(FormValidationException e) {
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

<div align=center>
<form name="mainForm" method="post">
<input type="hidden" id="updateID" name="updateID" value="">
<input type="hidden" id="oldDescrip" name="oldDescrip" value="">

<script type="text/javascript">
	function fillUpdate(comp) {
		document.getElementById("code").value = comp;
				
	}
	function fillUpdate2(comp) {
		document.getElementById("comp").value = comp;
	}
	function fillUpdate3(comp) {
		document.getElementById("kop").value = comp;
	}
	function fillUpdate4(comp) {
		document.getElementById("time").value = comp;
	}
	function fillUpdate5(comp) {
		document.getElementById("system").value = comp;
	}
	function fillUpdate6(comp) {
		document.getElementById("scale").value = comp;
	}
	function fillUpdate7(comp) {
		document.getElementById("method").value = comp;
					
	}
	
	
</script>

<br />

<span class="iTrustMessage"><%=headerMessage %></span>
<br />

<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="4">Update LOINC Code List</th>
	</tr>
	<tr class="subHeader">
		<th colspan=3 >Code</th>
	</tr>
	<tr>
		<td align="center" colspan=3>
			<input type="text" name="code" id="code" size="7" maxlength="7" />
		</td>
	</tr>
	<tr class="subHeader">
		<th>Component</th>
		<th>Kind Of Property</th>
		<th>Time Aspect</th>
	</tr>
	<tr>
		<td><input type="text" name="comp" id="comp"
								size="40" maxlength="100" /></td>
		<td><input type="text" name="kop" id="kop"
								size="40" maxlength="100" /></td>
		<td><input type="text" name="time" id="time"
								size="40" maxlength="100" /></td>
	</tr>
	<tr class="subHeader">		
		<th>System</th>
		<th>Scale Type</th>
		<th>Method Type</th>
	</tr>
<tr>
		<td><input type="text" name="system" id="system"
								size="40" maxlength="100" /></td>
		<td><input type="text" name="scale" id="scale"
								size="40" maxlength="100" /></td>
		<td><input type="text" name="method" id="method"
								size="40" maxlength="100" /></td>
								
	</tr>
</table>
<span style="font-size:14px;">NOTE: When updating, please enter original information in fields you do not want to change</span>
<br />
<br />
<input type="submit" name="add" value="Add Code" />
<input type="submit" name="update" value="Update Code" />
<br />
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="7">Current LOINC Codes</th>
	</tr>
	<tr class="subHeader">
		<td>Code</td>
		<td>Component</td>
		<td>Kind Of Property</td>
		<td>Time Aspect</td>
		<td>System</td>
		<td>Scale Type</td>
		<td>Method Type</td>
	</tr>
	<%
		List<LOINCbean> codeList = prodDAO.getLOINCDAO().getAllLOINC();
		String tempCode = "";
		String tempcomp = "";
		String tempkop = "";
		String temptime = "";
		String tempsystem="";
		String tempscale = "";
		String tempmethod="";

		
		for (LOINCbean codeEntry : codeList) {
			tempCode = codeEntry.getLabProcedureCode();
			tempcomp = codeEntry.getComponent();
			tempkop = codeEntry.getKindOfProperty();
			temptime = codeEntry.getTimeAspect();
			tempsystem = codeEntry.getSystem();
			tempscale = codeEntry.getScaleType();
			tempmethod = codeEntry.getMethodType();
			

	%>
		<tr>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate('<%=tempCode%>')"><%=tempCode%></a>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate2('<%=tempcomp%>')"><%=tempcomp%></a>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate3('<%=tempkop%>')"><%=tempkop%></a>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate4('<%=temptime%>')"><%=temptime%></a>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate5('<%=tempsystem%>')"><%=tempsystem%></a>
			<td nowrap><a href="javascript:void(0)" 
					onclick="fillUpdate6('<%=tempscale%>')"><%=tempscale%></a>
			<td nowrap><a href="javascript:void(0)" 
					onclick="fillUpdate7('<%=tempmethod%>')"><%=tempmethod%></a>
		</tr>
	<% } %>
</table>
</form>
</div>
<br />


<%@include file="/footer.jsp" %>
