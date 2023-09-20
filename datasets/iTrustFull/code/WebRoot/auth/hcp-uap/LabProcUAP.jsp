<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcUAPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Laboratory Procedures";
%>

<%@include file="/header.jsp" %>

<%
LabProcUAPAction action = new LabProcUAPAction(prodDAO, loggedInMID.longValue());

/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/LabProcUAP.jsp");
   	return;
}
else {
	session.removeAttribute("pid");
}

/* If the patient id doesn't check out, then kick 'em out to the exception handler */
EditPatientAction epaction = new EditPatientAction(prodDAO,loggedInMID.longValue(),pidString);
long pid = epaction.getPid();
		
List<LabProcedureBean> proc = action.viewProcedures(pid);	
%>
<br />
<table class="fTable" align=center>
	<tr>
		<th colspan="11">Laboratory Procedures</th>
	</tr>

	<tr class="subHeader">

    		<th>Patient</th>
  			<th>Lab Code</th>
   			<th>Rights</th>
	 		<th>Status</th>
  			<th>Commentary</th>
   			<th>Results</th>
 			<th>OfficeVisitID</th>
   			<th>Updated Date</th>
   			<th>Action</th>

  	</tr>
		<%for(LabProcedureBean bean : proc){ 
		PatientBean patient = new PatientDAO(prodDAO).getPatient(bean.getPid());%>
			<tr>
				<td ><%=patient.getFullName()%></td>
				<td ><%=bean.getLoinc()%></td>
				<td ><%=bean.getRights()%></td>
				<td ><%=bean.getStatus()%></td>
				<td ><%=bean.getCommentary()%></td>
				<td ><%=bean.getResults()%></td>
				<td ><%=bean.getOvID()%></td>
				<td ><%=bean.getTimestamp()%></td>
				<td >
					<a href="updateLabProc.jsp?ID=<%=bean.getProcedureID()%>">Update</a>
				</td>
				
			</tr>
		<%} %>
</table>
<br /><br />

<%@include file="/footer.jsp" %>
