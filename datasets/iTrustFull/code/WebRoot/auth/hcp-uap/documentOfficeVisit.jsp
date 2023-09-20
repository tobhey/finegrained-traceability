<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.AddOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Document Office Visit";
%>

<%@include file="/header.jsp" %>

<%	
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/documentOfficeVisit.jsp");
   	return;
}

AddOfficeVisitAction action = new AddOfficeVisitAction(prodDAO, pidString);
long pid = action.getPid();
List<OfficeVisitBean> visits = action.getAllOfficeVisits();
if ("true".equals(request.getParameter("formIsFilled"))) {
	long ovID = action.addEmptyOfficeVisit(loggedInMID.longValue());
	response.sendRedirect("editOfficeVisit.jsp?ovID=" + ovID);
	return;
}
%>

<div align=center>
<form action="documentOfficeVisit.jsp" method="post" id="formMain">

<input type="hidden" name="formIsFilled" value="true" />
<br /><br />
Are you sure you want to document a <em>new</em> office visit for <b><%=action.getUserName()%></b>?<br /><br />
<input style="font-size: 150%; font-weight: bold;" type=submit value="Yes, Document Office Visit">
</form>
<br />
Click on an old office visit to modify:<br />
<%for(OfficeVisitBean ov : visits){ %>
	<a href="editOfficeVisit.jsp?ovID=<%=ov.getID()%>"><%=ov.getVisitDateStr()%></a><br />
<%} %>

<br /><br /><br />
</div>
<itrust:patientNav thisTitle="Document Office Visit" />

<%@include file="/footer.jsp" %>
