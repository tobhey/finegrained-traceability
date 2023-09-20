<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Monitor Patients";
%>

<%@include file="/header.jsp" %>

<%
session.removeAttribute("patientList");

ViewMyRemoteMonitoringListAction action = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> data = action.getPatientsData();
session.setAttribute("patientList", data);

PatientDAO patDAO = prodDAO.getPatientDAO();
PersonnelDAO persDAO = prodDAO.getPersonnelDAO();
%>
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="6">Patient Data</th>
	</tr>

	<tr class="subHeader">
		<th width="20%">Patient</th>
		<th width="20%">Date</th>
		<th>Systolic Blood Pressure</th>
		<th>Diastolic Blood Pressure</th>
		<th>Glucose Level</th>
		<th>Reporter</th>

	</tr>
	<%
		String highlight = "";
		String reporterName = "";
		int index = 0;
		for (RemoteMonitoringDataBean bean : data) {
			reporterName = "";
			if(bean.getTime() == null) {
				highlight = "#ff6666";
			} else if(((bean.getSystolicBloodPressure() != -1) && (bean.getSystolicBloodPressure() < 90 || bean.getSystolicBloodPressure() > 140))
					|| ((bean.getDiastolicBloodPressure() != -1) && (bean.getDiastolicBloodPressure() < 60 || bean.getDiastolicBloodPressure() > 90))
					|| ((bean.getGlucoseLevel() != -1 ) && (bean.getGlucoseLevel() < 70 || bean.getGlucoseLevel() > 150))) {
				highlight = "#ffff00";
				reporterName = authDAO.getUserName(bean.getReporterMID());
			} else {
				highlight = "";
				reporterName = authDAO.getUserName(bean.getReporterMID());
			}
	%>
	<tr bgcolor="<%=highlight %>">
		<td ><a href="/iTrust/auth/hcp/viewAdditionalInfo.jsp?patient=<%=index%>"><%=action.getPatientName(bean.getPatientMID()) + " (MID " + bean.getPatientMID() + ")"%></a></td>
		<td ><%=bean.getTime() != null ? bean.getTime(): "No Information Provided"%></td>
		<td ><%=(bean.getTime() != null && bean.getSystolicBloodPressure() != -1) ? bean.getSystolicBloodPressure(): ""%></td>
		<td ><%=(bean.getTime() != null && bean.getDiastolicBloodPressure() != -1) ? bean.getDiastolicBloodPressure(): ""%></td>
		<td ><%=(bean.getTime() != null && bean.getGlucoseLevel() != -1) ? bean.getGlucoseLevel(): ""%></td>
		<td><%=reporterName%></td>
	</tr>
	<%
			index++;
		}
	%>
</table>
<br />
<br />

<%@include file="/footer.jsp" %>
