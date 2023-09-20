package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Checks if the patient had the given diagnoses during childhood.
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class ChildhoodInfectionFactor extends PatientRiskFactor {
	private final double[] icdCodes;
	private final long patientID;
	private final DAOFactory factory;

	public ChildhoodInfectionFactor(DAOFactory factory, long mid, double... icdCodes) {
		this.factory = factory;
		this.patientID = mid;
		this.icdCodes = icdCodes.clone();
	}

	public String getDescription() {
		return "This patient had a viral infection during their childhood that would increase the risk for this diesease";
	}

	public boolean hasFactor() {
		try {
			return factory.getRiskDAO().hadChildhoodInfection(patientID, icdCodes);
		} catch (DBException e) {
			System.err.println("DBException on Childhood Infection Risk, giving no risk");
			return false;
		}
	}
}
