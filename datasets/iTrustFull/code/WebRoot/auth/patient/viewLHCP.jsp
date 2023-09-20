<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "LCHP Information";
%>

<%@include file="/header.jsp"%>

<script type="text/javascript">
	function removeHCP(HCPID,formID) {
		document.getElementById("removeID").value = HCPID;
		document.getElementById(formID).submit();
	}
</script>

<%
int index = Integer.parseInt(request.getParameter("index"));
PersonnelBean myLHCP = ((List<PersonnelBean>) session.getAttribute("personnelList")).get(index);


%>
<div align = center>
<table class="fTable">
<tr><th colspan=2><%=myLHCP.getFullName() %></th></tr>
<tr><td>Specialty:</td><td><%=myLHCP.getSpecialty() == null ? "" : myLHCP.getSpecialty() %></td></tr>
<tr><td>Address:</td><td><%=myLHCP.getStreetAddress1() %> <br> 
						<%=  myLHCP.getStreetAddress2() %> <br>
						<%= myLHCP.getCity().equals("") ? "" : myLHCP.getCity() + "," %> <%= myLHCP.getState() + " " + myLHCP.getZip() %> </td></tr>
<tr><td>Phone:</td><td><%=myLHCP.getPhone().equals("--") ? "" : myLHCP.getPhone() %></td></tr>
<tr><td>Email:</td><td><%=myLHCP.getEmail() %></td></tr>
</table>
</div>

<%@include file="/footer.jsp"%>
