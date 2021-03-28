package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.action.base.PersonnelBaseAction;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.PersonnelValidator;

/**
 * Edits the designated personnel Used by admin/editPersonnel.jsp, staff/editMyDemographics.jsp,
 * editPersonnel.jsp
 * 
 * @author laurenhayward
 * 
 */
public class EditPersonnelAction extends PersonnelBaseAction {
	private long loggedInMID;
	private PersonnelDAO personnelDAO;
	private TransactionDAO transDAO;
	private AuthDAO authDAO;
	private PersonnelValidator validator = new PersonnelValidator();;

	/**
	 * Super class validates the patient id
	 * 
	 * @param factory The DAOFactory used to create the DAOs for this action.
	 * @param loggedInMID The MID of the user editing this personnel.
	 * @param pidString The ID of the user being edited.
	 * @throws iTrustException
	 */
	public EditPersonnelAction(DAOFactory factory, long loggedInMID, String pidString) throws iTrustException {
		super(factory, pidString);

		this.authDAO = factory.getAuthDAO();
		long pidlong = Long.parseLong(pidString);
		Role editor = authDAO.getUserRole(loggedInMID);
		Role editing = authDAO.getUserRole(pidlong);

		if (editor == editing && pidlong != loggedInMID)
			throw new iTrustException("You can only edit your own demographics!");
		else if (editor == Role.HCP && editing == Role.ADMIN || editor == Role.UAP && editing == Role.HCP
				|| editor == Role.ADMIN && editing == Role.UAP)
			throw new iTrustException("You are not authorized to edit this record!");

		this.personnelDAO = factory.getPersonnelDAO();
		this.transDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
	}

	/**
	 * Takes information from the personnelForm param and updates the patient
	 * 
	 * @param personnelForm
	 *            PersonnelBean with new information
	 * @throws iTrustException
	 * @throws FormValidationException
	 */
	public void updateInformation(PersonnelBean personnelForm) throws iTrustException,
			FormValidationException {
		personnelForm.setMID(pid);
		validator.validate(personnelForm);
		personnelDAO.editPersonnel(personnelForm);
		transDAO.logTransaction(TransactionType.CREATE_DISABLE_PATIENT_HCP, loggedInMID);
	}
	
	public void editMessageFilter(String filter) throws iTrustException, FormValidationException {
		PersonnelBean b = personnelDAO.getPersonnel(this.getPid());
		b.setMessageFilter(filter);
		this.updateInformation(b);
	}

}
