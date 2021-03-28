package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.FakeEmailDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.Messages;

/**
 * Handles retrieving personnel beans for a given personnel Used by viewPersonnel.jsp
 * 
 * @author laurenhayward
 * 
 */
public class ViewPersonnelAction {
	private PersonnelDAO personnelDAO;
	private TransactionDAO transDAO;
	private FakeEmailDAO emailDAO;
	private long loggedInMID;

	/**
	 * Set up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person retrieving personnel beans.
	 */
	public ViewPersonnelAction(DAOFactory factory, long loggedInMID) {
		this.emailDAO = factory.getFakeEmailDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.transDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
	}

	/**
	 * Retrieves a PersonnelBean for the mid passed as a param
	 * 
	 * @param input
	 *            the mid for which the PersonnelBean will be returned
	 * @return PersonnelBean
	 * @throws iTrustException
	 */
	public PersonnelBean getPersonnel(String input) throws iTrustException {
		try {
			long mid = Long.valueOf(input);
			PersonnelBean personnel = personnelDAO.getPersonnel(mid);
			if (personnel != null) {
				transDAO.logTransaction(TransactionType.ENTER_EDIT_DEMOGRAPHICS, loggedInMID, mid,
						Messages.getString("ViewPersonnelAction.0") + mid); //$NON-NLS-1$
				return personnel;
			} else
				throw new iTrustException(Messages.getString("ViewPersonnelAction.1")); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new iTrustException(Messages.getString("ViewPersonnelAction.2")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Returns a PatientBean for the currently logged in personnel
	 * 
	 * @return The PatientBean
	 * @throws iTrustException
	 */
	public List<Email> getEmailHistory() throws iTrustException {
		return emailDAO.getEmailsByPerson(personnelDAO.getPersonnel(loggedInMID).getEmail());
	}

}
