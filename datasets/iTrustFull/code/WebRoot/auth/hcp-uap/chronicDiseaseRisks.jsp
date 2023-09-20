<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ChronicDiseaseRiskAction"%>
<%@page import="edu.ncsu.csc.itrust.risk.RiskChecker"%>
<%@page import="edu.ncsu.csc.itrust.risk.factors.PatientRiskFactor"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Risk Factors for Chronic Diseases";
%>

<%@include file="/header.jsp" %>

<%try{
/* Require a Patient ID first */
String pidString = (String)session.getAttribute("pid");
if (pidString == null || 1 > pidString.length()) {
	response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/chronicDiseaseRisks.jsp");
   	return;
}
//else {
//	session.removeAttribute("pid");
//}

ChronicDiseaseRiskAction action = new ChronicDiseaseRiskAction(prodDAO, loggedInMID.longValue(), pidString);
long pid = action.getPatientID();
%>

<table class="fTable" align="center">
	<tr>
		<th colspan="2" >Disease Risk Factors</th>
	</tr>
<%
	List<RiskChecker> diseases = action.getDiseasesAtRisk();
	if(diseases.size() > 0) {
		for(RiskChecker disease: diseases) {
%>
    <tr>
    	<td colspan="2" class="subHeaderVertical"><%=disease.getName()%></td>
    </tr>
      <tr>
	      <td style="padding-left: 10px">
<%
			List<PatientRiskFactor> factors = disease.getPatientRiskFactors();
			for(PatientRiskFactor factor : factors) {
%>
		        <%=factor.getDescription()%><br />
<%
			}
%>
	      </td>
      </tr>
<%
		}
	}
}
	catch(Exception e) {
%>
	<tr>
    	<td colspan="2" align=center>No Data</td>
    </tr>
<%
	}
%>
</table>

<br /><br /><br />
<itrust:patientNav thisTitle="Risk Factors" />


<%@include file="/footer.jsp" %>
