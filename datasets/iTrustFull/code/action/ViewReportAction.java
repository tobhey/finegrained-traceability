package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * 
 * Action class for ViewReport.jsp
 *
 */
public class ViewReportAction {
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;

	/**
	 * Set up defaults
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the report.
	 */
	public ViewReportAction(DAOFactory factory, long loggedInMID) {
		patientDAO = factory.getPatientDAO();
		personnelDAO = factory.getPersonnelDAO();
	}

	/**
	 *  Get diagnosis list for the given patient
	 * @param pid the patient of interest
	 * @return list of diagnoses
	 * @throws iTrustException
	 */
	public List<DiagnosisBean> getDiagnoses(long pid) throws iTrustException {
		return patientDAO.getDiagnoses(pid);
	}

	/**
	 *  Get procedure list for the given patient
	 * @param pid the patient of interest
	 * @return list of procedures
	 * @throws iTrustException
	 */
	public List<ProcedureBean> getProcedures(long pid) throws iTrustException {
		return patientDAO.getProcedures(pid);
	}

	/**
	 *  Get prescription list for the given patient
	 * @param pid the patient of interest
	 * @return list of prescriptions
	 * @throws iTrustException
	 */
	public List<PrescriptionBean> getPrescriptions(long pid) throws iTrustException {
		return patientDAO.getCurrentPrescriptions(pid);
	}

	/**
	 *  Get declared HCPs list for the given patient
	 * @param pid the patient of interest
	 * @return list of declared HCPs
	 * @throws iTrustException
	 */
	public List<PersonnelBean> getDeclaredHCPs(long pid) throws iTrustException {
		return patientDAO.getDeclaredHCPs(pid);
	}
	
	/**
	 * Returns a PersonnelBean when given an MID
	 * @param mid HCP of interest
	 * @return PersonnelBean of the given HCP
	 * @throws iTrustException
	 */
	public PersonnelBean getPersonnel(long mid) throws iTrustException {
		return personnelDAO.getPersonnel(mid);
	}
}
