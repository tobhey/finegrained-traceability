package edu.ncsu.csc.itrust.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import edu.ncsu.csc.itrust.enums.State;

/**
 * JSP tag that adds an HTML select for states, from the enum.
 * 
 * @see {@link State}
 * 
 * Also selects the current state
 * @author Andy
 * 
 */
public class StateSelect implements Tag {
	private PageContext pageContext;
	private Tag parent;
	private String name;
	private String value;

	public StateSelect() {
		super();
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.write("<select name=\"" + getName() + "\" >");
			if (name == null || "".equals(name))
				out.write("<option value=\"\">Select State</option>");
			for (State state : State.values()) {
				String selected = state.toString().equals(getValue()) ? "selected=selected" : "";
				out.write("<option value=\"" + state.toString() + "\" " + selected + ">" + state.getName()
						+ "</option>");
			}
			out.write("</select>");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
