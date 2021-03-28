package edu.ncsu.csc.itrust.action;

import java.util.Arrays;
import java.util.List;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.validate.LabProcedureValidator;
/**
 * Class for LabProcUAP.jsp.  Handles lab procedures for UAPs
 */
public class LabProcUAPAction {
	private TransactionDAO transDAO;
	private LabProcedureDAO lpDAO;
	long loggedInMID;
	private LabProcedureValidator validator;
	private DAOFactory factory;

/**
 * Setup 
 * @param factory The DAOFactory used to create the DAOs used in this action.
 * @param loggedInMID UAP who is logged in
 */
	public LabProcUAPAction(DAOFactory factory, long loggedInMID) {
		transDAO = factory.getTransactionDAO();
		lpDAO = factory.getLabProcedureDAO();
		this.loggedInMID = loggedInMID;
		validator = new LabProcedureValidator();
		this.factory = factory;
	}
	
	/**
	 * Updates a lab procedure
	 * 
	 * @param b the procedure to update
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public void updateProcedure(LabProcedureBean b) throws DBException, FormValidationException{
		validator.validate(b);
		//need to check if status is what's being changed - if new status!=old status send email
		if(!b.getStatus().equals(lpDAO.getLabProcedure(b.getProcedureID()).getStatus())){
			new EmailUtil(factory).sendEmail(makeEmail(b));
		}
		lpDAO.updateLabProcedure(b);
		transDAO.logTransaction(TransactionType.ENTER_EDIT_LAB_PROCEDURE, loggedInMID, 
				b.getPid(), "UAP updated procedure id: "
				+ b.getProcedureID());
	}
	
	/**
	 * Sends an e-mail informing the patient that their procedure has been updated
	 * 
	 * @param b the procedure that was updated
	 * @return an e-mail to the patient with the notice
	 * @throws DBException
	 */
	private Email makeEmail(LabProcedureBean b) throws DBException{
		
		PatientBean p = new PatientDAO(factory).getPatient(b.getPid());
		
		Email email = new Email();
		email.setFrom("no-reply@itrust.com");
		email.setToList(Arrays.asList(p.getEmail()));
		email.setSubject("A Lab Procedure Was Updated");
		email.setBody(String.format("Dear %s, \n Your Lab Procedure (%s) has a new updated status of %s. Log on to iTrust to view.",  p.getFullName(),b.getLoinc(),b.getStatus()));
		return email;
	}
	
	/**
	 * Returns a list of all the lab procedures
	 * 
	 * @param id MID of the UAP viewing the procedures 
	 * @return a list of all the lab procedures for that UAP
	 * @throws DBException
	 */
	public List<LabProcedureBean> viewProcedures(long id) throws DBException {
		transDAO.logTransaction(TransactionType.VIEW_LAB_PROCEDURE, loggedInMID,
				id, "UAP viewed procedures");
		return lpDAO.getAllLabProceduresDate(id);
	}
}
