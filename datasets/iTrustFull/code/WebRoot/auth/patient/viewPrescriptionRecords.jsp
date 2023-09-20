<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPrescriptionRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.HCPLinkBean" %>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean" %>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import= "java.util.ArrayList"%>
<%@page import= "java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Get My Prescription Report";
%>

<%@include file="/header.jsp"%>

<%
List<HCPLinkBean> LinkList = new ArrayList<HCPLinkBean>();
PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue()); 
ViewPrescriptionRecordsAction action = new ViewPrescriptionRecordsAction(prodDAO,loggedInMID.longValue());
List<PatientBean> representees = action.getRepresentees();
boolean showMine = false;
boolean showOther = false;
boolean adEvent = false;
boolean showAdverseButton = false;
	
	if (request.getParameter("mine") != null && request.getParameter("mine").equals("View Current")) showMine = true;
	if (request.getParameter("other") != null && request.getParameter("other").equals("View")) showOther = true;
	if (request.getParameter("representee") != null && request.getParameter("representee").equals("-1")) showOther = false;
	if (request.getParameter("adevent") != null && request.getParameter("adevent").equals("Report Adverse Events")){
		adEvent = true;
		int check[] = new int[100];
		boolean oneChecked = false;
		for(int i = 0; i<100; i++){
			if (null != request.getParameter("checking"+i) && request.getParameter("checking"+i).toLowerCase().equals("y")) {
				check[i] = 1;
				oneChecked = true;
			}
		}
			
		List<HCPLinkBean> l = (List<HCPLinkBean>) session.getAttribute("beanlist");
		int i = 0;
		for(HCPLinkBean b : l){
			if(check[i] == 1) b.setChecked(true);
			else b.setChecked(false);
			i++;
		}
		session.setAttribute("beanlist", l);
		if(oneChecked){
			response.sendRedirect("reportAdverseEvent.jsp?prescriptions=yes");
		}
		else{%>
		<div align=center>
			<span class="iTrustError"><%="Must report on at least one prescription" %></span>
		</div>
		<% }
	}
	
%>
<div align="center">
	<form action="viewPrescriptionRecords.jsp" method="post">
		<table>
			<tr>
				<td>
					<span style="font-size: 24px; font-weight: bold;">View My Own Prescriptions</span>
				</td>
				<td>
					<input type="submit" name="mine" value="View Current"></input>
				</td>
			</tr>
			<tr>
				<td>
					<span style="font-size: 24px; font-weight: bold;">View Other's Prescriptions</span>
				</td>
			
<%	
		if (representees.size() > 0) { 
%>
				<td>
					<select name="representee">
						<option value="-1"></option>
<%
			int index = 0;
			for (PatientBean representee : representees) { 
%>
						<option value="<%=index %>"><%=representee.getFullName()%></option>
<%
				index ++;
			} 
%>
					</select>
					<input type="submit" name="other" value="View"></input>
				</td>
<%	
		} else { 
%>
				<td>
					<i>You are not anyone's representative</i>
				</td>
<%	
		} 
%>
			</tr>
		</table>
	</form>
	<br />
	<table class="fTable">
	
	
	
	
	
<%
	if (showMine) { 
		List<PrescriptionBean> prescriptions = action.getPrescriptionsForPatient(loggedInMID.longValue());
		if (prescriptions.size() == 0) { 
%>
		<tr>
			<td colspan=4>
				<i>No prescriptions found</i>
			</td>
		</tr>
<%
		} else { 
%>
		<tr>
			<th colspan=5><%= patient.getFullName() %></th>
		</tr>
		<tr class="subHeader">
			<td>ND Code</td>
			<td>Description</td>
			<td>Duration</td>
			<td>Prescribing HCP</td>
			<td>Report Adverse Event (Y/N)</td>
		</tr>
<%			
			int a = 0;
			showAdverseButton = true;
			for (PrescriptionBean prescription : prescriptions) {
				Date date = new Date();
				date.setYear(date.getYear()-1);
				if(prescription.getEndDate().after(date)){
%>
		<tr>
			<td ><a href="viewPrescriptionInformation.jsp?visitID=<%=prescription.getVisitID()%>&presID=<%=prescription.getId()%>"><%=prescription.getMedication().getNDCodeFormatted() %></a></td>
			<td ><%=prescription.getMedication().getDescription() %></td>
			<td ><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
			<td ><%=action.getPrescribingDoctor(prescription).getFullName() %></td>
			<% 
				HCPLinkBean HLbean = new HCPLinkBean();
				HLbean.setPrescriberMID(action.getPrescribingDoctor(prescription).getMID());
				HLbean.setDrug(prescription.getMedication().getDescription());
				HLbean.setCode(prescription.getMedication().getNDCode());
				LinkList.add(HLbean);
			%>
			<td>
	<form action="viewPrescriptionRecords.jsp" method="post">
			<input name="checking<%=a%>" type="text" value="N" size="1" maxlength="1"></input></td>
			<%a++;%>
			
		</tr>

	
	
<%			
			}}
		}
	} else if (showOther) {
		PatientBean representee = representees.get(Integer.parseInt(request.getParameter("representee"))); 
%>
		<tr>
			<th colspan=4><%= representee.getFullName() %></th>
		</tr>
<%	
		List<PrescriptionBean> prescriptions = action.getPrescriptionsForPatient(representee.getMID());
		if (prescriptions.size() == 0) { 
%>
		<tr>
			<td colspan=4>
				<i>No prescriptions found</i>
			</td>
		</tr>
<%
		} else { 
%>
		<tr class="subHeader">
			<td>ND Code</td>
			<td>Description</td>
			<td>Duration</td>
			<td>Prescribing HCP</td>
		</tr>
<%			
			for (PrescriptionBean prescription : prescriptions) { 
%>
		<tr>
			<td ><%=prescription.getMedication().getNDCodeFormatted() %></td>
			<td ><%=prescription.getMedication().getDescription() %></td>
			<td ><%=prescription.getStartDateStr() %> to <%=prescription.getEndDateStr() %></td>
			<td ><%= action.getPrescribingDoctor(prescription).getFullName() %></td>
		</tr>
<%
			} 
		}
	} 
%>
	</table>
		
	<br />
	<%
	if (showMine && showAdverseButton) {
		session.setAttribute("beanlist", LinkList);
		%>
		<input type="submit" name="adevent" value="Report Adverse Events"></input>
		</form>
		<%
	} %>
</div>

<%@include file="/footer.jsp"%>
