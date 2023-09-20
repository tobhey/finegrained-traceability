<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.SetSecurityQuestionAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.SecurityQA"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit Personnel";
%>

<%@include file="/header.jsp" %>

<%
	/* Require a Personnel ID first */
	String pidString= "" + loggedInMID.longValue();

	/* A bad personnel ID gets you exiled to the exception handler */
	EditPersonnelAction personnelEditor = new EditPersonnelAction(prodDAO,loggedInMID.longValue(), pidString);
	long pid  = personnelEditor.getPid();
	SetSecurityQuestionAction saction = new SetSecurityQuestionAction(prodDAO, loggedInMID.longValue());
	
	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled")!=null && request.getParameter("formIsFilled").equals("true");
	PersonnelBean personnelForm;
	SecurityQA sbean;
	if(formIsFilled){
		personnelForm = new BeanBuilder<PersonnelBean>().build(request.getParameterMap(),new PersonnelBean());
		sbean = new BeanBuilder<SecurityQA>().build(request.getParameterMap(),new SecurityQA());
		try {
			personnelEditor.updateInformation(personnelForm);
			saction.updateInformation(sbean);
%>
		<div align=center>
			<span class="iTrustMessage">Information Successfully Updated</span>
		</div>
<%
		} catch(FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
		}
	} else {
		personnelForm = prodDAO.getPersonnelDAO().getPersonnel(pid);
		sbean = saction.retrieveInformation();
	}
%>

<form action="editMyDemographics.jsp" method="post">
<input type="hidden" name="formIsFilled" value="true">
<input type="hidden" name="pid" value="<%=pid%>">
<br />
<div align=center>
	    <table class="fTable" align=center>
	      <tr><th colspan=2 >Personnel Information</th></tr>
	      <tr><td class="subHeaderVertical">
		  First Name:
		</td><td>
		  <input name="firstName"  value="<%=personnelForm.getFirstName()%>"  type="text">
	      </td></tr>
	      <tr ><td class="subHeaderVertical">
		 Last Name:
		</td><td>

		  <input name="lastName"  value="<%=personnelForm.getLastName()%>"  type="text">
	      </td></tr>
	      <tr><td class="subHeaderVertical">
		  Address:
		</td><td>

		  <input name="streetAddress1"  value="<%=personnelForm.getStreetAddress1()%>"  type="text"><br />
		  <input name="streetAddress2"  value="<%=personnelForm.getStreetAddress2()%>"  type="text">
	      </td></tr>
	      <tr ><td class="subHeaderVertical">
		  City:
		</td><td>
		  <input name="city"  value="<%=personnelForm.getCity()%>"  type="text">
	      </td></tr>
	      <tr ><td class="subHeaderVertical">

		  State:
		</td><td>
			<itrust:state name="state" value="<%=personnelForm.getState()%>"/>
	      </td></tr>
	      <tr ><td class="subHeaderVertical">
		  Zip:
		</td><td>
		  <input name="zip1" value="<%=personnelForm.getZip1()%>"  maxlength="5" type="text" size="5"> - 
		  <input name="zip2" value="<%=personnelForm.getZip2()%>"  maxlength="4" type="text" size="4">
	      </td></tr>
	      <tr ><td class="subHeaderVertical">

		  Phone:
		</td><td>
		  <input name="phone1"  value="<%=personnelForm.getPhone1()%>"  type="text" size="3" maxlength="3"> -
		  <input name="phone2"  value="<%=personnelForm.getPhone2()%>"  type="text" size="3" maxlength="3"> -
		  <input name="phone3"  value="<%=personnelForm.getPhone3()%>"  type="text" size="4" maxlength="4">
	      </td></tr>
		      <tr>
			    <th bgcolor=silver colspan=2>Authentication Information</th>
	          </tr>
		      <tr>
		      	<td class="subHeaderVertical">Security Question:</td>
		      	<td><input name="question"  value="<%=sbean.getQuestion()%>"  type="text"></td>
		      </tr>
		      <tr>
		      	<td class="subHeaderVertical">Security Answer:</td>
		      	<td><input name="answer"  value="<%=sbean.getAnswer()%>"  type="password"></td>
		      </tr>
		      <tr>
		      	<td class="subHeaderVertical">Confirm Security Answer:</td>
		      	<td><input name="confirmAnswer"  value="<%=sbean.getAnswer()%>"  type="password"></td>
		      </tr>
		    </table>
		  </td></tr>
		  <tr>
		  	<td colspan=2 align=center>
		  	<br />
      <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Edit Personnel Record">
		  	</td>
		  </tr>
      </table>
      <br />
</form>
<br />
<div align=center>
Note: in order to set the password for this user, use the "Reset Password" link at the login page.
</div>

<%@include file="/footer.jsp" %>
