
<%@include file="/global.jsp" %>
<%@page import="java.util.List" %>
<%@page import="edu.ncsu.csc.itrust.action.SearchUsersAction" %>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%
pageTitle = "iTrust - Please Select a Personnel";
%>

<%@include file="/header.jsp" %>

<h1>Please Select a Personnel</h1>

<%	
String mid = request.getParameter("UID_PERSONNELID");
session.setAttribute("mid", mid);
if (null != mid && !"".equals(mid)) {
	response.sendRedirect(request.getParameter("forward"));
}

String firstName = request.getParameter("FIRST_NAME");
String lastName = request.getParameter("LAST_NAME");
if(firstName == null)
	firstName = "";
if(lastName == null)
	lastName = "";
%>
<table>
<form method="post">
<input type="hidden" name="forward" value="<%=request.getParameter("forward") %>">
	<tr>
		<td><b>Personnel:</b></td>
		<td style="width: 150px; border: 1px solid Gray;">
			<input name="UID_PERSONNELID" value="" type="hidden">
			<input id="NAME_PERSONNELID" name="NAME_PERSONNELID" type="text" value="Select a User" readonly/>
		</td>
		<td>
			<%@include file="/util/getUserFrame.jsp" %>
			<input type="button" onclick="getUser('PERSONNELID');" value="Find User" >
		</td>
	</tr>
	<tr>
		<td align=center colspan=2><input type="submit" value="Select Personnel"></td>
	</tr>
</form>

	<tr> </tr>
	<form id="userSearchForm" action="getPersonnelID.jsp?forward=<%= request.getParameter("forward") %>" method="post">
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
		List<PersonnelBean> personnel = searchAction.searchForPersonnelWithName(firstName,lastName);
		out.println("Found " + personnel.size() + " Records <br />");
		out.println("<table border='1px'><tr><td width='175px'>MID</td><td width='250px'>First Name</td><td width='250px'>Last Name</td></tr>");
		for(PersonnelBean p : personnel){
%>
<form action="getPersonnelID.jsp?forward=<%= request.getParameter("forward") %>" method="post">
<input type="hidden" name="UID_PERSONNELID" value="<%= p.getMID() %>" />


<%
			out.println("<tr><td><input type='submit' width='100%' value='" + p.getMID() + "' /></form></td><td>" + p.getFirstName() + "</td><td>" + p.getLastName() + "</td></tr>");

		}
		out.println("</table>");
		
	}

%>

<%@include file="/footer.jsp" %>

