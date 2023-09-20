<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.regex.Pattern" %>
<%@page import="javax.xml.parsers.*" %>
<%@page import="org.xml.sax.*" %>
<%@page import="org.xml.sax.helpers.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.ParseException" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<style type="text/css">
		.fancyTable {
			font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
			width:100%;
			border-collapse:collapse;
		}
		
		.fancyTable td, .fancyTable th {
			font-size:.8em;
			background-color: #FFFFFF;
			border:1px solid #4F708D;
			padding:1px 2px 1px 2px;
		}
		
		.fancyTable th {
			font-size:1em;
			text-align:center;
			padding-top:0px;
			padding-bottom:0px;
			background-color:#4F708D;
			color:#ffffff;
		}
		
		.fancyTable tr.alt td {
			color:#000000;
			background-color:#DDDDFF;
		}
	</style>
</head>
<body style="margin-left:150px;">
<% 
final String path = "http://localhost:8080/iTrust/util/blackbox";
class TestParser {
	class BlackBoxTest
	{
		private String id = "";
		private String dateAdded = "";
		private String dateModified = "";
		private String author = "";
		private String role = "";
		private String useCase = "";
		private String description = "";
		private LinkedList<String> precondition = new LinkedList<String>();
		private LinkedList<String> step = new LinkedList<String>();
		private String expectedResults = "";
		private LinkedList<String> eResult = new LinkedList<String>();
		private String actualResults = "";
		private LinkedList<String> aResult = new LinkedList<String>();
		
		public void setId(String id)
		{
			this.id = id;
		}
		
		public String getId()
		{
			return id;
		}
		
		public void setAuthor(String author)
		{
			this.author = author;
		}
		
		public String getAuthor()
		{
			return author;
		}
		
		public void setRole(String role)
		{
			this.role = role;
		}
		
		public String getRole()
		{
			return role;
		}
		
		public void setUseCase(String uc)
		{
			this.useCase = uc;
		}
		
		public String getUseCase()
		{
			return useCase;
		}
		
		public void setDateAdded(String da)
		{
			dateAdded = da;
		}
		
		public String getDateAdded()
		{
			return dateAdded;
		}
		
		public void setDateModified(String dm)
		{
			dateModified = dm;
		}
		
		public String getDateModified()
		{
			return dateModified;
		}
		
		public String getDescription()
		{
			if(precondition.size() == 0 && step.size() == 0)
			{
				return description;
			}
			String d = "<strong>Preconditions:</strong><br/>";
			for(String p : precondition)
			{
				d = d + "--> " + p + "<br/>";
			}
			d = d + "<br/><strong>STEPS:</strong><br/>";
			for(int i = 1; i < step.size()+1; i++)
			{
				d = d + "\t" + i + ") " + step.get(i-1) + "<br/>";
			}
			return d;
		}
		
		public String getExpectedResults()
		{
			String eR = "";
			for(String e : eResult)
			{
				eR = eR + "--> " + e + "<br/>";
			}
			return eR;
		}
		
		public String getActualResults()
		{
			String aR = "";
			for(String a : aResult)
			{
				aR = aR + "--> " + a + "<br/>";
			}
			return aR;
		}
		
		public void setPrecondition(String p)
		{
			precondition.add(p);
		}
		
		public void setStep(String s)
		{
			step.add(s);
		}
		
		public void setEResult(String e)
		{
			eResult.add(e);
		}
		
		public void setAResult(String a)
		{
			aResult.add(a);
		}
		
		public void setDescription(String d)
		{
			this.description = d;
		}
	}
 
	class SortByTestID implements Comparator<BlackBoxTest> {

	 	public int compare(BlackBoxTest arg0, BlackBoxTest arg1) {
	 		 String x = arg0.getId();
	 		 String y = arg1.getId();
			 return x.compareTo(y);
	 	}
	}


	private LinkedList<BlackBoxTest> bbt = new LinkedList<BlackBoxTest>();
	private LinkedList<String> roles = new LinkedList<String>();
	private BlackBoxTest test;
	private String tempVal = "";
	
	public LinkedList<BlackBoxTest> getTests()
	{
		return bbt;
	}
	
	private void parseDocument() throws Exception{

		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();


	DefaultHandler handler = new DefaultHandler() {

		public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
			//reset
			tempVal = "";
			if(qName.equalsIgnoreCase("Test")) {
				//create a new instance of employee
				test = new BlackBoxTest();
				test.setId(attributes.getValue("id"));
			}
		}
	
		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
		}
	
