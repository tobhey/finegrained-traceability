package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data to flag an office visit.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class VisitFlag {

	public static final String DIAGNOSED = "Diagnosed";
	public static final String MISSED_MEDICATION = "Missed Medication";
	public static final String MISSING_MEDICATION = "Currently Missing Medication";
	public static final String LAST_VISIT = "Last Visit";
	public static final String IMMUNIZATION = "Needs Immunization";

	private String type;
	private String value;

	public VisitFlag(String type) {
		this.type = type;
	}

	public VisitFlag(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
