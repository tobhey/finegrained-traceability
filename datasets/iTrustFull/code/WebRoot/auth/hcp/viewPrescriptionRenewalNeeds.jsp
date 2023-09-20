<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPrescriptionRenewalNeedsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - My Patients with Potential Prescription-Renewal Needs";
%>

<%@include file="/header.jsp"%>

<%
PersonnelBean self = new PersonnelDAO(prodDAO).getPersonnel(loggedInMID); 

ViewPrescriptionRenewalNeedsAction expAction = new ViewPrescriptionRenewalNeedsAction(prodDAO, loggedInMID.longValue());

//List<PersonnelBean> personnelList = prodDAO.getPersonnelDAO().getAllPersonnel();

//session.setAttribute("personnelList", personnelList);

%>
<div align="center">
	<br />
	<table class="fTable">
	
	
	
	
	
	<%
		List<PatientBean> patients = expAction.getRenewalNeedsPatients();

		//patients = new PatientDAO(prodDAO).getAllPatients(); // Temporary test
		if (patients.size() == 0) { 
%>
		<tr>
			<td colspan=4>
				<i>No patients have renewal needs.</i>
			</td>
		</tr>
<%
		} else { 
%>
		<tr>
			<th colspan=4><%= self.getFullName() %></th>
		</tr>
		<tr class="subHeader">
			<td>Patient Name</td>
			<td>Phone Number</td>
			<td>Email Address</td>
		</tr>
<%	
	for (PatientBean patient : patients) { 
		//PersonnelBean doctor = expAction.getPrescribingDoctor(prescription);
%>
		<tr>
			<td ><a href='sendEmailNotification.jsp?mid=<%=patient.getMID()%>'><%=patient.getFullName() %></a></td>
			<td ><%=patient.getPhone() %></td>
			<td ><%=patient.getEmail() %></td>
			
			
			
			
		</tr>
<%			
			}
		}
	
	
%>
	</table>	
	<br />
</div>

<%@include file="/footer.jsp"%>
