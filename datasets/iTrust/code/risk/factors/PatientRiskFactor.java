package edu.ncsu.csc.itrust.risk.factors;

/**
 * Abstract class for risk factors, the hasRiskFactor delegates to the protected version. Caches the result in
 * case it gets checked more than once.
 * 
 * @author Andy
 * 
 */
abstract public class PatientRiskFactor {
	private Boolean hasRisk = null;

	/**
	 * @return A textual description of what the patient's status is that's causing the risk.
	 */
	abstract public String getDescription();

	/**
	 * @return A boolean indicating whether the patient has the increased risk.
	 */
	abstract protected boolean hasFactor();

	/**
	 * @return A boolean indicating whether the patient has the increased risk.
	 */
	public boolean hasRiskFactor() {
		if (hasRisk == null)
			hasRisk = hasFactor();
		return hasRisk;
	}
}
