package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a hospital.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class HospitalBean {
	String hospitalID;
	String hospitalName;

	public HospitalBean() {
	}

	public HospitalBean(String id) {
		hospitalID = id;
	}

	public HospitalBean(String id, String name) {
		hospitalID = id;
		hospitalName = name;
	}

	public String getHospitalID() {
		return hospitalID;
	}

	public void setHospitalID(String hospitalID) {
		this.hospitalID = hospitalID;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass().equals(this.getClass()) && this.equals((HospitalBean) obj);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	private boolean equals(HospitalBean other) {
		return hospitalID.equals(other.hospitalID) && hospitalName.equals(other.hospitalName);
	}
}
