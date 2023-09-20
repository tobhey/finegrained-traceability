package edu.ncsu.csc.itrust.beans;

import java.sql.Timestamp;



/**
 * A bean for storing data about a lab procedure.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class LabProcedureBean {
	
	public final static String Not_Received = "NOT YET RECEIVED";
	public final static String Pending = "PENDING";
	public final static String Completed = "COMPLETED";
	public final static String Allow = "ALLOWED";
	public final static String Restrict = "RESTRICTED";

	
	/**
	 * Unique 10-digit number that does not start with 9
	 */
	 private long pid;	 
	 
	 /**
	  * Unique identifier for a laboratory procedure of a patient
	  */
	 private long procedureID;
	 
	 /**
	  * Digits of the format nnnnn-n 
	  */
	 private String loinc; 
	
	 /**
	  * One of (NOT YET RECEIVED, PENDING, COMPLETED)
	  */
	 private String status;
	 
	 /**
	  * Up to 500 alphanumeric characters
	  */
	 private String commentary;
	 
	 /**
	  * Up to 500 alphanumeric characters
	  */
	 private String results;
	 
	 /**
	  * Office VisitID	Identifier that specifies the office visit in 
	  * which the laboratory procedure was ordered
	  */
	 private long ovID; 
	 
	 /**
	  * Date/Time of last status update 	Timestamp
	  */
	 private Timestamp timestamp;
	 
	 /**
	  * permission granted by lhcp who ordered test:
	  * "ALLOWED", "RESTRICTED"
	  */
	 private String rights = Allow;
	 
	 
	 
	 public LabProcedureBean(){
	 }


	/**
	 * Unique 10-digit number that does not start with 9
	 */
	public long getPid() {
		return pid;
	}


	/**
	 * Unique 10-digit number that does not start with 9
	 */
	public void setPid(long pid) {
		this.pid = pid;
	}


	/**
	  * Unique identifier for a laboratory procedure of a patient
	  */
	public long getProcedureID() {
		return procedureID;
	}


	/**
	  * Unique identifier for a laboratory procedure of a patient
	  */
	public void setProcedureID(long procedureID) {
		this.procedureID = procedureID;
	}


	 /**
	  * Digits of the format nnnnn-n 
	  */
	public String getLoinc() {
		return loinc;
	}


	 /**
	  * Digits of the format nnnnn-n 
	  */
	public void setLoinc(String loinc) {
		this.loinc = loinc;
	}


	/**
	  * One of (NOT YET RECEIVED, PENDING, COMPLETED)
	  */
	public String getStatus() {
		return status;
	}


	/**
	  * One of (NOT YET RECEIVED, PENDING, COMPLETED)
	  */
	public void setStatus(String status) {
		this.status = status;
	}


	 /**
	  * Up to 500 alphanumeric characters
	  */
	public String getCommentary() {
		return commentary;
	}


	 /**
	  * Up to 500 alphanumeric characters
	  */
	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}


	 /**
	  * Up to 500 alphanumeric characters
	  */
	public String getResults() {
		return results;
	}


	 /**
	  * Up to 500 alphanumeric characters
	  */
	public void setResults(String results) {
		this.results = results;
	}


	 /**
	  * Office VisitID	Identifier that specifies the office visit in 
	  * which the laboratory procedure was ordered
	  */
	public long getOvID() {
		return ovID;
	}


	 /**
	  * Office VisitID	Identifier that specifies the office visit in 
	  * which the laboratory procedure was ordered
	  */
	public void setOvID(long ovID) {
		this.ovID = ovID;
	}



	public Timestamp getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	 /**
	  * permission granted by lhcp who ordered test:
	  * "ALLOWED", "RESTRICTED"
	  */
	public String getRights() {
		return rights;
	}

	 /**
	  * permission granted by lhcp who ordered test:
	  * "ALLOWED", "RESTRICTED"
	  */
	public void setRights(String rights) {
		this.rights = rights;
	}
	
	
	 public void allow(){
		 this.rights = Allow;
	 }
	 
	 public void restrict(){
		 this.rights = Restrict;
	 }
	 
	 public void statusComplete(){
	 	this.status = Completed;
	 }
	 
	 public void statusPending(){
		 this.status = Pending;
	 }
	 
	 public void statusNotReceived(){
		 this.status = Not_Received;
	 }
	 
	 
	 
}
