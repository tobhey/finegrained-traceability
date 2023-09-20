<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Messages";
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Appointments</h2>
<%
	ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	List<ApptBean> appts = action.getMyAppointments();
	session.setAttribute("appts", appts);
	if (appts.size() > 0) { %>	
	<table class="fancyTable">
		<tr>
			<th>Patient</th>
			<th>Appointment Type</th>
			<th>Appointment Date/Time</th>
			<th>Duration</th>
			<th></th>
		</tr>
<%		 
		boolean conflicts[] = new boolean[appts.size()];
		for(int i=0; i<appts.size(); i++) {
			ApptBean a = appts.get(i);
			long t = a.getDate().getTime();
			long m = types.getDurationByType(a.getApptType()) * 60 * 1000;
			Timestamp time = new Timestamp(t+m);
			for(int j=i+1; j<appts.size(); j++) {
				if(appts.get(j).getDate().before(time)) {
					conflicts[i] = true;
					conflicts[j] = true;
				}
			}
		}
%>
<%		int index = 0;
		for(ApptBean a : appts) { 
			String comment = "";
			if(a.getComment() == null)
				comment = "No Comment";
			else
				comment = "<a href='viewAppt.jsp?apt="+index+"'>Read Comment</a>";
				
			Date d = new Date(a.getDate().getTime());
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			
			String row = "";
			if(conflicts[index])
				row = "<tr style='font-weight: bold;'";
			else
				row = "<tr";
%>
			<%=row+" "+((index%2 == 1)?"class=\"alt\"":"")+">"%>
				<td><%= action.getName(a.getPatient()) %></td>
				<td><%= a.getApptType() %></td>
				<td><%= format.format(d) %></td>
				<td><%= types.getDurationByType(a.getApptType())+" minutes" %></td>
				<td>
					<%=comment %>
				</td>
			</tr>
	<%		index ++; %>
	<%	} %>
	</table>
<%	} else { %>
	<div>
		<i>You have no Appointments</i>
	</div>
<%	} %>	
	<br />
</div>

<%@include file="/footer.jsp" %>
