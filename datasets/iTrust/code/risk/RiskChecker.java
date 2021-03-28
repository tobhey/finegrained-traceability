package edu.ncsu.csc.itrust.risk;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;
import edu.ncsu.csc.itrust.risk.factors.PatientRiskFactor;

/**
 * An abstract class which has a number of utlity methods for checking risks.
 * 
 * When extending this class, use the methods to fill in all of the necessary information. Then use isAtRisk
 * and getPatientRiskFactors to get the actual risks. See the known subclasses for an example
 * 
 * @author Andy
 * 
 */
abstract public class RiskChecker {
	private static final int RISK_THRESHOLD = 3;
	protected PatientBean patient;
	protected HealthRecord currentHealthRecord;
	protected DAOFactory factory;

	public RiskChecker(DAOFactory factory, long patientID) throws DBException, NoHealthRecordsException {
		this.factory = factory;
		HealthRecordsDAO hrDAO = factory.getHealthRecordsDAO();
		List<HealthRecord> records = hrDAO.getAllHealthRecords(patientID);
		if (records.size() > 0)
			currentHealthRecord = records.get(0);
		else
			throw new NoHealthRecordsException();

		patient = factory.getPatientDAO().getPatient(patientID);
	}

	/**
	 * @return The name of the disease being checked.
	 */
	abstract public String getName();

	/**
	 * @return A java.util.List of the risk factors increasing this patients disease risk.
	 */
	abstract protected List<PatientRiskFactor> getDiseaseRiskFactors();

	/**
	 * @return A boolean indicating whether the patient is at increased risk.
	 */
	abstract public boolean qualifiesForDisease();

	/**
	 * This method exists purely for performance - just stop once you hit the threshold. <br />
	 * <br />
	 * Also, the risk factors should be cached in the RiskFactor implementors - in getPatientRiskFactors there
	 * should be no double-querying
	 * 
	 * @return isAtRisk
	 */
	public boolean isAtRisk() {
		if (qualifiesForDisease()) {
			int numRisks = 0;
			List<PatientRiskFactor> diseaseRiskFactors = getDiseaseRiskFactors();
			for (PatientRiskFactor factor : diseaseRiskFactors) {
				if (factor.hasRiskFactor())
					numRisks++;
				if (numRisks >= RISK_THRESHOLD)
					return true;
			}
		}
		return false; // both an else from qualifies or NOT over the threshold
	}

	/**
	 * @return A java.util.List of patient risk factors associated with this disease.
	 */
	public List<PatientRiskFactor> getPatientRiskFactors() {
		List<PatientRiskFactor> patientRiskFactors = new ArrayList<PatientRiskFactor>();
		List<PatientRiskFactor> diseaseRiskFactors = getDiseaseRiskFactors();
		for (PatientRiskFactor factor : diseaseRiskFactors) {
			if (factor.hasRiskFactor()) {
				patientRiskFactors.add(factor);
			}
		}
		return patientRiskFactors;
	}
}
