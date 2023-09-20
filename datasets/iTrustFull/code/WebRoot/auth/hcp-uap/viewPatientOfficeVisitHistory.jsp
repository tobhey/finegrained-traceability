<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View All Patients";
%>

<%@include file="/header.jsp" %>

<%
ViewPatientOfficeVisitHistoryAction action = new ViewPatientOfficeVisitHistoryAction(prodDAO, loggedInMID.longValue());
List<PatientVisitBean> patientVisits = action.getPatients();
%>
<br />

<form action="viewReport.jsp" method="post" name="myform">
<table class="fTable" align="center">
	<tr>
		<th colspan="3">Past Patients</th>
	</tr>

	<tr class="subHeader">
		<th>Patient</th>
		<th>Address</th>
		<th>Last Visit</th>

	</tr>
	<%
		List<PatientBean> patients = new ArrayList<PatientBean>();
		int index = 0;
		for (PatientVisitBean bean : patientVisits) {
			patients.add(bean.getPatient());
	%>
	<tr>
		<td >
			<a href="editPHR.jsp?patient=<%=index%>">
		
		
			<%=bean.getPatientName()%>	
		
		
			</a>
			</td>
		<td ><%=bean.getAddress1() +" " +bean.getAddress2()%></td>
		<td ><%=bean.getLastOVDateM() +"/" +bean.getLastOVDateD() +"/" +bean.getLastOVDateY()%></td>
	</tr>
	<%
			index ++;
		}
		session.setAttribute("patients", patients);
	%>
</table>
</form>
<br />
<br />

<%@include file="/footer.jsp" %>
