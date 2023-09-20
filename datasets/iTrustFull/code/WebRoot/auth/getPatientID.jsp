<%@include file="/global.jsp" %>
<%@page import="java.util.List" %>
<%@page import="edu.ncsu.csc.itrust.action.SearchUsersAction" %>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%
pageTitle = "iTrust - Please Select a Patient";
%>

<%@include file="/header.jsp" %>

<%
	String uid_pid = request.getParameter("UID_PATIENTID");
	session.setAttribute("pid", uid_pid);
	if (null != uid_pid && !"".equals(uid_pid)) {
		response.sendRedirect(request.getParameter("forward"));
	}
	
	String firstName = request.getParameter("FIRST_NAME");
	String lastName = request.getParameter("LAST_NAME");
	if(firstName == null)
		firstName = "";
	if(lastName == null)
		lastName = "";
	

%>

<%@include file="/util/getUserFrame.jsp"%>
				

<form id="mainForm" action="getPatientID.jsp" method="post">
	<table>
		<tr>
			<td><b>Patient:</b></td>
			<td style="width: 150px; border: 1px solid Gray;">
				<input name="forward" type="hidden" value="<%=request.getParameter("forward") %>" />
				<input name="UID_PATIENTID" type="hidden" value="" />
				<input name="NAME_PATIENTID" type="text" readonly value="Select a Patient">
				</td>
			<td>
				<input type="button" onclick="getUser('PATIENTID');" value="Find User" />
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<input type="submit" value="Select Patient" />
			</td>
		</tr>
	</table>
</form>
<table>
	<tr> </tr>
	<form id="userSearchForm" action="getPatientID.jsp?forward=<%= request.getParameter("forward") %>" method="post">
		<tr>
				<td><b>First Name:</b></td>
				<td style="width: 150px; border: 1px solid Gray;">
					<input name="FIRST_NAME" type="text" value="<%= firstName %>" />
				</td>
		</tr>
		<tr>
				<td><b>Last Name:</b></td>
				<td style="width: 150px; border: 1px solid Gray;">
					<input name="LAST_NAME" type="text" value="<%= lastName %>" />
				</td>
		</tr>
		<tr>
				<td align="center" colspan="2">
					<input type="submit" value="User Search" />
				</td>
		</tr>
	</form>
</table>


<%
	if( (!"".equals(firstName)) || (!"".equals(lastName))){
		SearchUsersAction searchAction = new SearchUsersAction(prodDAO,loggedInMID.longValue());
		out.println("Searching for users named " + firstName + " " + lastName + "<br />");
		List<PatientBean> patients = searchAction.searchForPatientsWithName(firstName,lastName);
		out.println("Found " + patients.size() + " Records <br />");
		out.println("<table border='1px'><tr><td width='175px'>MID</td><td width='250px'>First Name</td><td width='250px'>Last Name</td></tr>");
		for(PatientBean p : patients){
%>
<form id="selectPatient<%= String.valueOf(patients.size()) %>" action="getPatientID.jsp?forward=<%= request.getParameter("forward") %>" method="post">
<input type="hidden" name="UID_PATIENTID" value="<%= p.getMID() %>" />


<%
			out.println("<tr><td><input type='submit' width='100%' value='" + p.getMID() + "' /></form></td><td>" + p.getFirstName() + "</td><td>" + p.getLastName() + "</td></tr>");

		}
		out.println("</table>");
		
	}

%>

<%@include file="/footer.jsp" %>
