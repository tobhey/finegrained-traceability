package edu.ncsu.csc.itrust.beans.forms;

/**
 * A form to contain data coming from editing an office visit.
 * 
 * A form is a bean, kinda. You could say that it's a "form" of a bean :) 
 * Think of a form as a real-life administrative form that you would fill out to get 
 * something done, not necessarily making sense by itself.
 */
public class EditOfficeVisitForm {
	private String ovID;
	private String hcpID;
	private String patientID;
	private String hospitalID;
	private String removeLabProcID;
	private String removeDiagID;
	private String removeProcID;
	private String removeImmunizationID;
	private String removeMedID;
	private String addLabProcID;
	private String addDiagID;
	private String addProcID;
	private String addImmunizationID;
	private String addMedID;
	private String notes;
	private String visitDate;
	private String startDate;
	private String endDate;
	private String dosage;
	private String instructions;
	private String causeOfDeath;

	public String getAddDiagID() {
		return addDiagID;
	}

	public void setAddDiagID(String addDiagID) {
		this.addDiagID = addDiagID;
	}

	public String getAddLabProcID() {
		return addLabProcID;
	}

	public void setAddLabProcID(String addLabProcID) {
		this.addLabProcID = addLabProcID;
	}
	
	public String getAddMedID() {
		return addMedID;
	}

	public void setAddMedID(String addMedID) {
		this.addMedID = addMedID;
	}

	public String getAddProcID() {
		return addProcID;
	}

	public void setAddProcID(String addProcID) {
		this.addProcID = addProcID;
	}

	public String getAddImmunizationID() {
		return addImmunizationID;
	}

	public void setAddImmunizationID(String addImmunizationID) {
		this.addImmunizationID = addImmunizationID;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOvID() {
		return ovID;
	}

	public void setOvID(String ovID) {
		this.ovID = ovID;
	}

	public String getRemoveLabProcID() {
		return removeLabProcID;
	}

	public void setRemoveLabProcID(String removeLabProcID) {
		this.removeLabProcID = removeLabProcID;
	}
	
	public String getRemoveDiagID() {
		return removeDiagID;
	}

	public void setRemoveDiagID(String removeDiagID) {
		this.removeDiagID = removeDiagID;
	}

	public String getRemoveMedID() {
		return removeMedID;
	}

	public void setRemoveMedID(String removeMedID) {
		this.removeMedID = removeMedID;
	}

	public String getRemoveProcID() {
		return removeProcID;
	}

	public void setRemoveProcID(String removeProcID) {
		this.removeProcID = removeProcID;
	}
	
	public String getRemoveImmunizationID() {
		return removeImmunizationID;
	}

	public void setRemoveImmunizationID(String removeImmunizationID) {
		this.removeImmunizationID = removeImmunizationID; 
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getHcpID() {
		return hcpID;
	}

	public void setHcpID(String hcpID) {
		this.hcpID = hcpID;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getHospitalID() {
		return hospitalID;
	}

	public void setHospitalID(String hospitalID) {
		this.hospitalID = hospitalID;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCauseOfDeath() {
		return causeOfDeath;
	}

	public void setCauseOfDeath(String causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
}
