<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>

<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - HCP Home";
%>

<%@include file="/header.jsp" %>

<%
ViewMyReportRequestsAction reportAction = new ViewMyReportRequestsAction(DAOFactory.getProductionInstance(), loggedInMID);
List<ReportRequestBean> reports = reportAction.getAllReportRequestsForRequester();
LabProcHCPAction lpAction = new LabProcHCPAction(DAOFactory.getProductionInstance(), loggedInMID);
List<LabProcedureBean> labProcedures = lpAction.getLabProcForNextMonth();

%>

<table cellpadding="1" cellspacing="1">
	<tr>
		<td>
		<h3>Announcements</h3>
		<i>New features in iTrust</i></td>
	</tr>
	<tr>
		<td>
		<ul>
			<li>No more typing in a date! We now have a calendar popup that
			makes entering dates incredibly easy!</li>
		</ul>
		</td>
	</tr>

	<tr>
		<td>
		<h3>Notifications</h3>
		</td>
	</tr>
	<tr>
		<td>
		<h4>Comprehensive Report History</h4>
		</td>
	</tr>

	<tr>
		<td>
		<ul>
<%
	if(reports.size() != 0) {
		for(ReportRequestBean report : reports) {
%>
			<li><%=reportAction.getLongStatus(report.getID())%></li>
<%
		} 
	} else {
%>
		<i>No Report Requests</i>
<%
	}
%>
		</ul>
		</td>
	</tr>

	<tr>
		<td>
		<h4>Lab Procedures Completed in the Last Month</h4>
		</td>
	</tr>
	<tr>
		<td><a href="LabProcHCP.jsp">View All Lab Procedures here</a><br />
		</td>
	</tr>
	<tr>
		<td>
		<ul>
			<%if(labProcedures.size() != 0) {
				for(LabProcedureBean bean : labProcedures) {
					PatientBean patient = new PatientDAO(DAOFactory.getProductionInstance()).getPatient(bean.getPid());%>
			<li>Lab Procedure <%=bean.getLoinc()%> for patient <%=patient.getFullName() %>
			<br />
			Results: 
<%
					if(bean.getResults().equals("")) {
%> 
						<i>none</i> 
<%
					} else {
%>
						<%=bean.getResults()%>
<%
					}
				}
			} else {
%>
				<i>No Recent Lab Procedures</i>
<%
			}
%>
			</li>
		</ul>
		</td>
	</tr>
</table>

<%@include file="/footer.jsp" %>
