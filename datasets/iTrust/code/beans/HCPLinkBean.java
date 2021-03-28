package edu.ncsu.csc.itrust.beans;

/**
 * A mini-bean to pass data between viewPrescriptionRecords.jsp and reportAdverseEvent.jsp
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class HCPLinkBean {
	long prescriberMID;
	String drug;
	boolean checked;
	String code;
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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
	public long getPrescriberMID() {
		return prescriberMID;
	}
	
	/**
	 * 
	 * @param mID the patients MID
	 */
	public void setPrescriberMID(long mID) {
		prescriberMID = mID;
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
}
