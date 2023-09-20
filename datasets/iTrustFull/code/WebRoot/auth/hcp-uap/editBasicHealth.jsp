<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.EditHealthHistoryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.HealthRecordForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Basic Health Record";
%>

<%@include file="/header.jsp" %>

<%
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/editBasicHealth.jsp");
   	return;
}
//else {
//	session.removeAttribute("pid");
//}
	
EditHealthHistoryAction action = new EditHealthHistoryAction(prodDAO,loggedInMID.longValue(), pidString);
long pid = action.getPid();
String patientName = action.getPatientName();
String confirm = "";
if ("true".equals(request.getParameter("formIsFilled"))) {
	try { 
		confirm = action.addHealthRecord(pid, new BeanBuilder<HealthRecordForm>().build(request.getParameterMap(), new HealthRecordForm()));
	} catch(FormValidationException e){
%>
		<div align=center>
			<span class="iTrustError"><%e.printHTML(pageContext.getOut());%></span>
		</div>
		<br />
<%
	}
}
List<HealthRecord> records = action.getAllHealthRecords(pid);
HealthRecord mostRecent = (records.size() > 0) ? records.get(0) : new HealthRecord(); //for the default values
%>

<script type="text/javascript">
function showAddRow(){
	document.getElementById("addRow").style.display="inline";
	document.getElementById("addRowButton").style.display="none";
	document.forms[0].height.focus();
}
</script>

<%
if (!"".equals(confirm)) {
%>
	<div align=center>
		<span class="iTrustMessage"><%=confirm%></span>
	</div>
<%
}
%>

<br />
<div align=center>
	<table align="center" class="fTable">
		<tr>
			<th colspan="10">Basic Health History</th>
		</tr>
		<tr class = "subHeader">
			<td>Height</td>
			<td>Weight</td>
			<td>Smokes?</td>
			<td>Blood Pressure</td>
			<td>HDL</td>
			<td>LDL</td>
			<td>Triglycerides</td>
			<td>Total Cholesterol</td>
			<td>Last Recorded</td>
			<td>By Personnel</td>
		</tr>
	<%
	for (HealthRecord hr : records) {
	%>
		<tr>
			<td align=center><%=hr.getHeight()%>in</td>
			<td align=center><%=hr.getWeight()%>lbs</td>
			<td align=center><%=hr.isSmoker() ? "Y" : "N"%></td>
			<td align=center><%=hr.getBloodPressure()%> mmHg</td>
			<td align=center><%=hr.getCholesterolHDL()%> mg/dL</td>
			<td align=center><%=hr.getCholesterolLDL()%> mg/dL</td>
			<td align=center><%=hr.getCholesterolTri()%> mg/dL</td>
			<td align=center><%=hr.getTotalCholesterol()%> mg/dL</td>
			<td align=center><%=hr.getDateRecorded()%></td>
<%
		PersonnelBean p = prodDAO.getPersonnelDAO().getPersonnel(hr.getPersonnelID());
%>
			<td align=center><%=p.getFullName()%></td>
		</tr>
	<%
	}
	%>
	</table>
	<br />
	<a href="javascript:showAddRow();" id="addRowButton" style="text-decoration:none;" >
		<input type=button value="Add Record" onClick="showAddRow();"> 
	</a>
</div>
<br />
<div id="addRow" style="display: none;" align=center>
<form action="editBasicHealth.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true">
<table class="fTable" align="center">
	<tr>
		<th colspan="2" style="background-color:silver;">Record Information</th>
	</tr>	
	<tr>
		<td class="subHeader">Height (in.):</td>
		<td ><input name="height"
			value="<%=mostRecent.getHeight()%>" style="width: 50px" type="text"
			maxlength="5"></td>
	</tr>
	<tr>
		<td class="subHeader">Weight (lbs.):</td>
		<td ><input name="weight"
			value="<%=mostRecent.getWeight()%>" style="width: 50px" type="text"
			maxlength="5"></td>
	</tr>
	<tr>
		<td class="subHeader">Smoker?:</td>
		<td ><input type="radio" name="isSmoker"
			value="false" checked /> No <input type="radio" name="isSmoker"
			value="true" /> Yes</td>
	</tr>
	<tr>
		<td class="subHeader">Blood Pressure (mmHg):</td>
		<td >
			<input name="bloodPressureN" value="<%=mostRecent.getBloodPressureN()%>" style="width: 40px" maxlength="3" type="text" /> 
			/ <input name="bloodPressureD" value="<%=mostRecent.getBloodPressureD()%>" style="width: 40px" maxlength="3" type="text" />
		</td>
	</tr>
	<tr>
		<td class="subHeader">Cholesterol (mg/dL):</td>
		<td >
		<table>
			<tr>
				<td style="text-align: right">HDL:</td>
				<td><input name="cholesterolHDL" value="<%=mostRecent.getCholesterolHDL()%>" 
				style="width: 38px" maxlength="3" type="text"></td>
			</tr>
			<tr>
				<td style="text-align: right">LDL:</td>
				<td>
					<input name="cholesterolLDL" value="<%=mostRecent.getCholesterolLDL()%>" style="width: 38px" maxlength="3" type="text">
				</td>
			</tr>
			<tr>
				<td style="text-align: right">Tri:</td>
				<td>
					<input name="cholesterolTri" value="<%=mostRecent.getCholesterolTri()%>" style="width: 38px" maxlength="3" type="text">
			    </td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br />
<input type="submit" value="Add Record">
</form>
</div>

<br />
<br />
<br />
<itrust:patientNav thisTitle="Basic Health History" />

<%@include file="/footer.jsp" %>
