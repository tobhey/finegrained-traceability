<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.DrugInteractionAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Edit ND Code Interactions";
%>

<%@include file="/header.jsp" %>

<%
	
	String headerMessage = "Select ND Codes with Interaction";
	String drug1 = "";
	String drug2 = "";
	if(request.getParameter("drug1") != null && request.getParameter("drug2") != null) {
		drug1 = request.getParameter("drug1").split(" ")[0];
		drug1 = drug1.replace("-", "");
		drug2 = request.getParameter("drug2").split(" ")[0];
		drug2 = drug2.replace("-", "");
	}
	
	if (request.getParameter("add") != null) {
		try {
			DrugInteractionAction interaction = new DrugInteractionAction(prodDAO, loggedInMID.longValue());
			headerMessage = interaction.reportInteraction(drug1, drug2, request.getParameter("description"));
		} catch(iTrustException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
		} catch(FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
		}
		
	}
			
	String headerColor = (headerMessage.indexOf("Error") > -1)
			? "#ffcccc"
			: "#00CCCC";
%>

<br />
<div align=center>
<form name="mainForm" method="post">
<span class="iTrustMessage"><%=headerMessage %></span>

<br />
<br />


<table cellpadding="10">
<tr><td>
<select name="drug1">
	<%
		List<MedicationBean> medList = prodDAO.getNDCodesDAO().getAllNDCodes();
		String tempCode = "";
		String tempDescrip = "";
		String escapedDescrip = "";
		for (MedicationBean medEntry : medList) {
			tempCode = medEntry.getNDCode();
			tempDescrip = medEntry.getDescription();
			escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
	%>

			<option><%=5 > tempCode.length() ? tempCode : tempCode.substring(0, 5)
				%>-<%=5 > tempCode.length() ? "" : tempCode.substring(5) %>
				<%=tempDescrip %>
			</option>

	<% } %>
	
</select></td><td>
<select name="drug2">
	<%
		for (MedicationBean medEntry : medList) {
			tempCode = medEntry.getNDCode();
			tempDescrip = medEntry.getDescription();
			escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
	%>

			<option><%=5 > tempCode.length() ? tempCode : tempCode.substring(0, 5)
				%>-<%=5 > tempCode.length() ? "" : tempCode.substring(5) %>
				<%=tempDescrip %>
			</option>

	<% } %>
	
</select>
</td></tr>
</table>
<b>Description: </b><input  type="text"
                                        id="description"
                                        name="description"
                                        size="40"
                                        maxlength="500" />


<br />
<br />
<input type="submit" name="add" value="Add Interaction">
<input type="button" onclick="location.href='/iTrust/auth/admin/editNDCodes.jsp'" value="Back" name="back" id="back"/>
</form>
</div>
<br />

<%@include file="/footer.jsp" %>
