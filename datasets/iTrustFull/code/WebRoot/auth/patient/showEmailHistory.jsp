<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="java.util.List"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Email History";
%>

<%@include file="/header.jsp" %>

<div align="center" style="margin: 10px;">
<%
	ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, loggedInMID.longValue());
	List<Email> Emails = action.getEmailHistory();
	%><table class="fTable"><%
	if (Emails.size() != 0){
		%> 
		
			<tr>
			<th> To </th> 
			<th> Subject </th> 
			<th> Body </th>
			<th> Date Sent </th>
			</tr>
		<%
		for (Email email : Emails){
%>
		<tr>
			<td> <%=email.getToListStr()%> </td>
			<td> <%=email.getSubject()%> </td> 
			<td> <%=email.getBody()%> </td>
			<td> <%=email.getTimeAdded() %> </td>
		</tr>
<%}

}
	else{
	%>
	<tr><td> No Emails on Record </td></tr>
	<%} %>
	</table>
</div>

<%@include file="/footer.jsp" %>
