<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.TransactionDAO"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Send Appointment Reminders";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "";
	int messagesSent = -1;
	boolean error = false;
	if(request.getParameter("submit") != null) {
		if(request.getParameter("threshold") != null) {
			int days = 0;
			try {
				days = Integer.parseInt(request.getParameter("threshold"));
			} catch(NumberFormatException e) {
				headerMessage = "Please enter a number into the threshold field.";
				error = true;
			}
			
			SendMessageAction action = new SendMessageAction(prodDAO, 9000000009L);
			MessageBean toSend = new MessageBean();
			DateFormat format = new SimpleDateFormat("hh:mm a, MM-dd-yyyy");
			
			Calendar current = Calendar.getInstance();
			
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_MONTH, days);
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);
			
			Calendar subject = Calendar.getInstance();
			
			ViewMyApptsAction apptAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
			
			List<ApptBean> apList = apptAction.getAllAppts();
			
			messagesSent = 0;
			
			for(ApptBean a : apList) {
				if(a.getDate().after(current.getTime()) && a.getDate().before(end.getTime())) {
					toSend.setFrom(9000000009L);
					subject.setTimeInMillis(a.getDate().getTime());
					int difference = subject.get(Calendar.DAY_OF_MONTH)-current.get(Calendar.DAY_OF_MONTH);
					if(subject.get(Calendar.MONTH)>current.get(Calendar.MONTH)) {
						difference = current.getActualMaximum(Calendar.DAY_OF_MONTH)-current.get(Calendar.DAY_OF_MONTH)+subject.get(Calendar.DAY_OF_MONTH);
					}
					toSend.setSubject("Reminder: upcoming appointment in " + difference + " day(s)");
					toSend.setSentDate(new Timestamp(current.getTimeInMillis()));
					toSend.setTo(a.getPatient());
					Date d = new Date(a.getDate().getTime());
					toSend.setBody("You have an appointment on "+ format.format(d) + " with Dr. " + action.getPersonnelName(a.getHcp()) + ".");
					action.sendMessage(toSend);
					toSend.setTo(9000000009L);
					toSend.setSubject(a.getPatient()+"::"+toSend.getSubject());
					action.sendMessage(toSend);
					messagesSent++;
				}
			}
			
			//log the messages
			TransactionDAO transDAO = prodDAO.getTransactionDAO();
			transDAO.logTransaction(TransactionType.SEND_REMINDERS, loggedInMID);
		}
	}
%>

<div align="center">
	<h2>Send Appointment Reminders</h2>
	<%=error?"<span class=\"iTrustMessage\">"+headerMessage+"</span><br /><br />":"" %>
	<%=messagesSent!=-1?"<span class=\"iTrustMessage\">"+messagesSent+" message(s) sent.</span><br /><br />":"" %>
	<form method="post" action="sendAppointmentReminders.jsp">
		<label for="threshold" >Enter reminder threshold:</label>
		<input type="text" name="threshold" id="threshold" /><span> days</span>
		<br />
		<br />
		<input type="submit" name="submit" value="Submit" />
	</form>
</div>

<%@include file="/footer.jsp" %>
