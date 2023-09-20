<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.AdverseEventBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewAdverseEventAction"%>
<%@page import="edu.ncsu.csc.itrust.charts.AdverseEventsData"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Adverse Event Chart";
%>

<%@include file="/header.jsp" %>

<%
// New instance of ViewAdverseEventAction class
ViewAdverseEventAction action = new ViewAdverseEventAction(prodDAO);

// Retrieve code identifier from URL
String code = (String)request.getParameter("code");

// Retrieve list of Adverse Event beans with this code, but only ones that have not been removed by the PHA
List<AdverseEventBean> adEvents = action.getUnremovedAdverseEventsByCode(code);

// Get the meaningful descriptive name for the prescription or immunization code
String name = action.getNameForCode(code);
%>

<!-- Use this tag to specify the location of the dataset for the chart -->
<jsp:useBean id="adverseEvents" class="edu.ncsu.csc.itrust.charts.AdverseEventsData"/>

<%
// This calls the class from the useBean tag and initializes the Adverse Event list and pres/immu name
adverseEvents.setAdverseEventsList(adEvents, name);
%>

<!-- The cewolf:chart tage defines attributes related to the chart you wish to generate -->
<cewolf:chart
     id="events"
     title="Adverse Events by Month"
     type="verticalbar"
     xaxislabel="Month"
     yaxislabel="Reported Adverse Events">
	<cewolf:data>
	       <cewolf:producer id="adverseEvents"/>
	</cewolf:data>
</cewolf:chart>

<!-- The cewolf:img tag defines the actual chart in your JSP page -->
<cewolf:img chartid="events" renderer="/charts/" width="600" height="400"/>


<%@include file="/footer.jsp" %>
