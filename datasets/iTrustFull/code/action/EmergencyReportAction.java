package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.AllergyBean;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.enums.PrescriptionAlerts;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.beans.ProcedureBean;

/**
 * Creates a new Emergency Report Used by emergencyReport.jsp
 * 
 * @author laurenhayward
 * 
 */
public class EmergencyReportAction extends PatientBaseAction {
	private TransactionDAO transDAO;
	private PatientDAO patientDAO;
	private AllergyDAO allergyDAO;
	private OfficeVisitDAO ovDAO;
	private PatientBean pb;
	private EmailUtil emailutil;
	private long loggedInMID;

	/**
	 * The super class handles validating the pid Logs viewing of the report
	 * 
	 * @param factory The DAOFactory used in creating the DAOs for this action.
	 * @param loggedInMID The MID of the user who is looking at the emergency report.
	 * @param pidString The ID of the patient whose report is being generated.
	 * @throws iTrustException
	 */
	public EmergencyReportAction(DAOFactory factory, long loggedInMID, String pidString) throws iTrustException {
		super(factory, pidString);
		this.patientDAO = factory.getPatientDAO();
		this.allergyDAO = factory.getAllergyDAO();
		this.transDAO = factory.getTransactionDAO();
		this.ovDAO = factory.getOfficeVisitDAO();
		this.loggedInMID = loggedInMID;
		emailutil = new EmailUtil(factory);
		
		pb = patientDAO.getPatient(this.pid);
		transDAO.logTransaction(TransactionType.VIEW_EMERGENCY_REPORT, this.loggedInMID, Long.valueOf(pidString), "viewed emergency report");
		emailutil.sendEmail(makeEmail());
	}

	/**
	 * Returns the patient's name
	 * 
	 * @return patient's full name
	 */
	public String getPatientName() {
		return (pb.getFirstName() + " " + pb.getLastName());
	}

	/**
	 * Returns the patient's age
	 * 
	 * @return patient's age
	 */
	public String getPatientAge() {
		return Integer.toString(pb.getAge());
	}
	
	/**
	 * Returns the patient's gender
	 * 
	 * @return patient's gender
	 */
	public String getPatientGender() {
		return pb.getGender().toString();
	}
	
	/**
	 * Returns the patient's emergency contact
	 * 
	 * @return patient's emergency contact
	 */
	public String getPatientEmergencyContact() {
		return pb.getEmergencyName() + " " + pb.getEmergencyPhone();
	}
	
	/**
	 * Returns the patient's blood type
	 * 
	 * @return the patient's blood type
	 */
	public String getBloodType() {
		return pb.getBloodType() + "";
	}

	/**
	 * Returns a list of allergies for the given patient
	 * 
	 * @return a list of AllergyBeans
	 * @throws iTrustException
	 */
	public List<AllergyBean> getAllergies() throws iTrustException {
		return allergyDAO.getAllergies(this.pid);
	}

	/**
	 * Returns a list of prescriptions the patient is currently taking
	 * 
	 * @return a list of PrescriptionBeans for which the patient is currently taking
	 * @throws iTrustException
	 */
	public List<PrescriptionBean> getCurrentPrescriptions() throws iTrustException {
		List<PrescriptionBean> allPrescriptions = patientDAO.getCurrentPrescriptions(this.pid);
		ArrayList<PrescriptionBean> warningList = new ArrayList<PrescriptionBean>();
		for (int i = 0; i < allPrescriptions.size(); i++) {
			if (PrescriptionAlerts.isAlert(allPrescriptions.get(i).getMedication().getNDCode()))
				warningList.add(allPrescriptions.get(i));
		}
		return warningList;
	}

	/**
	 * Returns a list of diagnoses that are in the range indicated by the DiagnosisRange enum
	 * 
	 * @return list of DiagnosisBeans
	 * @throws iTrustException
	 */
	public List<DiagnosisBean> getWarningDiagnoses() throws iTrustException {
		try {
			boolean dup = false;
			List<DiagnosisBean> allDiagnoses = patientDAO.getDiagnoses(this.pid);
			ArrayList<DiagnosisBean> warningList = new ArrayList<DiagnosisBean>();
			for (DiagnosisBean bean: allDiagnoses) {
				OfficeVisitBean ovb = ovDAO.getOfficeVisit(bean.getVisitID());

				if(ovb == null){
					continue;
					
				}
				if ("yes".equals(bean.getClassification()) || (ovb.getVisitDate().getTime() > Calendar.getInstance().getTimeInMillis() - 30 * 24 * 60 * 60 * 1000))  {
					for (DiagnosisBean wbean: warningList) {
						if (bean.getDescription().equals(wbean.getDescription())) {
							dup = true;
						}
					}
					if (!dup) {
						warningList.add(bean);
					}
				}
			}
			return warningList;
		} catch (DBException dbe) {
			throw new iTrustException(dbe.getMessage());
		}
	}

	/**
	 * Returns a list of prescriptions the patient is currently taking
	 * 
	 * @return a list of PrescriptionBeans for which the patient is currently taking
	 * @throws iTrustException
	 */
	public List<ProcedureBean> getImmunizations() throws iTrustException {
		List<ProcedureBean> allImmunizations = patientDAO.getImmunizationProcedures(this.pid);
		return allImmunizations;
	}
	
	/**
	 * Creates a fake e-mail to notify the user that an emergency report has been created and viewed.
	 * 
	 * @return the e-mail to be sent
	 * @throws DBException
	 */
	private Email makeEmail() throws DBException{

		Email email = new Email();
		List<PatientBean> reps = patientDAO.getRepresenting(pb.getMID());
		
		List<String> toAddrs = new ArrayList<String>();
		toAddrs.add(pb.getEmail());
		for (PatientBean r: reps) {
			toAddrs.add(r.getEmail());
		}
		
		email.setFrom("no-reply@itrust.com");
    	email.setToList(toAddrs); // patient and personal representative
    	email.setSubject(String.format("Emergency Report Viewed Notification"));
    	email.setBody("Dear " + pb.getFullName() + ",\n An emergency report has been generated. " + 
    			"Please login to iTrust to see who has viewed your records.");
		return email;
	}
}
