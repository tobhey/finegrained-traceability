package edu.ncsu.csc.itrust.action;

import java.util.List;
import java.util.ArrayList;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.validate.LabProcedureValidator;

/**
 * Action class for LabProcHCP.jsp.
 * 
 * @extends LabProcUAPAction
 */
public class LabProcHCPAction extends LabProcUAPAction {
	private TransactionDAO transDAO;
	private LabProcedureDAO lpDAO;
	private OfficeVisitDAO ovDAO;
	long loggedInMID;
	private LabProcedureValidator validator;

	
	/**
	 * Sets up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID  MID for the logged in HCP
	 */
	
	public LabProcHCPAction(DAOFactory factory, long loggedInMID) {
		super(factory, loggedInMID);
		transDAO = factory.getTransactionDAO();
		lpDAO = factory.getLabProcedureDAO();
		ovDAO = factory.getOfficeVisitDAO();
		this.loggedInMID = loggedInMID;
		validator = new LabProcedureValidator();

	}

	/**
	 * This method sorts by LOINC and returns the list
	 * 
	 * @param id the ID to sort the list by
	 * @return List sorted by LOINC
	 */
	public List<LabProcedureBean> sortByLOINC(long id) throws DBException {
		return lpDAO.getAllLabProceduresLOINC(id);
	}

	/**
	 * Returns a list of all the lab procedures for the next month
	 * 
	 * @return all the lab procedures for the next month
	 */
	public List<LabProcedureBean> getLabProcForNextMonth() throws DBException {
		List<LabProcedureBean> listLabProc = new ArrayList<LabProcedureBean>(0);
		List<OfficeVisitBean> listOV = ovDAO.getAllOfficeVisitsForLHCP(loggedInMID);
		for (OfficeVisitBean ov : listOV) {
			if (listLabProc.isEmpty() == true) {
				listLabProc = lpDAO.getLabProceduresForLHCPForNextMonth(ov.getID());
			}
			else {
				for (LabProcedureBean lb : lpDAO.getLabProceduresForLHCPForNextMonth(ov.getID())) {
					listLabProc.add(lb);
				}
			}
		}
		return listLabProc;
	}

	/**
	 * Changes the privacy settings
	 * 
	 * @param x the ID of the procedure to change 
	 * 
	 */
	public void changePrivacy(long x) throws DBException, FormValidationException {
		LabProcedureBean pb = lpDAO.getLabProcedure(x);
		if (checkAccess(x)) {
			if (pb.getRights().equals(LabProcedureBean.Restrict)) {
				pb.allow();
			} else {
				pb.restrict();
			}
			validator.validate(pb);
			lpDAO.updateRights(pb);
			transDAO.logTransaction(TransactionType.ENTER_EDIT_LAB_PROCEDURE, loggedInMID, pb.getPid(),
					"Privacy Changed procedure id: " + pb.getProcedureID());
		}

	}
	
	/**
	 * Checks to see if the logged in HCP is the one who made the procedure.  Used to generate links for page to edit OfficeVisit
	 * 
	 * @param x the ID of the HCP to check
	 * 
	 */
	public boolean checkAccess(long x) throws DBException, FormValidationException {
		LabProcedureBean pb = lpDAO.getLabProcedure(x);
		OfficeVisitBean ovbean = ovDAO.getOfficeVisit(pb.getOvID());
		return (loggedInMID == ovbean.getHcpID());

	}
}
