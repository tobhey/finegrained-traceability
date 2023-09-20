<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Update Lab Procedure";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "";


LabProcedureBean lbean = null;
long requestID = 0L;
String lpid = request.getParameter("ID");

if (lpid != null && !lpid.equals("")) {
	try {
		
		requestID = Long.parseLong(lpid);
		lbean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	
	LabProcHCPAction action2 = new LabProcHCPAction(prodDAO, loggedInMID.longValue());
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");


	if (formIsFilled) {
		//This page is not actually a "page", it just adds a user and forwards.
		lbean.setStatus(request.getParameter("Status"));
		lbean.setResults(request.getParameter("Results"));
		lbean.setCommentary(request.getParameter("Commentary"));

		try{
		action2.updateProcedure(lbean);%>
		<span>Information Updated Successfully</span>
		<% } catch(FormValidationException e){
			e.printHTML(out);
		}
	}
	
%>


<table  class="fTable">
	<tr>
		<th colspan="11">Lab Procedures</th>
	</tr>

	<tr>

    		<th>Patient</th>
  			<th>Lab Code</th>
   			<th>Rights</th>
	 		<th>Status</th>
  			<th>Commentary</th>
   			<th>Results</th>
 			<th>OfficeVisitID</th>
   			<th>Updated Date</th>

  	</tr>
		<%LabProcedureBean bean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);
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
				
			</tr>
</table>


<form action="UpdateLabProc.jsp?ID=<%=lpid%>"&message="Updated Laboratory Procedure" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<br />
<table align="center">
	<tr><td><%=headerMessage %></td></tr>
</table>
<br />
<table>

	<tr>
		<td>Status:</td>
		<td>
		<select name="Status">
		<option value="NOT YET RECEIVED"><%=lbean.Not_Received %></option>
		<option value="PENDING"><%=lbean.Pending %></option>
		<option value="COMPLETED"><%=lbean.Completed %></option>
		</select>
		</td>
	</tr>
	<tr>
		<td>Commentary:</td>
		<td><textarea name="Commentary"></textarea>
	</tr>
	<tr>
		<td>Results:</td>
		<td><textarea name="Results"></textarea></td>
	</tr>
	<tr>
		<td colspan=2 align=center><input type="submit"
			style="font-size: 16pt; font-weight: bold;" value="Update"></td>
	</tr>
</table>
</form>
<br />

<a href="../hcp/LabProcHCP.jsp">Go to View Laboratory Procedures</a>

<%@include file="/footer.jsp" %>
