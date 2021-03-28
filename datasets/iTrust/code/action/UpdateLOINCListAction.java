package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.LOINCbean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.LOINCDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.LOINCBeanValidator;

/**
 * Handles updating the LOINC Used.
 * 
 * Logical Observation Identifiers Names and Codes (LOINC) is a database and universal
 *  standard for identifying medical laboratory observations. 
 *  
 * @see http://loinc.org/
 */
public class UpdateLOINCListAction {
	private long performerID = 0;
	private LOINCDAO lDAO;
	private TransactionDAO transDAO;
	private LOINCBeanValidator validator = new LOINCBeanValidator();

	/**
	 * Sets up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param performerID The MID of the person updating the LOINCs.
	 */
	public UpdateLOINCListAction(DAOFactory factory, long performerID) {
		this.performerID = performerID;
		transDAO = factory.getTransactionDAO();
		lDAO = factory.getLOINCDAO();
	}

	/**
	 * Adds a new LOINC
	 * 
	 * @param diagn
	 *            New LOINC
	 * @return Status message
	 * @throws FormValidationException
	 */
	public String add(LOINCbean diagn) throws FormValidationException, iTrustException {
		validator.validate(diagn);

		List<LOINCbean> lblist = lDAO.getAllLOINC();

		boolean correctID = false;
		for (int i = 0; i < lblist.size(); i++) {
			if (lblist.get(i).getLabProcedureCode().equals(diagn.getLabProcedureCode())) {
				correctID = true;
				i = lblist.size();
			}
		}
		if (correctID) {
			throw new FormValidationException("Error: Code already exists.");
		}
		lDAO.addLOINC(diagn);
		transDAO.logTransaction(TransactionType.MANAGE_LOINC, performerID, 0L, "added LOINC code "
				+ diagn.getLabProcedureCode());
		return "Success: " + diagn.getLabProcedureCode() + " added";
	}

	/**
	 * Updates a LOINC
	 * 
	 * @param diagn
	 *            new information to update (but same code)
	 * @return Status message
	 * @throws FormValidationException
	 */
	public String updateInformation(LOINCbean diagn) throws FormValidationException {
		validator.validate(diagn);
		try {
			int rows = lDAO.update(diagn);
			if (0 == rows) {
				return "Error: Code not found.";
			} else {
				transDAO.logTransaction(TransactionType.MANAGE_LOINC, performerID, 0L, "updated LOINC code "
						+ diagn.getLabProcedureCode());
				return "Success: " + diagn.getLabProcedureCode() + " updated";
			}
		} catch (DBException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
