<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="edu.ncsu.csc.itrust.beans.SurveyBean"%>
<%@page import="edu.ncsu.csc.itrust.action.SurveyAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Patient Survey";
%>

<%@include file="/header.jsp"%>

<%
SurveyAction action = new SurveyAction(prodDAO, loggedInMID.longValue());
SurveyBean surveyBean = null;
long visitID = 0;
//get office visit ID from previous JSP
String visitIDStr = request.getParameter("ovID");
String visitDateStr = request.getParameter("ovDate");


if(visitIDStr != null && !visitIDStr.equals("")) {
	try {
		visitID = Long.parseLong(visitIDStr);

	} catch(Exception e) {
		e.printStackTrace();
	}
}

boolean formIsFilled = request.getParameter("formIsFilled") != null
&& request.getParameter("formIsFilled").equals("true");

if(formIsFilled) {
	surveyBean = new BeanBuilder<SurveyBean>().build(request.getParameterMap(), new SurveyBean());
	surveyBean.setVisitID(visitID);
	
	String waitingMinutes = request.getParameter("waitingMinutesString");
	if (waitingMinutes != null && !waitingMinutes.equals(""))
		surveyBean.setWaitingRoomMinutes(Integer.parseInt(waitingMinutes));
	
	String examMinutes = request.getParameter("examMinutesString");
	if (examMinutes != null && !examMinutes.equals(""))
		surveyBean.setExamRoomMinutes(Integer.parseInt(examMinutes));
	
	//update satisfaction number in bean
	if (request.getParameter("Satradios") != null) {
		if (request.getParameter("Satradios").equals("satRadio5")) {
			surveyBean.setVisitSatisfaction(5);
		} if (request.getParameter("Satradios").equals("satRadio4")) {
			surveyBean.setVisitSatisfaction(4);
		} if (request.getParameter("Satradios").equals("satRadio3")) {
			surveyBean.setVisitSatisfaction(3);
		} if (request.getParameter("Satradios").equals("satRadio2")) {
			surveyBean.setVisitSatisfaction(2);
		} if (request.getParameter("Satradios").equals("satRadio1")) {
			surveyBean.setVisitSatisfaction(1);
		}
	}

    //update treatment number in bean
	if (request.getParameter("Treradios") != null) {
		if (request.getParameter("Treradios").equals("treRadio5")) {
			surveyBean.setTreatmentSatisfaction(5);
		} if (request.getParameter("Treradios").equals("treRadio4")) {
			surveyBean.setTreatmentSatisfaction(4);
		} if (request.getParameter("Treradios").equals("treRadio3")) {
			surveyBean.setTreatmentSatisfaction(3);
		} if (request.getParameter("Treradios").equals("treRadio2")) {
			surveyBean.setTreatmentSatisfaction(2);
		} if (request.getParameter("Treradios").equals("treRadio1")) {
			surveyBean.setTreatmentSatisfaction(1);
		}
	}
    
    try {
    	//add survey data
    	action.addSurvey(surveyBean, visitID);
		response.sendRedirect("viewMyRecords.jsp?message=Survey%20Successfully%20Submitted");
		
     } catch(Exception e) {
    	%><span ><%=e.getMessage()%></span><%
    }
} else{
	if(visitDateStr.contains("<")) throw new iTrustException("Illegal parameter for ovDate.");
}


	
%>
<div id=Header>
<h1>iTrust Patient Survey for Office Visit on <%=visitDateStr %></h1></div>
<div id=Content>

<form action="survey.jsp" method="post" name="mainForm">
<input type="hidden" name="formIsFilled" value="true"> 
<input type="hidden" name="ovID" value="<%=visitIDStr%>">
<h3>How long did you have to wait during your visit?</h3>
<table>
	<tr>
		<td>In the waiting room?</td>
		<td><input type="text" name="waitingMinutesString" maxlength =3 size=3 /> </td>
		<td>1-999 minutes</td>
	</tr>
	
	<tr>
		<td>In the examination room <br />before seeing your physician?</td>
		<td><input type="text" name="examMinutesString" maxlength =3 size=3 /> </td>
		<td>1-999 minutes</td>
	</tr>
</table>

<h3>How satisfied were you with your office visit?</h3>
<table><tr><td>
<tr><td><input align="left" type="radio" name="Satradios" value="satRadio5">
Very Satisfied (5) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Satradios" value="satRadio4">
Satisfied (4) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Satradios" value="satRadio3">
Moderately Satisfied (3) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Satradios" value="satRadio2">
Somewhat Unhappy (2) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Satradios" value="satRadio1">
Very Unhappy (1) <br /></td></tr>
</table>

<h3>How satisfied were you with the treatment or information you received?</h3>
<table><tr><td>
<tr><td><input align="left" type="radio" name="Treradios" value="treRadio5">
Very Satisfied (5) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Treradios" value="treRadio4">
Satisfied (4) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Treradios" value="treRadio3">
Moderately Satisfied (3) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Treradios" value="treRadio2">
Somewhat Unhappy (2) <br /></td></tr>
<tr><td><input align="left" type="radio" name="Treradios" value="treRadio1">
Very Unhappy (1) <br /></td></tr>
</table>
<br />
<br />

<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Submit Survey">
</form>
</div>

<%@include file="/footer.jsp"%>
