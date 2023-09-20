<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.GenerateCalendarAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Calendar"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Appointment Calendar";
%>

<%@include file="/header.jsp" %>

<%
	GenerateCalendarAction action = new GenerateCalendarAction(prodDAO, loggedInMID.longValue());
	List<ApptBean> send;
	boolean conflicts[];
	Hashtable<Integer, ArrayList<ApptBean>> list;
	Calendar a = Calendar.getInstance();
	
	//Calendar Stuff
	String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	String weekDays[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	Calendar cal = Calendar.getInstance();
	int origDay = cal.get(Calendar.DAY_OF_MONTH);
	int origMonth = cal.get(Calendar.MONTH);
	int origYear = cal.get(Calendar.YEAR);
	cal.set(Calendar.DAY_OF_MONTH, 1);
	
	int c_month = cal.get(Calendar.MONTH);
	
	//Change Month from JSP
	if(request.getParameter("c") != null) {
		String cur = request.getParameter("c");
		try {
			c_month = Integer.parseInt(cur);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("calendar.jsp");
		}
	}
	
	int month = c_month;
	if(request.getParameter("m") != null) {
		String m = request.getParameter("m");
		int move = 0;
		try {
			move = Integer.parseInt(m);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("calendar.jsp");
		}
		month = c_month + move;
	}
	//Line to set month directly
	cal.set(Calendar.MONTH, month);
	int thisMonth = cal.get(Calendar.MONTH);
	int thisYear = cal.get(Calendar.YEAR);
	//Get First Day of Month
	cal.set(Calendar.DAY_OF_MONTH, 1);
	int firstDayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
	//Get Last Day and Week of Month
	cal.add(Calendar.MONTH, 1);
    cal.add(Calendar.DAY_OF_MONTH, -1);
	int lastDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
	int lastWeekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
	
	//Compile Appointments for this month
	list = action.getApptsTable(thisMonth, thisYear);
	send = action.getSend();
	conflicts = action.getConflicts();
	
%>
<div align="center">
<table width="90%">
	<tr>
		<td style="text-align: left;">
			<a href="calendar.jsp?c=<%= month %>&m=-1">Last</a>	
		</td>
		<td style="text-align: center;">
			<h3><%= "Appointments for "+months[thisMonth]+" "+thisYear %></h3>
		</td>
		<td style="text-align: right;">
			<a href="calendar.jsp?c=<%= month %>&m=1">Next</a>
		</td>
	</tr>
</table>
<table id="calendarTable" style="clear: both;">
	<%
		out.print("<tr>\n");
		for(String d : weekDays) {
			out.print("<th>"+d+"</th>");
		}
		out.print("</tr>\n");
		int calDay = 1 + Calendar.SUNDAY - firstDayOfMonth;
		int p = 0;
		for(int i=0; i<lastWeekOfMonth; i++) {
			out.print("<tr>\n");
			for(int j=0; j<7; j++) {
				if(calDay == origDay && thisMonth == origMonth && thisYear == origYear)
					out.print("<td class=\"today\"><div class=\"cell\">");
				else
					out.print("<td><div class=\"cell\">");
				if(calDay > 0 && calDay <= lastDayOfMonth) out.print("<div style=\"float: right;\">"+calDay+"</div>");
				else out.print("<div class=\"blankDay\"></div>");
				if(list.containsKey(calDay)) {
					ArrayList<ApptBean> l = list.get(calDay);
					for(ApptBean b : l) {	
						a.setTimeInMillis(b.getDate().getTime());
						//String t = a.get(Calendar.HOUR)+":"+((a.get(Calendar.MINUTE)<10)? "0"+a.get(Calendar.MINUTE): a.get(Calendar.MINUTE))+" "+((a.get(Calendar.AM_PM) == 0) ? "AM" : "PM");
						out.print("<div class=\"calendarEntry "+(conflicts[p]?"conflict":"")+" \">"+b.getApptType()+"<br /><a name=\""+b.getApptType()+"-"+calDay+"\" style=\"font-size: 12px;\" href=\"viewAppt.jsp?apt="+ p++ +"\" >Read Details</a></div>");
					}
				}
				calDay++;
				out.print("</div></td>\n");
			}
			out.print("</tr>");
		}
		session.setAttribute("appts", send);
	%>
</table>
<br />
<br />
</div>
<%@include file="/footer.jsp" %>
