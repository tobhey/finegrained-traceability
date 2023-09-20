<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DrugInteractionBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateNDCodeListAction"%>
<%@page import="edu.ncsu.csc.itrust.action.DrugInteractionAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain ND Codes";
%>

<%@include file="/header.jsp" %>

<%
	UpdateNDCodeListAction ndUpdater = new UpdateNDCodeListAction(prodDAO, loggedInMID.longValue());
	DrugInteractionAction interactionAction = new DrugInteractionAction(prodDAO, loggedInMID.longValue());
	
	String headerMessage = "Viewing Current ND Codes";
	String code1 = request.getParameter("code1") != null
			? request.getParameter("code1").trim()
			: "";
	String code2 = request.getParameter("code2") != null
			? request.getParameter("code2").trim()
			: "";
	String code = code1 + code2;
	
	if (request.getParameter("add") != null || request.getParameter("update") != null || request.getParameter("delete") != null) {
		try {
			if(request.getParameter("add") != null || request.getParameter("update") != null) {
				MedicationBean med =
					new MedicationBean(code, request.getParameter("description"));
				headerMessage = (request.getParameter("add") != null)
						? ndUpdater.addNDCode(med)
						: ndUpdater.updateInformation(med);
			} else {
				if(request.getParameter("codeToDelete") != null) {
					interactionAction.deleteInteraction(code, request.getParameter("codeToDelete").trim());
					headerMessage = "Interaction deleted successfully";
				} else
					headerMessage = "Interaction does not exist";
			}
		} catch(FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
			headerMessage = "Validation Errors";
		} catch(iTrustException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=e.getMessage() %></span>
			</div>
<%
			headerMessage = "Validation Errors";
		}
		
	}
			
	String headerColor = (headerMessage.indexOf("Error") > -1)
			? "#ffcccc"
			: "#00CCCC";
%>

<br />
<div align=center>
<form name="mainForm" method="post">
<input type="hidden" id="codeToDelete" name="codeToDelete" value="">
<input type="hidden" id="updateID" name="updateID" value="">
<input type="hidden" id="oldDescrip" name="oldDescrip" value="">
<script type="text/javascript">
	function fillUpdate(code) {
		document.getElementById("code1").value = code.substring(0,5);
		document.getElementById("code2").value = code.substring(5);
		document.getElementById("description").value
			= unescape(document.getElementById("UPD" + code).value);
		document.getElementById("oldDescrip").value
			= unescape(document.getElementById("UPD" + code).value);
		document.getElementById("interactions").innerHTML = "";
		document.getElementById("intDesc").innerHTML = "";
		drugs = document.getElementById("INTDRUG" + code).value.split("\n");
		descs = document.getElementById("INTDESC" + code).value.split("\n");
		for(d in drugs) {
			if(d == drugs.length - 1) break;			
			intLink = document.createElement("a");
			intLink.href = "javascript:void(0)";
			intLink.id = "drugInteraction" + d; 
			
			if(d == 0) {
				addIntDesc(drugs[d], descs[d]);
			}
			
			if(document.all) {
				intLink.attachEvent("onclick", addIntDescIE);
				//Strip newline off end of drug code
				drugs[d] = drugs[d].substring(0, drugs[d].length-1);
			} else {
				intLink.setAttribute("onclick", "addIntDesc('" + drugs[d] + "', '" + descs[d] + "');");
			}

			var intText = drugs[d];	
			if(document.getElementById("UPD" + intText).value != "" && document.getElementById("UPD" + intText).value != "undefined") {
				intText = intText + " " + unescape(document.getElementById("UPD" + drugs[d]).value);
			}
			intText = intText.substring(0,5) + "-" + intText.substring(5);
			
			intLinkText = document.createTextNode(intText);
			intLink.appendChild(intLinkText);
			document.getElementById("interactions").appendChild(intLink);
			document.getElementById("interactions").appendChild(document.createElement('br'));
			
		}
		if(drugs.length <= 1) {
			document.getElementById("interactions").innerHTML = "No Interactions";
			if(document.getElementById("delete").style != null) {
				document.getElementById("delete").style.visibility = "hidden";
			}
		}
			
		
	}

	function addIntDescIE() {
		linkID = event.srcElement.id;
		linkID = linkID.substring(15); //Get number after "drugInteraction"
		linkNumber = eval(linkID);
		addIntDesc(drugs[linkNumber], descs[linkNumber]);
	}

	function addIntDesc(code, desc) {
		document.getElementById("intDesc").innerHTML = desc;
		document.getElementById("delete").style.visibility = ""; 
		document.getElementById("codeToDelete").value = code;
	}
