<%@page import="java.sql.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
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
<body>
<a href="/iTrust">Back to iTrust</a> - <a href="displayDatabase.jsp">View html format</a>
<h2>FOR TESTING PURPOSES ONLY</h2>
<%
	Connection conn = DAOFactory.getProductionInstance().getConnection();
	ResultSet tableRS = conn.createStatement().executeQuery("show tables");
	while(tableRS.next()){
		String tableName = tableRS.getString(1);
		ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + tableName);
		int numCol = rs.getMetaData().getColumnCount();
		%><b>== <%=tableName%> ==</b><br />^<%
		for(int i=1; i<=numCol;i++){
			%><%=rs.getMetaData().getColumnName(i)%>^<%
		}
		%><%
		while(rs.next()){
			%><br />|<%
			for(int i=1;i<=numCol;i++){
				try{
					String data = rs.getString(i);
					if(data!=null && data.equals("")){
						data=" ";
					}
					%><%=data%>|<%
				} catch(SQLException e){
					%>--Error in date, might be empty--|<%
				}
			}
			%><%
		}
		%><br /><br /><%
	}
	conn.close();
%>
</body>
</html>
