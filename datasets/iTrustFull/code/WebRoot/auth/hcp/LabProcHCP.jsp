<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Laboratory Procedures";
%>

<%@include file="/header.jsp" %>

<%
LabProcHCPAction action2 = new LabProcHCPAction(prodDAO, loggedInMID.longValue());
if(request.getParameter("priv")!=null && request.getParameter("priv").equals("yes")){
	action2.changePrivacy(Long.parseLong(request.getParameter("ID")));
}

/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/LabProcHCP.jsp");
   	return;
}
else {
	session.removeAttribute("pid");
}

/* If the patient id doesn't check out, then kick 'em out to the exception handler */
EditPatientAction action = new EditPatientAction(prodDAO,loggedInMID.longValue(),pidString);
long pid = action.getPid();

List<LabProcedureBean> proc = action2.viewProcedures(pid);
%>

<br />
<table  class="fTable">
	<tr>
		<th colspan="11">Lab Procedures</th>
	</tr>

	<tr class="subHeader">
  		<td>Patient</td>
  		<td>Lab Code</td>
  		<td>Rights</td>
		<td>Status</td>
		<td>Commentary</td>
		<td>Results</td>
		<td>OfficeVisitID</td>
		<td>Updated Date</td>
		<td>Edit Office Visit</td>
		<td>Change Privacy</td>
		<td>Action</td>
  	</tr>
<%
	if(proc.size() > 0) {
		for(LabProcedureBean bean : proc){ 
			PatientBean patient = new PatientDAO(prodDAO).getPatient(bean.getPid());
%>
			<tr>
				<td ><%=patient.getFullName()%></td>
				<td ><%=bean.getLoinc()%></td>
				<td ><%=bean.getRights()%></td>
				<td ><%=bean.getStatus()%></td>
				<td ><%=bean.getCommentary()%></td>
				<td ><%=bean.getResults()%></td>
				<td ><%=bean.getOvID()%></td>
				<td ><%=bean.getTimestamp()%></td>
				<td >  <%if(action2.checkAccess(bean.getProcedureID())){%>
					<a href="/iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=<%=bean.getOvID()%>">Edit Office Visit</a><br />
				<%} %></td>
				<td >  <%if(action2.checkAccess(bean.getProcedureID())){%>
					<a href="/iTrust/auth/hcp/LabProcHCP.jsp?ID=<%=bean.getProcedureID()%>&priv=yes">Allow/Disallow Viewing</a><br />
				<%} %></td>
				<td > 
					<a href="/iTrust/auth/hcp/UpdateLabProc.jsp?ID=<%=bean.getProcedureID()%>">Update</a><br />
				</td>
				
			</tr>
<%
		}
	}
	else {
%>
	<tr colspan=10>
		<td align=center>
			No Data
		</td>
	</tr>
<%		
	}
%>
</table>
<br /><br />

<%@include file="/footer.jsp" %>
