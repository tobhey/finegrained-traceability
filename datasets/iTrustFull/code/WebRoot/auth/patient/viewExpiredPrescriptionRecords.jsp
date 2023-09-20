<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewExpiredPrescriptionsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Get My Expired Prescription Reports";
%>

<%@include file="/header.jsp"%>

<%
PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue()); 

ViewExpiredPrescriptionsAction expAction = new ViewExpiredPrescriptionsAction(prodDAO, loggedInMID.longValue());

List<PersonnelBean> personnelList = prodDAO.getPersonnelDAO().getAllPersonnel();

session.setAttribute("personnelList", personnelList);

%>
<div align="center">
	<br />
	<table class="fTable">
	
	
	
	
	
	<%
		List<PrescriptionBean> prescriptions = expAction.getPrescriptionsForPatient(loggedInMID.longValue());
		if (prescriptions.size() == 0) { 
%>
		<tr>
			<td colspan=4>
				<i>No prescriptions found</i>
			</td>
		</tr>
<%
		} else { 
%>
		<tr>
			<th colspan=4><%= patient.getFullName() %></th>
		</tr>
		<tr class="subHeader">
			<td>ND Code</td>
			<td>Description</td>
			<td>Duration</td>
			<td>Prescribing HCP</td>
		</tr>
<%	
	for (PrescriptionBean prescription : prescriptions) { 
		PersonnelBean doctor = expAction.getPrescribingDoctor(prescription);
%>
		<tr>
			<td ><a href="viewPrescriptionInformation.jsp?visitID=<%=prescription.getVisitID()%>&presID=<%=prescription.getId()%>"><%=prescription.getMedication().getNDCodeFormatted() %></a></td>
			<td ><%=prescription.getMedication().getDescription() %></td>
			<td ><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
			<td ><a href=viewLHCP.jsp?index=<%=doctor.getIndexIn(personnelList) %> ><%=doctor.getFullName() %></a></td>
			
			
			
		</tr>
<%			
			}
		}
	
	
%>
	</table>	
	<br />
</div>

<%@include file="/footer.jsp"%>
