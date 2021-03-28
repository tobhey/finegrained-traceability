package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.CPTCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.ProcedureBeanValidator;

/**
 * Updates the CPT Code (Medical Procedures) List Used by editCPTProcedureCodes.jsp
 * 
 * The CPT code set accurately describes medical, surgical, and diagnostic services 
 * and is designed to communicate uniform information about medical services and procedures 
 * among physicians, coders, patients, accreditation organizations, and payers for administrative, 
 * financial, and analytical purposes.
 *
 * @see http://www.ama-assn.org/ama/pub/physician-resources/solutions-managing-your-practice/coding-billing-insurance/cpt/about-cpt.shtml
 * @author laurenhayward
 */
public class UpdateCPTCodeListAction {
	private long loggedInMID;
	private TransactionDAO transDAO;
	private CPTCodesDAO cptDAO;
	private ProcedureBeanValidator validator = new ProcedureBeanValidator();

	/**
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the administrator who is updating the CPTs.
	 */
	public UpdateCPTCodeListAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.transDAO = factory.getTransactionDAO();
		this.cptDAO = factory.getCPTCodesDAO();
	}

	/**
	 * Adds a new cpt code (med procedure)
	 * 
	 * @param proc
	 *            ProcedureBean that holds the new cpt code
	 * @return status message
	 * @throws FormValidationException
	 */
	public String addCPTCode(ProcedureBean proc) throws FormValidationException {
		validator.validate(proc);
		try {
			if (cptDAO.addCPTCode(proc)) {
				transDAO.logTransaction(TransactionType.MANAGE_PROCEDURE_CODE, loggedInMID, 0L,
						"added CPT code " + proc.getCPTCode());
				return "Success: " + proc.getCPTCode() + " - " + proc.getDescription() + " added";
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
	 * Updates a procedure with new information from the ProcedureBean
	 * 
	 * @param proc
	 *            ProcedureBean with new information (but same CPT code)
	 * @return Status message
	 * @throws FormValidationException
	 */
	public String updateInformation(ProcedureBean proc) throws FormValidationException {
		validator.validate(proc);
		try {
			int rows = updateCode(proc);
			if (0 == rows) {
				return "Error: Code not found. To edit an actual code, "
						+ "change the description and add a new code with the old description";
			} else {
				transDAO.logTransaction(TransactionType.MANAGE_PROCEDURE_CODE, loggedInMID, 0L,
						"updated CPT code " + proc.getCPTCode());
				return "Success: " + rows + " row(s) updated";
			}
		} catch (DBException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * Updates the cpt code
	 * 
	 * @param proc the code to be updated
	 * @return updated code
	 * @throws DBException
	 */
	private int updateCode(ProcedureBean proc) throws DBException {
		return cptDAO.updateCode(proc);
	}
}