</script>


<span class="iTrustMessage"><%=headerMessage %></span>

<br />
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="3">Update ND Code List</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Description</th>
	</tr>
	<tr>
		<td style="padding-right: 10px;">
			<input  type="text"
					id="code1"
					name="code1"
					size="5"
					maxlength="5"
			/>-<input type="text"
					id="code2"
					name="code2"
					size="4"
					maxlength="4"/>
		</td>
		<td>
			<input  type="text"
					id="description"
					name="description"
					size="40"
					maxlength="50" />
		</td>
	</tr>
	<tr class="subHeader">
		<th>Interaction</th>
		<th>Description</th>
	</tr>
	<tr>
		<td style="padding-right: 10px;" id="interactions">

		</td>
		<td>
			<div id="intDesc"></div>
			<div align="center"><input type="submit" value="Delete Interaction" name="delete" id="delete" style="visibility: hidden"/></div>
		</td>	
	</tr>
</table>
<br />
<input type="submit" name="add" value="Add Code" />
<input type="submit" name="update" value="Update Code" />

<br />
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="2">Current NDCs</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Description</th>
	</tr>
	<%
		List<MedicationBean> medList = prodDAO.getNDCodesDAO().getAllNDCodes();
		List<DrugInteractionBean> interactionList;
		String tempCode = "";
		String tempDescrip = "";
		String escapedDescrip = "";
		String intDrugsString = "";
		String intDescsString = "";
		
		for (MedicationBean medEntry : medList) {
			tempCode = medEntry.getNDCode();
			tempDescrip = medEntry.getDescription();
			escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
			interactionList = interactionAction.getInteractions(tempCode);
			intDrugsString = "";
			intDescsString = "";
			for(DrugInteractionBean b : interactionList) {
				if(tempCode.equals(b.getFirstDrug())) intDrugsString += b.getSecondDrug() + "\n";
				if(tempCode.equals(b.getSecondDrug())) intDrugsString += b.getFirstDrug() + "\n";
				intDescsString += b.getDescription() + "\n";
			}
			//intDrugsString = URLEncoder.encode(intDrugsString, "UTF-8").replaceAll("\\+", "%20");
			//intDescsString = URLEncoder.encode(intDescsString, "UTF-8").replaceAll("\\+", "%20");
	%>
		<tr>
			<td><%=5 > tempCode.length() ? tempCode : tempCode.substring(0, 5)
				%>-<%=5 > tempCode.length() ? "" : tempCode.substring(5) %>
			</td>
			<td><a href="javascript:void(0)"
					onclick="fillUpdate('<%=tempCode %>')"
						><%=tempDescrip %></a>
				<input type="hidden"
						id="UPD<%=tempCode %>"
						name="UPD<%=tempCode %>"
						value="<%=escapedDescrip %>">
				<input type="hidden"
						id="INTDRUG<%=tempCode %>"
						name="INTDRUG<%=tempCode %>"
						value="<%=intDrugsString %>">
				<input type="hidden"
						id="INTDESC<%=tempCode %>"
						name="INTDESC<%=tempCode %>"
						value="<%=intDescsString %>">
			</td>
		</tr>
	<% } %>
</table>
</form>
<br />
<br />
<a href="editNDCInteractions.jsp">Edit Interactions</a>
</div>
<br />

<%@include file="/footer.jsp" %>
