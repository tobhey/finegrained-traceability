<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="edu.ncsu.csc.itrust.beans.SurveyResultBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewSurveyResultAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.HospitalsDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Survey Results";
loginMessage = "";
session.removeAttribute("personnelList");
%>

<%@include file="/header.jsp" %>

<div align="center">
	<h2>Search HCP Survey Results</h2>
<%
	ViewSurveyResultAction action = new ViewSurveyResultAction(prodDAO, loggedInMID.longValue());
	HospitalsDAO hospitalDAO = new HospitalsDAO(prodDAO);
	List<HospitalBean> hcpHospitals = hospitalDAO.getAllHospitals();
	SurveyResultBean bean = null;
	String zipCode = "";
	String hospitalID = "";
	String specialty = "";
	List<SurveyResultBean> resultList = null;

	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");

	if (formIsFilled) {
		bean = new BeanBuilder<SurveyResultBean>().build(request
				.getParameterMap(), new SurveyResultBean());

		try {
			//set data to be sent from input text box and drop down box
			zipCode = request.getParameter("hcpZip");
			hospitalID = request.getParameter("hcpHospitalID");
			specialty = request.getParameter("hcpSpecialty");

			//do not submit if zip is not filled in
			if((!zipCode.equals("") && zipCode != null) && (!hospitalID.equals("") && hospitalID != null)) {
				%><span >Data for both Zip Code and Hospital ID is not allowed.  Please choose either Zip or Hospital ID.</span><%
			}
			else if (!zipCode.equals("") && zipCode != null) {
				bean.setHCPzip(zipCode);
				bean.setHCPspecialty(specialty);
				resultList = action.getSurveyResultsForZip(bean);
			} else if (!hospitalID.equals("") && hospitalID != null) {
				bean.setHCPhospital(hospitalID);
				bean.setHCPspecialty(specialty);
				resultList = action.getSurveyResultsForHospital(bean);
			} else {
				%><span >You must enter either a zip code or a hospital ID.</span><%
			}
		} catch(FormValidationException e){
			e.printHTML(pageContext.getOut());
		}
	}
%>


<form action="surveyResults.jsp" method="post"><input type="hidden" name="formIsFilled" value="true"> <br />
<div style="width: 50%; text-align:left;">
This search allows you to search for a HCP based on a zip code or hospital ID.  To search, you must enter either a zip code OR a hospital ID, but not both.
<br />Note: Search by zip code returns all HCPs that have zip codes with a match of the first 3 digits.
<br />
<br />
</div>
<table>
	<tr>
		<td>Zip Code:</td>
		<td><input type="text" name="hcpZip" maxlength="10" size="10" value="<%=zipCode%>"></td>
		<td> - OR -</td>
		<td>Hospital ID:</td>
		<td><select name="hcpHospitalID">
				<option value="">N/A</option>
				<%for(HospitalBean hos : hcpHospitals) {%>
					<option value="<%=hos.getHospitalID()%>"<%if (hos.getHospitalID().equals(hospitalID)) {%> selected="selected"<%}%>><%=hos.getHospitalID()%></option>
				<%} %>				
			</select>
		</td>
	</tr>
	<tr>
	<td colspan="5" align="center">Specialty (optional):</td>
	</tr>
	<tr>
		<td colspan="5" align="center"><select name="hcpSpecialty">
			<option value="<%=SurveyResultBean.ANY_SPECIALTY %>"<%if (specialty.equals(SurveyResultBean.ANY_SPECIALTY)) {%> selected="selected"<%}%>>-- Any Specialty --</option>
			<option value="<%=SurveyResultBean.GENERAL_SPECIALTY %>"<%if (specialty.equals(SurveyResultBean.GENERAL_SPECIALTY)) {%> selected="selected"<%}%>><%=SurveyResultBean.GENERAL_SPECIALTY%></option>
			<option value="<%=SurveyResultBean.HEART_SPECIALTY %>"<%if (specialty.equals(SurveyResultBean.HEART_SPECIALTY)) {%> selected="selected"<%}%>><%=SurveyResultBean.HEART_SPECIALTY%></option>
			<option value="<%=SurveyResultBean.OBGYN_SPECIALTY %>"<%if (specialty.equals(SurveyResultBean.OBGYN_SPECIALTY)) {%> selected="selected"<%}%>><%=SurveyResultBean.OBGYN_SPECIALTY%></option>
			<option value="<%=SurveyResultBean.PEDIATRICIAN_SPECIALTY %>"<%if (specialty.equals(SurveyResultBean.PEDIATRICIAN_SPECIALTY)) {%> selected="selected"<%}%>><%=SurveyResultBean.PEDIATRICIAN_SPECIALTY%></option>
			<option value="<%=SurveyResultBean.SURGEON_SPECIALTY %>"<%if (specialty.equals(SurveyResultBean.SURGEON_SPECIALTY)) {%> selected="selected"<%}%>><%=SurveyResultBean.SURGEON_SPECIALTY%></option>
			</select>
		</td>
	</tr>
	<tr>
 	<tr>
		<td colspan=5 align=center><input type="submit"
			style="font-size: 16pt; font-weight: bold;" value="Search"></td>
	</tr>
