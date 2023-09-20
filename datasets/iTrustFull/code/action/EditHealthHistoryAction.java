package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.forms.HealthRecordForm;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.HealthRecordFormValidator;

/**
 * Edits the health history of a patient, used by editBasicHealth.jsp
 * 
 * @author laurenhayward
 * 
 */
public class EditHealthHistoryAction extends PatientBaseAction {
	private TransactionDAO transDAO;
	private HealthRecordsDAO hrDAO;
	private AuthDAO authDAO;
	private long loggedInMID;
	private HealthRecordFormValidator validator = new HealthRecordFormValidator();

	/**
	 * The patient ID is validated by the superclass
	 * 
	 * @param factory The DAOFactory which will be used to generate the DAOs used for this action.
	 * @param loggedInMID The user authorizing this action.
	 * @param pidString The patient (or other user) who is being edited.
	 * @throws iTrustException
	 */
	public EditHealthHistoryAction(DAOFactory factory, long loggedInMID, String pidString)
			throws iTrustException {
		super(factory, pidString);
		this.hrDAO = factory.getHealthRecordsDAO();
		this.authDAO = factory.getAuthDAO();
		this.transDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
	}

	/**
	 * returns the patient name
	 * 
	 * @return patient name
	 * @throws DBException
	 * @throws iTrustException
	 */
	public String getPatientName() throws DBException, iTrustException {
		return authDAO.getUserName(pid);
	}

	/**
	 * Adds a health record for the given patient
	 * 
	 * @param pid  The patient record who is being edited.
	 * @param hr  The filled out health record form to be added.
	 * @return message - "Information Recorded" or exception's message
	 * @throws FormValidationException
	 */
	public String addHealthRecord(long pid, HealthRecordForm hr) throws FormValidationException,
			iTrustException {
		validator.validate(hr);
		HealthRecord record = transferForm(pid, hr);
		hrDAO.add(record);
		transDAO.logTransaction(TransactionType.ENTER_EDIT_PHR, loggedInMID, pid, "EditHealthHistory - Add Record");
		return "Information Recorded";
	}

	
/**
 * Moves the information from the form to a HealthRecord
 * 
 * @param pid Patient of interest
 * @param form Form to be translated
 * @return a HealthRecord containing all the information in the form
 * @throws FormValidationException
 */
	
	private HealthRecord transferForm(long pid, HealthRecordForm form) throws FormValidationException {
		HealthRecord record = new HealthRecord();
		record.setPatientID(pid);
		record.setPersonnelID(loggedInMID);
		record.setBloodPressureD(Integer.valueOf(form.getBloodPressureD()));
		record.setBloodPressureN(Integer.valueOf(form.getBloodPressureN()));
		record.setCholesterolHDL(Integer.valueOf(form.getCholesterolHDL()));
		record.setCholesterolLDL(Integer.valueOf(form.getCholesterolLDL()));
		record.setCholesterolTri(Integer.valueOf(form.getCholesterolTri()));
		if (record.getTotalCholesterol() < 100 || record.getTotalCholesterol() > 600)
			throw new FormValidationException("Total cholesterol must be in [100,600]");
		record.setHeight(Double.valueOf(form.getHeight()));
		record.setWeight(Double.valueOf(form.getWeight()));
		record.setSmoker(Boolean.valueOf(form.getIsSmoker()));
		return record;
	}

	/**
	 * Returns a list of all HealthRecords for the given patient
	 * 
	 * @param pid  The ID of the patient to look up
	 * @return list of HealthRecords
	 * @throws iTrustException
	 */
	public List<HealthRecord> getAllHealthRecords(long pid) throws iTrustException {
		transDAO.logTransaction(TransactionType.VIEW_HEALTH_RECORDS, loggedInMID, pid, "EditHealthHistory - View Records");
		return hrDAO.getAllHealthRecords(pid);
	}
}
