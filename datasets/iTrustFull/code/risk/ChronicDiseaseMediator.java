package edu.ncsu.csc.itrust.risk;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;

/**
 * Figures out which risk checker strategy to use
 * 
 * @author Andy
 * 
 */
public class ChronicDiseaseMediator {
	private List<RiskChecker> riskCheckers;

	/**
	 * Given a factory and patient ID, and sets up a list of all risk checkers
	 * 
	 * @param factory The DAOFactory to be used in creating the necessary DAOs to perform the checks.
	 * @param pid The MID of the patient in question.
	 * @throws DBException
	 * @throws NoHealthRecordsException
	 */
	public ChronicDiseaseMediator(DAOFactory factory, long pid) throws DBException, NoHealthRecordsException {
		riskCheckers = new ArrayList<RiskChecker>();
		riskCheckers.add(new HeartDiseaseRisks(factory, pid));
		riskCheckers.add(new Type1DiabetesRisks(factory, pid));
		riskCheckers.add(new Type2DiabetesRisks(factory, pid));
	}

	/**
	 * @return A list of diseases that this patient is at risk for
	 */
	public List<RiskChecker> getDiseaseAtRisk() {
		List<RiskChecker> diseases = new ArrayList<RiskChecker>();
		for (RiskChecker diseaseChecker : riskCheckers) {
			if (diseaseChecker.isAtRisk())
				diseases.add(diseaseChecker);
		}
		return diseases;
	}
}
