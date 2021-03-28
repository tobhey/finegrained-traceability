package edu.ncsu.csc.itrust.beans;

/**
 * This is the container bean for the Prescription Report requirement. It contains a PrescriptionBean (which
 * in turn contains a Medication Bean), and an office visit bean. <br />
 * <br />
 * This was made so that the prescription report could be done all in one clean query.<br />
 * <br />
 * See {@link PrescriptionBean} See {@link OfficeVisitBean}
 * 
 * @author Andy Meneely
 * 
 */
public class PrescriptionReportBean {
	PrescriptionBean prescription;
	OfficeVisitBean officeVisit;

	public PrescriptionReportBean() {
	}

	public PrescriptionBean getPrescription() {
		return prescription;
	}

	public void setPrescription(PrescriptionBean prescription) {
		this.prescription = prescription;
	}

	public OfficeVisitBean getOfficeVisit() {
		return officeVisit;
	}

	public void setOfficeVisit(OfficeVisitBean officeVisit) {
		this.officeVisit = officeVisit;
	}
}
