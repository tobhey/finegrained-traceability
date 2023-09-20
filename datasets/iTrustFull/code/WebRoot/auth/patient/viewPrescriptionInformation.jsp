<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.EditPrescriptionAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Prescription Information";
%>

<%@include file="/header.jsp" %>

<%
PersonnelDAO docs = new PersonnelDAO(prodDAO);
OfficeVisitDAO myDAO = new OfficeVisitDAO(prodDAO);
int id = Integer.parseInt(request.getParameter("presID"));

PrescriptionBean prescription = null;
for (PrescriptionBean pres : myDAO.getPrescriptions(Long.parseLong(request.getParameter("visitID")))) {
	if (pres.getId() == id) {
		prescription = pres;
	}
}
if (prescription == null) {
	%><center><h1>Error: Prescription not found, please try again later.</h1></center><%
} else {
%>
	<table align=center border=1 class="fTable">
		<tr>
			<th colspan=5>Prescription Information</th>
		</tr>
		<tr class="subHeader">
			<td align=center>Perscribing Doctor</td>
			<td align=center>Medication</td>
			<td align=center>Dosage</td>
			<td align=center>Dates</td>
			<td align=center colspan=2>Instructions</td>
		</tr>
		<tr>
			<td align=center"><%=docs.getName(myDAO.getOfficeVisit(Long.parseLong(request.getParameter("visitID"))).getHcpID()) %></td>
			<td align=center><%=prescription.getMedication().getDescription() %> (<%=prescription.getMedication().getNDCode() %>)</td>
			<td align=center><%=prescription.getDosage() %>mg</td>
			<td align=center><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
			<td align=center><%=prescription.getInstructions() %></td>
		</tr>
	</table>
	<center>
	<form action="home.jsp" method=get>
	<input type=submit name="Home" value="Home">
	</form>
	</center>
<%}%>
<%@include file="/footer.jsp" %>