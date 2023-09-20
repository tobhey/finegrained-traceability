<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>

<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FamilyMemberBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.risk.RiskChecker"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Personal Health Record";
%>

<%@include file="/header.jsp" %>

<%
PatientDAO patientDAO = new PatientDAO(prodDAO);
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
PersonnelBean personnelb = personnelDAO.getPersonnel(loggedInMID.longValue());
DateFormat df = DateFormat.getDateInstance();

String switchString = "";
if (request.getParameter("switch") != null) {
	switchString = request.getParameter("switch");
}

String relativeString = "";
if (request.getParameter("relative") != null) {
	relativeString = request.getParameter("relative");
}

String patientString = "";
if (request.getParameter("patient") != null) {
	patientString = request.getParameter("patient");
}

String pidString;
if (switchString.equals("true")) pidString = "";
else if (!relativeString.equals("")) {
	int relativeIndex = Integer.parseInt(relativeString);
	List<PatientBean> relatives = (List<PatientBean>) session.getAttribute("relatives");
	pidString = "" + relatives.get(relativeIndex).getMID();
	session.removeAttribute("relatives");
	session.setAttribute("pid", pidString);
}
else if (!patientString.equals("")) {
	int patientIndex = Integer.parseInt(patientString);
	List<PatientBean> patients = (List<PatientBean>) session.getAttribute("patients");
	pidString = "" + patients.get(patientIndex).getMID();
	session.removeAttribute("patients");
	session.setAttribute("pid", pidString);
}
else pidString = (String)session.getAttribute("pid");

if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("../getPatientID.jsp?forward=hcp-uap/editPHR.jsp");
   	return;
}
//else {
//	session.removeAttribute("pid");
//}

EditPHRAction action = new EditPHRAction(prodDAO,loggedInMID.longValue(), pidString);
long pid = action.getPid();
String confirm = action.updateAllergies(pid,request.getParameter("description"));

PatientBean patient = action.getPatient();
List<HealthRecord> records = action.getAllHealthRecords();
HealthRecord mostRecent = records.size() > 0 ? records.get(0) : null;
List<OfficeVisitBean> officeVisits = action.getAllOfficeVisits();
List<FamilyMemberBean> family = action.getFamily(); 
%>


<%@page import="edu.ncsu.csc.itrust.exception.NoHealthRecordsException"%><script type="text/javascript">
function showRisks(){
	document.getElementById("risks").style.display="inline";
	document.getElementById("riskButton").style.display="none";
}
</script>

<% if (!"".equals(confirm)) {%>
<span ><%=confirm%></span><br />
<% } %>

