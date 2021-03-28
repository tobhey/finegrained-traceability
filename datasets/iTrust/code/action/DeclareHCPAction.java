package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used by the patient to declare HCPs as "designated", in editHCPs.jsp.
 * 
 * @author Andy Meneely
 * 
 */
public class DeclareHCPAction {
	private TransactionDAO transDAO;
	private PatientDAO patientDAO;
	private AuthDAO authDAO;
	private long loggedInMID;

	/**
	 * Sets up defaults
	 * 
	 * @param factory The DAO factory to be used for generating the DAOs for this action.
	 * @param loggedInMID
	 *            This patient
	 * @author Andy Meneely
	 */
	public DeclareHCPAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.transDAO = factory.getTransactionDAO();
		this.patientDAO = factory.getPatientDAO();
		this.authDAO = factory.getAuthDAO();
	}

	/**
	 * Lists the declared HCPs for this current patient
	 * 
	 * @return Returns a list of the declared HCPs
	 * @throws iTrustException
	 * @author Andy Meneely
	 */
	public List<PersonnelBean> getDeclaredHCPS() throws iTrustException {
		return patientDAO.getDeclaredHCPs(loggedInMID);
	}

	/**
	 * Validate an HCP's MID and declare them, if possible
	 * 
	 * @param hcpStr
	 *            The MID of an HCP to declare
	 * @return A status message,
	 * @throws iTrustException
	 * @author Andy Meneely
	 */
	public String declareHCP(String hcpStr) throws iTrustException {
		try {
			long hcpID = Long.valueOf(hcpStr);
			if (authDAO.getUserRole(hcpID) != Role.HCP)
				throw new iTrustException("This user is not a licensed healthcare professional!");

			boolean confirm = patientDAO.declareHCP(loggedInMID, hcpID);

			if (confirm) {
				transDAO.logTransaction(TransactionType.DECLARE_HCP, loggedInMID, hcpID,
						"patient declared hcp");
				return "HCP successfully declared";
			} else
				return "HCP not declared";
		} catch (NumberFormatException e) {
			throw new iTrustException("HCP's MID not a number");
		} 
	}

	/**
	 * Validate an HCP's MID and undeclare them, if possible
	 * 
	 * @param input
	 *            The MID of an HCP to undeclare
	 * @return
	 * @throws iTrustException
	 * @author Andy Meneely
	 */
	public String undeclareHCP(String input) throws iTrustException {
		try {
			long hcpID = Long.valueOf(input);
			boolean confirm = patientDAO.undeclareHCP(loggedInMID, hcpID);
			if (confirm) {
				transDAO.logTransaction(TransactionType.DECLARE_HCP, loggedInMID, hcpID,
						"patient undeclared hcp");
				return "HCP successfully undeclared";
			} else
				return "HCP not undeclared";
		} catch (NumberFormatException e) {
			throw new iTrustException("HCP's MID not a number");
		} 
	}
}
