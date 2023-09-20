<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPrescriptionRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Get Prescription Report";
%>

<%@include file="/header.jsp" %>

<div align=center>
<h1>Prescription Report</h1>
<%
	String pidString = (String)session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/getPrescriptionReport.jsp");
  		return;
	}
	//else {
	//	session.removeAttribute("pid");
	//}
	
	long pid = Long.parseLong(pidString);
	ViewPrescriptionRecordsAction action = new ViewPrescriptionRecordsAction(DAOFactory.getProductionInstance(), loggedInMID);
	PatientBean patient = action.getPatient(pid);
	List<PrescriptionBean> prescriptions = action.getPrescriptionsForPatient(pid);

	if (prescriptions.size() == 0) { %>
	<i>No prescriptions found</i><br />
	<br />
	<br />
<%		} else { %>
	<table class="fTable">
		<tr>
			<th>ND Code</th>
			<th>Description</th>
			<th>Duration</th>
			<th>Prescribing HCP</th>
		</tr>
<%			for (PrescriptionBean prescription : prescriptions) { %>
		<tr>
			<td ><%=prescription.getMedication().getNDCodeFormatted() %></td>
			<td ><%=prescription.getMedication().getDescription() %></td>
			<td ><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
			<td ><%= action.getPrescribingDoctor(prescription).getFullName() %></td>
		</tr>
<%			} %>
	</table>
<%		} %>
</div>
<br />
<br />
<itrust:patientNav thisTitle="Prescriptions"/>


<%@include file="/footer.jsp" %>
