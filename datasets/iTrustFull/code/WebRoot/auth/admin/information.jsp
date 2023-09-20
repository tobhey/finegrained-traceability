<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>

<%@include file="/global.jsp" %>

<%PersonnelBean personnelb = new PersonnelDAO(prodDAO).getPersonnel(loggedInMID);%>

<div align="center" style="margin-bottom: 30px;">
	<img src="/iTrust/image/user/<%=loggedInMID.longValue() %>.png" alt="MID picture">
</div>

<div align="center">
<table width="165">
	<tr>
		<td>Name: </td>
		<td><%=personnelb.getFullName()%></td>
	</tr>
	<tr>
		<td>Location: </td>
		<td>Somewhere</td>
	</tr>
</table>
</div>