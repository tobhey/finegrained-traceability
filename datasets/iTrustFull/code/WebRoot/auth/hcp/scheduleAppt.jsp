<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.action.AddApptAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Schedule an Appointment";

String headerMessage = "Please fill out the form properly - comments are optional.";
%>

<%@include file="/header.jsp" %>
<%
	AddApptAction action = new AddApptAction(prodDAO, loggedInMID.longValue());
	EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	PatientDAO patientDAO = prodDAO.getPatientDAO();
	
	long patientID = 0L;
	
	if (session.getAttribute("pid") != null) {
		String pidString = (String) session.getAttribute("pid");
		patientID = Long.parseLong(pidString);
		try {
			action.getName(patientID);
		} catch (iTrustException ite) {
			patientID = 0L;
			session.removeAttribute("pid");
		}
	}
	else {
		session.removeAttribute("pid");
	}
	
	if (patientID == 0L) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp");
	} else {	
		if (request.getParameter("schedule") != null) {
			if(!request.getParameter("schedDate").equals("")) {	
				ApptBean appt = new ApptBean();
				DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				Date date = format.parse(request.getParameter("schedDate")+" "+request.getParameter("time1")+":"+request.getParameter("time2")+" "+request.getParameter("time3"));
				appt.setHcp(loggedInMID);
				appt.setPatient(patientID);
				appt.setApptType(request.getParameter("apptType"));
				appt.setDate(new Timestamp(date.getTime()));
				String comment = "";
				if(request.getParameter("comment").equals(""))
					comment = null;
				else 
					comment = request.getParameter("comment");
				appt.setComment(comment);
				try {
				headerMessage = action.addAppt(appt);
				if(headerMessage.startsWith("Success")) {
					session.removeAttribute("pid");
					response.sendRedirect("home.jsp");
				}
		
				} catch (FormValidationException e){
				%>
				<div align=center><span class="iTrustError"><%=e.getMessage()%></span></div>
				<%	
				}
			}
			else 
				headerMessage = "Please input a date for the appointment.";
		}
		
		List<ApptTypeBean> apptTypes = types.getApptTypes();
%>

<div align="left">
	<h2>Schedule an Appointment</h2>
	<h4>with <%= action.getName(patientID) %> (<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp">someone else</a>):</h4>
	<span class="iTrustMessage"><%=headerMessage %></span><br /><br />
	<form id="mainForm" method="post" action="scheduleAppt.jsp">
		<span>Appointment Type: </span>
		<select name="apptType">
			<%
				for(ApptTypeBean b : apptTypes) {
					%>
					<option value="<%= b.getName() %>"><%= b.getName() %> - <%= b.getDuration() %> minutes</option>
					<%
				}
			%>
		</select>
		<br /><br />
		<span>Schedule Date: </span><input readonly type="text" name="schedDate" value="" /><input type="button" value="Select Date" onclick="displayDatePicker('schedDate');" /><br /><br />
		<span>Schedule Time: </span>
		<select name="time1">
			<%
				String hour = "";
				for(int i = 1; i <= 12; i++) {
					if(i < 10) hour = "0"+i;
					else hour = i+"";
					%>
						<option value="<%=hour%>"><%=hour%></option>
					<%
				}
			%>
		</select>:<select name="time2">
			<%
				String min = "";
				for(int i = 0; i < 60; i+=5) {
					if(i < 10) min = "0"+i;
					else min = i+"";
					%>
						<option value="<%=min%>"><%=min%></option>
					<%
				}
			%>
		</select>
		<select name="time3"><option value="AM">AM</option><option value="PM">PM</option></select><br /><br />
		<span>Comment: </span><br />
		<textarea name="comment" cols="100" rows="10"></textarea><br />
		<br />
		<input type="submit" value="Schedule" name="schedule"/>
	</form>
	<br />
	<br />
</div>
<%	} %>

<%@include file="/footer.jsp" %>