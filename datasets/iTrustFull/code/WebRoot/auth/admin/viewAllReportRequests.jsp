<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View All Report Requests";
%>

<%@include file="/header.jsp" %>

<%
PatientDAO patientDAO = prodDAO.getPatientDAO();
PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
PersonnelBean personnelb = personnelDAO.getPersonnel(loggedInMID.longValue());

ViewMyReportRequestsAction action = new ViewMyReportRequestsAction(prodDAO, loggedInMID.longValue());
String actionString = request.getParameter("Action");
String idString = request.getParameter("ID");
if (actionString != null && !actionString.equals("")) {
	if (idString != null && !idString.equals("")  && actionString.equals("Approve")) {
		try {
			long ID = Long.parseLong(idString);
			action.approveReportRequest(ID);
%>
	<div align=center>
		<span class="iTrustMessage">Report Request Approved</span>
	</div>
<%
		} catch (Exception e) {
%>
	<div align=center>
		<span class="iTrustError"><%=e.getMessage() %></span>
	</div>
<%
		}
	}
	if (idString != null && !idString.equals("")  && actionString.equals("Confirm")) {
		try {
			long ID = Long.parseLong(idString);
			String comment = request.getParameter("Comment");
			//need validation here - students did it wrong last year and we never fixed it
			action.rejectReportRequest(ID, comment);
%>
	<div align=center>
		<span class="iTrustError">Report Request Rejected</span>
	</div>
<%
		} catch (Exception e) {
%>
	<div align=center>
		<span class="iTrustError"><%=e.getMessage() %></span>
	</div>
<%
		}
	}
}

List<ReportRequestBean> list = action.getAllReportRequests();
%>

<br />

<table align="center" class="fTable">
	<tr>
		<th colspan="10">Report Requests</th>
	</tr>
	<tr class="subHeader">
    		<th>ID</th>
   			<th>Patient</th>
  			<th>Approver</th>
  			<th>Requested Date</th>
  			<th>Approved Date</th>
  			<th>Viewed Date</th>
  			<th>Status</th>
  			<th>Comment</th>
  			<th>Action</th>
  	</tr>
  	<%for (ReportRequestBean bean:list) { 
  		PatientBean patient = patientDAO.getPatient(bean.getPatientMID());
  		PersonnelBean approver = (bean.getApproverMID() == 0L) ? null : personnelDAO.getPersonnel(bean.getApproverMID());
  		%>
			<tr>
				<td ><%=bean.getID()%></td>
				<td ><%=patient.getFullName()%></td>
				<td ><%=(approver == null) ? "" : approver.getFullName()%><!--  --></td>
				<td ><%=bean.getRequestedDateString()%></td>
				<td ><%=bean.getApprovedDateString()%></td>
				<td ><%=bean.getViewedDateString()%></td>
				<td ><%=bean.getStatus()%></td>
				<td ><%=bean.getComment() == null ? "" : bean.getComment()%></td>
				<td ><%if(bean.getStatus().equals(ReportRequestBean.Requested)){ %>
					<a href="viewAllReportRequests.jsp?Action=Approve&ID=<%=bean.getID()%>">Approve</a>
					<a href="viewAllReportRequests.jsp?Action=Reject&ID=<%=bean.getID()%>">Reject</a>
				<%}else{%>&nbsp;<%} %></td>
			</tr>
			<%if (actionString != null && actionString.equals("Reject")) {
				long ID = Long.parseLong(idString);
				if (ID == bean.getID()) {
					%><tr><td colspan=5>Please enter a reason for the rejection and confirm</td>
					<td colspan=4>
					<form action="viewAllReportRequests.jsp" method="post" name="mainForm">
					<input type="hidden" name="Action" value="Confirm"> 
					<input type="hidden" name="ID" value="<%=bean.getID()%>"> 
					<input type="text" name="Comment" size=40>
					<input type="submit" value="Confirm">
					</form>
					</td></tr>
				<%}
			}
		} 
		%>
</table>
<br />


<%@include file="/footer.jsp" %>
