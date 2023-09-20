<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Send a Message";
%>

<%@include file="/header.jsp" %>

<%
	SendMessageAction action = new SendMessageAction(prodDAO, loggedInMID.longValue());
	PatientDAO patientDAO = prodDAO.getPatientDAO();
	
	long patientID = 0L;
	
	if (session.getAttribute("pid") != null) {
		String pidString = (String) session.getAttribute("pid");
		patientID = Long.parseLong(pidString);
		try {
			action.getPatientName(patientID);
		} catch (iTrustException ite) {
			patientID = 0L;
			session.removeAttribute("pid");
		}
	}
	else {
		session.removeAttribute("pid");
	}
	
	if (patientID == 0L) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/sendMessage.jsp");
	} else {	
		if (request.getParameter("messageBody") != null) {
			String body = request.getParameter("messageBody");
			MessageBean message = new MessageBean();
			message.setBody(request.getParameter("messageBody"));
			message.setSubject(request.getParameter("subject"));
			message.setRead(0);
			message.setFrom(loggedInMID);
			message.setTo(patientID);
			try {
			action.sendMessage(message);
			session.removeAttribute("pid");
			response.sendRedirect("messageOutbox.jsp");

			} catch (FormValidationException e){
			%>
			<div align=center><span class="iTrustError"><%=e.getMessage()%></span></div>
			<%	
			}
		}
%>

<div align="left">
	<h2>Send a Message</h2>
	<h4>to <%= action.getPatientName(patientID) %> (<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/sendMessage.jsp">someone else</a>):</h4>
	<form id="mainForm" method="post" action="sendMessage.jsp">
		<span>Subject: </span><input type="text" name="subject" size="50" /><br /><br />
		<span>Message: </span><br />
		<textarea name="messageBody" cols="100" rows="10"></textarea><br />
		<br />
		<input type="submit" value="Send" name="sendMessage"/>
	</form>
	<br />
	<br />
</div>
<%	} %>

<%@include file="/footer.jsp" %>
