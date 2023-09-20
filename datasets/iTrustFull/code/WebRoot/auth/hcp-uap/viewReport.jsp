<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewReportAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HealthRecord"%>
<%@page import="edu.ncsu.csc.itrust.beans.AllergyBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FamilyMemberBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="java.text.NumberFormat" %>
<%@page import="java.text.DecimalFormat" %>
<%@page import="edu.ncsu.csc.itrust.Localization" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Comprehensive Patient Report";
%>

<%@include file="/header.jsp" %>

<%
//long loggedInMID = request.getUserPrincipal()==null ? 0L : Long.valueOf(request.getUserPrincipal().getName());

PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
ViewMyReportRequestsAction rAction = new ViewMyReportRequestsAction(prodDAO, loggedInMID.longValue());
int patientIndex = Integer.parseInt(request.getParameter("patient"));
List<PatientBean> patients = (List<PatientBean>) session.getAttribute("patients");
//session.removeAttribute("patients");
String pidString = "" + patients.get(patientIndex).getMID();
String rrString = request.getParameter("requestID");
long patientMID;
int requestID;
ReportRequestBean reportRequest;
try {
	if (pidString == null || pidString.equals("")) throw new Exception("Error: patientMID is null");
	if (rrString == null || rrString.equals("")) throw new Exception("Error: requestID is null");
	patientMID = Long.parseLong(pidString);
	requestID = Integer.parseInt(rrString);
	reportRequest = rAction.getReportRequest(requestID);
	rAction.setViewed(requestID);
	reportRequest = rAction.getReportRequest(requestID);

ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, patientMID);
ViewReportAction viewAction = new ViewReportAction(prodDAO, loggedInMID.longValue());
PatientBean patient = action.getPatient();
List<OfficeVisitBean> officeVisits = action.getAllOfficeVisits();
List<HealthRecord> records = action.getAllHealthRecords();
List<AllergyBean> allergies = action.getAllergies();
List<PatientBean> represented = action.getRepresented();
List<PatientBean> representing = action.getRepresenting();
List<FamilyMemberBean> family = action.getFamily(); 
List<DiagnosisBean> diagnoses = viewAction.getDiagnoses(patientMID);
List<ProcedureBean> procedures = viewAction.getProcedures(patientMID);
List<PrescriptionBean> prescriptions = viewAction.getPrescriptions(patientMID);
List<PersonnelBean> hcps = viewAction.getDeclaredHCPs(patientMID);

%><h3>Comprehensive Patient Report for <%=patient.getFullName()%></h3>

<div align=center>
<table>
	<tr><td valign=top>
	<table class="fTable">
		<tr>
			<th colspan="2">Patient Information</th>
		</tr>
		<tr>
			<td class="subHeaderVertical">Name:</td>
			<td ><%=patient.getFullName()%></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Address:</td>
			<td >
			<%=patient.getStreetAddress1()%><br />
			<%= "".equals(patient.getStreetAddress2()) ? "" : patient.getStreetAddress2() + "<br />"%>
			<%=patient.getStreetAddress3()%><br />									  
			</td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Phone:</td>
			<td ><%=patient.getPhone()%></td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Email:</td>
			<td ><%=patient.getEmail()%></td>
		</tr>
	</table>
	</td>
	<td width="15px">&nbsp;</td>
	<td>
	<table class="fTable">
		<tr>
			<th colspan="2">Insurance Information</th>
		</tr>
		<tr>
			<td class="subHeaderVertical">Name (ID):</td>
			<td ><%=patient.getIcName()%> (<%=patient.getIcID()%>)</td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Address:</td>
			<td >
			<%=patient.getIcAddress1()%><br />
			<%="".equals(patient.getIcAddress2()) ? "" : patient.getIcAddress2() + "<br />"%>
			<%=patient.getIcAddress3()%><br />							
			</td>
		</tr>
		<tr>
			<td class="subHeaderVertical">Phone:</td>
			<td ><%=patient.getIcPhone()%></td>
		</tr>
	</table>
	</td></tr>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="10">Health Records</th>
		</tr>
	<tr class="subHeader">
		<td>Height</td>
		<td>Weight</td>
		<td>Smokes?</td>
		<td>Blood Pressure</td>
		<td>HDL</td>
		<td>LDL</td>
		<td>Triglycerides</td>
		<td>Total Cholesterol</td>		
		<td>Last Recorded</td>
		<td>By Personnel</td>
	</tr>
