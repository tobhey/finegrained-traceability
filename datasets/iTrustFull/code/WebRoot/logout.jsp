<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust Logout";

session.invalidate();
validSession = false;
response.sendRedirect("/iTrust");
%>
