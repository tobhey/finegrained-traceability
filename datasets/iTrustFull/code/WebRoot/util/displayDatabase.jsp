<%@page import="java.sql.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.LinkedList"%>
<html>
<head>
	<title>Display Database</title>
	<style type="text/css">
		body {
		margin: 4px;
		font-family: Arial;
		font-size: 0.8em;
		}
		.results { 
		 border-collapse: collapse;
		}
		.results tr th {
		 font-size: 0.9em;
		 padding: 0px 5px 0px 5px;
		 background-color: Navy;
		 color: White;
		}
		.results tr td {
		 font-size: 0.8em;
		 padding: 0px 5px 0px 5px;
		}
		.results tr th, .results tr td {
		 border: 1px solid Gray;
		}
	</style>
</head>
<body style="margin-left:150px;">
<a href="/iTrust">Back to iTrust</a> - <a href="displayWikiDatabase.jsp">View wiki format</a>
<h2>FOR TESTING PURPOSES ONLY</h2>
<%
	LinkedList<String> tableList = new LinkedList<String>();

	Connection conn = DAOFactory.getProductionInstance().getConnection();
	ResultSet tableRS = conn.createStatement().executeQuery("show tables");
	while(tableRS.next()){
		String tableName = tableRS.getString(1);
		tableList.add(tableName);
		ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + tableName);
		int numCol = rs.getMetaData().getColumnCount();
		%><b><a name="<%=tableName%>"><%=tableName%></b><br /><table class="results"><tr><%
		for(int i=1; i<=numCol;i++){
			%><th><%=rs.getMetaData().getColumnName(i)%></th><%
		}
		%></tr><%
		while(rs.next()){
			%><tr><%
			for(int i=1;i<=numCol;i++){
				try{
					String data = rs.getString(i);
					%><td><%=data%></td><%
				} catch(SQLException e){
					%><td>--Error in date, might be empty--</td><%
				}
			}
			%></tr><%
		}
		%></table><br /><br /><%
	}
	conn.close();
%>

<script>
if (!document.layers)
document.write('<div id="divStayTopLeft" style="position:absolute">')
</script>

<layer id="divStayTopLeft">

<!--EDIT BELOW CODE TO YOUR OWN MENU-->
<table border="1" width="130" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" bgcolor="#FFFFCC">
      <p align="center"><b><font size="3">Table of Contents</font></b></td>
  </tr>
  <tr>
    <td width="100%" bgcolor="#FFFFFF">
      <p align="left">
      	   <a href="/iTrust" style="font-size:10px;">< Back to iTrust</a><br/><br/>
<%
	   for(String table : tableList)
	   {
%>      
	       <a href="<%="#"+table %>" style="font-size:10px;"><%=table %></a><br>
<%
	   }
%>
    </td>
  </tr>
</table>
<!--END OF EDIT-->

</layer>


<script type="text/javascript">

/*
Floating Menu script-  Roy Whittle (http://www.javascript-fx.com/)
Script featured on/available at http://www.dynamicdrive.com/
This notice must stay intact for use
*/

//Enter "frombottom" or "fromtop"
var verticalpos="fromtop"

if (!document.layers)
document.write('</div>')

function JSFX_FloatTopDiv()
{
	var startX = 5,
	startY = 5;
	var ns = (navigator.appName.indexOf("Netscape") != -1);
	var d = document;
	function ml(id)
	{
		var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
		if(d.layers)el.style=el;
		el.sP=function(x,y){this.style.left=x;this.style.top=y;};
		el.x = startX;
		if (verticalpos=="fromtop")
		el.y = startY;
		else{
		el.y = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		el.y -= startY;
		}
		return el;
	}
	window.stayTopLeft=function()
	{
		if (verticalpos=="fromtop"){
		var pY = ns ? pageYOffset : document.body.scrollTop;
		ftlObj.y += (pY + startY - ftlObj.y)/8;
		}
		else{
		var pY = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		ftlObj.y += (pY - startY - ftlObj.y)/8;
		}
		ftlObj.sP(ftlObj.x, ftlObj.y);
		setTimeout("stayTopLeft()", 10);
	}
	ftlObj = ml("divStayTopLeft");
	stayTopLeft();
}
JSFX_FloatTopDiv();
</script>
</body>
</html>