		public void endElement(String uri, String localName,
			String qName) throws SAXException {
	
			if(qName.equalsIgnoreCase("Test")) {
				//add it to the list
				bbt.add(test);
	
			}else if (qName.equalsIgnoreCase("DateAdded")) {
				test.setDateAdded(tempVal);
			}else if (qName.equalsIgnoreCase("DateModified")) {
				test.setDateModified(tempVal);
			}else if (qName.equalsIgnoreCase("Author")) {
				test.setAuthor(tempVal);
			}
			else if (qName.equalsIgnoreCase("Description"))
			{
				test.setDescription(tempVal);
			}
			else if (qName.equalsIgnoreCase("Precondition")) {
				test.setPrecondition(tempVal);
			}
			else if (qName.equalsIgnoreCase("eResult")) {
				test.setEResult(tempVal);
			}
			else if (qName.equalsIgnoreCase("Step")) {
				test.setStep(tempVal);
			}
			else if (qName.equalsIgnoreCase("aResult")) {
				test.setAResult(tempVal);
			}else if (qName.equalsIgnoreCase("Role")) {
				test.setRole(tempVal);
				if(!roles.contains(tempVal))
				{
					roles.add(tempVal);
				}
			}else if (qName.equalsIgnoreCase("UseCase")) {
				test.setUseCase(tempVal);
			}
		}
	};
		//parse the file and also register this class for call backs
		sp.parse(path+"/BlackBoxTestPlan.xml", handler);
	}
	
	private LinkedList<String> getRolesList()
	{
		return roles;
	}

	private LinkedList<BlackBoxTest> getTestsForRole(String r)
	{
		LinkedList<BlackBoxTest> testList = new LinkedList<BlackBoxTest>();
		for(BlackBoxTest b : bbt)
		{
			if(b.getRole().equals(r))
			{
				testList.add(b);
			}
		}
		return testList;
	}
	
	private LinkedList<String> getHTMLOutput(LinkedList<BlackBoxTest> list)
	{
		LinkedList<String> testList = new LinkedList<String>();
		Collections.sort(list, new SortByTestID());
		for(BlackBoxTest b : list)
		{
			String temp = "";
			temp+=		"\n\t\t<td>" + b.getId() + "</td>";
			temp+=		"\n\t\t<td>" + b.getDescription() +"</td>";
			temp+=		"\n\t\t<td>" + b.getExpectedResults() + "</td>";
			temp+=		"\n\t\t<td>" + b.getActualResults() + "</td>";
			temp+=		"\n\t\t<td>" + b.getUseCase() + "</td>";
			temp+=		"\n\t\t<td>" + b.getRole() + "</td>";
			temp+=		"\n\t\t<td>" + b.getDateAdded() + "</td>";
			temp+=		"\n\t\t<td>" + b.getDateModified() + "</td>";
			testList.add(temp);
		}
		return testList;
	}
	
}

TestParser t = new TestParser();
t.parseDocument();
LinkedList<String> rolesList = t.getRolesList();
%>

<%
	for(String role : rolesList)
	{
%>
<div align="left"><p style="font-family:verdana;font-size:1em;"><a name="<%=role%>"><%=role %></p></div>
<table class="fancyTable">
	<tr class="fancyTable">
		<th style="width:10%">Test ID</th>
		<th style="width:30%">Description</th>
		<th style="width:30%">Expected Results</th>
		<th style="width:5%">Actual Results</th>
		<th style="width:5%">Use Case</th>
		<th style="width:5%">Role</th>
		<th style="width:5%">Date Added</th>
		<th style="width:5%">Date Modified</th>									
	</tr>
<%	
		int i = 0;
		LinkedList<String> outputList = t.getHTMLOutput(t.getTestsForRole(role));
		for(String test : outputList)
		{
%>
			<tr valign="top" <%if(i%2 == 0){ %>class="alt" <%} %>>
				<%=test %>
			</tr>	
<% 
			i++;
		}
%>
	</table>
<%
	}
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
      	   <a href="/iTrust" style="font-size:12px;">< Back to iTrust</a><br/><br/>
<%
	   for(String role : rolesList)
	   {
%>      
	       <a href="<%="#"+role%>" style="font-size:15px;"><%=role %></a><br>
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
