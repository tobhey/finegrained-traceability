<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Login";

/* Note: there are better ways of implementing this feature. See the comment in LoginFailureAction */
	LoginFailureAction action = new LoginFailureAction(prodDAO, request.getRemoteAddr());
	
	if("true".equals(request.getParameter("loginError"))) {
		loginMessage = action.recordLoginFailure();
	}
%>

<%@include file="/header.jsp" %>
<%
	if(action.isValidForLogin()) {
%>

<div style="text-align: center; font-size: +2em">
	Welcome to iTrust
</div>

<div style="margin-top: 15px; margin-right: 40px; height: 150px;">
iTrust is a medical application that provides patients with a means to keep up with their medical history and records as well as communicate with their doctors, including selecting which doctors to be their primary caregiver, seeing and sharing satisfaction results, and other tasks.
iTrust is also an interface for medical staff from various locations.  iTrust allows the staff to keep track of their patients through messaging capabilities, scheduling of office visits, diagnoses, prescribing medication, ordering and viewing lab results, among other functions. 
</div>

<%
	} else { 
%>		

		
		You have too many failed logins times in a short span of time.<br />
		Please wait 15 minutes before logging in again.
<%
}
%>
<%@include file="/footer.jsp" %>

