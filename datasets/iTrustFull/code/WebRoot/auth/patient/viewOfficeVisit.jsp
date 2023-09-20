<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Office Visit Details";
%>

<%@include file="/header.jsp"%>
<%
session.removeAttribute("personnelList");

	String ovID = request.getParameter("ovID");
	String repMID = request.getParameter("repMID");
	
	ViewOfficeVisitAction action = null;
	
	if(repMID != null){
		//This constructor checks that the representative is correct
		action = new ViewOfficeVisitAction(prodDAO, loggedInMID.longValue(), repMID, ovID);
	} else{
		action = new ViewOfficeVisitAction(prodDAO, loggedInMID.longValue(),ovID);
	}

	/* (if(request.getParameter() */
	
	OfficeVisitBean ov = action.getOfficeVisit();	
	String hcpName = action.getHCPName(ov.getHcpID());
%>
<br />
<table class="fTable" align="center">
	<tr><th colspan=2>Office Visit Details</th></tr>
	<tr>
		<td  class="subHeader">Date:</td>
		<td><%=ov.getVisitDateStr()%></td>
	</tr>
	<tr>
		<td  class="subHeader">HCP:</td>
<%
		List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
		int index = 0;
%>
		<td><a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%=index%>"><%=hcpName%></a></td>
<%
		PersonnelBean personnel = new PersonnelDAO(prodDAO).getPersonnel(ov.getHcpID());
		personnelList.add(personnel);
		index++;
		session.setAttribute("personnelList", personnelList);
%>
	</tr>
	<tr>
		<td  class="subHeader">Notes:</td>
		<td>
			<%= ov.getNotes() %>
		</td>
	</tr>
</table>
<br /><br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="2">Diagnoses</th>
	</tr>
	<tr  class="subHeader">
		<th>ICD Code</th>
		<th>Description</th>
	</tr>
	<% if (ov.getDiagnoses().size() == 0) { %>
	<tr>
		<td colspan="2" >No Diagnoses for this visit</td>
	</tr>
	<% } else { 
		for(DiagnosisBean d : ov.getDiagnoses()) {%>
		<tr>
			<td ><itrust:icd9cm code="<%=d.getICDCode()%>"/></td>
			<td  style="white-space: nowrap;"><%=d.getDescription() %></td>
		</tr>
	   <%} 
  	   }  %>
</table>
<br /><br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="5" style="background-color:silver;">Medications</th>
	</tr>
	<tr class="subHeader">
		<td>NDCode</td>
		<td>Description</td>
		<td>Date of Usage</td>
		<td>Dosage</td>
		<td>Instructions</td>
	</tr>
	<% if (ov.getPrescriptions().size() == 0) { %>
	<tr>
		<td colspan="5" class = "valueCell" align="center">No Medications on record</td>
	</tr>
	<% } else { 
		for(PrescriptionBean m : ov.getPrescriptions()) { %>
		<tr>
			<td class = "valueCell"><%=m.getMedication().getNDCodeFormatted()%></td>
			<td class = "valueCell"><%=m.getMedication().getDescription() %></td>
			<td class = "valueCell"><%=m.getStartDateStr()%> to <%=m.getEndDateStr()%></td>
			<td class = "valueCell"><%=m.getDosage()%>mg</td>
			<td class = "valueCell"><%=m.getInstructions()%></td>
		</tr>
	<%  } 
	  } %>
</table>
<br /><br />
<table class="fTable" align="center" >
	<tr>
		<th colspan="2" style="background-color:silver;">Procedures</th>
	</tr>
	<tr class="subHeader">
		<td>CPT Code</td>
		<td>Description</td>
	</tr>
	<% if (ov.getProcedures().size() == 0) { %>
	<tr>
		<td colspan="2" >No Procedures on record</td>
	</tr>
	<% } else { 
		for(ProcedureBean p : ov.getProcedures()) {%>
		<tr>
			<td ><%=p.getCPTCode() %></td>
			<td ><%=p.getDescription() %></td>
		</tr>
	<%  } 
	   }  %>
</table>
<br />

<%@include file="/footer.jsp"%>
