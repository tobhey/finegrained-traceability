package edu.ncsu.csc.itrust.beans.forms;

/**
 * A form to contain data coming from editing a health record.
 * 
 * A form is a bean, kinda. You could say that it's a "form" of a bean :) 
 * Think of a form as a real-life administrative form that you would fill out to get 
 * something done, not necessarily making sense by itself.
 */
public class HealthRecordForm {
	private String height = "0.0";
	private String weight = "0.0";
	private String isSmoker = "false";
	private String bloodPressureN = "0";
	private String bloodPressureD = "0";
	private String cholesterolHDL = "0";
	private String cholesterolLDL = "0";
	private String cholesterolTri = "0";

	public HealthRecordForm() {
	}

	public String getBloodPressureD() {
		return bloodPressureD;
	}

	public void setBloodPressureD(String bloodPressureD) {
		this.bloodPressureD = bloodPressureD;
	}

	public String getBloodPressureN() {
		return bloodPressureN;
	}

	public void setBloodPressureN(String bloodPressureN) {
		this.bloodPressureN = bloodPressureN;
	}

	public String getCholesterolHDL() {
		return cholesterolHDL;
	}

	public void setCholesterolHDL(String cholesterolHDL) {
		this.cholesterolHDL = cholesterolHDL;
	}

	public String getCholesterolLDL() {
		return cholesterolLDL;
	}

	public void setCholesterolLDL(String cholesterolLDL) {
		this.cholesterolLDL = cholesterolLDL;
	}

	public String getCholesterolTri() {
		return cholesterolTri;
	}

	public void setCholesterolTri(String cholesterolTri) {
		this.cholesterolTri = cholesterolTri;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getIsSmoker() {
		return isSmoker;
	}

	public void setIsSmoker(String isSmoker) {
		this.isSmoker = isSmoker;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}
