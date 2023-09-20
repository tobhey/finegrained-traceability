<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Message ";
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Messages</h2>
	
<%
	ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
	EditPatientAction f_action = new EditPatientAction(prodDAO, loggedInMID.longValue(), loggedInMID.toString());
	PatientDAO dao = new PatientDAO(prodDAO);
	List<MessageBean> messages = null;
	
	//Edit Filter backend
	boolean editing = false;
	String headerMessage = "";
	String[] fields = new String[6];
	if(request.getParameter("edit") != null && request.getParameter("edit").equals("true")) {
		editing = true;
		
		int i;
		for(i=0; i<6; i++) {
			fields[i] = "";
		}
		
		if(request.getParameter("cancel") != null) 
			response.sendRedirect("messageInbox.jsp"); 
		else if(request.getParameter("test") != null || request.getParameter("save") != null) {
			boolean error = false;
			String nf = "";
			nf += request.getParameter("sender").replace(",","")+",";
			nf += request.getParameter("subject").replace(",","")+",";
			nf += request.getParameter("hasWords").replace(",","")+",";
			nf += request.getParameter("notWords").replace(",","")+",";
			nf += request.getParameter("startDate").replace(",","")+",";
			nf += request.getParameter("endDate");
			
			//Validate Filter
			nf = action.validateAndCreateFilter(nf);
			if(nf.startsWith("Error")) {
				error = true;
				headerMessage = nf;
			}
			
			if(!error) {
				if(request.getParameter("test") != null) {
					response.sendRedirect("messageInbox.jsp?edit=true&testFilter="+nf);
				} else if(request.getParameter("save") != null) {
					f_action.editMessageFilter(nf);
					response.sendRedirect("messageInbox.jsp?filter=true"); 
				}
			}
		}
		
		if(request.getParameter("testFilter") != null) {
			String filter = request.getParameter("testFilter");
			String[] f = filter.split(",", -1);
			for(i=0; i<6; i++) {
				try {
					fields[i] = f[i];
				} catch(ArrayIndexOutOfBoundsException e) {
					//do nothing
				}
			}
		} else {
			String filter = dao.getPatient(loggedInMID.longValue()).getMessageFilter();
			if(!filter.equals("")) {
				String[] f = filter.split(",", -1);
				for(i=0; i<6; i++) {
					try {
						fields[i] = f[i];
					} catch(ArrayIndexOutOfBoundsException e) {
						//do nothing
					}
				}
			}
		}
	}
	
	//Sorts messages
	if(request.getParameter("sort") != null) {
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
	
	//Filters Messages
	boolean is_filtered = false;
	if((request.getParameter("filter") != null && request.getParameter("filter").equals("true")) || request.getParameter("testFilter") != null) {
		String filter = "";
		if(request.getParameter("testFilter") != null) {
			filter = request.getParameter("testFilter");
		} else {
			filter = dao.getPatient(loggedInMID.longValue()).getMessageFilter();
		}
		if(!filter.equals("") && !filter.equals(",,,,,")) {
			List<MessageBean> filtered = action.filterMessages(messages, filter);
			messages = filtered;
			is_filtered = true;
		}
	}
	
	session.setAttribute("messages", messages);
	%>
	
	<%
	
	if(editing) {
		%>
		<div class="filterEdit">
			<div align="center">
				<span style="font-size: 13pt; font-weight: bold;">Edit Message Filter</span>
				<%= headerMessage.equals("") ? "" : "<br /><span class=\"iTrustMessage\">"+headerMessage+"</span><br /><br />" %>
				<form method="post" action="messageInbox.jsp?edit=true">
					<table>
						<tr style="text-align: right;">
							<td>
								<label for="sender">Sender: </label>
								<input type="text" name="sender" id="sender" value="<%=fields[0] %>" />
							</td>
							<td style="padding-left: 10px; padding-right: 10px;">
								<label for="hasWords">Has the words: </label>
								<input type="text" name="hasWords" id="hasWords" value="<%=fields[2] %>" />
							</td>
							<td>
								<label for="startDate">Start Date: </label>
								<input type="text" name="startDate" id="startDate" value="<%=fields[4] %>" />
								<input type="button" value="Select Date" onclick="displayDatePicker('startDate');" />
							</td>
						</tr>
						<tr style="text-align: right;">
							<td>
								<label for="subject">Subject: </label>
								<input type="text" name="subject" id="subject" value="<%=fields[1] %>" />
							</td>
							<td style="padding-left: 10px; padding-right: 10px;">
								<label for="notWords">Does not have the words: </label>
								<input type="text" name="notWords" id="notWords" value="<%=fields[3] %>" />
							</td>
							<td>
								<label for="endDate">End Date: </label>
								<input type="text" name="endDate" id="endDate" value="<%=fields[5] %>" />
								<input type="button" value="Select Date" onclick="displayDatePicker('endDate');" />
							</td>
						</tr>
						<tr style="text-align: center;">
							<td colspan="3">
								<input type="submit" name="test" value="Test Filter" />
								<input type="submit" name="save" value="Save" />
								<input type="submit" name="cancel" value="Cancel" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<br />
		<%
	}
	
	%>
	
	<form method="post" action="messageInbox.jsp<%=is_filtered?"?filter=true":"" %>">	
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
			<input type="submit" name="sort" value="Sort" />
		</td>
	</tr>
	<tr>
		<td><a href="messageInbox.jsp?edit=true" >Edit Filter</a></td>
		<td><a href="messageInbox.jsp?filter=true" >Apply Filter</a></td>
	</tr>
	</table>
	</form>
	<br />
	<%if(messages.size() > 0) { %>
	<table class="fancyTable">
		<tr>
			<th>Sender</th>
			<th>Subject</th>
			<th>Received</th>
			<th></th>
		</tr>
<%		int index = 0; 
		for(MessageBean message : messages) {
		if(message.getRead() == 0) {%>
		<tr style="font-weight: bold;" <%=(index%2 == 1)?"class=\"alt\"":"" %>>
			<td><%= action.getName(message.getFrom()) %></td>
			<td><%= message.getSubject() %></td>
			<td><%= message.getSentDate() %></td>
			<td><a href="viewMessageInbox.jsp?msg=<%= index %>">Read</a></td>
		</tr>
<% 			   } else { %>
		<tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
			<td><%= action.getName(message.getFrom()) %></td>
			<td><%= message.getSubject() %></td>
			<td><%= message.getSentDate() %></td>
			<td><a href="viewMessageInbox.jsp?msg=<%= index %>">Read</a></td>
		</tr>
<% 			  } %>
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
