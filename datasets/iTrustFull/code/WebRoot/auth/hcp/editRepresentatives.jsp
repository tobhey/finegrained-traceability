<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Manage Representatives";
%>

<%@include file="/header.jsp" %>

<br />
<%	
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/editRepresentatives.jsp");
   	return;
}

EditRepresentativesAction action = null;
List<PatientBean> representees = null;
long pid = 0L;
try {
	action = new EditRepresentativesAction(prodDAO,loggedInMID.longValue(), pidString);
	pid=action.getPid();
	String representee = request.getParameter("UID_repID");
	String confirm = "";
	if(representee!=null && !representee.equals("")){
		confirm = action.addRepresentative(representee);
	}
	String removeId = request.getParameter("removeId");
	if(removeId!=null && !removeId.equals("")){
		confirm = action.removeRepresentative(removeId);
	}

	if(!"".equals(confirm)){
%>
	<div align=center>
		<span class="iTrustMessage"><%=confirm%></span>
	</div>
	<br />
<%
	}
} catch(iTrustException ite) {
%>
	<div align=center>
		<span class="iTrustError"><%=ite.getMessage() %></span>
	</div>
	<br />
<%
} finally {
	representees = action.getRepresented(pid);
}
%>

<form method="post" name="mainForm">
<input type="hidden" name="pid" value="<%=pid %>">
<input type="hidden" id="removeId" name="removeId" value="">
<script type="text/javascript">
	function removeRep(repMID) {
		document.getElementById("removeId").value = repMID;
		document.mainForm.submit();
	}
</script>
<table class="fTable" align="center">
	<tr>
		<th colspan="3">Current Representatives</th>
	</tr>
	<tr class="subHeader">
		<td>Name</td>
		<td style="width: 30px">Action</td>
	</tr>
<%
	if(representees.size() ==0) { 
%>
		<tr><td  colspan="3" style="text-align: center;">No representatives specified</td></tr>
<%
	} 
	else { 
	    for(PatientBean p : representees){
%>
				<tr>		
					<!-- <td ><%=p.getMID() %></td>-->
					<td ><%=p.getFullName()%></td>
					<td ><a href="javascript:removeRep('<%=p.getMID()%>')">Remove</a></td>
				</tr>
<% 
	    }
	}
%>
	<tr><td colspan=2><br></td></tr>
	<tr>
		<td colspan="3">
			<table>
				<tr>
				<td><b>Patient:</b></td>
				<td style="width: 150px; border: 1px solid Gray;">
					<input name="UID_repID" value="" type="hidden">
					<span id="NAME_repID" name="NAME_repID">Not specified</span>
				</td>
				<td>
					<%@include file="/util/getUserFrame.jsp" %>
					<input type="button" onclick="getUser('repID');" value="Find User" >
				</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
<div align=center>
<input name="action" type="submit" value="Represent this patient">
</div>
</form>

<br /><br /><br />
<itrust:patientNav />

<%@include file="/footer.jsp" %>
