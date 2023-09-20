<%@ page isErrorPage="true"%>
<%
if(exception!=null){
	exception.printStackTrace();
	request.getSession().setAttribute("errorMessage",exception.getClass().getSimpleName() + ": "+ exception.getMessage());
}
response.sendRedirect("/iTrust/auth/forwardUser.jsp");
%>