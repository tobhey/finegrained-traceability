<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@include file="/authenticate.jsp" %>

<%
	if(validSession) {
		errorMessage = (String) session.getAttribute("errorMessage");
		session.removeAttribute("errorMessage");
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title><%=pageTitle %></title>
		<link href="/iTrust/css/main.css" type="text/css" rel="stylesheet" />
		<link href="/iTrust/css/datepicker.css" type="text/css" rel="stylesheet" />
		<script src="/iTrust/js/DatePicker.js" type="text/javascript"></script>

		<script src="/iTrust/js/jquery-1.2.6.js" type="text/javascript"></script>
		<link href="/iTrust/css/facebox/facebox.css" media="screen" rel="stylesheet" type="text/css"/>
		<script src="/iTrust/js/facebox/facebox.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			jQuery(document).ready(function($) {
				$('a[rel*=facebox]').facebox()
			});
			$.facebox.settings.loading_image = '/iTrust/image/facebox/loading.gif';
			$.facebox.settings.close_image   = '/iTrust/image/facebox/closelabel.gif';
		</script>
	</head>
	<body>
