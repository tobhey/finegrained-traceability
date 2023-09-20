<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.SetSecurityQuestionAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.SecurityQA"%>
<%@page import="org.apache.commons.validator.CreditCardValidator"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Edit Patient";
%>

<%@include file="/header.jsp"%>

<%
	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	EditPatientAction action = new EditPatientAction(prodDAO,
			loggedInMID.longValue(), "" + loggedInMID.longValue());
	SetSecurityQuestionAction saction = new SetSecurityQuestionAction(
			prodDAO, loggedInMID.longValue());
	long pid = action.getPid();

	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
	PatientBean p;
	SecurityQA s;
	if (formIsFilled) {
		p = new BeanBuilder<PatientBean>().build(request
				.getParameterMap(), new PatientBean());
		s = new BeanBuilder<SecurityQA>().build(request
				.getParameterMap(), new SecurityQA());
		try {
			action.updateInformation(p);
			saction.updateInformation(s);
			
%>
		<div align=center>
			<span class="iTrustMessage">Information Successfully Updated</span>
		</div>
<%
	} catch (FormValidationException e) {
%>
		<div align=center>
			<span class="iTrustError"><%=e.getMessage() %></span>
		</div>
<%
		p = action.getPatient();
		}
	} else {
		p = action.getPatient();
		s = saction.retrieveInformation();
	}
%>

<form action="editMyDemographics.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true"> 
<input type="hidden" name="pid" value="<%=pid%>"> 
<br />
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
				<td><input name="email" value="<%=p.getEmail()%>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Address:</td>
				<td><input name="streetAddress1"
					value="<%=p.getStreetAddress1()%>" type="text"><br />
				<input name="streetAddress2" value="<%=p.getStreetAddress2()%>"
					type="text"></td>
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
				<td class="subHeaderVertical">Mother MID:</td>
				<td><input name="MotherMID" value="<%=p.getMotherMID()%>"
					maxlength="10" type="text"></td>
			</tr>

			<tr>
				<td class="subHeaderVertical">Father MID:</td>
				<td><input name="FatherMID" value="<%=p.getFatherMID()%>"
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
		</td>
		<td width="15px">&nbsp;</td>
		<td valign=top>
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
	</tr>
	<tr height="15px">
		<td colspan=3>&nbsp;</td>
	</tr>
	<tr>
		<td valign="top">
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Authentication Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Security Question:</td>
				<td><input name="question" value="<%=s.getQuestion()%>"
					type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Security Answer:</td>
				<td><input name="answer" value="<%=s.getAnswer()%>"
					type="password"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Confirm Security Answer:</td>
				<td><input name="confirmAnswer" value="<%=s.getAnswer()%>"
					type="password"></td>
			</tr>
		</table>
		</td>
		<td width="15px">&nbsp;</td>
		<td valign="top">
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
		</td>
	</tr>
</table>
<br />
<div align="center"><input type="submit" name="action"
	style="font-size: 16pt; font-weight: bold;" value="Edit Patient Record">
<br />
<br />
Note: in order to set your password, use the "Reset Password" link at
the login page.</div>
</form>

<%@include file="/footer.jsp"%>
