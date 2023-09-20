<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Reminder Message";
%>

<%@include file="/header.jsp" %>

<%
	ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
	MessageBean original = null;

	if (request.getParameter("msg") != null) {
		String msgParameter = request.getParameter("msg");
		int msgIndex = 0;
		try {
			msgIndex = Integer.parseInt(msgParameter);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("remindersMessageOutbox.jsp");
		}
		List<MessageBean> messages = null; 
		if (session.getAttribute("messages") != null) {
			messages = (List<MessageBean>) session.getAttribute("messages");
		} else {
			response.sendRedirect("remindersMessageOutbox.jsp");
		}
		original = (MessageBean)messages.get(msgIndex);
		action.setRead(original);
		session.setAttribute("message", original);
	}
	else {
		response.sendRedirect("messageInbox.jsp");
	}
	
%>
	<div>
		<table width="100%" style="background-color: #DDDDDD;">
			<tr>
				<td><b>To:</b> <%= action.getName(Long.parseLong(original.getSubject().substring(0, original.getSubject().indexOf("::")))) %></td>
			</tr>
			<tr>
				<td><b>Subject:</b> <%= original.getSubject().substring(original.getSubject().indexOf("::")+2) %></td>
			</tr>
			<tr>
				<td><b>Date &amp; Time:</b> <%= original.getSentDate() %></td>
			</tr>
		</table>
	</div>
	
	<table>
		<tr>
			<td colspan="2"><b>Message:</b></td>
		</tr>
		<tr>
			<td colspan="2"><%= original.getBody() %></td>
		</tr>
	</table>


<%@include file="/footer.jsp" %>
