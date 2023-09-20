<%@page import="edu.ncsu.csc.itrust.DBUtil"%>

<%
if(!DBUtil.canObtainProductionInstance()){
	response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
}


if(request.getUserPrincipal() != null) {
	long mid = Long.valueOf(request.getUserPrincipal().getName());
	userName = authDAO.getUserName(mid);
}
else
{
	if (null != userRole)
	{
		userRole = null;
		response.sendRedirect("/iTrust/errors/reboot.jsp");
	}
}


if (request.getAuthType() != null) {
			
		if (request.getSession(false) != null) {
			boolean isValidLogin = loginFailureAction.isValidForLogin();
			if(!isValidLogin) {
				session.invalidate();
				return;
			}
			else {
			
				loggedInMID = new Long(Long.valueOf(request.getUserPrincipal().getName()));
				session.setAttribute("loggedInMID", loggedInMID);
			}
		}
}

%>
