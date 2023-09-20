<%@page import="edu.ncsu.csc.itrust.action.ResetPasswordAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.iTrustException"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Reset Password";
%>

<%@include file="/header.jsp"%>

<h1>Reset Password</h1>
<%
	ResetPasswordAction action = new ResetPasswordAction(prodDAO);
	if (action.isMaxedOut(request.getRemoteAddr())) {
		response.sendRedirect("/iTrust/auth/forward?error=Too+many+attempts!");
	}

	long mid = action.checkMID(request.getParameter("mid"));
	String role = null;

	try {
		role = action.checkRole(mid, request.getParameter("role"));
	} catch (iTrustException e) {
%>
<span >User does not exist with this role and mid.</span>
<%
	}
%>

<form action="/iTrust/util/resetPassword.jsp" method="post">
<table>
<%
	if (mid == 0 || role == null) {
%>
	<tr>
		<td colspan=2><b>Please enter your Role and MID</b></td>
	</tr>
	<tr>
		<td>Role:</td>
		<td>
			<select name="role">
				<option value="patient">Patient</option>
				<option value="hcp">HCP</option>
				<option value="uap">UAP</option>
				<option value="er">ER</option>
				<option value="pha">PHA</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>MID:</td>
		<td>
			<input type=TEXT maxlength=10 name="mid">
		</td>
	</tr>
	<tr>
		<td colspan=2 align=center>
			<input type="submit" value="Submit">
		</td>
	</tr>

<%
	} else {
		String answer = action.checkAnswerNull(request
				.getParameter("answer"));
		if (answer == null) {
			try {
%>
	<tr>
		<td colspan=2>
			<b><%=action.getSecurityQuestion(mid)%></b>
		</td>
	</tr>
	<tr>
		<td>Answer:</td>
		<td><input type=password maxlength=50 name="answer"> <input
			type=hidden name="mid" value="<%=mid%>"> <input type=hidden
			name="role" value="<%=role%>"></td>
	</tr>
	<tr>
		<td>New Password:</td>
		<td><input type=password maxlength=20 name="password"></td>
	</tr>
	<tr>
		<td>Confirm:</td>
		<td><input type=password maxlength=20 name="confirmPassword"></td>
	</tr>
	<tr>
		<td colspan=2 align=center><input type="submit" value="Submit"></td>
	</tr>

<%
			} catch (iTrustException e) {
%>
	<tr>
		<td>
			<font color='red'>This user has not set a security question/answer.</font>
		</td>
	</tr>
<%
			}
			
		} else {
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirmPassword");
			String confirm = "";
			try {
				confirm = action.resetPassword(mid, role, answer, 
				                               password, confirmPassword, 
				                               request.getRemoteAddr());
			} catch (FormValidationException e) {
				e.printHTML(pageContext.getOut());
%>
	<tr>
		<td>
			<a href="resetPassword.jsp">
				<h2>Please try again</h2>
			</a>
		</td>
	</tr>
<%
			}
%>
	<tr>
		<td>
			<%=confirm%>
		</td>
	</tr>

<%
		}
	}
%>
</table>
</form>

<%@include file="/footer.jsp" %>

