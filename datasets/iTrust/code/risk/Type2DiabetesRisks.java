package edu.ncsu.csc.itrust.risk;

import static edu.ncsu.csc.itrust.enums.Ethnicity.AfricanAmerican;
import static edu.ncsu.csc.itrust.enums.Ethnicity.AmericanIndian;
import static edu.ncsu.csc.itrust.enums.Ethnicity.Asian;
import static edu.ncsu.csc.itrust.enums.Ethnicity.Hispanic;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;
import edu.ncsu.csc.itrust.risk.factors.AgeFactor;
import edu.ncsu.csc.itrust.risk.factors.CholesterolFactor;
import edu.ncsu.csc.itrust.risk.factors.EthnicityFactor;
import edu.ncsu.csc.itrust.risk.factors.FamilyHistoryFactor;
import edu.ncsu.csc.itrust.risk.factors.HypertensionFactor;
import edu.ncsu.csc.itrust.risk.factors.PatientRiskFactor;
import edu.ncsu.csc.itrust.risk.factors.PriorDiagnosisFactor;
import edu.ncsu.csc.itrust.risk.factors.WeightFactor;

/**
 * Checks to see if the given patient is at risk for Type II Diabetes.
 * For details on what each method does, see {@link RiskChecker}
 * 
 * @author Andy
 * 
 */
public class Type2DiabetesRisks extends RiskChecker {
	public Type2DiabetesRisks(DAOFactory factory, long patientID) throws DBException,
			NoHealthRecordsException {
		super(factory, patientID);
	}

	@Override
	public boolean qualifiesForDisease() {
		return patient.getAge() >= 12;
	}

	@Override
	protected List<PatientRiskFactor> getDiseaseRiskFactors() {
		List<PatientRiskFactor> factors = new ArrayList<PatientRiskFactor>();
		factors.add(new AgeFactor(patient, 45));
		factors.add(new EthnicityFactor(patient, Hispanic, AfricanAmerican, AmericanIndian, Asian));
		factors.add(new WeightFactor(currentHealthRecord, 25));
		factors.add(new HypertensionFactor(currentHealthRecord));
		factors.add(new CholesterolFactor(currentHealthRecord));
		factors.add(new PriorDiagnosisFactor(factory, patient.getMID(), 250.0, 251.0));
		factors.add(new FamilyHistoryFactor(factory, patient.getMID(), 250.0, 251.0));
		return factors;
	}

	@Override
	public String getName() {
		return "Type 2 Diabetes";
	}
}
