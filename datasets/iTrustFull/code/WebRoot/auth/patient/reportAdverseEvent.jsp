<%@page import="edu.ncsu.csc.itrust.action.ReportAdverseEventAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.AdverseEventBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HCPLinkBean"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="java.util.List"%>
<%@page import= "java.util.ArrayList"%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Report Adverse Event";
%>

<%@include file="/header.jsp"%>

<%
ReportAdverseEventAction action;


if(request.getParameter("Comment") != (null) && session.getAttribute("HCPMID") == null){
	List<HCPLinkBean> linkList = new ArrayList<HCPLinkBean>();
	linkList = (List<HCPLinkBean>) session.getAttribute("beanlist");
	List<AdverseEventBean> adverseList = new ArrayList<AdverseEventBean>();
	boolean bad = false;
	for(HCPLinkBean lBean : linkList){
		if (lBean.isChecked()){
			action = new ReportAdverseEventAction("" + lBean.getPrescriberMID(), prodDAO, loggedInMID.longValue());
			AdverseEventBean aeBean = new AdverseEventBean();
			aeBean.setMID(""+loggedInMID);
			aeBean.setDescription(request.getParameter("Comment"));
			aeBean.setDrug(""+lBean.getDrug());
			aeBean.setCode(""+lBean.getCode());
			aeBean.setPrescriber("" + lBean.getPrescriberMID());
			adverseList.add(aeBean);
			
			String msg = action.addReport(aeBean);
			
			if(!msg.equals("")){
				bad = true;
				
				%>
			<div align=center>
				<span class="iTrustError"><%=msg %></span>
			</div>
			<%
			}
		}
	}
	action = new ReportAdverseEventAction(prodDAO, loggedInMID.longValue());
	action.sendMails(adverseList);
	if(!bad){
		response.sendRedirect("home.jsp?rep=1");
	}
}

if(request.getParameter("Comment") != (null) && session.getAttribute("HCPMID") != null){
	action = new ReportAdverseEventAction("" + session.getAttribute("HCPMID"), prodDAO, loggedInMID.longValue());
	AdverseEventBean aeBean = new AdverseEventBean();
	aeBean.setMID(""+loggedInMID);
	aeBean.setDescription(request.getParameter("Comment"));
	aeBean.setDrug(""+session.getAttribute("presID"));
	aeBean.setCode(""+session.getAttribute("code"));
	
	//aeBean = new BeanBuilder<AdverseEventBean>().build(request.getParameterMap(), new AdverseEventBean());
	action.addReport(aeBean);
	action.sendMail(aeBean);
	response.sendRedirect("home.jsp?rep=1");
	
}

if(request.getParameter("Comment") == (null) && request.getParameter("HCPMID") != null){
	action = new ReportAdverseEventAction(request.getParameter("HCPMID"), prodDAO, loggedInMID.longValue());
	session.setAttribute("HCPMID", request.getParameter("HCPMID"));
	session.setAttribute("presID", request.getParameter("presID"));
	session.setAttribute("code", request.getParameter("code"));
}

%>
<div align="center">
<form id="mainForm" method="get" action="reportAdverseEvent.jsp">
	<h2>Report an Adverse Event</h2>

	<textarea name="Comment" cols="100" rows="10"></textarea><br />
	<br />
	<input type="submit" value="Save" name="addReport"/>


</form>
</div>

<%@include file="/footer.jsp"%>
