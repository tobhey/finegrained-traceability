
<div class="menu_category">
	<form method="post" action="/iTrust/j_security_check">
<%
	if(loginMessage != null) {
%>
	<div style="align: center; margin-bottom: 10px;">
		<span class="iTrustError" style="font-size: 16px;"><%=loginMessage%></span>
	</div>
<%
	}
%>
	<span>MID</span><br />
	<input type="text" maxlength="10" name="j_username"><br />
	<span>Password</span><br />
	<input type="password" maxlength="20" name="j_password"><br /><br />
	
	<input type="submit" value="Login"><br /><br />

	<a style="font-size: 80%;" href="/iTrust/util/resetPassword.jsp">Reset Password</a>

	</form>

<%
if( ! "true".equals(System.getProperty("itrust.production") ) ) { 
%>
	<!-- This section is for testing purposes only!! -->
	  <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=1&j_password=pw">Patient 1</a>
	| <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=2&j_password=pw">Patient 2</a><br />
	  <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=9000000000&j_password=pw">HCP</a>
	| <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=8000000009&j_password=uappass1">UAP</a>
	| <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=9000000006&j_password=pw">ER</a><br />
	  <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=7000000001&j_password=pw">PHA</a>
	| <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=9000000001&j_password=pw">Admin</a>
	| <a class="iTrustTestNavlink" href="/iTrust/j_security_check?j_username=9999999999&j_password=pw">Tester</a>
	
<% 
} 
%>
</div>
<script type="text/javascript">
	document.forms[0].j_username.focus();
</script>
