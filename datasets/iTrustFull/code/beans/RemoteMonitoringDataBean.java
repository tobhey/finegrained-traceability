package edu.ncsu.csc.itrust.beans;

import java.sql.Timestamp;

/**
 * A bean for storing remote monitoring data for patients.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class RemoteMonitoringDataBean {
	private long patientMID;
	private Timestamp time;
	private int systolicBloodPressure;
	private int diastolicBloodPressure;
	private int glucoseLevel;
	private String reporterRole;
	private long reporterMID;

	/**
	 * Constructor with no parameters
	 */
	public RemoteMonitoringDataBean() {
		
	}
	
	/**
	 * Constructor with loggedInMID parameter
	 */
	public RemoteMonitoringDataBean(long patientMID) {
		this.patientMID = patientMID;
	}
	
	public long getReporterMID() {
		return reporterMID;
	}

	public void setReporterMID(long reporterMID) {
		this.reporterMID = reporterMID;
	}
	
	public long getPatientMID() {
		return patientMID;
	}
	
	public void setLoggedInMID(long patientMID) {
		this.patientMID = patientMID;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public int getSystolicBloodPressure() {
		return systolicBloodPressure;
	}
	
	public void setSystolicBloodPressure(int systolicBloodPressure) {
		this.systolicBloodPressure = systolicBloodPressure;
	}
	
	public int getDiastolicBloodPressure() {
		return diastolicBloodPressure;
	}
	
	public void setDiastolicBloodPressure(int diastolicBloodPressure) {
		this.diastolicBloodPressure = diastolicBloodPressure;
	}
	
	public int getGlucoseLevel() {
		return glucoseLevel;
	}
	
	public void setGlucoseLevel(int glucoseLevel) {
		this.glucoseLevel = glucoseLevel;
	}
	
	public String getReporterRole() {
		return reporterRole;
	}

	public void setReporterRole(String reporterRole) {
		this.reporterRole = reporterRole;
	}

}
