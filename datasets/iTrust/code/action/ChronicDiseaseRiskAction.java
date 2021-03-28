package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.risk.ChronicDiseaseMediator;
import edu.ncsu.csc.itrust.risk.RiskChecker;

/**
 * 
 * Used for chronicDiseaseRisks.jsp. Passes most of the logic off to {@link ChronicDiseaseMediator}, and the
 * various subclasses of {@link RiskChecker}.
 * 
 * @author Andy Meneely
 * 
 */
public class ChronicDiseaseRiskAction extends PatientBaseAction {
	private AuthDAO authDAO;
	private long loggedInMID;
	private ChronicDiseaseMediator diseaseMediator;
	private TransactionDAO transDAO;

	/**
	 * 
	 * @param factory
	 * @param loggedInMID
	 * @param pidString
	 *            The patient ID to be validated and used
	 * @throws iTrustException
	 * @throws DBException
	 * @throws NoHealthRecordsException
	 *             This is thrown if a patient is added without any health records to be checked. Try to avoid
	 *             having this exception be thrown in a normal flow of events.
	 * @author Andy Meneely
	 */
	public ChronicDiseaseRiskAction(DAOFactory factory, long loggedInMID, String pidString)
			throws iTrustException, DBException, NoHealthRecordsException {
		super(factory, pidString);
		this.authDAO = factory.getAuthDAO();
		this.transDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
		this.diseaseMediator = new ChronicDiseaseMediator(factory, pid);
	}

	/**
	 * Returns the ID of the patient to be checked.
	 * 
	 * @return patient ID whose risk we are checking
	 * @author Andy Meneely
	 */
	public long getPatientID() {
		return pid;
	}

	/**
	 * Gives the full name of the patient
	 * 
	 * @return Full name of the patient who we are checking
	 * @throws DBException
	 * @throws iTrustException
	 * @author Andy Meneely
	 */
	public String getUserName() throws DBException, iTrustException {
		return authDAO.getUserName(pid);
	}

	/**
	 * Returns the risks for which this patient is at risk for. All logic has been passed to
	 * {@link ChronicDiseaseMediator} and the subclasses of {@link RiskChecker}.
	 * 
	 * @return List of risks
	 * @throws iTrustException
	 * @throws DBException
	 * @author Andy Meneely
	 */
	public List<RiskChecker> getDiseasesAtRisk() throws iTrustException, DBException {
		transDAO.logTransaction(TransactionType.IDENTIFY_RISK_FACTORS, loggedInMID, pid, "");
		return diseaseMediator.getDiseaseAtRisk();
	}
}
