<%@page import="edu.ncsu.csc.itrust.datagenerators.TestDataGenerator"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<html>
<head>
<title>FOR TESTING PURPOSES ONLY</title>
<style type="text/css">
.message{
	border-style: solid;
	border-width: 1px;
	background-color: #AAFFAA;
}
</style>
</head>
<body>
<div align=center>
<h1>Test Utilities</h1>
<%if(System.getProperty("itrust.dev.home")==null){ %> 
<b>You are not configured to use this test utility.</b><br />
To use this, you must add the following line to Tomcat's startup: <br />
-Ditrust.dev.home="&lt;your workspace&gt;\iTrust"<br /><br />
For example, if you're using Eclipse WTP and your project is located at "e:\workspace\iTrust", do the following: <br />
<div style="width:500px; text-align: left">
<ol>
	<li>Under the "Servers" view, double-click on your Tomcat server.</li>
	<li>Click on "Open Launch Configuration"</li>
	<li>Go to the "Arguments" tab</li>
	<li>Under "VM arguments", add the following line of text <b>-Ditrust.dev.home="e:\workspace\iTrust"</b></li>
	<li>Restart your server for the change to take effect</li>	
</ol>
</div>
You should only have to do this once per development location.<%
} else {
%> You specified your project
developement location to be at <b><%=System.getProperty("itrust.dev.home")%></b><br />
<br />

<%
TestDataGenerator gen = new TestDataGenerator(System.getProperty("itrust.dev.home"), DAOFactory.getProductionInstance());
String methodName = request.getParameter("execute");
if (methodName != null) {
	gen.getClass().getMethod(methodName, new Class[]{}).invoke(gen, new Object[]{});
	%><span >Method <%=methodName%> invoked - see console for details</span><%
}%>

<h2>Execute the following test data generators</h2>
<%
for (Method method : gen.getClass().getDeclaredMethods()) {%> 
	<a href="andystestutil.jsp?execute=<%=method.getName()%>"><%=method.getName()%></a><br />
<%
}
}
%>
<br /><br />
<h1><a href="/iTrust">Back to iTrust</a></h1>
</div>
</body>
</html>