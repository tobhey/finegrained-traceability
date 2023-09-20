package edu.ncsu.csc.itrust.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * JSP tag used as a utility to link to the actual ICD9CM site
 * 
 * @author Andy
 * 
 */
public class ICD9CMLink implements Tag {
	private PageContext pageContext;
	private Tag parent;
	private String code;

	public ICD9CMLink() {
		super();
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.write("<a href=\"http://icd9cm.chrisendres.com/2007/index.php?srchtype=diseases&srchtext="
					+ code + "&Submit=Search&action=search\">" + code + "</a>");
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCode(double code) {
		this.code = String.valueOf(code);
	}
}
