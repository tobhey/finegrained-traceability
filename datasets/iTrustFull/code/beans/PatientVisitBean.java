package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a patient's visit.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class PatientVisitBean {
	private PatientBean patient = null;
	private String PatientName = "";
	private String Address1 = "";
	private String Address2 = "";
	private String LastOVDateM = "";
	private String LastOVDateD = "";
	private String LastOVDateY = "";
	private String LastOVDate = "";
	
	public String getLastOVDate() {
		return LastOVDate;
	}

	public void setLastOVDate(String lastOVDate) {
		LastOVDate = lastOVDate;
	}

	public String getLastOVDateM() {
		return LastOVDateM;
	}

	public void setLastOVDateM(String lastOVDateM) {
		LastOVDateM = lastOVDateM;
	}

	public String getLastOVDateD() {
		return LastOVDateD;
	}

	public void setLastOVDateD(String lastOVDateD) {
		LastOVDateD = lastOVDateD;
	}

	public String getLastOVDateY() {
		return LastOVDateY;
	}

	public void setLastOVDateY(String lastOVDateY) {
		LastOVDateY = lastOVDateY;
	}

	public PatientVisitBean() {
		
	}

	public String getPatientName() {
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public String getAddress1() {
		return Address1;
	}

	public void setAddress1(String address1) {
		Address1 = address1;
	}

	public String getAddress2() {
		return Address2;
	}

	public void setAddress2(String address2) {
		Address2 = address2;
	}

	public PatientBean getPatient() {
		return patient;
	}

	public void setPatient(PatientBean patient) {
		this.patient = patient;
	}

}
