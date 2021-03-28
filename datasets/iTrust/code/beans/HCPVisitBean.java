package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a visit with an HCP.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class HCPVisitBean {
	private String HCPName;
	private String HCPSpecialty;
	private String HCPAddr;
	private String OVDate;
	private boolean designated;
	private long hcpMID;
	
	public HCPVisitBean() {
		HCPName = "";
		HCPSpecialty = "";
		HCPAddr = "";
		OVDate = "";
		
	}
	
	public void setHCPMID(long mid) {
		hcpMID = mid;
	}
	
	public long getHCPMID() {
		return hcpMID;
	}
	
	public String getHCPName() {
		return HCPName;
	}
	
	public void setHCPName(String name) {
		if (null != name) {
			HCPName = name;
		}
	}
	
	public String getHCPSpecialty() {
		return HCPSpecialty;
	}
	
	public void setHCPSpecialty(String specialty) {
		if (null != specialty) {
			HCPSpecialty = specialty;
		}
		else {
			HCPSpecialty = "none";
		}
	}
	
	public String getHCPAddr() {
		return HCPAddr;
	}
	
	public void setHCPAddr(String addr) {
		if (null != addr) {
			HCPAddr = addr;
		}
	}
	
	public String getOVDate() {
		return OVDate;
	}
	
	public void setOVDate(String date) {
		if (null != date) {
			OVDate = date;
		}
	}
	
	public boolean isDesignated() {
		return designated;
	}
	
	public void setDesignated(boolean val) {
		designated = val;
	}
}
