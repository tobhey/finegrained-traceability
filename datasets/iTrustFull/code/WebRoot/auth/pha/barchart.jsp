<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.AdverseEventBean"%>


<%@include file="/global.jsp" %>


<head>
<title>Adverse Event Reports By Month</title>
</head>
<body>

<%
List<AdverseEventBean> events = (List<AdverseEventBean>)session.getAttribute("events");
if(events != null) {
	List<AdverseEventBean> thisIdEvents = new ArrayList<AdverseEventBean>();
	String [] monthNames = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept",
	                        "Oct","Nov","Dec"};
	for(AdverseEventBean aeBean : events) {
		if(aeBean.getCode().equals(request.getParameter("id")) && (aeBean.getStatus() == null || "".equals(aeBean.getStatus()))) {
			thisIdEvents.add(aeBean);
		}
	}
	if(thisIdEvents.size() > 0) {
		%>
		<b><%=thisIdEvents.get(0).getDrug() + "(" + thisIdEvents.get(0).getCode() %>) Adverse Events By Month</b><br /><br />
		<table width="200" border="0" cellpadding="0" cellspacing="0">
		<%
		int[] heights = new int[thisIdEvents.size()];
		String[] months = new String[thisIdEvents.size()];
		String currentMonth = thisIdEvents.get(0).getDate().substring(0, 7);
		int heightCount = 0;
		int monthCount = 0;
		int maxHeight = 1;
		for(AdverseEventBean aeBean : thisIdEvents) {
			if(!aeBean.getDate().substring(0, 7).equals(currentMonth)) {
				heights[monthCount] = heightCount;
				if(heightCount > maxHeight)
					maxHeight = heightCount;
				heightCount = 0;
				currentMonth = aeBean.getDate().substring(0, 7);
				monthCount++;
			}
			String date = aeBean.getDate().substring(0, 7);
			int month = Integer.parseInt(date.substring(5));
			String monthName = monthNames[month - 1];
			months[monthCount] = monthName + " " + date.substring(0,4);
			heightCount++;
		}
		heights[monthCount] = heightCount;
		if(heightCount > maxHeight)
			maxHeight = heightCount;
		monthCount++;
		
		String currentColor = "#000000";
		for(int i = 0; i < maxHeight; i++) {
		%>
			<tr height="10">
			<td width="18" bgcolor="#FFFFFF" style="text-align: right;"><%=maxHeight - i %></td>
			<td width="2">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr><td width="2" style="border-bottom: 1px solid #AAAAAA; border-right: 2px solid #AAAAAA">&nbsp;</td></tr>
				<tr><td width="2" style="border-top: 1px solid #AAAAAA; border-right: 2px solid #AAAAAA">&nbsp;</td></tr>
				</table>
			</td>
			
		<%
			for(int j = 0; j < monthCount; j++) {
				if(heights[j] >= maxHeight - i) {
					%>
					<td width="15" bgcolor="<%=currentColor %>"></td>
					<%
				} else {
					%>
					<td width="15" bgcolor="#FFFFFF"></td>
					<%
				}
				%>
				<td width="5"></td>
				<%
				if(currentColor.equals("#000000"))
					currentColor = "#CC0000";
				else
					currentColor = "#000000";
			}
			
		%>
			</tr>
		
		<%
		}
		%>
		<tr height="20"><td width="18"></td><td width="2" style="border-top: 2px solid #AAAAAA;">&nbsp;</td>
		<%
		for(int m = 0; m < monthCount; m++) {
			%>
			<td style="border-top: 2px solid #AAAAAA"><%=months[m] %></td>
			<td style="border-top: 2px solid #AAAAAA">&nbsp;</td>
			<%
		}
		%>
		</tr>
		<%
	}
		%>
	
	</table>
<%
}
%>
</body>


