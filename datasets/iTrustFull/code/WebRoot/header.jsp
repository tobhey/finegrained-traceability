<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf' %>

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
		<link href="/iTrust/css/main2.css" type="text/css" rel="stylesheet" />
		<link href="/iTrust/css/datepicker.css" type="text/css" rel="stylesheet" />
		<script src="/iTrust/js/DatePicker.js" type="text/javascript"></script>
	<!-- ADDED FOR RICH TEXT -->	<script type="text/javascript" src="/iTrust/ckeditor/ckeditor_basic.js"></script>
	<!-- ADDED FOR RICH TEXT -->	<script src="/iTrust/ckeditor/_samples/sample.js" type="text/javascript"></script>
	<!-- ADDED FOR RICH TEXT --> 	<link href="/iTrust/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css" />
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
		<script type="text/javascript">
			<!--
			
			function startup() {
				var h = document.getElementById('m').offsetHeight;
				document.getElementById('l').style.minHeight=h+'px';
				document.getElementById('r').style.minHeight=h+'px';
			}
			-->
		</script>
	</head>
	<body onload="startup()">
		<div class="iTrustHeader">
			<img style="float: left;" src="/iTrust/image/new/title.png" alt="iTrust Logo" />
			<div class="iTrustNav">
				<div style="float: left; width: 20%;">
						<a class="iTrustNavlink" href="/iTrust">Home</a>
						&nbsp;&nbsp;&nbsp;
	<%
				if( validSession ) {
					
					if(    (loggedInMID != null)
						&& (loggedInMID.longValue() != 0L) ) //if no one is logged in
					{
						if(userRole != "tester") { //if it's a tester
	%>					
							<a class="iTrustNavlink"
							   href="/iTrust/auth/<%=userRole %>/information.jsp"
							   rel="facebox">Information</a>
	<%
						} //end tester section
						
	%>
				</div>
				<div style="float: right; width: 20%; text-align: right; margin-right: 20px;">
					<% out.println(userName); %>
					| <a class="iTrustNavlink" href="/iTrust/logout.jsp">Logout</a>
	<%				} //no one is logged in
				}	  //valid session
	%>
				</div>
				<div style="clear: both;">
				</div>
			</div>
		</div>	

		<div class="iTrustMain">
			<div class="iTrustMenu" id="iTrustMenu" style="margin-left: -2px">
				<img id="menuPic" src="/iTrust/image/new/menu.png"  />
				<img src="/iTrust/image/new/menu_top.png"  />
				<div class="iTrustMenuContents" style="margin-top: -4px">
<%						if (  validSession ) {
						if (    (loggedInMID != null)
						     && (loggedInMID.longValue() != 0L)) //someone is logged in
						{
							if (userRole.equals("patient")) {
								%><%@include file="/auth/patient/menu.jsp"%><%
							}
							else if (userRole.equals("uap")) {
								%><%@include file="/auth/uap/menu.jsp"%><%
							}
							else if (userRole.equals("hcp")) {
								%><%@include file="/auth/hcp/menu.jsp"%><%
							}
							else if (userRole.equals("er")) {
								%><%@include file="/auth/er/menu.jsp"%><%
							}
							else if (userRole.equals("pha")) {
								%><%@include file="/auth/pha/menu.jsp"%><%
							}
							else if (userRole.equals("admin")) {
								%><%@include file="/auth/admin/menu.jsp"%><%
							}
							else if (userRole.equals("tester")) {
								%><%@include file="/auth/tester/menu.jsp"%><%
							}
						} //no one is logged in	
						else {
							String uri = request.getRequestURI();
							if( uri.indexOf("privacyPolicy.jsp") >= 0) { //looking at privacy policy, include logout menu.
	%>
								<%@include file="logoutMenu.jsp"%>
	<%
							} else {									//we are actually logged out entirely, show login menu
	%>
								<%@include file="loginMenu.jsp"%>		
	<%
							} //else
						}   //else
					} //if valid session
					else {
	%>
						<%@include file="/logoutMenu.jsp"%>
	<%
					}
%>
				</div>	
				<img src="/iTrust/image/new/menu_bottom.png"  />	
			</div>
			<div class="iTrustPage" style="padding-left: 180px;">
				<div class="leftBorder" id="l"></div>
				<div class="iTrustContent" id="m">
<%
	if(errorMessage != null) {
%>	
					<div style="text-align: center; width: 100%; background-color: black;">
						<span style="color: red; font-size: 28px; font-weight: bold;"><%=errorMessage %></span>
					</div>
<% 
	}
%>