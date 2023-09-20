<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.util.List"%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Send a Message";
%>

<%@include file="/header.jsp"%>

<%
	SendMessageAction action = new SendMessageAction(prodDAO, loggedInMID.longValue());
	int index;
	int dlhcpIndex = -1;
	int representeeIndex = -1;
	int repDLHCPIndex = -1;
	if (request.getParameter("sendMessage") != null && request.getParameter("sendMessage").equals("Send")) {
		try {
		MessageBean message = new MessageBean();
		message.setFrom(loggedInMID.longValue());
		message.setTo(((PersonnelBean)session.getAttribute("dlhcp")).getMID());
		message.setBody(request.getParameter("messageBody"));
		message.setSubject(request.getParameter("subject"));
		message.setRead(0);
		action.sendMessage(message);
		session.removeAttribute("dlhcp");
		response.sendRedirect("messageOutbox.jsp");
		} catch (FormValidationException e){
			%>
			<div align=center><span class="iTrustError"><%=e.getMessage()%></span></div>
			<%
		}
	}
	
	if (request.getParameter("selectDLHCP") != null && request.getParameter("selectDLHCP").equals("Select")) {
		if (request.getParameter("dlhcp") != null && !request.getParameter("dlhcp").equals("-1")) dlhcpIndex = Integer.parseInt(request.getParameter("dlhcp"));
	} else if (request.getParameter("selectRepDLHCP") != null && request.getParameter("selectRepDLHCP").equals("Select")) {
		if (request.getParameter("representee") != null && !request.getParameter("representee").equals("-1")) representeeIndex = Integer.parseInt(request.getParameter("representee"));
		if (request.getParameter("repDLHCP") != null && !request.getParameter("repDLHCP").equals("-1")) repDLHCPIndex = Integer.parseInt(request.getParameter("repDLHCP"));
	} else if (request.getParameter("selectRep") != null && request.getParameter("selectRep").equals("Select")) {
		if (request.getParameter("representee") != null && !request.getParameter("representee").equals("-1")) representeeIndex = Integer.parseInt(request.getParameter("representee"));
	}
%>
<div align="left">
<form id="mainForm" method="get" action="sendMessage.jsp">
	<h2>Send a Message</h2>
<% if (dlhcpIndex == -1 && representeeIndex == -1) { %>
<%
		List<PersonnelBean> dlhcps = action.getMyDLHCPs();
		List<PatientBean> representees = action.getMyRepresentees();
		session.setAttribute("dlhcps", dlhcps);
		session.setAttribute("representees", representees);
%>
	<h4>To One of My DLHCPs</h4>
<%		if (dlhcps.size() > 0) { %>
	<select name="dlhcp">
		<option value="-1"></option>
<%			index = 0; %>
<%			for(PersonnelBean dlhcp : dlhcps) { %>
		<option value="<%= index %>"><%= dlhcp.getFullName() %></option>
<%				index ++; %>
<%			} %>
	</select>
	<input type="submit" value="Select" name="selectDLHCP"/>
<%		} else { %>
	<i>You haven't declared any HCPs.</i>
<%		} %>
	
	<h4>On Behalf of One of My Representees</h4>
<%		if (representees.size() > 0) { %>
	<select name="representee">
		<option value="-1"></option>
<%			index = 0; %>
<%			for(PatientBean representee : representees) { %>
		<option value="<%= index %>"><%= representee.getFullName() %></option>
<%				index ++; %>
<%			} %>
	</select>
	<input type="submit" value="Select" name="selectRep"/>
<%		} else { %>
	<i>No other patients have declared you as a representative.</i>
<%		} %>
<%	} else if (dlhcpIndex >= 0) { %>
<%
		List<PersonnelBean> dlhcps = (List<PersonnelBean>) session.getAttribute("dlhcps");
		PersonnelBean dlhcp = dlhcps.get(dlhcpIndex);
		session.setAttribute("dlhcp", dlhcp);
%>
	<h4>To <%= dlhcp.getFullName() %></h4>
	<span>Subject: </span><input type="text" name="subject" size="50" /><br /><br />
	<span>Message: </span><br />
	<textarea name="messageBody" cols="100" rows="10"></textarea><br />
	<br />
	<input type="submit" value="Send" name="sendMessage"/>
<%	} else if (repDLHCPIndex >= 0) { %>
<%
		List<PersonnelBean> repDLHCPs = (List<PersonnelBean>) session.getAttribute("repDLHCPs");
		PersonnelBean dlhcp = repDLHCPs.get(repDLHCPIndex);
		session.setAttribute("dlhcp", dlhcp);
		List<PatientBean> representees = (List<PatientBean>) session.getAttribute("representees");
		PatientBean representee = representees.get(representeeIndex);
%>
	<h4>To <%= dlhcp.getFullName() %> on Behalf of <%= representee.getFullName() %></h4>
	<span>Subject: </span><input type="text" name="subject" size="50" /><br /><br />
	<span>Message: </span><br />
	<textarea name="messageBody" cols="100" rows="10"></textarea><br />
	<br />
	<input type="submit" value="Send" name="sendMessage"/>
<%	} else if (representeeIndex >= 0) { %>
<%
		List<PatientBean> representees = (List<PatientBean>) session.getAttribute("representees");
		PatientBean representee = representees.get(representeeIndex);
		List<PersonnelBean> repDLHCPs = action.getDLHCPsFor(representee.getMID());
		session.setAttribute("repDLHCPs", repDLHCPs);
%>
	<h4>To One of <%= representee.getFullName() %>'s DLHCPs</h4>
	<input type="hidden" name="representee" value="<%= representeeIndex %>"/>
<%		if (repDLHCPs.size() > 0) { %>
	<select name="repDLHCP">
		<option value="-1"></option>
<%			index = 0; %>
<%			for(PersonnelBean repDLHCP : repDLHCPs) { %>
		<option value="<%= index %>"><%= repDLHCP.getFullName() %></option>
<%				index ++; %>
<%			} %>
	</select>
	<input type="submit" value="Select" name="selectRepDLHCP"/>
<%		} else { %>
	<i><%= representee.getFullName() %> has not declared any HCPs.</i>
<%		} %>
<%	} %>
</form>
</div>

<%@include file="/footer.jsp"%>
