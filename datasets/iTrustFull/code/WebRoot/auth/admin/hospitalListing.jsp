<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain Hospital Listing and Assignments";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "Current Hospital Listing";
	
	UpdateHospitalListAction hospUpdater =
		new UpdateHospitalListAction(DAOFactory.getProductionInstance(), loggedInMID);
	
	if (request.getParameter("add") != null || request.getParameter("update") != null) {
		HospitalBean hosp = new BeanBuilder<HospitalBean>().build(request.getParameterMap(), new HospitalBean());
		try {
			headerMessage = (request.getParameter("add") != null)
					? hospUpdater.addHospital(hosp)
					: hospUpdater.updateInformation(hosp);
		} catch(FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
			headerMessage = "Validation Errors";
		}
	}
	String headerColor = (headerMessage.indexOf("Error") > -1)
			? "#ffcccc"
			: "#00CCCC";
%>

<div align=center>
<form name="mainForm" method="post">
<input type="hidden" id="updateID" name="updateID" value="">
<input type="hidden" id="oldName" name="oldName" value="">
<script type="text/javascript">
	function fillUpdate(id) {
		document.getElementById("hospitalID").value = id;
		document.getElementById("hospitalName").value
			= unescape(document.getElementById("UPD" + id).value);
		document.getElementById("oldDescrip").value
			= unescape(document.getElementById(
					"UPD" + id).value);
	}
</script>

<h3>Hospital Listing</h3>

<span class="iTrustMessage"><%=headerMessage %></span>

<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="2" >Update Hospital List</th>
	</tr>
	<tr class="subHeader">
		<th>Hospital ID</th>
		<th>Hospital Name</th></tr>
	<tr>
		<td><input type="text" name="hospitalID" id="hospitalID" size="10" maxlength="10" /></td>
		<td><input type="text" name="hospitalName" id="hospitalName"
								size="30" maxlength="100" /></td>
	</tr>
</table>
<br />
<input type="submit" name="add" value="Add Hospital" />
<input type="submit" name="update" value="Update Hospital Information" />
<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="2">Current Hospitals</th></tr>
	<tr class="subHeader">
		<th>Hospital ID</th>
		<th>Hospital Name</th>
	</tr>
	<%
		List<HospitalBean> hospList =
			DAOFactory.getProductionInstance().getHospitalsDAO().getAllHospitals();
		String tempID = "";
		String tempName = "";
		String escapedName = "";
		for (HospitalBean hospEntry : hospList) {
			tempID = hospEntry.getHospitalID() + "";
			tempName = hospEntry.getHospitalName();
			escapedName = URLEncoder.encode(tempName, "UTF-8").replaceAll("\\+", "%20");
	%>
		<tr>
			<td ><%=tempID %></td>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate('<%=tempID %>')"><%=tempName %></a>
				<input type="hidden"
						id="UPD<%=tempID %>"
						name="UPD<%=tempID %>"
						value="<%=escapedName %>">
			</td>
		</tr>
	<% } %>
</table>
</form>
</div>
<br />

<%@include file="/footer.jsp" %>
