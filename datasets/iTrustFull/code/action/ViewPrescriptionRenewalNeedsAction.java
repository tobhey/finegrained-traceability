package edu.ncsu.csc.itrust.action;


import java.util.List;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.Messages;

/*
 * This action returns a list of patients with a special-diagnosis-history who
 * have the logged in HCP as a DHCP and whose medications are going to
 * expire within seven days. 
 */
public class ViewPrescriptionRenewalNeedsAction {
	private PatientDAO patientDAO;
	private TransactionDAO transDAO;
	private long loggedInMID;

	/**
	 * Set up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the prescription renewals.
	 */
	public ViewPrescriptionRenewalNeedsAction(DAOFactory factory, long loggedInMID) {
		this.patientDAO = factory.getPatientDAO();
		this.transDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Uses PatientDAO as a helper class to return a list of patients with a special-diagnosis-history who
	 * have the logged in HCP as a DHCP and whose medications are going to
	 * expire within seven days.
	 * 
	 * @return A list of PatientBean's
	 */
	public List<PatientBean> getRenewalNeedsPatients() {
	
		try {	
			transDAO.logTransaction(TransactionType.VIEW_RENEWAL_NEEDS_PATIENTS, loggedInMID, 
					0L, loggedInMID + Messages.getString("ViewPrescriptionRenewalNeedsAction.0")); //$NON-NLS-1$
			return patientDAO.getRenewalNeedsPatients(loggedInMID);
		}
		catch (DBException e) {
			System.out.println(Messages.getString("ViewPrescriptionRenewalNeedsAction.1")); //$NON-NLS-1$
			System.out.println(e);
			return null;
		}
	}
	
}
