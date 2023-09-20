package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.beans.HealthRecord;

/**
 * Checks if cholesterol is over a given threshold for a given patient
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class CholesterolFactor extends PatientRiskFactor {
	private static final int hdlThreshold = 35;
	private static final int ldlThreshold = 240;
	private static final int triThreshold = 250;
	private HealthRecord record;

	public CholesterolFactor(HealthRecord currentHealthRecord) {
		this.record = currentHealthRecord;
	}

	public String getDescription() {
		return "Patient has bad cholesterol";
	}

	public boolean hasFactor() {
		return (record.getCholesterolHDL() < hdlThreshold) || (record.getCholesterolLDL() > ldlThreshold)
				|| (record.getCholesterolTri() > triThreshold);
	}
}
