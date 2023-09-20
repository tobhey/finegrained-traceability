package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for Document Office Visit page (documentOfficeVisit.jsp). This just adds an empty office visit, and
 * provides a list of office visits in case you want to edit an old office visit.
 * 
 * Very similar to {@link AddPatientAction}
 * 
 * @author Andy Meneely
 * 
 */
public class AddOfficeVisitAction extends PatientBaseAction {
	private DAOFactory factory;
	private OfficeVisitDAO ovDAO;
	private TransactionDAO transDAO;

	/**
	 * Sets up the defaults for the class
	 * @param factory
	 * @param pidString
	 *            Patient ID to be validated by the superclass, {@link PatientBaseAction}
	 * @throws iTrustException
	 */
	public AddOfficeVisitAction(DAOFactory factory, String pidString) throws iTrustException {
		super(factory, pidString);
		this.factory = factory;
		this.transDAO = factory.getTransactionDAO();
		ovDAO = factory.getOfficeVisitDAO();
	}

	/**
	 * Adds an empty office visit
	 * 
	 * @param loggedInMID
	 *            For logging purposes
	 * @return Office visit ID (primary key) of the new office visit
	 * @throws DBException
	 */
	public long addEmptyOfficeVisit(long loggedInMID) throws DBException {
		OfficeVisitBean ov = new OfficeVisitBean();
		ov.setHcpID(loggedInMID);
		ov.setPatientID(pid);
		long visitID = ovDAO.add(ov);
		transDAO.logTransaction(TransactionType.DOCUMENT_OFFICE_VISIT, loggedInMID, pid, "visit id: "
				+ visitID);
		return visitID;
	}

	/**
	 * Lists all office visits for a particular patient, regardless of who originally documented the office
	 * visit.
	 * 
	 * @return List of office visits,
	 * @throws iTrustException
	 */
	public List<OfficeVisitBean> getAllOfficeVisits() throws iTrustException {
		return ovDAO.getAllOfficeVisits(pid);
	}

	/**
	 * Returns the full name of the patient with this MID
	 * 
	 * @return name in the form of a string
	 * @throws DBException
	 * @throws iTrustException
	 */
	public String getUserName() throws DBException, iTrustException {
		return factory.getAuthDAO().getUserName(pid);
	}
}
