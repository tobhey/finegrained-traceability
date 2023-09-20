<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Monitored Patients";
%>

<%@include file="/header.jsp" %>

<%
ViewMyRemoteMonitoringListAction action = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> data = action.getPatientDataWithoutLogging();
%>
<br />

<form action="getPatientMonitorList.jsp" method="post" name="myform">
<table class="fTable" align="center">
	<tr>
		<th colspan="3">Patients</th>
	</tr>

	<tr class="subHeader">
		<th>Patient</th>

	</tr>
	<%
		List<Long> patients = new ArrayList<Long>();
		int index = 0;
		for (RemoteMonitoringDataBean bean : data) {
			if (!patients.contains(bean.getPatientMID())){
				patients.add(bean.getPatientMID());
			
	%>
	<tr>
		<td >
			<a href="addTelemedicineData.jsp?patient=<%=index%>">
		
		
			<%=action.getPatientName(bean.getPatientMID())%>	
		
		
			</a>
			</td>
	</tr>
	<%
			index ++;
			}
		}
		session.setAttribute("patients", patients);
	%>
</table>
</form>
<br />
<br />

<%@include file="/footer.jsp" %>