<%	
	PersonnelBean personnel;
	for(HealthRecord hr : records) {
		personnel = personnelDAO.getPersonnel(hr.getPersonnelID());
%>
	<tr>
		<td align=center><%=hr.getHeight()%>in</td>
		<td align=center><%=hr.getWeight() %>lbs</td>
		<td align=center><%=hr.isSmoker() ? "Y" : "N" %></td>
		<td align=center><%=hr.getBloodPressure()%> mmHg</td>
		<td align=center><%=hr.getCholesterolHDL()%> mg/dL</td>
		<td align=center><%=hr.getCholesterolLDL()%> mg/dL</td>
		<td align=center><%=hr.getCholesterolTri()%> mg/dL</td>
		<td align=center><%=hr.getTotalCholesterol()%> mg/dL</td>		
		<td align=center><%=hr.getDateRecorded()%></td>
		<td align=center><%=personnel.getFullName()%></td>
	</tr>
	<%}%>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan=3>Diagnoses</th>
		</tr>
			<tr class="subHeader">
				<td>Visit ID</td>
				<td>ICD Code</td>
				<td>Description</td>
			</tr>
	
			<%if(diagnoses.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No diagnoses on record</td>
			</tr>
			<%} else {
				NumberFormat numberFormatter = NumberFormat.getInstance(Localization.instance().getCurrentLocale());
				((DecimalFormat)numberFormatter).setMaximumFractionDigits(2);
				((DecimalFormat)numberFormatter).setMinimumFractionDigits(2);
				for(DiagnosisBean diagnosis : diagnoses){%>
			<tr>
				<td style="text-align: center;"><%=diagnosis.getOvDiagnosisID()%></td>
				<td style="text-align: center;"><%=numberFormatter.format(Float.parseFloat(diagnosis.getICDCode()))%></td>
				<td style="text-align: center;"><%=diagnosis.getDescription()%></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="2">Designated HCPs</th>
		</tr>
			<tr class="subHeader">
				<td>HCP Name</td>
			</tr>
	
			<%if(hcps.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No designated HCPs on record</td>
			</tr>
			<%} else {
				for(PersonnelBean hcp : hcps){%>
			<tr>
				<td style="text-align: center;"><%=hcp.getFullName()%></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="2">Allergies</th>
		</tr>
			<tr class="subHeader">
				<td>Allergy Description</td>
				<td>First Found</td>
			</tr>
	
			<%if(allergies.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No Allergies on record</td>
			</tr>
			<%} else {
				for(AllergyBean allergy : allergies){%>
			<tr>
				<td style="text-align: center;"><%=allergy.getDescription()%></td>
				<td style="text-align: center;"><%=allergy.getFirstFoundStr()%></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="3">Procedures</th>
		</tr>
			<tr class="subHeader">
				<td>Visit ID</td>
				<td>CPT Code</td>
				<td>Description</td>
			</tr>
	
			<%if(procedures.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No procedures on record</td>
			</tr>
			<%} else {
				for(ProcedureBean procedure : procedures){%>
			<tr>
				<td style="text-align: center;"><%=procedure.getOvProcedureID()%></td>
				<td style="text-align: center;"><%=procedure.getCPTCode()%></td>
				<td style="text-align: center;"><%=procedure.getDescription()%></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="6">Prescriptions</th>
		</tr>
			<tr class="subHeader">
				<th>Visit ID</th>
				<th>NDCode</th>
				<th>StartDate</th>
				<th>EndDate</th>
				<th>Dosage</th>
				<th>Instructions</th>
			</tr>
	
			<%if(prescriptions.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No prescriptions on record</td>
			</tr>
			<%} else {
				for(PrescriptionBean prescription : prescriptions){%>
			<tr>
				<td style="text-align: center;"><%=prescription.getVisitID()%></td>
				<td style="text-align: center;"><%=prescription.getMedication().getNDCode()%></td>
				<td style="text-align: center;"><%=prescription.getStartDateStr()%></td>
				<td style="text-align: center;"><%=prescription.getEndDateStr()%></td>
				<td style="text-align: center;"><%=prescription.getDosage()%></td>
				<td style="text-align: center;"><%=prescription.getInstructions()%></td>
			</tr>			
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="4">Office Visits</th>
		</tr>
			<tr class="subHeader">
				<th>Visit Date</th>
				<th>HCP</th>
				<th>Hospital ID</th>
				<th>Notes</th>
			</tr>
	
			<%if(officeVisits.size()==0){ %>
			<tr>
				<td colspan="2" style="text-align: center;">No office visits on record</td>
			</tr>
			<%} else {
				for(OfficeVisitBean ov : officeVisits){
					personnel = personnelDAO.getPersonnel(ov.getHcpID());
%>
			<tr>
				<td style="text-align: center;"><%=ov.getVisitDateStr()%></td>
				<td style="text-align: center;"><%=personnel.getFullName()%></td>
				<td style="text-align: center;"><%=ov.getHospitalID()%></td>
				<td style="text-align: center;"><%=ov.getNotes()%></td>
			</tr>	
			<%  }
			  } %>
</table>
<br />
<table class="fTable">
		<tr>
			<th colspan="2">Known Relatives</th>
		</tr>
			<tr class="subHeader">
				<th>Name</th>
				<th>Relation</th>
			</tr>
			<%
			  if(family.size()==0){%>
				<tr>
					<td colspan="3" style="text-align: center;">No Relations on	record</td>
				</tr>
			  <%} else {
				  for(FamilyMemberBean member : family) {%>
			  	<tr>
					<td><%=member.getFullName()%></td>
					<td><%=member.getRelation()%></td>
				</tr>
			  <%  }
				}%>
</table>
<br />
<table class="fTable">
		<tr>
			<th>Patients <%=patient.getFirstName()%> is representing</th>
		</tr>
			<tr class="subHeader">
				<th>Patient</th>
			</tr>
			<%if(represented.size() ==0){ %>
			<tr>
				<td><%=patient.getFirstName()%> is not representing any patients</td>
			</tr>
			<%} else { 
				for(PatientBean p : represented){%>
				<tr>
					<td align=center>
						<%=p.getFullName()%>
					</td>
				</tr>
			 <% }
			  }%>
</table>
<br />
<table class="fTable">
		<tr>
			<th >Patients Representing <%=patient.getFirstName()%></th>
		</tr>
			<tr class="subHeader">
				<th>Patient</th>
			</tr>
			<%if(representing.size() ==0){ %>
			<tr>
				<td><%=patient.getFirstName()%> is not represented by any patients</td>
			</tr>
			<%} else { 
				for(PatientBean p : representing){%>
				<tr>
					<td align=center>
						<%=p.getFullName()%>
					</td>
				</tr>
			 <% }
			  }%>
</table>
<br />
<br />

<%} catch (Exception ex) {
	%><%=ex.getClass()+", " %><%=ex.getCause()+", " %><%=ex.getMessage() %><%
}
%>

<%@include file="/footer.jsp" %>
