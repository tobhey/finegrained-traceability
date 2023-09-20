package edu.ncsu.csc.itrust.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A bean for storing data about an office visit at the hospital.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class OfficeVisitBean {
	private long visitID = 0;
	private long patientID = 0;
	private long hcpID = 0;
	private String notes = "";
	private String visitDateStr = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	private String hospitalID = "";
	private List<DiagnosisBean> diagnoses = new ArrayList<DiagnosisBean>();
	private List<PrescriptionBean> prescriptions = new ArrayList<PrescriptionBean>();
	private List<ProcedureBean> procedures = new ArrayList<ProcedureBean>();

	public OfficeVisitBean() {
	}

	/**
	 * For use ONLY by DAOs
	 * 
	 * @param visitID
	 */
	public OfficeVisitBean(long visitID) {
		this.visitID = visitID;
	}

	public long getID() {
		return visitID;
	}

	public long getPatientID() {
		return this.patientID;
	}

	public long getHcpID() {
		return this.hcpID;
	}

	public String getNotes() {
		return this.notes;
	}

	public Date getVisitDate() {
		Date d = null; 
		try {
			d = new SimpleDateFormat("MM/dd/yyyy").parse(visitDateStr);
		} catch (ParseException e) {
			System.out.println(e.toString());
		}
		
		return d;
	}

	public String getVisitDateStr() {
		return visitDateStr;
	}

	public List<DiagnosisBean> getDiagnoses() {
		return diagnoses;
	}

	public List<PrescriptionBean> getPrescriptions() {
		return prescriptions;
	}

	public List<ProcedureBean> getProcedures() {
		return procedures;
	}

	public long getVisitID() {
		return visitID;
	}

	public void setDiagnoses(List<DiagnosisBean> diagnoses) {
		this.diagnoses = diagnoses;
	}

	public void setHcpID(long hcpID) {
		this.hcpID = hcpID;
	}

	public void setPrescriptions(List<PrescriptionBean> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}

	public void setProcedures(List<ProcedureBean> procedures) {
		this.procedures = procedures;
	}

	public void setVisitDateStr(String visitDate) {
		this.visitDateStr = visitDate;
	}

	public String getHospitalID() {
		return hospitalID;
	}

	public void setHospitalID(String hospitalID) {
		this.hospitalID = hospitalID;
	}

}
