<%@ page import="java.net.URLEncoder" %>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.GetUserNameAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>

<html>
<head>
  <title>iTrust - Find User</title>
  <style type="text/css">
.getUserBody {
	background-color: White; 
	border: solid 2px #37609f; 
	margin: 0px;
	color: #37609f;
	white-space: nowrap;
	width: 446px;
	height: 146px;
	overflow: hidden;
}
.getUserTitle {
	width: 100%; 
	text-align: center; 
	background-color:#11213b; 
	color: White; 
	font-weight: bold;
	font-size: 11pt;
	padding: 2px 0px 2px 0px;
}
.getUserTitleClose {
	float: right; 
	display: block; 
	position: absolute; 
	top: 4px;
	left: 410px;  
}
.getUserCloseLink {
	font-size: 12px;
	color: Red;
}
.getUserCloseLink:hover {
	text-decoration: none;
	color: White;
}
.getUserSearchButton {
	color: White; 
	font-weight: bold;
	width: 180px; 
	background-color: #11213b; 
	border: solid 2px #37609f;
}
.getUserSearchButton:hover {
	background-color: #44546e; 
}
.getUserName {
	font-size: 14pt;
	color: Black;
	font-weight: bold;
}
.getUserCorrect {
	border: 1px solid #66ff66; 
	background-color: #eeffee;
	color: #11aa11;
	font-weight: bold;
}
.getUserCorrect:hover {
	border: 1px solid #44ee44;
	background-color: #bbffbb;
}
.getUserIncorrect {
	border: 1px solid #ff6666; 
	background-color: #ffeeee;
	color: #aa1111;
	font-weight: bold;
}
.getUserIncorrect:hover {
	border: 1px solid #ee4444;
	background-color: #ffbbbb;
}
.resultTable tr td {
	padding: 0px 3px 0px 3px;
}
  </style>
</head>
<body class="getUserBody">
<div class="getUserTitle">
	iTrust User Search <div class="getUserTitleClose"><a class="getUserCloseLink" href="javascript:void(0);" onclick="parent.getUserClose();">Close</a></div>
</div>
	<form method="post">
<% 
	String mid = request.getParameter("mid");
	String paramS = request.getParameter("s");
	if(mid != null && mid.trim() != "") {
		try {
				String userName = new GetUserNameAction(DAOFactory.getProductionInstance()).getUserName(mid);
				String action = "";
				if(paramS != null) 
					action = "parent.setUser('" + paramS +"','" + mid + "','" + userName + "');";
				%>
				<table align="center">
					<tr>
						<td style="text-align: center; white-space: nowrap;">
							Found User: <span class="getUserName"><br /><%=userName%></span><br /><br />
							Please confirm that this is the user you wish to select.<br /><br style="line-height: 6px;" />
							<input type="button" name="correct" value="This user is correct" class="getUserCorrect" onclick="<%=action %>" />  							
							<input type="submit" name="incorrect" value="Find another user" class="getUserIncorrect" />&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				<%
			} catch (iTrustException e) {
				response.sendRedirect("getUser.jsp?s=" + paramS + "&error=" + URLEncoder.encode(e.getMessage(),"UTF-8") );
			}
	} else { %>
		<table align="center">
			<tr>
				<td colspan="2" style="text-align: center; padding-bottom: 10px;">
					<b>Please enter a User ID to search for.</b>
				</td>
			</tr>
			<tr>
				<td style="text-align: right; font-weight: bold; color: black;">User ID (MID):</td>
				<td><input name="mid" maxlength="10" type="text" autocomplete="off" /></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center; padding-top: 2px;">
					<input type="submit" value="Search for User" class="getUserSearchButton">
				</td>
			</tr>
		<% if(mid == "") { %>
			<tr>
				<td colspan="2" style="text-align: center; font-weight: bold; color: Red;">
					Please enter an MID
				</td>
			</tr>
		<% } else if(request.getParameter("error") != null) { %>
			<tr>
				<td colspan="2" style="text-align: center; font-weight: bold; color: Red;">
					<%=request.getParameter("error") %>
				</td>
			</tr>
		<% } %>
		</table>
  <% }%>		
	</form>
  </body>
</html>