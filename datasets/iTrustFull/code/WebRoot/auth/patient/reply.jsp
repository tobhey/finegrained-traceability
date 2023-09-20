<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Reply";
%>

<%@include file="/header.jsp" %>

<%

	SendMessageAction action = new SendMessageAction(prodDAO, loggedInMID);
	MessageBean original = null;
	
	if (session.getAttribute("message") != null) {
		original = (MessageBean)session.getAttribute("message");
		session.setAttribute("original", original);
		session.removeAttribute("message");
	} else if (request.getParameter("messageBody") != null) {
		if (session.getAttribute("original") != null) {
			original = (MessageBean)session.getAttribute("original");
			MessageBean messageNew = new MessageBean();
			messageNew.setBody(request.getParameter("messageBody"));
			messageNew.setFrom(loggedInMID);
			messageNew.setTo(original.getFrom());
			messageNew.setSubject(request.getParameter("subject"));
			messageNew.setRead(0);
			messageNew.setParentMessageId(original.getMessageId());
			action.sendMessage(messageNew);
			response.sendRedirect("messageInbox.jsp");
		} 
	} else {
		response.sendRedirect("messageInbox.jsp");
	}
%>

	<h2>Reply</h2>
	<h4>to a message from <%= action.getPersonnelName(original.getFrom()) %>:</h4>
	<form id="mainForm" method="post" action="reply.jsp">
		<span>Subject: </span><input type="text" name="subject" size="50" value="RE: <%= original.getSubject() %>" /><br /><br />
		<span>Message: </span><br />
		<textarea name="messageBody" cols="100" rows="10"></textarea><br />
		<br />
		<input type="submit" value="Send" name="sendMessage"/>
	</form>
	<br />
	<br />


<%@include file="/footer.jsp" %>
