<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.LabProcUAPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

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

	
	LabProcUAPAction action2 = new LabProcUAPAction(prodDAO, loggedInMID.longValue());
	boolean formIsFilled = request.getParameter("formIsFilled") != null
	&& request.getParameter("formIsFilled").equals("true");

	
	if (formIsFilled) {
		//This page is not actually a "page", it just adds a user and forwards.
		lbean.setStatus(request.getParameter("Status"));
		lbean.setResults(request.getParameter("Results"));
		lbean.setCommentary(request.getParameter("Commentary"));

		try{
		action2.updateProcedure(lbean);
%>
	<br />
	<div align=center>
		<span class="iTrustMessage">Information Updated Successfully</span>
	</div>
	<br />
<%
	} catch(FormValidationException e){
%>
	<br />
	<div align=center>
		<span class="iTrustError"><%=e.getMessage() %></span>
	</div>
	<br />
<%
		}
	}
	
%>

<%
if (request.getParameter("message") != null) {
	%><span class="iTrustMessage" style="font-size: 16px;"><%=request.getParameter("message") %></span><%
}
%>
<br />
<div align=center>
<table class="fTable">
	<tr>
		<th colspan="11">Laboratory Procedures</th>
	</tr>

	<tr class="subHeader">

    		<th>PatientMID</th>
  			<th>Lab Code</th>
   			<th>Rights</th>
	 		<th>Status</th>
  			<th>Commentary</th>
   			<th>Results</th>
 			<th>OfficeVisitID</th>
   			<th>Updated Date</th>

  	</tr>
		<%LabProcedureBean bean = prodDAO.getLabProcedureDAO().getLabProcedure(requestID);%>
			<tr>
				<td ><%=bean.getPid()%></td>
				<td ><%=bean.getLoinc()%></td>
				<td ><%=bean.getRights()%></td>
				<td ><%=bean.getStatus()%></td>
				<td ><%=bean.getCommentary()%></td>
				<td ><%=bean.getResults()%></td>
				<td ><%=bean.getOvID()%></td>
				<td ><%=bean.getTimestamp()%></td>
				
			</tr>
</table>


<form action="updateLabProc.jsp?ID=<%=lpid%>"&message="Updated Laboratory Procedure" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<br />
	<%=headerMessage %>
<br />
<table class="fTable">
	<tr>
		<th colspan=2>Update Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">Status:</td>
		<td>
		<select name="Status">
		<option value="NOT YET RECEIVED"><%=lbean.Not_Received %></option>
		<option value="PENDING"><%=lbean.Pending %></option>
		<option value="COMPLETED"><%=lbean.Completed %></option>
		</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Commentary:</td>
		<td><textarea name="Commentary"></textarea>
	</tr>
	<tr>
		<td class="subHeaderVertical">Results:</td>
		<td><textarea name="Results"></textarea></td>
	</tr>
</table>
<br />
<input type="submit" style="font-size: 14pt; font-weight: bold;" value="Update">
</div>
</form>
<br />

<%@include file="/footer.jsp" %>
