package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Checks if a patient has a family history of a given diagnosis range. Delegates straight to the DAO.
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class FamilyHistoryFactor extends PatientRiskFactor {
	private long patientID;
	private DAOFactory factory;
	private double icdUpper;
	private double icdLower;

	public FamilyHistoryFactor(DAOFactory factory, long patientID, double icdLower, double icdUpper) {
		this.factory = factory;
		this.patientID = patientID;
		this.icdLower = icdLower;
		this.icdUpper = icdUpper;
	}

	public String getDescription() {
		return "Patient has a family history of this disease";
	}

	public boolean hasFactor() {
		try {
			return factory.getRiskDAO().hasFamilyHistory(patientID, icdLower, icdUpper);
		} catch (DBException e) {
			e.printStackTrace();
			return false;
		}
	}
}
