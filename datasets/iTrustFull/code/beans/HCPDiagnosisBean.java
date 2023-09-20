package edu.ncsu.csc.itrust.beans;

import java.util.List;

/**
 * A bean for storing data about the diagnosis given by an HCP.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class HCPDiagnosisBean {

	private long HCPID;
	private String HCPname;
	private long numPatients;
	private List<MedicationBean> medList;
	private List<LabProcedureBean> labList;
	private int visitSat;
	private int treatmentSat;
	private int surveyCount;
	
	public HCPDiagnosisBean() {
		numPatients = 0;
		visitSat = 0;
		treatmentSat = 0;
		surveyCount = 0;
	}

	public long getHCP() {
		return HCPID;
	}
	
	public void setHCP(long hcp) {
		HCPID = hcp;
	}
	
	public String getHCPName() {
		return HCPname;
	}
	
	public void setHCPName(String name) {
		HCPname = name;
	}
	
	public long getNumPatients() {
		return numPatients;
	}
	
	public void incNumPatients() {
		numPatients++;
	}
	
	public List<MedicationBean> getMedList() {
		return medList;
	}
	
	public void setMedList(List<MedicationBean> medlist) {
		medList = medlist;
	}
	
	public List<LabProcedureBean> getLabList() {
		return labList;
	}
	
	public void setLabList(List<LabProcedureBean> lablist) {
		labList = lablist;
	}
	
	public String getVisitSatisfaction() {
		return (visitSat != 0 && surveyCount != 0)?Double.toString(((double)visitSat/(double)surveyCount)):"no results available";
	}
	
	public void setVisitSat(int sat) {
		visitSat += sat;
		surveyCount++;
	}

	public String getTreatmentSatisfaction() {
		return (treatmentSat != 0 && surveyCount != 0)?Double.toString((double)treatmentSat/(double)surveyCount):"no results available";
	}	
	
	public void setTreatmentSat(int sat) {
		treatmentSat += sat;
	}

}
