<%@ page import="java.sql.*, edu.ncsu.csc.itrust.*, edu.ncsu.csc.itrust.users.*, edu.ncsu.csc.itrust.beans.*"  %>
<%@ page import="java.util.*, com.lowagie.text.html.HtmlEncoder" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <title>iTrust - Visit Trends</title>
	<%@ include file="/resources/head.jsp" %>
</head>
<body>

<%			

				
			Calendar today=Calendar.getInstance();
			int curMonth=today.get(Calendar.MONTH);
			int curYear=today.get(Calendar.YEAR);
				
			String startMonth = request.getParameter("startMonth");
			String startYear = request.getParameter("startYear");
			String endMonth = request.getParameter("endMonth");
			String endYear = request.getParameter("endYear");
			boolean isPostBack = request.getParameter("action") != null;
			
			String DateEndStr = endYear+"-"+endMonth;
			String DateStartStr = startYear+"-"+startMonth;
			String firstVisit=Records.getEarliestVisit();
if((null == firstVisit) || ("".equals(firstVisit))) {
	out.println("<br /><br />");
	out.println("<h3 style=\"text-align: center;\">There Are NO Visits Registered</h3>");
	out.println("<br /><br />");
}
else {
			String firstMonth=firstVisit.substring(5,7);
			String firstYear=firstVisit.substring(0,4);
			
			String[] months={"January", "February", "March", "April", "May",
							 "June", "July", "August", "September",
							  "October", "November", "December"};
			
%>			

<form method="post" id="formMain">
	<script type="text/javascript" src="Resources/Scripts/DatePicker.js"></script>

	<table align="center">
		<tr>
			<th colspan="2" style="background-color: Silver;">
				Visit Trends
			</th>
		</tr>
		<tr>
			<td colspan="2" style="text-align: center; border: solid 1px Silver;">
				Select a starting and ending month and click GenerateReport.		
			</td>
		</tr>
		<tr style="background-color:#ffcccc">
			<td style="text-align: right;">
				Starting Month:
			</td>
			<td>
				<select name="startMonth">
					<% out.println("<option value="+firstMonth+">"+months[Integer.parseInt(firstMonth)-1]+"</option>"); %>
					<option value="01">January</option>
					<option value="02">February</option>
					<option value="03">March</option>
					<option value="04">April</option>
					<option value="05">May</option>
					<option value="06">June</option>
					<option value="07">July</option>
					<option value="08">August</option>
					<option value="09">September</option>
					<option value="10">October</option>
					<option value="11">November</option>
					<option value="12">December</option>
				</select><br />
				<select name="startYear">
					<% out.println("<option value="+firstYear+">"+firstYear+"</option>"); %>
					<% for (int cnt=curYear;cnt+1>Integer.parseInt(firstYear);cnt--) 
						out.println("<option value="+cnt+">"+cnt+"</option>"); %>
				</select>
			</td>
			<tr style="background-color:#ffcccc">
			<td style="text-align: right;">
				Ending Month:
			</td>
			<td>
				<select name="endMonth">
					<% out.println("<option value="+(curMonth+1)+">"+months[curMonth]+"</option>"); %>
					<option value="01">January</option>
					<option value="02">February</option>
					<option value="03">March</option>
					<option value="04">April</option>
					<option value="05">May</option>
					<option value="06">June</option>
					<option value="07">July</option>
					<option value="08">August</option>
					<option value="09">September</option>
					<option value="10">October</option>
					<option value="11">November</option>
					<option value="12">December</option>
				</select><br />
				<select name="endYear">
					<% out.println("<option value="+curYear+">"+curYear+"</option>"); %>
					<% for (int cnt=curYear-1;cnt+1>Integer.parseInt(firstYear);cnt--) 
						out.println("<option value="+cnt+">"+cnt+"</option>"); %>
				</select>
			</td>
			
					<tr>
			<td align=center colspan=2>
				<input type="submit" name="action" value="Generate Report">
			</td>
		</tr>
		</table>
		
		<% if (startMonth!=null && startYear!=null && endMonth!=null && endYear!=null) {
		  	if (Integer.parseInt(startYear)>Integer.parseInt(endYear) || 
				(Integer.parseInt(startYear)==Integer.parseInt(endYear) && 
				 Integer.parseInt(startMonth)>Integer.parseInt(endMonth))){
		  				%><table align="center" style="background-color: #ffeeee; border: 1px solid #ff6666;">
						<tr><td align=center><b>An Error has occured:</b><br /><br />Start date is after End Date.</td></tr>
					</table><br /><%
		   }
		   else if (Integer.parseInt(endYear)>curYear || 
				(Integer.parseInt(endYear)==curYear && 
				 Integer.parseInt(endMonth)>curMonth+1)){
		  				%><table align="center" style="background-color: #ffeeee; border: 1px solid #ff6666;">
						<tr><td align=center><b>An Error has occured:</b><br /><br />End date is in the future.</td></tr>
					</table><br /><% 
		   }
		   else {
			if (startMonth.length()<2) startMonth="0"+startMonth;
			if (endMonth.length()<2) endMonth="0"+endMonth;
			VisitReport vr=null;
			try {
				vr=new VisitReport(startYear+"-"+startMonth, endYear+"-"+endMonth);
				vr.computeVisits();
			}
			catch (UserDataException e) {
				out.println("<table align=center bgcolor=red><tr><td><font color=white>");
				out.println("<b>An Error has Occured</b>: <br />");
				out.println(e.getErrorMessage());
				out.println("</font></td></tr></table>");
			}
			Vector<Integer> minors=vr.getMinors();
			Vector<Integer> adults=vr.getAdults();			
			
		int mostVisits;
		int leastVisits;
		
		if (minors.elementAt(0) < adults.elementAt(0)) {
			mostVisits=adults.elementAt(0);
			leastVisits=minors.elementAt(0);
		}
		else {
			mostVisits=minors.elementAt(0);
			leastVisits=adults.elementAt(0);
		}
		
		int NUM_DAYS=vr.getNumMonths();
		
		for ( int i = 1; i < NUM_DAYS; i++ )
		{
			if ( minors.elementAt(i) > mostVisits ) mostVisits = minors.elementAt(i);
			if ( minors.elementAt(i) < leastVisits ) leastVisits = minors.elementAt(i);
			if ( adults.elementAt(i) > mostVisits ) mostVisits = adults.elementAt(i);
			if ( adults.elementAt(i) < leastVisits ) leastVisits = adults.elementAt(i);

		}
		
			int GRAPH_WIDTH = 1100;
			int GRAPH_HEIGHT = 500;
			int GRAPH_VALUES = 10;
			if (GRAPH_VALUES>mostVisits) GRAPH_VALUES=mostVisits;
			if (GRAPH_VALUES==0) GRAPH_VALUES=1;
			int VALUE_HEIGHT = GRAPH_HEIGHT / GRAPH_VALUES;
			int DAY_SPACING = GRAPH_WIDTH / (NUM_DAYS*2);
			int DAY_WIDTH = DAY_SPACING / 2;
		
		int valueRange = mostVisits - leastVisits;
		
		// buffer the values
		mostVisits += (int)(valueRange*0.1f);
		leastVisits -= (int)(valueRange*0.1f);
		
		if ( leastVisits < 0 )
			leastVisits = 0;
			
		valueRange = mostVisits - leastVisits;
		
		double valueInc = (double) valueRange / (double) GRAPH_VALUES;
		
		 %>
		<br />
		<table align="center">
		
			<tr>
			
				<td align="right" valign="bottom">
					<i style="font-size:75%"># of visits</i>
				</td>
				<td align="center" valign="top">
					<b>Visits Per Month<br /><br /></b>
				</td>
			
			</tr>
			
			<tr>
			
				<!-- value legend -->
				<td valign="top">
				
					<div style="position: relative">
				
					<%
					int val;
					for ( int i = 0; i <= GRAPH_VALUES; i++ )
					{
						val = (int) (valueInc * i);
						int top = ( GRAPH_VALUES - i ) * VALUE_HEIGHT;
						%>
						
						<div style="position: absolute; text-align: right; width:100%; height: <%=VALUE_HEIGHT%>px; top: <%=top%>px">
							<%=val%>
						</div>
						
						<%
					}
					
					%>
					
					</div>
				
				</td>
				
				<!-- actual graph -->
				<td valign="top" style="font-size: 50%; width: <%=GRAPH_WIDTH%>px; height: <%=GRAPH_HEIGHT%>px">
				<br />
				<div style="position: relative;font-size:1px">
				
				<%
				
				for ( int i = 0; i <= GRAPH_VALUES; i++ )
				{
					int top = ( GRAPH_VALUES - i ) * VALUE_HEIGHT;
					%>
					
					<div style="position: absolute; background-color:#666; width:100%; height:1px; top: <%=top%>px"></div>
					
					<%
				}
				
				for ( int i = 0; i < NUM_DAYS; i++ )
				{
					int left = ( (i*2+1) * DAY_SPACING ) - ( DAY_WIDTH / 2 );
					int height = (int)(GRAPH_HEIGHT * (( minors.elementAt(i) - leastVisits ) / (float)(valueRange)));
					
					if ( height <= 0 )
						height = 1;
					
					int invHeight = GRAPH_HEIGHT - height;
					
					%>
<div style="position:absolute;top:<%=invHeight%>px;left:<%=left%>px;height:<%=height%>px;width:<%=DAY_WIDTH%>px;background-color:#0066FF;border: solid 1px #0099FF; border-right:solid 1px #003366; border-bottom:solid 1px #003366"></div>
					<%
					int left2 = ( (i*2+2) * DAY_SPACING ) - ( DAY_WIDTH / 2 );
					int height2 = (int)(GRAPH_HEIGHT * (( adults.elementAt(i) - leastVisits ) / (float)(valueRange)));
					
					if ( height2 <= 0 )
						height2 = 1;
					
					int invHeight2 = GRAPH_HEIGHT - height2;
					
					%>
<div style="position:absolute;top:<%=invHeight2%>px;left:<%=left2%>px;height:<%=height2%>px;width:<%=DAY_WIDTH%>px;background-color:#CC66FF;border: solid 1px #CC99FF; border-right:solid 1px #CC3366; border-bottom:solid 1px #CC3366"></div>

					<%
				}
				
				%>
				
				</div>
				
				</td>
			
			</tr>
			
			<tr>
			
			<td align="right">
				<br /><br />
				<%= DateStartStr %>
			</td>
			<td align="right">
				<br /><br />
				<%= DateEndStr %>
			</td>
			
			</tr>
		
		</table>
		
		<%
	} // end if ( DayCounts != null )
	}
	
}
	
	%>
		
</form>

    <%@ include file="/resources/footer.jsp" %>
  </body>
</html>
