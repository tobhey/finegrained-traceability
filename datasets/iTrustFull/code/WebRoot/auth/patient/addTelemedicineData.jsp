<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddRemoteMonitoringDataAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="java.util.List"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Report Status";
%>

<%@include file="/header.jsp"%>

<%
/* Require a Patient ID first */
long patientMID;
boolean representativeReporting = false;
String reportingFor = (String)request.getParameter("reportingFor");
if(reportingFor == null || 1 > reportingFor.length() || reportingFor.equals("null")) {
	patientMID = loggedInMID.longValue(); //If self-reporting
} else {
	patientMID = Long.parseLong(reportingFor);
	representativeReporting = true;
}


	AddRemoteMonitoringDataAction action = new AddRemoteMonitoringDataAction(
			prodDAO, loggedInMID.longValue(), patientMID);

	/* Update information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
	
	if (formIsFilled) {
		try {
			if (request.getParameter("glucoseLevel") == null || "".equals(request.getParameter("glucoseLevel"))){
				action.addRemoteMonitoringData(Integer.parseInt(request.getParameter("systolicBloodPressure")), 
						Integer.parseInt(request.getParameter("diastolicBloodPressure")));
			} else if (request.getParameter("systolicBloodPressure") == null || "".equals(request.getParameter("systolicBloodPressure"))){
				action.addRemoteMonitoringData(Integer.parseInt(request.getParameter("glucoseLevel")));
			} else {
				action.addRemoteMonitoringData(
						Integer.parseInt(request.getParameter("systolicBloodPressure")),
						Integer.parseInt(request.getParameter("diastolicBloodPressure")),
						Integer.parseInt(request.getParameter("glucoseLevel")));
			}
			
%>
		<div align=center>
			<span class="iTrustMessage">Information Successfully Added</span>
		</div>
<%
		} catch (FormValidationException e) {
			formIsFilled = false;
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
		} catch(NumberFormatException e) {
			formIsFilled = false;
%>
			<div align=center>
				<span class="iTrustError">Invalid entry: <%=e.getMessage() %>. Please enter a whole number.</span>
			</div>
<%
		}
	}
	if(!formIsFilled) {
%>

<form action="addTelemedicineData.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true">
<input type="hidden" name="reportingFor" value="<%=(String)request.getParameter("reportingFor")%>">
<br />
<table cellspacing=0 align=center cellpadding=0>
	<tr><th>
	<%=(patientMID == loggedInMID.longValue()) ? "Self-Reporting":"Report for " + action.getPatientName(patientMID) %>
	</th></tr>
	<tr>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<td class="subHeaderVertical">Systolic Blood Pressure:</td>
				<td><input name="systolicBloodPressure" value="" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Diastolic Blood Pressure:</td>
				<td><input name="diastolicBloodPressure" value="" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Glucose Level:</td>
				<td><input name="glucoseLevel" value="" type="text"></td>
			</tr>
			
		</table>
		</td>
		<td width="15px">&nbsp;</td>
		<td valign=top>
		
		</td>
	</tr>
</table>
<br />
<div align="center"><input type="submit" name="action"
	style="font-weight: bold;" value="Report">

<br />
<br />
<br />
<%
		if(!representativeReporting) {
%>
<table class="fTable" align=center>
	<th>Patient Representative Reporting</th>
		<%
			EditRepresentativesAction repsAction = new EditRepresentativesAction(
					prodDAO, 0L, Long.toString(patientMID));
			List<PatientBean> patients = repsAction.getRepresented(patientMID);
			if(patients.size() == 0) {
				%>
				<tr class="subHeader">
					<th>No Patients Represented</th>
				</tr>
				<%
			} else {
				%>
				<tr class="subHeader">
					<th>Patient</th>
				</tr>
				<%
			}
			for(PatientBean p : patients) {
				%>
				<tr><td><a href="#" onclick="javascript:document.getElementsByName('formIsFilled')[0].value='false';document.getElementsByName('reportingFor')[0].value='<%=p.getMID()%>';document.forms[0].submit();"><%=p.getFullName() %></a></td></tr>
				<%
			}
			
		%>
	
</table>
</div>
</form>

	<%
		}
	}
	%>
<br />
<br />

<%@include file="/footer.jsp"%>
