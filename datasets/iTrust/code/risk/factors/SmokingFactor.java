package edu.ncsu.csc.itrust.risk.factors;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Risk factor delegates to the DAO to see if a patient has ever smoked in their life
 * See {@link PatientRiskFactor} for details on what each method typically does.
 * 
 * @author Andy
 * 
 */
public class SmokingFactor extends PatientRiskFactor {
	private long patientID;
	private DAOFactory factory;

	public SmokingFactor(DAOFactory factory, long patientID) {
		this.factory = factory;
		this.patientID = patientID;
	}

	public String getDescription() {
		return "Patient is or was a smoker";
	}

	public boolean hasFactor() {
		try {
			return factory.getRiskDAO().hasSmoked(patientID);
		} catch (DBException e) {
			e.printStackTrace();
			return false;
		}
	}
}
