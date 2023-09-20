<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Reminders Message Outbox";
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>Reminders Message Outbox</h2>
<%
	ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, 9000000009L);
	List<MessageBean> messages = null;
	if(request.getParameter("sortby") != null) {
		if(request.getParameter("sortby").equals("name")) {
			if(request.getParameter("sorthow").equals("asce")) {
				messages = action.getAllMyMessagesNameAscending();
			} else {
				messages = action.getAllMyMessagesNameDescending();
			}
		} else if(request.getParameter("sortby").equals("time")) {
			if(request.getParameter("sorthow").equals("asce")) {
				messages = action.getAllMyMessagesTimeAscending();
			} else {
				messages = action.getAllMyMessages();
			}
		}
	}
	else {
		messages = action.getAllMyMessages();
	}
	session.setAttribute("messages", messages);
	if (messages.size() > 0) { %>
	<form method="post" action="remindersMessageOutbox.jsp">	
	<table>
	<tr>
		<td>
			<select name="sortby">
					<option value="time">Sort</option>
					<option value="name">Name</option>
					<option value="time">Time</option>
			</select>
		</td>
		<td>
			<select name="sorthow">
					<option value="desc">Order</option>
					<option value="asce">Ascending</option>
					<option value="desc">Descending</option>
			</select>
		</td>
		<td>
			<input type="submit" value="Sort" />
		</td>
	</tr>
	</table>
	</form>
	<br />
	<table class="fancyTable">
		<tr>
			<th>To</th>
			<th>Subject</th>
			<th>Sent</th>
			<th></th>
		</tr>
<%		int index = 0; %>
<%		for(MessageBean message : messages) { %>
		<tr <%=(message.getRead() == 0)?" style=\"font-weight: bold;\"":"" %>  <%=(index%2 == 1)?"class=\"alt\"":"" %>>
			<td><%= action.getName(Long.parseLong(message.getSubject().substring(0, message.getSubject().indexOf("::")))) %></td>
			<td><%= message.getSubject().substring(message.getSubject().indexOf("::")+2) %></td>
			<td><%= message.getSentDate() %></td>
			<td><a href="viewReminderMessage.jsp?msg=<%= index %>">Read</a></td>
		</tr>
<%			index ++; %>
<%		} %>
	</table>
<%	} else { %>
	<div>
		<i>You have no messages</i>
	</div>
<%	} %>	
	<br />
</div>

<%@include file="/footer.jsp" %>
