<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList" %>
<%@page import="edu.ncsu.csc.itrust.beans.Email" %>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>
<%@page import="edu.ncsu.csc.itrust.EmailUtil" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - HCP - send Email Notification";
%>

<%@include file="/header.jsp"%>




<%
PersonnelBean self = new PersonnelDAO(prodDAO).getPersonnel(loggedInMID);

boolean flag;
long mid;
if (request.getParameter("mid") != null && !request.getParameter("mid").equalsIgnoreCase("")) {
	mid = Long.parseLong(request.getParameter("mid"));
	flag = true;
} else {
	flag = false;
	mid = 0;
}

%>

<% 
if (flag) {
	PatientDAO patients = new PatientDAO(prodDAO);
	PatientBean myPatient = patients.getPatient(mid);
%>
<center>
<table border=10 bordercolor=darkred>
<tr><td>

	<form action="sendEmailNotification.jsp" method=post>
	<h3>Send Email Form:</h3>

	<table>
	<tr>
		<td>From: </td>
		<td><%=self.getFullName() %> </td>
	</tr>
	<tr>
		<td>To: </td>
		<td>
			<input type=text name="email" value="<%=myPatient.getEmail() %>"> (<%=myPatient.getFullName() %>)
		</td>
	</tr>
	<tr>
		<td>Subject:</td>
		<td>
			<input type=text value="Reminder" name="subject">
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<textarea name="thetext" rows="15" cols="43">Dear <%=myPatient.getFirstName() %>,</textarea>
		</td>
	</tr>
	</table>
	<br />

	<input type=hidden name="id" value="<%=myPatient.getMID() %>">
	<input type=submit value="Send Email">
	<input type=reset value="Reset">

	</form>

</td></tr>
</table>
</center>
<%
} else {
	if (request.getParameter("email") == null || request.getParameter("email").equals("")) {
		response.sendRedirect("/iTrust/auth/hcp/home.jsp");
	}
	
	String email = request.getParameter("email");
	String subject = request.getParameter("subject");
	String message = request.getParameter("thetext");
	long id = Long.parseLong(request.getParameter("id"));
	
	Email myEmail = new Email();
	
	List<String> toList = new ArrayList<String>();
	toList.add(email);
	myEmail.setToList(toList);
	
	myEmail.setFrom(self.getEmail());
	myEmail.setBody(message);
	myEmail.setSubject(subject);
	
	EmailUtil emailer = new EmailUtil(prodDAO);
	emailer.sendEmail(myEmail);
	
	PatientDAO patients = new PatientDAO(prodDAO);
	PatientBean myPatient = patients.getPatient(id);
%>
<center>
<table border=10 bordercolor=darkgreen>
<tr><td>

	<h3><font color="darkred">Your Email was sent:</font></h3>
	
	<table>
	<tr>
		<td>From: </td>
		<td><%=self.getFullName() %> </td>
	</tr>
	<tr>
		<td>To: </td>
		<td>
			<input type=text readonly value="<%=email %>" style="background-color: lightgrey"> (<%=myPatient.getFullName() %>)
		</td>
	</tr>
	<tr>
		<td>Subject:</td>
		<td>
			<input type=text readonly value="<%= subject %>" style="background-color: lightgrey">
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<textarea readonly name="thetext" rows="15" cols="43" style="background-color: lightgrey"><%=message %></textarea>
		</td>
	</tr>
	</table>
	<br />
	<form action="/iTrust/auth/hcp/home.jsp" method=get>
	<input type=submit value="Home">
	</form>

</td></tr>
</table>	
</center>
<%
}
%>
<%@include file="/footer.jsp" %>