package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Edits a patient's personal representatives. Used by hcp/editRepresentatives.jsp
 * 
 * @author laurenhayward
 * 
 */
public class EditRepresentativesAction extends PatientBaseAction {
	private PatientDAO patientDAO;
	private TransactionDAO transDAO;
	private AuthDAO authDAO;
	private long loggedInMID;

	/**
	 * Super class validates the patient mid
	 * 
	 * @param factory The DAOFactory used in creating the DAOs for this action.
	 * @param loggedInMID The MID of the patient editing his/her representatives.
	 * @param pidString The MID of the representative in question.
	 * @throws iTrustException
	 */
	public EditRepresentativesAction(DAOFactory factory, long loggedInMID, String pidString)
			throws iTrustException {
		super(factory, pidString);
		this.loggedInMID = loggedInMID;
		this.transDAO = factory.getTransactionDAO();
		this.patientDAO = factory.getPatientDAO();
		this.authDAO = factory.getAuthDAO();
	}

	/**
	 * Return a list of patients that pid represents
	 * 
	 * @param pid The id of the personnel we are looking up representees for.
	 * @return a list of PatientBeans
	 * @throws iTrustException
	 */
	public List<PatientBean> getRepresented(long pid) throws iTrustException {
		return patientDAO.getRepresented(pid);
	}

	/**
	 * Makes the patient (pid) represent the input mid parameter
	 * 
	 * @param pidString
	 *            the mid of the person who will be represented (the representee)
	 * @return a message
	 * @throws iTrustException
	 */
	public String addRepresentative(String pidString) throws iTrustException {
		try {
			long representee = Long.valueOf(pidString);
			if (authDAO.getUserRole(representee) != Role.PATIENT)
				throw new iTrustException("This user is not a patient!");
			else if (super.pid == representee)
				throw new iTrustException("This user cannot represent themselves.");

			boolean confirm = patientDAO.addRepresentative(pid, representee);
			if (confirm) {
				transDAO.logTransaction(TransactionType.DECLARE_REPRESENTATIVE, loggedInMID, pid, "patient "
						+ pid + " now represents patient " + representee);
				return "Patient represented";
			} else
				return "No change made";
		} catch (NumberFormatException e) {
			return "MID not a number";
		}
	}

	/**
	 * Makes the patient (pid) no longer represent the input mid param
	 * 
	 * @param input
	 *            the mid of the person be represented (representee)
	 * @return a message
	 * @throws iTrustException
	 */
	public String removeRepresentative(String input) throws iTrustException {
		try {
			long representee = Long.valueOf(input);
			boolean confirm = patientDAO.removeRepresentative(pid, representee);
			if (confirm) {
				transDAO.logTransaction(TransactionType.DECLARE_REPRESENTATIVE, loggedInMID, pid, "patient "
						+ pid + " no longer represents patient " + representee);
				return "Patient represented";
			} else
				return "No change made";
		} catch (NumberFormatException e) {
			return "MID not a number";
		}
	}
}
