<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.List"%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Messages";
%>

<%@include file="/header.jsp"%>

<div align="center">
<h2>My Messages</h2>
<%
	ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
	List<MessageBean> messages = action.getAllMyMessages();
	session.setAttribute("messages", messages);
	if (messages.size() > 0) { %>
	
	<table class="fTable">
		<tr>
			<th>Sender</th>
			<th>Message</th>
			<th>Received</th>
			<th>&nbsp;</th>
		</tr>
<%		int index = 0; %>
<%		for(MessageBean message : messages) { %>
		<tr>
			<td><%= action.getPersonnelName(message.getFrom()) %></td>
			<td><%= message.getBody() %></td>
			<td><%= message.getSentDate() %></td>
			<td><a href="reply.jsp?msg=<%= index %>">Reply</a></td>
		</tr>
<%			index ++; %>
<%		} %>
	</table>
<%	} else { %>
	<div>
		<i>You have no messages</i>
	</div>
<%	} %>	
	<br /><br />
</div>

<%@include file="/footer.jsp"%>
