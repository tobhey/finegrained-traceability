<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HCPDiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.MyDiagnosisAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.LOINCDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - My Diagnoses";
%>

<%@include file="/header.jsp"%>

<%
session.removeAttribute("personnelList");
String icdcode = request.getParameter("icd");
LOINCDAO loincDAO = DAOFactory.getProductionInstance().getLOINCDAO();
MyDiagnosisAction action = new MyDiagnosisAction(prodDAO, loggedInMID.longValue());
List<DiagnosisBean> diagnoses = action.getDiagnoses();
%>

<input type="hidden" name="formIsFilled" value="true">
<div align="center">
<h2>Diagnoses</h2>
<table class="fTable">
	<tr>
		<th>Diagnosis</th>
	</tr>
<%for(DiagnosisBean d : diagnoses) { %>
	<tr>
		<td><a href="myDiagnoses.jsp?icd=<%=d.getICDCode()%>"><%=d.getFormattedDescription()%></a></td>
	</tr>
<%} %>
</table>
</div> 
<br />

<%
if (icdcode != null && !icdcode.equals("")) {
	List<HCPDiagnosisBean> hcps = action.getHCPByDiagnosis(icdcode); 
%>
<div align="center">
	<h2>HCPs having experience with diagnosis <%=icdcode %></h2>
	<table class="fTable">
		<tr>
			<th>HCP</th>
			<th>Number of Patients</th>
			<th>List of Prescriptions</th>
			<th>List of Lab Procedures</th>
			<th>Average Office Visit Satisfaction</th>
			<th>Average Treatment Satisfaction</th>
		</tr>
<%
	if(hcps.size() > 0) {
		List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
		int index = 0;
		for (HCPDiagnosisBean bean: hcps) {
%>
		<tr>
			<td><a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%=index%>"><%=bean.getHCPName()%></a></td>
			<td><%=bean.getNumPatients()%></td>
			<td><%if (bean.getMedList().isEmpty()) { out.print("(no prescriptions)"); } else { 
					for (PrescriptionBean p: action.getPrescriptionsByHCPAndICD(bean.getHCP(), icdcode)) {%>
						<a href="viewPrescriptionInformation.jsp?visitID=<%=p.getVisitID()%>&presID=<%=p.getId()%>">
							<%=p.getMedication().getNDCode() + " " + p.getMedication().getDescription() + " prescribed"%>
						</a><br/>
					<%}} %></td>
						
			<td><%if (bean.getLabList().isEmpty()) { out.print("(no lab procedures ordered)"); } else { for (LabProcedureBean p: bean.getLabList()) {%><%=p.getLoinc() + " " + loincDAO.getProcedures(p.getLoinc()).get(0).getComponent() + " procedure ordered"%><br/><%}} %></td>
			<td><%=bean.getVisitSatisfaction() %></td>
			<td><%=bean.getTreatmentSatisfaction() %></td>
		</tr>
<%
			PersonnelBean personnel = new PersonnelDAO(prodDAO).getPersonnel(bean.getHCP());
			personnelList.add(personnel);
			index++;
		}
		session.setAttribute("personnelList", personnelList);
	}
	else {
%>
		<tr>
			<td colspan="6" align="center">
				No Data
			</td>
		</tr>
<%
	}
%>
	</table>
</div>
<%
}
%>

<%@include file="/footer.jsp"%>
