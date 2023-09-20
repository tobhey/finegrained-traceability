<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
PatientBean patient = new PatientDAO(DAOFactory.getProductionInstance()).getPatient(loggedInMID); 
%>

<div align="center" style="margin-bottom: 30px;">
	<img src="/iTrust/image/user/<%=loggedInMID.longValue() %>.png" alt="MID picture">
</div>

<div align="center">
<table>
	<tr>
		<td>Name: </td>
		<td><%=patient.getFullName()%></td>
	</tr>
	<tr>
		<td>Gender: </td>
		<td><%=patient.getGender() %></td>
	</tr>
	<tr>
		<td>Location: </td>
		<td><%=patient.getCity() + ", " + patient.getState()%></td>
	</tr>
	<tr>
		<td>DOB: </td>
		<td><%=patient.getDateOfBirthStr() %></td>
	</tr>
	<tr>
		<td>Blood Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><%=patient.getBloodType() %> </td>
	</tr>
</table>
</div>