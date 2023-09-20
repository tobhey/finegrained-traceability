package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a adverse event based on a drug.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class AdverseEventBean {
	String MID;
	String drug;
	String description;
	String code;
	String date;
	String status;
	String Prescriber;
	
	public String getPrescriber() {
		return Prescriber;
	}

	public void setPrescriber(String prescriber) {
		Prescriber = prescriber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	int id;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the patient MID
	 */
	public String getMID() {
		return MID;
	}
	
	/**
	 * 
	 * @param mID the patients MID
	 */
	public void setMID(String mID) {
		MID = mID;
	}
	
	/**
	 * 
	 * @return the drug the event is being reported for
	 */
	public String getDrug() {
		return drug;
	}
	
	/**
	 * 
	 * @param drug sets the drug the event is being reported for
	 */
	public void setDrug(String drug) {
		this.drug = drug;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	

}
