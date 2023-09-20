<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddRemoteMonitoringDataAction"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Report Status";
%>

<%@include file="/header.jsp"%>

<%
String switchString = "";
if (request.getParameter("switch") != null) {
	switchString = request.getParameter("switch");
}

String patientString = "";
if (request.getParameter("patient") != null) {
	patientString = request.getParameter("patient");
}

String pidString;
if (switchString.equals("true")) pidString = "";
else if (!patientString.equals("")) {
	int patientIndex = Integer.parseInt(patientString);
	List<Long> patients = (List<Long>) session.getAttribute("patients");
	pidString = "" + patients.get(patientIndex);
	session.removeAttribute("patients");
	session.removeAttribute("patient");
	session.setAttribute("pid", pidString);
}
else pidString = (String)session.getAttribute("pid");

if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("../uap/getPatientMonitorList.jsp?forward=uap/addTelemedicineData.jsp");
   	return;
}
	AddRemoteMonitoringDataAction action = new AddRemoteMonitoringDataAction(
			prodDAO, loggedInMID.longValue(),Long.parseLong(pidString));

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
	session.removeAttribute("pid");
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
<br />
<table cellspacing=0 align=center cellpadding=0>
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
	
	<%
	}
	%>
<br />
<br />
</div>
</form>

<%@include file="/footer.jsp"%>
