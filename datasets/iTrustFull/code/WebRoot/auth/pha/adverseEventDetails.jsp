<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.MonitorAdverseEventAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.AdverseEventBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Adverse Event Details";
%>

<%@include file="/header.jsp" %>

<%
MonitorAdverseEventAction action = new MonitorAdverseEventAction(prodDAO, loggedInMID.longValue());
if(request.getParameter("remove") != null) {
	try {
		action.remove(Integer.parseInt(request.getParameter("id")));
		%>
		<div align=center>
			<span class="iTrustMessage">Report successfully removed</span>
		</div>
		<%
	} catch(DBException e) {
		%>
		<div align=center>
			<span class="iTrustError"><%=e.getMessage()%></span>
		</div>
		<%
	}catch(iTrustException e) {
		%>
		<div align=center>
			<span class="iTrustError"><%=e.getMessage()%></span>
		</div>
		<%
	}
} else if(request.getParameter("moreInfo") != null) {
	try{
	action.sendEmail(Long.parseLong(request.getParameter("patientMID")), "I would like to know more about your experience with this medication");
	%>
	<div align=center>
		<span class="iTrustMessage">Request sent</span>
	</div>
	<%
	} catch(DBException e) {
		%>
		<div align=center>
			<span class="iTrustError"><%=e.getMessage()%></span>
		</div>
		<%
	}
} else {

	List<AdverseEventBean> events = (List<AdverseEventBean>)session.getAttribute("events");
	int num = 0;
	String reporter = "";
	try{
		num = Integer.parseInt(request.getParameter("eventNumber"));
		reporter = action.getName(Long.parseLong(events.get(num).getMID()));
	}catch(NumberFormatException e){
		%>
		<div align=center>
			<span class="iTrustError"><%=e.getMessage() %></span>
		</div>
	<%
	}
	String drug = events.get(num).getDrug() + " (" + events.get(num).getCode() + ")";
	String date = events.get(num).getDate();
	String description = events.get(num).getDescription();
	
	%>
	<form action="adverseEventDetails.jsp">
			<input type="hidden" name="id" value="<%=events.get(num).getId() %>" />
			<input type="hidden" name="patientMID" value="<%=Long.parseLong(events.get(num).getMID()) %>" />
	
			<b>Reporter: </b>
			<div><%=reporter%></div>
		<br />
			<b><%=session.getAttribute("prescriptionImmunization") %>: </b>
			<div><%=drug%></div>
		<br />
			<b>Date: </b>
			<div><%=date%></div>
		<br />
		<b>Description: </b>
		<div style="width:100%;"><%=description%></div>
	<br />
	<br />
	
	<input type="submit" value="Remove Event" name="remove" />
	<input type="submit" value="Request More Information" name="moreInfo" />
	</form>
	
	<%
	session.removeAttribute("events");
	session.removeAttribute("prescriptionImmunization");

}
	%>
<%@include file="/footer.jsp" %>