<br />
<div align=center>
	<div style="margin-right: 10px; display: inline-table;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="2">Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td ><%=patient.getFullName()%></td>
			</tr>
			<tr>
				<td  class="subHeaderVertical">Address:</td>
				<td > <%=patient.getStreetAddress1()%><br />
				     <%="".equals(patient.getStreetAddress2()) ? "" : patient.getStreetAddress2() + "<br />"%>
				     <%=patient.getStreetAddress3()%><br />									  
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td ><%=patient.getPhone()%></td>
			</tr>
			<tr>
				<td class="subHeaderVertical" >Email:</td>
				<td ><%=patient.getEmail()%></td>
			</tr>
			<tr>
				<th colspan="2">Insurance Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical" >Provider Name:</td>
				<td ><%=patient.getIcName()%></td>
			</tr>
			<tr>
				<td  class="subHeaderVertical">Address:</td>
				<td > <%=patient.getIcAddress1()%><br />
					<%="".equals(patient.getIcAddress2()) ? "" : patient.getIcAddress2() + "<br />"%>
					<%=patient.getIcAddress3()%><br />							
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td ><%=patient.getIcPhone()%></td>
			</tr>
		</table>
		<br />
		<a href="editPatient.jsp" style="text-decoration: none;">
			<input type=button value="Edit" onClick="location='editPatient.jsp';">
		</a>
	</div>
	<div style="margin-right: 10px; display: inline-table;">
		<table class="fTable" align="center">
			<tr>
				<th colspan="2">Basic Health Records</th>
			</tr>
			<% if (null == mostRecent) { %>
			<tr><td colspan=2>No basic health records are on file for this patient</td></tr>
			<% } else {%>
			<tr>
				<td class="subHeaderVertical">Height:</td>
				<td ><%=mostRecent.getHeight()%>in.</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Weight:</td>
				<td ><%=mostRecent.getWeight()%>lbs.</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Smoker?:</td>
				<td ><%=mostRecent.isSmoker() ? "Yes" : "No"%></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Blood Pressure:</td>
				<td ><%=mostRecent.getBloodPressureN()%>/<%=mostRecent.getBloodPressureD()%>mmHg</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Cholesterol:</td>
				<td >
				<table>
					<tr>
						<td style="text-align: right">HDL:</td>
						<td><%=mostRecent.getCholesterolHDL()%> mg/dL</td>
					</tr>
					<tr>
						<td style="text-align: right">LDL:</td>
						<td><%=mostRecent.getCholesterolLDL()%> mg/dL</td>
					</tr>
					<tr>
						<td style="text-align: right">Tri:</td>
						<td><%=mostRecent.getCholesterolTri()%> mg/dL</td>
					</tr>
					<tr>
						<td style="text-align: right">Total:</td>
						<td>
							<span id="totalSpan" style="font-weight: bold; color: #000;"><%=mostRecent.getTotalCholesterol()%> mg/dL</span>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<% } //closing for "there is a most recent record for this patient" %>
		</table>
		<br />
		<a href="editBasicHealth.jsp" style="text-decoration: none;">
			<input type="button" value="View/Edit History" onClick="location='editBasicHealth.jsp';">
		</a>
	</div>
	<div style="display: inline-table;">
		<table class="fTable" align="center">
			<tr>
				<th>Office Visits</th>
			</tr>
			<tr>
				<td align="center">
					<div style="overflow:auto; height:200px; width:200px;">
					<% for (OfficeVisitBean ov : officeVisits) { %>
						<a href="editOfficeVisit.jsp?ovID=<%=ov.getVisitID()%>"><%=df.format(ov.getVisitDate())%></a><br />
					<% } %>
					</div>
				</td>
			</tr>
		</table>
		<br />
		<a href="getPrescriptionReport.jsp" style="text-decoration: none;" >
			<input type=button value="Get Prescription Report" onClick="location='getPrescriptionReport.jsp';">
		</a>				
	</div>
</div>
<br />

<table class="fTable" align="center" >
	<tr>
		<th colspan="9">Family Medical History</th>
	</tr>
	<tr class="subHeader">
		<td> Name </td>
		<td> Relation </td>
		<td> High Blood Pressure </td>
		<td> High Cholesterol </td>
		<td> Diabetes </td>
		<td> Cancer </td>
		<td> Heart Disease </td>
		<td> Smoker </td>
		<td> Cause of Death </td>
	</tr>
<% if (0 == family.size()) {%>
	<tr>
		<td colspan="9" style="text-align: center;">No Relations on	record</td>
	</tr>
<%	} 
	else {
		List<PatientBean> patientRelatives = new ArrayList<PatientBean>();
		int index = 0;
		for(FamilyMemberBean member : family) {
			patientRelatives.add(patientDAO.getPatient(member.getMid())); 
%>
	<tr>					
		<td class = "valueCell" ><a href="editPHR.jsp?relative=<%=index%>"><%=member.getFullName()%></a></td>
		<td ><%=member.getRelation()%></td>
		<td  align=center><%=action.doesFamilyMemberHaveHighBP(member) ? "x" : ""%></td>
		<td  align=center><%=action.doesFamilyMemberHaveHighCholesterol(member) ? "x" : ""%></td>
		<td  align=center><%=action.doesFamilyMemberHaveDiabetes(member) ? "x" : ""%></td>
		<td  align=center><%=action.doesFamilyMemberHaveCancer(member) ? "x" : ""%></td>
		<td  align=center><%=action.doesFamilyMemberHaveHeartDisease(member) ? "x" : ""%></td>
		<td  align=center><%=action.isFamilyMemberSmoker(member) ? "x" : ""%></td>
		<td ><%=action.getFamilyMemberCOD(member)%></td>
	</tr>
<%			index++;
		}
		session.setAttribute("relatives", patientRelatives);
	} %>
