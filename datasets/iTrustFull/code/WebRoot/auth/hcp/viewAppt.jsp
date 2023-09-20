<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<%
	ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	ApptBean original = null;

	if (request.getParameter("apt") != null) {
		String aptParameter = request.getParameter("apt");
		int aptIndex = 0;
		try {
			aptIndex = Integer.parseInt(aptParameter);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("viewMyAppts.jsp");
		}
		List<ApptBean> appts = null; 
		if (session.getAttribute("appts") != null) {
			appts = (List<ApptBean>) session.getAttribute("appts");
			if(aptIndex > appts.size() || aptIndex < 0) {
				aptIndex = 0;
				response.sendRedirect("oops.jsp");
			}
		} else {
			response.sendRedirect("viewMyAppts.jsp");
		}
		original = (ApptBean)appts.get(aptIndex);
	}
	else {
		response.sendRedirect("viewMyAppts.jsp");
	}
	
	Date d = new Date(original.getDate().getTime());
	DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	
%>
	<div>
		<table width="100%" style="background-color: #DDDDDD;">
			<tr>
				<th>Appointment Info</th>
			</tr>
			<tr>
				<td><b>Patient:</b> <%= action.getName(original.getPatient()) %></td>
			</tr>
			<tr>
				<td><b>Type:</b> <%= original.getApptType() %></td>
			</tr>
			<tr>
				<td><b>Date/Time:</b> <%= format.format(d) %></td>
			</tr>
			<tr>
				<td><b>Duration:</b> <%= types.getDurationByType(original.getApptType())+" minutes" %></td>
			</tr>
		</table>
	</div>
	
	<table>
		<tr>
			<td colspan="2"><b>Comments:</b></td>
		</tr>
		<tr>
			<td colspan="2"><%= (original.getComment()== null)?"No Comment":original.getComment() %></td>
		</tr>
	</table>


<%@include file="/footer.jsp" %>
