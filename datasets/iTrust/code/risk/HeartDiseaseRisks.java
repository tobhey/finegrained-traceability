package edu.ncsu.csc.itrust.risk;

import static edu.ncsu.csc.itrust.enums.Ethnicity.AfricanAmerican;
import static edu.ncsu.csc.itrust.enums.Ethnicity.AmericanIndian;
import static edu.ncsu.csc.itrust.enums.Ethnicity.Hispanic;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.Gender;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;
import edu.ncsu.csc.itrust.risk.factors.AgeFactor;
import edu.ncsu.csc.itrust.risk.factors.CholesterolFactor;
import edu.ncsu.csc.itrust.risk.factors.EthnicityFactor;
import edu.ncsu.csc.itrust.risk.factors.FamilyHistoryFactor;
import edu.ncsu.csc.itrust.risk.factors.GenderFactor;
import edu.ncsu.csc.itrust.risk.factors.HypertensionFactor;
import edu.ncsu.csc.itrust.risk.factors.PatientRiskFactor;
import edu.ncsu.csc.itrust.risk.factors.PriorDiagnosisFactor;
import edu.ncsu.csc.itrust.risk.factors.SmokingFactor;
import edu.ncsu.csc.itrust.risk.factors.WeightFactor;

/**
 * Checks to see if the given patient is at risk for Heart Disease
 * For details on what each method does, see {@link RiskChecker}
 * @author Andy
 * 
 */
public class HeartDiseaseRisks extends RiskChecker {
	public HeartDiseaseRisks(DAOFactory factory, long patientID) throws DBException, NoHealthRecordsException {
		super(factory, patientID);
	}

	@Override
	public boolean qualifiesForDisease() {
		return true;
	}

	@Override
	protected List<PatientRiskFactor> getDiseaseRiskFactors() {
		List<PatientRiskFactor> factors = new ArrayList<PatientRiskFactor>();
		factors.add(new GenderFactor(patient, Gender.Male));
		factors.add(new AgeFactor(patient, 45));
		factors.add(new EthnicityFactor(patient, Hispanic, AfricanAmerican, AmericanIndian));
		factors.add(new WeightFactor(currentHealthRecord, 30));
		factors.add(new HypertensionFactor(currentHealthRecord));
		factors.add(new CholesterolFactor(currentHealthRecord));
		factors.add(new SmokingFactor(factory, patient.getMID()));
		factors.add(new PriorDiagnosisFactor(factory, patient.getMID(), 250.0, 251.0));
		factors.add(new FamilyHistoryFactor(factory, patient.getMID(), 350.0, 460.0));
		return factors;
	}

	@Override
	public String getName() {
		return "Heart Disease";
	}
}
