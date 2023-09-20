package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a medical referral.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class ReferralBean {
	private long id = 0L;
	private long patientID = 0L;
	private long senderID = 0L;
	private long receiverID = 0L;
	private String referralDetails = "";
	private String consultationDetails = "";
	private ReferralStatus status = ReferralStatus.Pending;
	
	public enum ReferralStatus {
		Pending,
		Finished,
		Declined
	}

	public ReferralBean() {
	}

	@Override
	public boolean equals(Object other) {
		return (other != null) && this.getClass().equals(other.getClass())
				&& this.equals((ReferralBean) other);
	}

	private boolean equals(ReferralBean other) {
		return (id == other.id
				&& senderID == other.senderID
				&& receiverID == other.receiverID
				&& referralDetails.equals(other.referralDetails)
				&& consultationDetails.equals(other.consultationDetails)
				&& status.equals(other.status));
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}

	public long getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(long receiverID) {
		this.receiverID = receiverID;
	}

	public String getReferralDetails() {
		return referralDetails;
	}

	public void setReferralDetails(String referralDetails) {
		this.referralDetails = referralDetails;
	}

	public String getConsultationDetails() {
		return consultationDetails;
	}

	public void setConsultationDetails(String consultationDetails) {
		this.consultationDetails = consultationDetails;
	}

	public ReferralStatus getStatus() {
		return status;
	}

	public void setStatus(ReferralStatus status) {
		this.status = status;
	}

	public long getPatientID() {
		return patientID;
	}

	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}

	

}
