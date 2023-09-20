package edu.ncsu.csc.itrust.action;


import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;


/**
 * Edits a patient's prescription information.  Used by hcp-uap/editPrescription.jsp
 * @author Ben Smith
 */
public class EditPrescriptionAction {

	private OfficeVisitDAO ovDAO;


	/**
	 * Creates a new action by initializing the office visit
	 * database access object.
	 * 
	 * @param factory
	 * @throws iTrustException
	 */
	public EditPrescriptionAction(DAOFactory factory) throws iTrustException {
		this.ovDAO = factory.getOfficeVisitDAO();

	}

	/**
	 * Edits an existing prescription in the database.
	 * 
	 * @param pres The prescription bean that has been changed.
	 * @throws DBException
	 */
	public void editPrescription(PrescriptionBean pres) throws DBException {
		
		ovDAO.editPrescription(pres);
	}
}
