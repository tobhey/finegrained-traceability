<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.TransactionDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedList"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Patient Home";
%>

<%@include file="/header.jsp" %>

<%
session.removeAttribute("personnelList");
ViewMyRecordsAction surveyAction = new ViewMyRecordsAction(prodDAO,loggedInMID.longValue());
List <OfficeVisitBean> surList = prodDAO.getOfficeVisitDAO().getOfficeVisitsWithNoSurvey(loggedInMID.longValue());
PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());
List<ReportRequestBean> reports = surveyAction.getReportRequests();
ViewMyReportRequestsAction reportAction = new ViewMyReportRequestsAction(prodDAO, loggedInMID.longValue());
LabProcedureDAO lpDAO = new LabProcedureDAO(prodDAO);
List<LabProcedureBean> labProcedures = lpDAO.getLabProceduresForPatientForNextMonth(loggedInMID.longValue());
List<PatientBean> represented = new PatientDAO(prodDAO).getRepresented(loggedInMID.longValue());
TransactionDAO transDAO = new TransactionDAO(prodDAO);
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
// Create an ArrayList and index to hide MIDs from user
List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
int personnel_counter = 0;
%>

<%

if(request.getParameter("rep") != null && request.getParameter("rep").equals("1")){
%>
<div align=center>
				<span class="iTrustMessage"><%="Adverse Event Successfully Reported"%></span>
</div>
<%} %>


<div id="Header">
<h1>Welcome <%=prodDAO.getAuthDAO().getUserName(Long.valueOf(request.getUserPrincipal().getName()))%>!</h1>
</div>

<div id="Content" align="left">



<h3>Announcements</h3>
<i>New features in iTrust</i>

<ul>
<li>Very happy or upset about your last office visit? Fill out a survey and let us know!</li>
<li>No more typing in a date! We now have a calendar pop-up that makes setting dates incredibly easy!.</li>
</ul>

<h3>Notifications</h3>


<h4>Surveys that still need to be completed</h4>

<%
if (0 != surList.size()) {
%>
<ul>
<%
	for (OfficeVisitBean ov : surList) {
		if (!surveyAction.isSurveyCompleted(ov.getID())){
%>
			<li><a href="survey.jsp?ovID=<%=ov.getVisitID()%>&ovDate=<%=ov.getVisitDateStr()%>">Complete survey</a> for your office visit on <%=ov.getVisitDateStr()%></li>
<%
		}
	}
%>
</ul>
<%
}
else {
%>
	<i>No Unfinished Surveys</i>
<%
}
%>

<h4>Comprehensive Report History</h4>
<%
if (reports.size() != 0) {
%>
<ul>
<%
	for (ReportRequestBean report : reports) {
%>
		<li><%=reportAction.getLongStatus(report.getID())%></li>
<%
	}
%>
</ul>
<%
} 
else {
%>
	<i>No Report Requests</i>
<%
}
%>

<h4>Lab Procedures Completed in the Last Month</h4>
<span>Lab Procedures for you</span><br />

<%
if (0 != labProcedures.size()) {
%>
	<ul>
<%
	for (LabProcedureBean bean : labProcedures) {
%>
		<li>Lab Procedure <%=bean.getLoinc()%> from Office Visit <a href="viewOfficeVisit.jsp?ovID=<%=bean.getOvID()%>"><%=bean.getOvID()%></a><br />
		Results:
<%
		if (bean.getResults().equals("")) {
%> 
			<i>none</i>
<%
		}
		else {
%>
			<%=bean.getResults()%><%
		}
%>
		</li>
<%
	}
%>
	</ul>
<%
}
else {
%>
	<i>No Recent Lab Procedures</i><br />
<%
}
%>

<span>Lab Procedures for Represented Patients</span><br />
<%
if (0 != represented.size()) {
%>
	<ul>
<%
	for (PatientBean patientBean : represented) {
%>
		<b>Patient <%=patientBean.getFullName()%></b>
<%
		List<LabProcedureBean> labBeans = lpDAO.getLabProceduresForPatientForNextMonth(patientBean.getMID());
		if (0 != labBeans.size()) {
%>
			<ul>
<%
			for (LabProcedureBean bean : labBeans) {
%>
				<li>Lab Procedure <%=bean.getLoinc()%><br /> Results:
<%
				if (bean.getResults().equals("")) {
%> 
					<i>none</i> 
<%
				}
				else {
					out.print(bean.getResults());
				}
%>
				</li>
<%
			}
%>
			</ul>
<%
		}
		else {
%>
			<i>No Recent Lab Procedures</i><br />
<%
		}
	}
%>
	</ul>
<%
}
else {
%>
			<i>No Recent Lab Procedures</i><br />
<%
}
%>


<h4>Access by Undesignated Personnel</h4>
<%


Date today = new Date();
Date earlier = new Date();
earlier.setDate(today.getDate() - 90);

List<TransactionBean> transactions = transDAO.getRecordAccesses(patient.getMID(), earlier, today, false);

if (transactions.size() > 0) {
%>
	<ul>
<%
	Hashtable<Long, LinkedList<String>> ht = new Hashtable<Long, LinkedList<String>>();
	for (TransactionBean transaction : transactions) {
		Timestamp accessTime = transaction.getTimeLogged();
		
		// Begin - remove duplicate entries from the display
		Long mid = new Long(transaction.getLoggedInMID());
		String date = new String(accessTime.getMonth() + "/" + accessTime.getDate());
		
		boolean skip = false;
		if(!ht.containsKey(transaction.getLoggedInMID())) {
			ht.put(mid, new LinkedList<String>());
		}
		
		LinkedList<String> ll = (LinkedList<String>) ht.get(mid);
		if(ll.contains(date)) {
			skip = true;
		}
		else {
			ll.add(date);
		}
		// End - remove duplicate entries from the display
		
		if(!skip) {
%>
		<li><%=authDAO.getUserName(mid.longValue())%> accessed your records on <%=date %></li>
<%
		}
	}
%>
	</ul>

<%
}
else {
%>
	<i>None reported</i>
<%
}
%>
</div>

<%@include file="/footer.jsp" %>
