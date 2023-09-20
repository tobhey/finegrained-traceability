<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Message";
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
			response.sendRedirect("messageOutbox.jsp");
		}
		List<MessageBean> messages = null; 
		if (session.getAttribute("messages") != null) {
			messages = (List<MessageBean>) session.getAttribute("messages");
			if(msgIndex > messages.size() || msgIndex < 0) {
				msgIndex = 0;
				response.sendRedirect("oops.jsp");
			}
		} else {
			response.sendRedirect("messageOutbox.jsp");
		}
		original = (MessageBean)messages.get(msgIndex);
		session.setAttribute("message", original);
	}
	else {
		response.sendRedirect("messageOutbox.jsp");
	}
	
%>
	<div>
		<table width="100%" style="background-color: #DDDDDD;">
			<tr>
				<td><b>To:</b> <%= action.getName(original.getTo()) %></td>
			</tr>
			<tr>
				<td><b>Subject:</b> <%= original.getSubject() %></td>
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
		<tr>
			<td colspan="2"><a href="messageOutbox.jsp">Back</a></td>
		</tr>
	</table>


<%@include file="/footer.jsp" %>
