package edu.ncsu.csc.itrust.risk;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.Ethnicity;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;
import edu.ncsu.csc.itrust.risk.factors.ChildhoodInfectionFactor;
import edu.ncsu.csc.itrust.risk.factors.EthnicityFactor;
import edu.ncsu.csc.itrust.risk.factors.FamilyHistoryFactor;
import edu.ncsu.csc.itrust.risk.factors.PatientRiskFactor;

/**
 * Checks to see if the given patient is at risk for Type 1 Diabetes.
 * For details on what each method does, see {@link RiskChecker}
 * 
 * @author Andy
 * 
 */
public class Type1DiabetesRisks extends RiskChecker {
	public Type1DiabetesRisks(DAOFactory factory, long patientID) throws DBException,
			NoHealthRecordsException {
		super(factory, patientID);
	}

	@Override
	public boolean qualifiesForDisease() {
		return patient.getAge() < 12;
	}

	@Override
	protected List<PatientRiskFactor> getDiseaseRiskFactors() {
		List<PatientRiskFactor> factors = new ArrayList<PatientRiskFactor>();
		factors.add(new EthnicityFactor(patient, Ethnicity.Caucasian));
		factors.add(new FamilyHistoryFactor(factory, patient.getMID(), 250.0, 251.0));
		factors.add(new ChildhoodInfectionFactor(factory, patient.getMID(), 79.30));
		return factors;
	}

	@Override
	public String getName() {
		return "Type 1 Diabetes";
	}
}
