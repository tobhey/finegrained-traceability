package edu.ncsu.csc.itrust.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * JSP tag that is used as the patient navigation bar at the bottom of the screen.
 * 
 * To add a new page, add to the two arrays, and make sure that the page accepts PID
 * 
 * @author Andy
 * 
 */
public class PatientNavigation implements Tag {
	private PageContext pageContext;
	private Tag parent;
	// A more elegant solution here would be to use enums and have a displayName, a name, and url
	private String pageTitles[] = { "Health Records", "Basic Health History", "Demographics",
			"Document Office Visit", "Risk Factors", "Prescriptions" };
	private String pageURLs[] = { "editPHR.jsp", "editBasicHealth.jsp", "editPatient.jsp",
			"documentOfficeVisit.jsp", "chronicDiseaseRisks.jsp", "getPrescriptionReport.jsp" };
	private String thisTitle;

	public PatientNavigation() {
		super();
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.write("<span class=\"navigation\">");
			for (int i = 0; i < pageTitles.length; i++) {
				if (pageTitles[i].equals(thisTitle)) {
					out.write(pageTitles[i]);
				} else
					out.write("<a href=\"/iTrust/auth/hcp-uap/" + pageURLs[i] + "\">"
							+ pageTitles[i] + "</a>");
				out.write(" | ");
			}
			out.write("<a href=\"/iTrust/auth/hcp-uap/editPHR.jsp?switch=true\">Switch Patient</a>");
			out.write("<br /></span>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		return SKIP_BODY;
	}

	public void release() {
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	public Tag getParent() {
		return parent;
	}

	public String getThisTitle() {
		return thisTitle;
	}

	public void setThisTitle(String thisPage) {
		this.thisTitle = thisPage;
	}
}
