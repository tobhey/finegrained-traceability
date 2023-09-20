<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.EditPrescriptionAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Prescription Information";
%>

<%@include file="/header.jsp" %>

<%
boolean makeChange = false;
if (request.getParameter("change") != null) {
	if (request.getParameter("change").equalsIgnoreCase("True")) {
		makeChange = true;
		
	}
}

EditPrescriptionAction prescriptionAction = new EditPrescriptionAction(prodDAO);

String ovIDString = request.getParameter("ovID");
String pidString = (String)session.getAttribute("pid");
int presID = Integer.parseInt(request.getParameter("presID"));

EditOfficeVisitAction officeAction = new EditOfficeVisitAction(prodDAO, loggedInMID, pidString, ovIDString);
OfficeVisitBean ov = officeAction.getOfficeVisit();

PrescriptionBean prescription = null;
for (PrescriptionBean pres : ov.getPrescriptions()) {
	if (pres.getId() == presID) {
		prescription = pres;
		break;
	}
}
if (prescription == null) {%>
	<h1>ERROR: prescription not found.</h1>
<%}else{%>
	<%if (!makeChange) { %>
		<%// This portion prints a form to edit the prescription information. %>
		<form action="editPrescription.jsp?ovID=<%=ovIDString%>&presID=<%=presID%>&change=True" method=post>
		<table align=center border=1 class="fTable">
			<tr>
				<th colspan=5>Edit Prescription Instructions</th>
			</tr>
			<tr class="subHeader">
				<td align=center>Medication</td>
				<td align=center>Dosage</td>
				<td align=center>Dates</td>
				<td align=center colspan=2>Instructions</td>
			</tr>
			<tr>
				<td align=center><%=prescription.getMedication().getDescription() %> (<%=prescription.getMedication().getNDCode() %>)</td>
				<td align=center><input type="text" name="dosage" value="<%=prescription.getDosage() %>" size="5">(In mg)</td>
				<td align=center><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
				<td align=center><textarea name="instructions" rows="2" cols="20"><%=prescription.getInstructions() %></textarea></td>
			</tr>
		</table>
		<br />
		<center>
		<input type="submit" value="Change">
		<input type="reset" value="Reset">
		</center>
		</form>
		
	<%} else {%>
		<%// This portion prints out the recently edited prescription information and provides a link to the home page.
		try {
			prescription.setDosage(Integer.parseInt(request.getParameter("dosage")));
			prescription.setInstructions(request.getParameter("instructions"));
			prescriptionAction.editPrescription(prescription);
			%> 
			<table align=center border=1 class="fTable">
			<tr>
				<th colspan=5>Prescription Updated!</th>
			</tr>
			<tr class="subHeader">
				<td align=center>Medication</td>
				<td align=center>Dosage</td>
				<td align=center>Dates</td>
				<td align=center colspan=2>Instructions</td>
			</tr>
			<tr>
				<td align=center><%=prescription.getMedication().getDescription() %> (<%=prescription.getMedication().getNDCode() %>)</td>
				<td align=center><%=prescription.getDosage() %>mg</td>
				<td align=center><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
				<td align=center><%=prescription.getInstructions() %></td>
			</tr>
			</table>
			<%
		} catch(Exception e) {
			%><center><b>The Prescription could not be edited due to an internal error.<br />Please Try again later.<br /><%=e.toString() %></b></center> <%
		}
		
		%>
	<%}%>
<%}%>
<%@include file="/footer.jsp" %>