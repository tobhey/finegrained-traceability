<%@page import="edu.ncsu.csc.itrust.action.ChangeSessionTimeoutAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Change Session Timeout";
%>

<%@include file="/header.jsp" %>

<%
	ChangeSessionTimeoutAction action = new ChangeSessionTimeoutAction(prodDAO);
	if("true".equals(request.getParameter("formIsFilled"))){
		try{
			action.changeSessionTimeout(request.getParameter("minutes"));
			%><span>Session Timeout Changed. Changes will take place on re-authentication. </span><%
		} catch(FormValidationException e){
			e.printHTML(pageContext.getOut());
		}
	}
%>
<br /><br />
<form action="sessionTimeout.jsp" method="post">
<input type=hidden name="formIsFilled" value="true">

Change the timeout to  
<input name="minutes" value="<%=action.getSessionTimeout()%>" size="3"> minutes (minimum 1).<br /><br />
<input type=submit value="Change">
</form>
<br /><br />
The global session timeout is the interval of time that an account can remain inactive without having to re-authenticate. 
<br /><br />

<%@include file="/footer.jsp" %>
