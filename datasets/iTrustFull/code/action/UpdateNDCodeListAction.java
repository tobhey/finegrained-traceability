package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.MedicationBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.NDCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.MedicationBeanValidator;

/**
 * Handles updating the ND Code (Prescription) List Used by editNDCodes.jsp
 * 
 * The National Drug Code (NDC) is a universal product identifier used in the
 * United States for drugs intended for human use.
 * 
 * @see http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm
 * @author laurenhayward
 */
public class UpdateNDCodeListAction {
	private long performerID = 0;
	private TransactionDAO transDAO;
	private NDCodesDAO ndDAO;
	private MedicationBeanValidator validator = new MedicationBeanValidator();

	/**
	 * Set up defaults.
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param performerID The MID of the user updating the ND lists.
	 */
	public UpdateNDCodeListAction(DAOFactory factory, long performerID) {
		this.performerID = performerID;
		ndDAO = factory.getNDCodesDAO();
		transDAO = factory.getTransactionDAO();
	}

	/**
	 * Adds a new ND Code (prescription) to the list
	 * 
	 * @param med
	 *            The new ND Code to be added
	 * @return Status message
	 * @throws FormValidationException
	 */
	public String addNDCode(MedicationBean med) throws FormValidationException {
		validator.validate(med);
		try {
			if (ndDAO.addNDCode(med)) {
				transDAO.logTransaction(TransactionType.MANAGE_DRUG_CODE, performerID, 0L, "added ND code "
						+ med.getNDCode());
				return "Success: " + med.getNDCode() + " - " + med.getDescription() + " added";
			} else
				return "The database has become corrupt. Please contact the system administrator for assistance.";
		} catch (DBException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (iTrustException e) {
			return e.getMessage();
		}
	}

	/**
	 * Updates the ND Code with new information from the MedicationBean
	 * 
	 * @param med
	 *            the MedicationBean that holds new information but the same code
	 * @return status message
	 * @throws FormValidationException
	 */
	public String updateInformation(MedicationBean med) throws FormValidationException {
		validator.validate(med);
		try {
			int rows = updateCode(med);
			if (0 == rows) {
				return "Error: Code not found.";
			} else {
				transDAO.logTransaction(TransactionType.MANAGE_DRUG_CODE, performerID, 0L, "updated ND code "
						+ med.getNDCode());
				return "Success: " + rows + " row(s) updated";
			}
		} catch (DBException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * Medication information should already be validated
	 * 
	 * @param med
	 * @return
	 * @throws DBException
	 */
	private int updateCode(MedicationBean med) throws DBException {
		return ndDAO.updateCode(med);
	}

}
