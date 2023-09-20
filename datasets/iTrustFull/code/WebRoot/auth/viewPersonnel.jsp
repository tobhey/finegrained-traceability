<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>

<%@page import="java.util.List"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Personnel Details";
%>

<%@include file="/header.jsp"%>

<%
String personnel = request.getParameter("personnel");
int personnelIndex = Integer.parseInt(personnel);
List<PersonnelBean> personnelList = (List<PersonnelBean>) session.getAttribute("personnelList");
PersonnelBean p = personnelList.get(personnelIndex);

ViewPersonnelAction action = new ViewPersonnelAction(prodDAO, loggedInMID.longValue());
%>

<br />
<div align="center">
	<span style="font-weight: bold; font-size: 24px;"><%=p.getFullName()%></span>
</div>
<br />
<div align="center">
	<img src="/iTrust/image/user/<%=p.getMID()%>.png" alt="MID picture">
</div>
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="2">Personnel Details</th>
	</tr>
	<tr >
		<td class="subHeaderVertical">Specialty:</td>
		<td><%=p.getSpecialty() %></td>
	<tr >
		<td class="subHeaderVertical">Location:</td>
		<td><%=p.getStreetAddress1() +" " + p.getStreetAddress2() + " " + p.getCity() + " " + p.getState() + " " + p.getZip() %></td>
	</tr>
	<tr >
		<td class="subHeaderVertical">Phone:</td>
		<td><%=p.getPhone()%></td>
	</tr>
	<tr >
		<td class="subHeaderVertical">Email:</td>
		<td><%=p.getEmail() %></td>
	</tr>
</table>
<br />


<%@include file="/footer.jsp"%>
