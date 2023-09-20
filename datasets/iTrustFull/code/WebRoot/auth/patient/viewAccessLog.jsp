<%@page import="edu.ncsu.csc.itrust.action.ViewMyAccessLogAction"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.ArrayList"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Access Log";
%>

<%@include file="/header.jsp"%>

<%
session.removeAttribute("personnelList");

	ViewMyAccessLogAction action = new ViewMyAccessLogAction(DAOFactory.getProductionInstance(), loggedInMID);
	List<TransactionBean> accesses;
	try{
		accesses = action.getAccesses(request.getParameter("startDate"), request.getParameter("endDate"), "role".equals(request.getParameter("sortBy")));
	} catch(FormValidationException e){
		e.printHTML(pageContext.getOut());
		accesses = action.getAccesses(null,null,false);
	}
	
	
%>

<br />
<table class="fTable" align='center'>
	<tr>
		<th><a href="#" onClick="javascript:sortBy('date');">Date</a></th>
		<th>Accessor</th>
		<th><a href = "#" onClick="javascript:sortBy('role');">Role</a></th>
		<th>Description</th>
	</tr>
<%
	boolean hasData = false;
	List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
	int index = 0;
	for(TransactionBean t : accesses){ 
		PersonnelBean hcp = new PersonnelDAO(DAOFactory.getProductionInstance()).getPersonnel(t.getLoggedInMID());
		if (hcp != null) {
			hasData = true;

	%>
			<tr>
				<td ><%=t.getTimeLogged()%></td>
				<td ><a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%=index%>"><%=hcp.getFullName()%></a></td>
				<td><%=t.getRole() %></td>
				<td ><%=t.getAddedInfo()%> (<%=t.getTransactionType().getCode()%>)</td>		
			</tr>
	<%
		PersonnelBean personnel = new PersonnelDAO(prodDAO).getPersonnel(t.getLoggedInMID());
		personnelList.add(personnel);
		index++;
		
			}
			else if("Personal Health Representative".equals(t.getRole())) {
		PatientBean p = new PatientDAO(DAOFactory.getProductionInstance()).getPatient(t.getLoggedInMID());
	%>
			<tr>
				<td ><%=t.getTimeLogged()%></td>
				<td ><%=p.getFullName()%></td>
				<td><%=t.getRole()%></td>
				<td ><%=t.getAddedInfo()%> (<%=t.getTransactionType().getCode()%>)</td>		
			</tr>
	<%

		}
	}
	session.setAttribute("personnelList", personnelList);
	if(!hasData) {
%>
	<tr>
		<td colspan=3 align="center">No Data</td>
	</tr>
<%
	}
	
	String startDate = action.getDefaultStart(accesses);
	String endDate = action.getDefaultEnd(accesses);
	if("role".equals(request.getParameter("sortBy"))) {
		startDate = request.getParameter("startDate");
		endDate = request.getParameter("endDate");
	}
%>
</table>
<br />
<br />

<form action="viewAccessLog.jsp" method="post">

<input type="hidden" name="sortBy" value=""></input>

<div align=center>
<table class="fTable" align="center">
	<tr class="subHeader">
		<td>Start Date:</td>
		<td>
			<input name="startDate" value="<%=startDate%>" size="10">
			<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
		</td>
		<td>End Date:</td>
		<td>
			<input name="endDate" value="<%=endDate%>">
			<input type=button value="Select Date" onclick="displayDatePicker('endDate');">
		</td>
	</tr>
</table>
<br />
<input type="submit" name="submit" value="Filter Records">

</div>
</form>

<script type='text/javascript'>
function sortBy(dateOrRole) {
	document.getElementsByName('sortBy')[0].value = dateOrRole;
	document.forms[0].submit.click();
}

</script>

<%@include file="/footer.jsp"%>
