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
	pageTitle = "iTrust - My Providers";
%>

<%@include file="/header.jsp"%>

<script type="text/javascript">
	function removeHCP(HCPID,formID) {
		document.getElementById("removeID").value = HCPID;
		document.getElementById(formID).submit();
	}
</script>

<%
PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());
String[] designateHCPs = request.getParameterValues("doctor");
String filterName = request.getParameter("filter_name");
String filterSpecialty = request.getParameter("filter_specialty");
String filterZip = request.getParameter("filter_zip");
String removeID = request.getParameter("removeID");

boolean filtered = false;

ViewVisitedHCPsAction action = new ViewVisitedHCPsAction(DAOFactory.getProductionInstance(),loggedInMID.longValue());
List<HCPVisitBean> hcpVisits = action.getVisitedHCPs();

String confirm = "";
try {
	if (removeID != null && !removeID.equals("")) {
		confirm = action.undeclareHCP(removeID);
	}
	else if (designateHCPs != null && !designateHCPs[0].equals("")) {
		for (String designateHCP : designateHCPs) {
			confirm = action.declareHCP(designateHCP);
		}
	}
}
catch (iTrustException ie) {
%>
<span ><%=ie.getMessage()%></span>
<% 
}
if(!"".equals(confirm)){%><span><%=confirm%></span><%}

List<PersonnelBean> personnel = null;
if (filterName != null && !filterName.equals("")) {
	filtered = true;
	personnel = action.filterHCPList(filterName, filterSpecialty, filterZip);
}
 
%>


<br />

<div align=center>
<% if (!filtered) { %>
	<h3>Provider list for <%=patient.getFullName()%></h3>
	<br />

	<form name="mainForm" id="mainForm" action="viewVisitedHCPs.jsp" method="post" onSubmit="return false;" target="_top">
		<input type="hidden" id="removeID" name="removeID" value="" />

		<table id="hcp_table" class="fTable" style="text-align: center;">
			<tr>
				<th>HCP Name</th>
				<th>Specialty</th>
				<th>Address</th>
				<th>Date of Office Visit</th>
				<th>Designated HCP?</th>
			</tr>

<%
	
	int i = 0; 
	for (HCPVisitBean vb: hcpVisits) { 
%>
			<tr>
				<td><%=vb.getHCPName()%></td>
				<td><%=vb.getHCPSpecialty()%></td>
				<td><%=vb.getHCPAddr()%></td>
				<td><%=vb.getOVDate()%></td>
				<td>
					<input name="doctor" value="<%=vb.getHCPName()%>" 
							type="checkbox"<%=vb.isDesignated()?"checked=\"checked\"":""%> 
							onClick="if(document.getElementsByName('doctor')[<%=i%>].checked) {this.form.submit();} else {removeHCP('<%=vb.getHCPName()%>', 'mainForm');}"/>
				</td>
			</tr> 
<%
		i++;
	}

	
	
%>
			<tr>
				<td colspan="5" style="color: #CC3333; text-align: right; font-weight: bold; font-size: 12px;">
					Select checkbox to update designated HCP
				</td>
			</tr>
		</table>
	</form>
<%
} 
else {
%>

	<form name="filterForm" id="filterForm" action="viewVisitedHCPs.jsp" method="post" onSubmit="return false;" target="_top">
		<table id="filter_hcp_table" class="fTable">
			<tr>
				<th>HCP Name</th>
				<th>Specialty</th>
				<th>Address</th>
				<th>Designated HCP?</th>
			</tr>
<% 
	List<PersonnelBean> added = null;
	if (null != personnel) {
		int i = 0; 
		for (PersonnelBean ele: personnel) {
		
%>
			<tr>
				<td><%=ele.getFullName() %></td>
				<td><%=ele.getSpecialty() == null?"none":ele.getSpecialty()%></td>
				<td><%=new String(ele.getStreetAddress1() +" "+ ele.getStreetAddress2() +" "+ ele.getCity() +", "+ ele.getState() +" "+ ele.getZip()) %></td>
				<td>
					<input name="doctor" value="<%=ele.getFullName()%>" 
							type="checkbox"<%=action.checkDeclared(ele.getMID())?"checked=\"checked\"":""%> 
							onClick="if(document.getElementsByName('doctor')[<%=i%>].checked) {this.form.submit();} else {removeHCP('<%=ele.getFullName()%>','filterForm');}"/>
				</td>
			</tr> 
<%

			i++;
			
		}
	
	
		
	}
%>
			<tr>
				<td colspan="5" style="color: #CC3333; text-align: right; font-weight: bold; font-size: 12px;">
					Select checkbox to update designated HCP
				</td>
			</tr>
		</table>
		
		<input type="hidden" id="removeID" name="removeID" value="" >
	</form>

<%
}
%>
</div>

<br /><br />
<form id="searchForm" action="viewVisitedHCPs.jsp" method="post">
	<div align=center>
	<table class="fTable" style="border: none;" border=0>
		<tr>
			<th colspan=2>Search HCPs</th>
		</tr>
		<tr style="text-align: left;">
			<td class="subHeaderVertical">Last Name:</td>
			<td ><input type="text" name="filter_name" size="30" maxlength="255" ></td>
		</tr>
		<tr style="text-align: left;">
			<td class="subHeaderVertical">Specialty:</td>
			<td ><input type="text" name="filter_specialty" size="30" maxlength="255" ></td>
		</tr>
		<tr style="text-align: left;">
			<td class="subHeaderVertical">Zip Code: </td>
			<td><input type="text" name="filter_zip" size="10" maxlength="10"></td>
		</tr>
	</table>
	<br />
	<input type="submit" name="update_filter" value="Search">
	</div>
</form>


<%@include file="/footer.jsp"%>
