package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Checks for prior diagnoses by delegatin to the DAO
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class PriorDiagnosisFactor extends PatientRiskFactor {
	private long patientID;
	private DAOFactory factory;
	private double lowerICDCode;
	private double upperICDCode;

	public PriorDiagnosisFactor(DAOFactory factory, long patientID, double lowerICDCode, double upperICDCode) {
		this.factory = factory;
		this.patientID = patientID;
		this.lowerICDCode = lowerICDCode;
		this.upperICDCode = upperICDCode;
	}

	public String getDescription() {
		return "Patient has had related diagnoses";
	}

	public boolean hasFactor() {
		try {
			return factory.getRiskDAO().hadPriorDiagnoses(patientID, lowerICDCode, upperICDCode);
		} catch (DBException e) {
			e.printStackTrace();
			return false;
		}
	}
}
