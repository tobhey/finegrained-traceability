package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.beans.HealthRecord;

/**
 * Checks if the patient's blood pressure is above a given threshold.
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class HypertensionFactor extends PatientRiskFactor {
	private static final int systolicThreshold = 240;
	private static final int diastolicThreshold = 120;
	private HealthRecord record;

	public HypertensionFactor(HealthRecord currentHealthRecord) {
		this.record = currentHealthRecord;
	}

	public String getDescription() {
		return "Patient has hypertension";
	}

	public boolean hasFactor() {
		return (record.getBloodPressureSystolic() > systolicThreshold)
				|| (record.getBloodPressureDiastolic() > diastolicThreshold);
	}
}