</table>
</form>

<%
	if (resultList != null) {
%>
<br />

<h2>Survey Results</h2>
<%
	if (resultList.size() == 0) {
%>
		<span class="iTrustError">No HCPs were found that meet your search criteria.  Please change your search criteria and search again.
		</span>
<%
	} else {
%>
<table  class="fTable">
	<tr>
		<th colspan="14">LHCP Search Results:</th>

	</tr>
	<tr class="subHeader">
		<th>Doctor's Name</th>
		<th>Address 1</th>
		<th>Address 2</th>
		<th>City</th>
		<th>State</th>
		<th>Zip</th>
		<th>Specialty</th>
		<th>Hospital</th>
		<th>Waiting Room<br />Minutes</th>
		<th>Exam Room<br />Minutes</th>
		<th>Visit<br />Satisfaction</th>
		<th>Treatment<br />Satisfaction</th>
		<th>Percent<br />Results</th>
	</tr>
	<%
		List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
		int index = 0;
		for (SurveyResultBean surveyResult : resultList) {
	%>
	<tr>
		<td align=center >
		<a href="/iTrust/auth/viewPersonnel.jsp?personnel=<%=index%>"><%=surveyResult.getHCPFirstName()%> <%=surveyResult.getHCPLastName()%></a></td>
		<td align=center ><%=surveyResult.getHCPaddress1()%></td>
		<td align=center ><%=surveyResult.getHCPaddress2()%></td>
		<td align=center ><%=surveyResult.getHCPcity()%></td>
		<td align=center ><%=surveyResult.getHCPstate()%></td>
		<td align=center ><%=surveyResult.getHCPzip()%></td>
		<td align=center ><%=surveyResult.getHCPspecialty()%></td>
		<td align=center ><%=surveyResult.getHCPhospital()%></td>
		<td align=center ><%=String.format("%.2f", surveyResult.getAvgWaitingRoomMinutes())%></td>
		<td align=center ><%=String.format("%.2f", surveyResult.getAvgExamRoomMinutes())%></td>
		<td align=center ><%=String.format("%.2f", surveyResult.getAvgVisitSatisfaction())%></td>
		<td align=center ><%=String.format("%.2f", surveyResult.getAvgTreatmentSatisfaction())%></td>
		<td align=center ><%=String.format("%.0f", surveyResult.getPercentSatisfactionResults())%>%</td>
	</tr>
<%
		PersonnelBean personnel = new PersonnelDAO(prodDAO).getPersonnel(surveyResult.getHCPMID());
		personnelList.add(personnel);
		index++;
		}
	session.setAttribute("personnelList", personnelList);
%>
</table>
<table>
<tr><td>
<i>The search results show the the averages for each HCP and include:</i> 
<ol><li>Number of minutes patients typically wait in a waiting room
<li>Average number of minutes patients wait in the examination room prior to seeing a physician
<li>Average office visit satisfaction
<li>Average satisfaction with treatment/information
<li>Percent of office visits for which satisfaction information is available</ol>
</td></tr></table>
<% } %>

<% }  %>
 <br />
</div>

<%@include file="/footer.jsp" %>
