package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about Diagnosis.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class DiagnosisBean {
	private long ovDiagnosisID = 0L; // optional
	private String icdCode;
	private String description;
	private String classification;
	private long visitID;

	public DiagnosisBean() {
	}

	/**
	 * This functionality will be moved elsewhere.
	 * 
	 */
	public DiagnosisBean(String code, String description, String classification) {
		this.icdCode = code;
		this.description = description;
		
		if (null != classification && classification.equals("yes")) {
			this.classification = classification;
		}
		else {
			this.classification = "no";
		}
	}

	/**
	 * Gets the ICD Code for this procedure
	 * 
	 * @return The ICD Code for this procedure
	 */
	public String getICDCode() {
		return icdCode;
	}

	public void setICDCode(String code) {
		icdCode = code;
	}
	
	public String getClassification() {
		return classification;
	}

	/**
	 * Gets the ICD Description for this procedure
	 * 
	 * @return The ICD Description for this procedure
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormattedDescription() {
		return description + "(" + icdCode + ")";
	}

	/**
	 * Optional - for use with editing an office visit
	 * 
	 * @return
	 */
	public long getOvDiagnosisID() {
		return ovDiagnosisID;
	}

	public void setOvDiagnosisID(long ovDiagnosisID) {
		this.ovDiagnosisID = ovDiagnosisID;
	}

	public void setVisitID(long vid) {
		visitID = vid;
	}
	
	public long getVisitID() {
		return visitID;
	}
}