</table>
<br />
<div align=center>
	<div style="margin-right: 10px; display: inline-table;">
		<% List<AllergyBean> allergies = action.getAllergies(); %>
		<table class="fTable" align="center" >
			<tr>
				<th colspan="2">Allergies</th>
			</tr>
			<tr class="subHeader">
				<td>Allergy Description</td>
				<td>First Found</td>
			</tr>
	
			<% if (0 == allergies.size()) { %>
			<tr>
				<td  colspan="2" style="text-align: center;">No Allergies on record</td>
			</tr>
			<% } else {
				for (AllergyBean allergy : allergies) {%>
			<tr>
				<td  style="text-align: center;"><%=allergy.getDescription()%></td>
				<td  style="text-align: center;"><%=df.format(allergy.getFirstFound())%></td>
			</tr>			
			<% } } %>
			<form name="AddAllergy" action="editPHR.jsp" method="post">
			<tr >
				<th colspan="2" style="text-align: center;">New</th>
			</tr>
			<tr>
				<td colspan="2">
					<input type="text" size="30" maxlength="50"	name="description">
					<input type="submit" name="addA" value="Add Allergy">
				</td>
			</tr>
		</table>
	</div>
	<div style="margin-right: 10px; display: inline-table;">
		<table class="fTable" align=center>
			<tr>
				<th colspan=2 style="background-color:silver;">Chronic Disease Risk Factors</th>
			</tr>
			<tr>
				<td align="center">
					<div id="risks" style="display: none;">
						Patient is at risk for the following:<br />
						<% try{
								List<RiskChecker> diseases = action.getDiseasesAtRisk();
								for (RiskChecker disease : diseases) { %>
						  			<span ><%=disease.getName()%></span><br />
							<% }
						   } catch (NoHealthRecordsException e) {
							   %><%=e.getMessage()%><%
						   } %>
						<a style="font-size: 80%" href="chronicDiseaseRisks.jsp">More Information</a>
					</div>
				</td>
			</tr>
		</table>
		<br />
		<div id="riskButton">
			<input type=button value="Show Chronic Diseases Risk Factors" onclick="javascript:showRisks();">
		</div>
	</div>
	<div style="display: inline-table;">
		<table class="fTable" align=center>
			<tr>
				<th colspan="3" style="background-color:silver;">Immunizations</th>
			</tr>
			<tr class="subHeader">
	  			<td>CPT Code</td>
 				<td>Description</td>
  				<td>Date Received</td>
 			</tr>
<%	
		for (OfficeVisitBean ov: officeVisits) { 
			for (ProcedureBean proc:  action.getCompleteOfficeVisit(ov.getVisitID()).getProcedures()) {
				if (null != proc.getAttribute() && proc.getAttribute().equals("immunization")) { 
%>
			<tr>
				<td ><%=proc.getCPTCode() %></td>
				<td ><%=proc.getDescription() %></td>
				<td ><a href="editOfficeVisit.jsp?ovID=<%=ov.getVisitID()%>"><%=proc.getDate() %></a></td>	
			</tr>
<%
				}
			}
		}
%>
		</table>
	</div>
</div>		

<br /><br /><br />
<itrust:patientNav thisTitle="Health Records" />

<%@include file="/footer.jsp" %>
