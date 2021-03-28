package edu.ncsu.csc.itrust.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A bean for storing data about a prescription.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class PrescriptionBean {
	private long id = 0L;
	private MedicationBean medication = new MedicationBean();
	private long visitID = 0L;
	private String startDateStr = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	private String endDateStr = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	private int dosage = 0;
	private String instructions = "";

	public PrescriptionBean() {
	}

	@Override
	public boolean equals(Object other) {
		return (other != null) && this.getClass().equals(other.getClass())
				&& this.equals((PrescriptionBean) other);
	}

	private boolean equals(PrescriptionBean other) {
		return (medication == other.medication || (medication != null && medication.equals(other.medication)))
				&& visitID == other.visitID
				&& startDateStr.equals(other.startDateStr)
				&& endDateStr.equals(other.endDateStr)
				&& dosage == other.dosage
				&& instructions.equals(other.instructions);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	public int getDosage() {
		return dosage;
	}

	public void setDosage(int dosage) {
		this.dosage = dosage;
	}

	public Date getEndDate() {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(endDateStr);
		} catch (java.text.ParseException e) {
			return null;
		}
	}

	public void setEndDateStr(String endDate) {
		this.endDateStr = endDate;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instruction) {
		this.instructions = instruction;
	}

	public MedicationBean getMedication() {
		return medication;
	}

	public void setMedication(MedicationBean medication) {
		this.medication = medication;
	}

	public Date getStartDate() {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(startDateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setStartDateStr(String startDate) {
		this.startDateStr = startDate;
	}

	public long getVisitID() {
		return visitID;
	}

	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
