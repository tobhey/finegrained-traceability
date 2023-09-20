<%@page errorPage="/auth/exceptionHandler.jsp" %>

<!-- imports -->
<%@page import="edu.ncsu.csc.itrust.action.ReferralManagementAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReferralBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.enums.Role"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<!-- header -->
<%@include file="/global.jsp" %>
<%pageTitle = "iTrust - HCP Consultations";%>
<%@include file="/header.jsp" %>

<%
String task = request.getParameter("task");
%>


<%if (task == null || task.equalsIgnoreCase("")) {%>
	<center>
		<h1>HCP Consultations</h1>
		<form action='hcpConsultation.jsp' method=post>
			<input type='submit' name='task' value='Send a Consultation'>
			<input type='submit' name='task' value='View Pending Consultations'>
		</form>
	</center>
	<br />
	
	
<%} else if (task.equalsIgnoreCase("Send a Consultation")) {%>
	<%
	// Find all patients associated with this hcp
	PatientDAO patDAO = new PatientDAO(prodDAO);
	List<PatientBean> allPatients = patDAO.getAllPatients();
	List<PatientBean> myPatients = new ArrayList<PatientBean>();
	for (PatientBean pat : allPatients) {
		if (patDAO.checkDeclaredHCP(pat.getMID(), loggedInMID)) {
			myPatients.add(pat);
		}
	}
	
	// Find all hcps who are not this hcp
	PersonnelDAO personnel = new PersonnelDAO(prodDAO);
	List<PersonnelBean> allPersonnel = personnel.getAllPersonnel();
	List<PersonnelBean> allHCPs = new ArrayList<PersonnelBean>();
	for (PersonnelBean per : allPersonnel) {
		if (per.getRole() == Role.HCP && per.getMID() != loggedInMID) {
			allHCPs.add(per);
		}
	}
	%>
	<center>
		<h1>Send a Consultation</h1>
		<form action='hcpConsultation.jsp' method=post>
		<input type='hidden' name='task' value='form'>
		<table>
			<tr>
				<td>Select Patient:</td>
				<td>
					<select size=1 name='patient'>
					<%for (PatientBean pat : myPatients) {%>
						<option value='<%=pat.getMID()%>'><%=pat.getFullName() %></option>
					<%}%>
					</select>
				</td>
			</tr>
			<tr>
				<td>Select HCP</td>
				<td>
					<select size=1 name='hcp'>
					<%for (PersonnelBean per : allHCPs) { %>
						<option value='<%=per.getMID()%>'><%=per.getFullName() %></option>
					<%} %>
					</select>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type='submit' value='Select'></td>
			</tr>
		</table>
		</form>
	</center>
	
	
<%} else if (task.equalsIgnoreCase("View Pending Consultations")) {%>
	<%
	ReferralManagementAction refAction = new ReferralManagementAction(prodDAO, loggedInMID);
	List<ReferralBean> refsFromMe = refAction.getReferralsSentFromMe();
	List<ReferralBean> refsToMe = refAction.getReferralsSentToMe();
	PatientDAO patientDAO = new PatientDAO(prodDAO);
	PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
	%>
	<center>
		<h1>View Pending Consultations</h1>
	</center>
	<table align=center border=1 class="fTable">
		<tr>
			<th colspan=5>Outgoing Consultations</th>
		</tr>
		<tr class="subHeader">
			<td>Status</td>
			<td>Patient</td>
			<td>Sending Doctor</td>
			<td>Receiving Doctor</td>
			<td>Edit</td>
		</tr>
		<%if (refsFromMe.isEmpty()) {%>
		<tr>
			<td colspan=5><center>No Outgoing Consultation Requests</center></td>
		</tr>
		<%}%>
		<%for (ReferralBean ref : refsFromMe) {%>
			<tr>
				<td>
				<%=
					(ref.getStatus() == ReferralBean.ReferralStatus.Pending) ? 
							("Pending") : 
							( (ref.getStatus() == ReferralBean.ReferralStatus.Finished) ? 
									("Finished") : 
									("Declined"))
				%>
				</td>
				<td><%=patientDAO.getPatient(ref.getPatientID()).getFullName()%> (<%=ref.getPatientID()%>)</td>
				<td><%=personnelDAO.getPersonnel(ref.getSenderID()).getFullName()%> (<%=ref.getSenderID()%>)</td>
				<td><%=personnelDAO.getPersonnel(ref.getReceiverID()).getFullName()%> (<%=ref.getReceiverID()%>)</td>
				<td><a href='hcpConsultation.jsp?task=update&toFrom=from&id=<%=ref.getId()%>'>edit</a></td>
			</tr>
		<%}%>
	</table>
	<br />
	<br />
		<table align=center border=1 class="fTable">
		<tr>
			<th colspan=5>Incoming Consultations</th>
		</tr>
		<tr class="subHeader">
			<td>Status</td>
			<td>Patient</td>
			<td>Sending Doctor</td>
			<td>Receiving Doctor</td>
			<td>Edit</td>
		</tr>
		<%if (refsToMe.isEmpty()) {%>
		<tr>
			<td colspan=5><center>No Incoming Consultation Requests</center></td>
		</tr>
		<%}%>
		<%for (ReferralBean ref : refsToMe) {%>
			<tr>
				<td>
				<%=
					(ref.getStatus() == ReferralBean.ReferralStatus.Pending) ? 
							("Pending") : 
							( (ref.getStatus() == ReferralBean.ReferralStatus.Finished) ? 
									("Finished") : 
									("Declined"))
				%>
				</td>
				<td><%=patientDAO.getPatient(ref.getPatientID()).getFullName()%> (<%=ref.getPatientID()%>)</td>
				<td><%=personnelDAO.getPersonnel(ref.getSenderID()).getFullName()%> (<%=ref.getSenderID()%>)</td>
				<td><%=personnelDAO.getPersonnel(ref.getReceiverID()).getFullName()%> (<%=ref.getReceiverID()%>)</td>
				<td><a href='hcpConsultation.jsp?task=update&toFrom=to&id=<%=ref.getId()%>'>edit</a></td>
			</tr>
		<%}%>
	</table>
	<br />
	
	
<%} else if (task.equalsIgnoreCase("update")) { %>
	<%
	String toFrom = request.getParameter("toFrom");
	String id = request.getParameter("id");
	// Test for parameter errors:
	// 	empty parameters or options
	if (toFrom == null || 
		toFrom.equalsIgnoreCase("") || 
		id == null || 
		id.equalsIgnoreCase("") || 
		!(toFrom.equalsIgnoreCase("to") || toFrom.equalsIgnoreCase("from"))) {%>
		<center>
		<h1>ERROR: Missing important parameters. <a href='hcpConsultation.jsp'>Try Again</a></h1>
		</center>
	<%}
	ReferralManagementAction refAction = new ReferralManagementAction(prodDAO, loggedInMID);
	long rid = Long.parseLong(id);
	List<ReferralBean> referrals = null;
	if (toFrom.equalsIgnoreCase("to")) {
		referrals = refAction.getReferralsSentToMe();
	} else {
		referrals = refAction.getReferralsSentFromMe();
	}
	ReferralBean myRef = null;
	for (ReferralBean ref : referrals) {
		if (ref.getId() == rid) {
			myRef = ref;
			break;
		}
	}
	if (myRef == null) {%>
		<center>
		<h1>ERROR: Referral does not exist. <a href='hcpConsultation.jsp'>Choose another</a></h1>
		</center>
	<%}%>
	<center>
	<h1>Update Consultation Form:</h1><br />
	</center>
	<table border=10 bordercolor=darkred align=center><tr><td>
	<table border='0' width='400'>
		<tr>
			<td>Patient:</td>
			<td><%=(new PatientDAO(prodDAO)).getPatient(myRef.getPatientID()).getFullName()%></td>
		</tr>
		<tr>
			<td>Sending HCP:</td>
			<td><%=(new PersonnelDAO(prodDAO)).getPersonnel(myRef.getSenderID()).getFullName()%></td>
		</tr>
		<tr>
			<td>Receiving HCP:</td>
			<td><%=(new PersonnelDAO(prodDAO)).getPersonnel(myRef.getReceiverID()).getFullName()%></td>
		</tr>
	</table>
	<form action='hcpConsultation.jsp' method=post>
	<input type='hidden' name='task' value='change'>
	<input type='hidden' name='patID' value='<%=myRef.getPatientID()%>'>
	<input type='hidden' name='toID' value='<%=myRef.getReceiverID()%>'>
	<input type='hidden' name='fromID' value='<%=myRef.getSenderID()%>'>
	<input type='hidden' name='refID' value='<%=myRef.getId()%>'>
	<%if (toFrom.equalsIgnoreCase("to")) {%>
	Referral Details:<br />
	<textarea name="refDetails" readonly rows="5" cols="48" style="background-color: lightgrey"><%=myRef.getReferralDetails()%></textarea><br />
	Consultation Details:<br />
	<textarea name="consDetails" rows="5" cols="48"><%=myRef.getConsultationDetails()%></textarea><br />
	<%} else { %>
	Referral Details:<br />
	<textarea name="refDetails" rows="5" cols="48"><%=myRef.getReferralDetails()%></textarea><br />
	Consultation Details:<br />
	<textarea name="consDetails" rows="5" cols="48" readonly style="background-color: lightgrey"><%=myRef.getConsultationDetails()%></textarea><br />
	<%} %>
	<select size=1 name='status'>
		<option <%=(myRef.getStatus() == ReferralBean.ReferralStatus.Pending) ? "selected='selected'" : "" %>>Pending</option>
		<option <%=(myRef.getStatus() == ReferralBean.ReferralStatus.Finished) ? "selected='selected'" : "" %>>Finished</option>
		<option <%=(myRef.getStatus() == ReferralBean.ReferralStatus.Declined) ? "selected='selected'" : "" %>>Declined</option>
	</select>
	</td></tr></table>
	<center>
	<input type='submit' value='Update'>
	</center>
	</form>
	

<%} else if (task.equalsIgnoreCase("change")) {%>
	<%
	try {
		long patID = Long.parseLong(request.getParameter("patID"));
		long toID = Long.parseLong(request.getParameter("toID"));
		long fromID = Long.parseLong(request.getParameter("fromID"));
		long refID = Long.parseLong(request.getParameter("refID"));
		String consDetails = request.getParameter("consDetails");
		String refDetails = request.getParameter("refDetails");
		String status = request.getParameter("status");
		
		ReferralBean myRef = new ReferralBean();
		myRef.setId(refID);
		myRef.setPatientID(patID);
		myRef.setReceiverID(toID);
		myRef.setSenderID(fromID);
		myRef.setConsultationDetails(consDetails);
		myRef.setReferralDetails(refDetails);
		myRef.setStatus(
			status.equalsIgnoreCase("Pending") ? 
					ReferralBean.ReferralStatus.Pending :
					status.equalsIgnoreCase("Finished") ?
							ReferralBean.ReferralStatus.Finished :
							ReferralBean.ReferralStatus.Declined
		);
		
		ReferralManagementAction refAction = new ReferralManagementAction(prodDAO, loggedInMID);
		refAction.updateReferral(myRef);
		%>
		<center>
		<h1>Consultation updated</h1><br />
		</center>
		<table border=10 bordercolor=darkgreen align=center><tr><td>
		<table border='0' width='400'>
			<tr>
				<td>Patient:</td>
				<td><%=(new PatientDAO(prodDAO)).getPatient(myRef.getPatientID()).getFullName()%></td>
			</tr>
			<tr>
				<td>Sending HCP:</td>
				<td><%=(new PersonnelDAO(prodDAO)).getPersonnel(myRef.getSenderID()).getFullName()%></td>
			</tr>
			<tr>
				<td>Receiving HCP:</td>
				<td><%=(new PersonnelDAO(prodDAO)).getPersonnel(myRef.getReceiverID()).getFullName()%></td>
			</tr>
		</table>
		Referral Details:<br />
		<textarea name="refDetails" readonly rows="5" cols="48" style="background-color: lightgrey"><%=myRef.getReferralDetails()%></textarea><br />
		Consultation Details:<br />
		<textarea name="consDetails" readonly rows="5" cols="48" style="background-color: lightgrey"><%=myRef.getConsultationDetails()%></textarea><br />
		</td></tr></table>
		<br />
		
		
		
	<%} catch (Exception e) {%>
		<center>
		<h1>ERROR: Referral couldn't be updated</h1>
		</center>
	<%} %>
	
	
<%} else if (task.equalsIgnoreCase("form")) {%>
	<%
	String patient = request.getParameter("patient");
	String hcp = request.getParameter("hcp");
	PatientBean pat = (new PatientDAO(prodDAO)).getPatient(Long.parseLong(patient));
	PersonnelBean per = (new PersonnelDAO(prodDAO)).getPersonnel(Long.parseLong(hcp));
	%>
	<center>
	<h1>Consultation Form:</h1>
	<form action='hcpConsultation.jsp' method=post>
	<input type='hidden' name='task' value='send'>
	<input type='hidden' name='patient' value='<%=patient%>'>
	<input type='hidden' name='hcp' value='<%=hcp%>'>
	<table border=10 bordercolor=darkred><tr><td>
	<table border='0' width='400'>
		<tr>
			<td>Refer Patient:</td>
			<td><%=pat.getFullName()%> (<%=patient%>)</td>
		</tr>
		<tr>
			<td>To HCP:</td>
			<td><%=per.getFullName()%> (<%=hcp%>)</td>
		</tr>
	</table>
	<textarea name="msg" rows="5" cols="48">Details</textarea><br />
	</td></tr></table>
	<input type='submit' value='Submit'>
	</form>
	</center>
	
	
<%} else if (task.equalsIgnoreCase("send")) {
	String patient = request.getParameter("patient");
	String hcp = request.getParameter("hcp");
	String msg = request.getParameter("msg");
	
	PatientBean pat = (new PatientDAO(prodDAO)).getPatient(Long.parseLong(patient));
	PersonnelBean per = (new PersonnelDAO(prodDAO)).getPersonnel(Long.parseLong(hcp));
	
	ReferralManagementAction refAction = new ReferralManagementAction(prodDAO, loggedInMID);
	
	// Create a new ReferralBean and load all the information into it.
	ReferralBean newRef = new ReferralBean();
	newRef.setReferralDetails(msg);
	newRef.setConsultationDetails("");
	newRef.setPatientID(Long.parseLong(patient));
	newRef.setReceiverID(Long.parseLong(hcp));
	newRef.setSenderID(loggedInMID);
	newRef.setStatus(ReferralBean.ReferralStatus.Pending);
	
	try {
		refAction.sendReferral(newRef);%>
		<center>
		<h1>Thank you, your Consultation Request was sent.</h1>
		<table border=10 bordercolor=darkgreen><tr><td>
		<table border='0' width='400'>
			<tr>
				<td>Refer Patient:</td>
				<td><%=pat.getFullName()%> (<%=patient%>)</td>
			</tr>
			<tr>
				<td>To HCP:</td>
				<td><%=per.getFullName()%> (<%=hcp%>)</td>
			</tr>
		</table>
		<textarea readonly name="msg" rows="5" cols="48" style="background-color: lightgrey"><%=msg %></textarea><br />
		</td></tr></table>
		</center>
		<br />
	<%} catch (DBException e) {%>
		<center><h1>ERROR: The referral couldn't be sent. <%=e.toString() %></h1></center>
	<%}%>
	
	
<%} %>
<%@include file="/footer.jsp" %>