<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Additional Info";
%>

<%@include file="/header.jsp" %>

<%
int patientIndex = 0;
if(request.getParameter("patient") != null){
	String patient = request.getParameter("patient");
	session.setAttribute("patient", patient);
	patientIndex = Integer.parseInt(patient);
}
else{
	String patient = (String) session.getAttribute("patient");
	patientIndex = Integer.parseInt(patient);
}
List<RemoteMonitoringDataBean> patientList = (List<RemoteMonitoringDataBean>) session.getAttribute("patientList");
RemoteMonitoringDataBean rmdb = patientList.get(patientIndex);
PatientDAO patDAO = prodDAO.getPatientDAO();
long patientMID = rmdb.getPatientMID();
PatientBean p = patDAO.getPatient(patientMID);

ViewMyRemoteMonitoringListAction listAction = new ViewMyRemoteMonitoringListAction(prodDAO,loggedInMID.longValue());
String startDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
String endDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
if("date".equals(request.getParameter("sortBy"))) {
	startDate = request.getParameter("startDate");
	endDate = request.getParameter("endDate");
%>
	<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="2">Patient Details</th>
		</tr>
		<tr >
			<td class="subHeaderVertical">Name:</td>
			<td><%=p.getFullName()%></td>
		</tr>
		<tr >
			<td class="subHeaderVertical">Phone:</td>
			<td><%=p.getPhone()%></td>
		</tr>
	</table>
	<br />
<%
	List<PatientBean> patientRepresentatives = patDAO.getRepresenting(patientMID);
%>
	<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="4">Patient's Representatives</th>
		</tr>
<%
	if(patientRepresentatives.isEmpty()){
%>
		<tr>
			<td>This patient has no representatives.</td>
		</tr>
<%
	}
	for(PatientBean pat : patientRepresentatives){
%>
		<tr>
			<td class="subHeaderVertical">Name:</td>
			<td><%=pat.getFullName()%></td>
			<td class="subHeaderVertical">Phone:</td>
			<td><%=pat.getPhone()%></td>
		</tr>
<%
	}
%>
	</table>
	<br />
	<br />
	<table class="fTable" align="center">
		<tr>
			<th colspan="5">Patient Monitoring Statistics</th>
		</tr>
		<tr class="subHeader">
			<th width="20%">Date</th>
			<th>Systolic Blood Pressure</th>
			<th>Diastolic Blood Pressure</th>
			<th>Glucose Level</th>
			<th>Reporter</th>
		</tr>
<%
	List<RemoteMonitoringDataBean> patientStats = listAction.getPatientDataByDate(patientMID, startDate, endDate);
	if(patientStats.isEmpty()){
%>
		<tr bgcolor="#ff6666">
			<td><b>No Information Provided</b></td>
			<td><b>No Information Provided</b></td>
			<td><b>No Information Provided</b></td>
			<td><b>No Information Provided</b></td>
			<td><b>No Information Provided</b></td>
		</tr>
<%
	}
	String highlight = "";
	String reporterName = "";
	for(RemoteMonitoringDataBean stats : patientStats){
		highlight = "";
		int sysBP = stats.getSystolicBloodPressure();
		int diasBP = stats.getDiastolicBloodPressure();
		int gluLvl = stats.getGlucoseLevel();
		
		if(((sysBP != -1) && (sysBP < 90 || sysBP > 140)) || ((diasBP != -1) && (diasBP < 60 || diasBP > 90)) || ((gluLvl != -1) && (gluLvl < 70 || gluLvl > 150))) {
			highlight = "#ffff00";
		}
		
		reporterName = authDAO.getUserName(stats.getReporterMID());		
%>
		<tr bgcolor="<%=highlight%>">
			<td><%=stats.getTime()%></td>
			<td><%=sysBP == -1?"":sysBP%></td>
			<td><%=diasBP == -1?"":diasBP%></td>
			<td><%=gluLvl == -1?"":gluLvl%></td>
			<td><%=reporterName%></td>
		</tr>
<%
	}
%>
	</table>
	<br />
<%
} else{
%>

<form action="viewAdditionalInfo.jsp" id=datebuttons style="display: inline;" method="post">
<input type="hidden" name="sortBy" value="date"></input>
<div align=center>
<table class="fTable" align="center">
	<tr class="subHeader">
		<td>Start Date:</td>
		<td>
			<input name="startDate" value="<%=startDate%>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>
		<td>End Date:</td>
		<td>
			<input name="endDate" value="<%=endDate%>">
			<input type=button value="Select Date" onclick="displayDatePicker('endDate');">
		</td>
	</tr>
</table>
<br />
<input type="submit" name="submit" value="Get Records" onclick="javascript:sortBy();">
</div>
</form>
<%
}
%>

<script type='text/javascript'>
function sortBy() {
	document.getElementsByName('sortBy')[0].value = "date";
	document.forms[0].submit.click();
}
</script>

<%@include file="/footer.jsp"%>