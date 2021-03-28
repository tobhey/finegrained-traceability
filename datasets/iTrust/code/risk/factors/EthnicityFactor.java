package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.enums.Ethnicity;

/**
 * Checks if the ethnicity of a patient matches the one at risk
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class EthnicityFactor extends PatientRiskFactor {
	private Ethnicity[] atRisk;
	private PatientBean patient;

	public EthnicityFactor(PatientBean patient, Ethnicity... atRisks) {
		this.atRisk = atRisks.clone();
		this.patient = patient;
	}

	public String getDescription() {
		return "Patient's ethnicity is " + patient.getEthnicity();
	}

	public boolean hasFactor() {
		for (Ethnicity ethnicity : atRisk) {
			if (patient.getEthnicity().equals(ethnicity) && !Ethnicity.NotSpecified.equals(ethnicity))
				return true;
		}
		return false;
	}
}
