<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Edit Patient";
%>

<%@include file="/header.jsp"%>

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		pidString = (String) session.getAttribute("editPid");
		if (pidString == null || 1 > pidString.length()) {
			response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/editPatient.jsp");
			return;
		}
	}
	else {
		session.removeAttribute("pid");
		session.setAttribute("editPid", pidString);
	}

	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	EditPatientAction action = new EditPatientAction(prodDAO,
			loggedInMID.longValue(), pidString);

	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
	PatientBean p;
	if (formIsFilled) {
		p = new BeanBuilder<PatientBean>().build(request
				.getParameterMap(), new PatientBean());
		try {
			action.updateInformation(p);
%>
	<br />
	<div align=center>
		<span class="iTrustMessage">Information Successfully Updated</span>
	</div>
	<br />
<%
	} catch (FormValidationException e) {
%>
	<br />
	<div align=center>
		<span class="iTrustError"><%=e.getMessage() %></span>
	</div>
	<br />
<%
		}
	} else {
		p = action.getPatient();
	}
%>

<form action="editPatient.jsp" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<table cellspacing=0 align=center cellpadding=0>
	<tr>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">First Name:</td>
				<td><input name="firstName" value="<%=p.getFirstName()%>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Last Name:</td>
				<td><input name="lastName" value="<%=p.getLastName()%>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Email:</td>
				<td><input name="email" value="<%=p.getEmail()%>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Address:</td>
				<td><input name="streetAddress1"
					value="<%=p.getStreetAddress1()%>" type="text"><br />
				<input name="streetAddress2" value="<%=p.getStreetAddress2()%>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">City:</td>
				<td><input name="city" value="<%=p.getCity()%>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">State:</td>
				<td><itrust:state name="state" value="<%=p.getState()%>" /></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Zip:</td>
				<td><input name="zip1" value="<%=p.getZip1()%>" maxlength="5"
					type="text" size="5"> - <input name="zip2"
					value="<%=p.getZip2()%>" maxlength="4" type="text" size="4">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td><input name="phone1" value="<%=p.getPhone1()%>" type="text"
					size="3" maxlength="3"> - <input name="phone2"
					value="<%=p.getPhone2()%>" type="text" size="3" maxlength="3">
				- <input name="phone3" value="<%=p.getPhone3()%>" type="text"
					size="4" maxlength="4"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Mother MIDs:</td>
				<td><input name="motherMID" value="<%=p.getMotherMID()%>"
					maxlength="10" type="text"></td>
			</tr>

			<tr>
				<td class="subHeaderVertical">Father MID:</td>
				<td><input name="fatherMID" value="<%=p.getFatherMID()%>"
					maxlength="10" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Credit Card Type:</td>
				<td><select name="creditCardType">
				<option value="">Select:</option>
				<option value="MASTERCARD" <%= p.getCreditCardType().equals("MASTERCARD") ? "selected" : "" %>>Mastercard</option>
				<option value="VISA" <%= p.getCreditCardType().equals("VISA") ? "selected" : "" %>>Visa</option>
				<option value="DISCOVER" <%= p.getCreditCardType().equals("DISCOVER") ? "selected" : "" %>>Discover</option>
				<option value="AMEX" <%= p.getCreditCardType().equals("AMEX") ? "selected" : "" %>>American Express</option>
				</select>
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Credit Card Number:</td>
				<td><input name="creditCardNumber" value="<%=p.getCreditCardNumber()%>"
					maxlength="19" type="text"></td>
			</tr>
		</table>
		<br />
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Insurance Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td><input name="icName" value="<%=p.getIcName()%>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Address:</td>
				<td><input name="icAddress1" value="<%=p.getIcAddress1()%>"
					type="text"><br />
				<input name="icAddress2" value="<%=p.getIcAddress2()%>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">City:</td>
				<td><input name="icCity" value="<%=p.getIcCity()%>" type="text">
				</td>
			</tr>

			<tr>
				<td class="subHeaderVertical">State:</td>
				<td><itrust:state name="icState" value="<%=p.getIcState()%>" />
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Zip:</td>
				<td><input name="icZip1" value="<%=p.getIcZip1()%>"
					maxlength="5" type="text" size="5"> - <input name="icZip2"
					value="<%=p.getIcZip2()%>" maxlength="4" type="text" size="4">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td><input name="icPhone1" value="<%=p.getIcPhone1()%>"
					type="text" size="3" maxlength="3"> - <input
					name="icPhone2" value="<%=p.getIcPhone2()%>" type="text" size="3"
					maxlength="3"> - <input name="icPhone3"
					value="<%=p.getIcPhone3()%>" type="text" size="4" maxlength="4">

				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Insurance ID:</td>
				<td><input name="icID" value="<%=p.getIcID()%>" type="text">
				</td>
			</tr>
		</table>
		</td>
		<td width="15px">&nbsp;</td>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Emergency Contact</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td><input name="emergencyName"
					value="<%=p.getEmergencyName()%>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td><input name="emergencyPhone1"
					value="<%=p.getEmergencyPhone1()%>" type="text" size="3"
					maxlength="3"> - <input name="emergencyPhone2"
					value="<%=p.getEmergencyPhone2()%>" type="text" size="3"
					maxlength="3"> - <input name="emergencyPhone3"
					value="<%=p.getEmergencyPhone3()%>" type="text" size="4"
					maxlength="4"></td>
			</tr>
		</table>
		<br />
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Health Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Ethnicity:</td>
				<td><select name="ethnicityStr">
					<%
						String selected = "";
						for (Ethnicity eth : Ethnicity.values()) {
							selected = (eth.equals(p.getEthnicity())) ? "selected=selected"
									: "";
					%>
					<option value="<%=eth.getName()%>" <%=selected%>><%=eth.getName()%></option>
					<%
						}
					%>
				</select></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Blood Type:</td>
				<td><select name="bloodTypeStr">
					<%
						for (BloodType bt : BloodType.values()) {
							selected = (bt.equals(p.getBloodType())) ? "selected=selected"
									: "";
					%>
					<option value="<%=bt.getName()%>" <%=selected%>><%=bt.getName()%></option>
					<%
						}
					%>
				</select>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender:</td>
				<td><select name="genderStr">
					<%
						for (Gender g : Gender.values()) {
							selected = (g.equals(p.getGender())) ? "selected=selected" : "";
					%>
					<option value="<%=g.getName()%>" <%=selected%>><%=g.getName()%></option>
					<%
						}
					%>
				</select></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Date Of Birth:</td>
				<td><input type=text name="dateOfBirthStr" maxlength="10"
					size="10" value="<%=p.getDateOfBirthStr()%>"> <input
					type=button value="Select Date"
					onclick="displayDatePicker('dateOfBirthStr');"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Date Of Death:</td>
				<td><input type=text name="dateOfDeathStr" maxlength="10"
					size="10" value="<%=p.getDateOfDeathStr()%>"> <input
					type=button value="Select Date"
					onclick="displayDatePicker('dateOfDeathStr');"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Cause of Death:</td>
				<td><select name="causeOfDeath" style="font-size: 10">
					<option value="">-- None Selected --</option>
<% 
					for (DiagnosisBean diag : prodDAO.getICDCodesDAO().getAllICDCodes()){
						String select = "";
						if (diag.getICDCode().equals(p.getCauseOfDeath()))
							select = "selected=\"selected\"";
%>
					<option <%=select%> value="<%=diag.getICDCode()%>"><%=diag.getICDCode() %>
					- <jsp:expression>diag.getDescription()</jsp:expression></option>
<%					
			}
%>
				</select></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Topical Notes:</td>
				<td><textarea name="topicalNotes"><%=p.getTopicalNotes()%></textarea>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br />
<div align=center>
	<input type="submit" name="action"
		style="font-size: 16pt; font-weight: bold;" value="Edit Patient Record"><br /><br />
	<span style="font-size: 14px;">
		Note: in order to set the password for this user, use the "Reset Password" link at the login page.
	</span>
</div>
</form>
<br />
<br />
<itrust:patientNav thisTitle="Demographics" />

<%@include file="/footer.jsp"%>
