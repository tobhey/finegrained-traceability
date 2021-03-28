package edu.ncsu.csc.itrust.beans;

/**
 * Bean to be used for survey results (search).  Stores address information about a HCP
 * in addition to their specialty, hospital, and averages from survey (results range from 1-5).  This
 * beans also contains a variable that stores the percent of office visits that satisfaction results are 
 * available.
 */
public class SurveyResultBean {

	private long hcpMID;
	private String hcpFirstName;
	private String hcpLastName;
	private String hcpAddress1;
	private String hcpAddress2;
	private String hcpCity;
	private String hcpState;
	private String hcpZip;
	private String hcpSpecialty;
	private String hcpHospitalID;
	private float avgWaitingRmMinutes;
	private float avgExamRmMinutues;
	private float avgVisitSatisfaction;
	private float avgTreatmentSatisfaction;
	private float percentSatResultsAvailable;
	
	//list of specialties
	public final static String GENERAL_SPECIALTY = "General";
	public final static String SURGEON_SPECIALTY = "Surgeon";
	public final static String HEART_SPECIALTY = "Heart Specialist";
	public final static String PEDIATRICIAN_SPECIALTY = "Pediatrician";
	public final static String OBGYN_SPECIALTY = "OB/GYN";
	public final static String ANY_SPECIALTY = "None";
	
	public void setHCPMID(long mid) {
		hcpMID = mid;
	}
	public long getHCPMID() {
		return hcpMID;
	}
	
	public void setHCPFirstName(String firstName) {
		this.hcpFirstName = firstName;
	}
	public String getHCPFirstName() {
		return hcpFirstName;
	}
	
	public void setHCPLastName(String lastName) {
		this.hcpLastName = lastName;
	}
	public String getHCPLastName() {
		return hcpLastName;
	}
	
	public void setHCPaddress1(String address1) {
		this.hcpAddress1 = address1;
	}
	public String getHCPaddress1() {
		return hcpAddress1;
	}
	
	public void setHCPaddress2(String address2) {
		this.hcpAddress2 = address2;
	}
	public String getHCPaddress2() {
		return hcpAddress2;
	}
	
	public void setHCPcity(String city) {
		this.hcpCity = city;
	}
	public String getHCPcity() {
		return hcpCity;
	}
	
	public void setHCPstate(String state) {
		this.hcpState = state;
	}
	public String getHCPstate() {
		return hcpState;
	}
	
	public void setHCPzip(String zip) {
		this.hcpZip = zip;
	}
	public String getHCPzip() {
		return hcpZip;
	}
	
	public void setHCPspecialty(String specialty) {
		this.hcpSpecialty = specialty;
	}
	public String getHCPspecialty() {
		return hcpSpecialty;
	}
	
	public void setHCPhospital(String hospital) {
		this.hcpHospitalID = hospital;
	}
	public String getHCPhospital() {
		return hcpHospitalID;
	}
	
	public void setAvgWaitingRoomMinutes(float waitingRoomMinutes) {
		this.avgWaitingRmMinutes = waitingRoomMinutes;
	}
	public float getAvgWaitingRoomMinutes() {
		return avgWaitingRmMinutes;
	}
	
	public void setAvgExamRoomMinutes(float examRoomMinutes) {
		this.avgExamRmMinutues = examRoomMinutes;
	}
	public float getAvgExamRoomMinutes() {
		return avgExamRmMinutues;
	}
	
	public void setAvgVisitSatisfaction(float visitSatisfaction) {
		this.avgVisitSatisfaction = visitSatisfaction;
	}
	public float getAvgVisitSatisfaction() {
		return avgVisitSatisfaction;
	}
	
	public void setAvgTreatmentSatisfaction(float treatmentSatisfaction) {
		this.avgTreatmentSatisfaction = treatmentSatisfaction;
	}
	public float getAvgTreatmentSatisfaction() {
		return avgTreatmentSatisfaction;
	}
	
	public void setPercentSatisfactionResults (float percent) {
		this.percentSatResultsAvailable= percent;
	}
	public float getPercentSatisfactionResults() {
		return percentSatResultsAvailable;
	}
}
