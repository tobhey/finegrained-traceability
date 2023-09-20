package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.AllergyBean;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.FamilyMemberBean;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.dao.mysql.FamilyDAO;
import edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO;
import edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.NoHealthRecordsException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.risk.ChronicDiseaseMediator;
import edu.ncsu.csc.itrust.risk.RiskChecker;
import edu.ncsu.csc.itrust.validate.AllergyBeanValidator;


/**
 * Edits the patient health record for a given patient Used by editPHR.jsp
 * 
 * @author laurenhayward
 * 
 */
public class EditPHRAction extends PatientBaseAction {
	private DAOFactory factory;
	private PatientDAO patientDAO;
	private AllergyDAO allergyDAO;
	private FamilyDAO familyDAO;
	private HealthRecordsDAO hrDAO;
	private OfficeVisitDAO ovDAO;
	private TransactionDAO transDAO;
	private ICDCodesDAO icdDAO;
	private long loggedInMID;
	private ChronicDiseaseMediator diseaseMediator;
	private PersonnelDAO personnelDAO;
	private PersonnelBean HCPUAP;
	private PatientBean patient;
	private EmailUtil emailutil;
	
	/**
	 * Super class validates the patient id
	 * 
	 * @param factory The DAOFactory to be used in creating DAOs for this action.
	 * @param loggedInMID The MID of the currently logged in user who is authorizing this action.
	 * @param pidString The MID of the patient whose personal health records are being added.
	 * @throws iTrustException
	 * @throws DBException
	 * @throws NoHealthRecordsException
	 */
	public EditPHRAction(DAOFactory factory, long loggedInMID, String pidString) throws iTrustException,
			DBException {
		super(factory, pidString);
		this.patientDAO = factory.getPatientDAO();
		this.allergyDAO = factory.getAllergyDAO();
		this.familyDAO = factory.getFamilyDAO();
		this.hrDAO = factory.getHealthRecordsDAO();
		this.ovDAO = factory.getOfficeVisitDAO();
		this.transDAO = factory.getTransactionDAO();
		this.icdDAO = factory.getICDCodesDAO();
		this.loggedInMID = loggedInMID;
		this.personnelDAO = factory.getPersonnelDAO();
		this.HCPUAP = personnelDAO.getPersonnel(loggedInMID);
		this.patient = patientDAO.getPatient(pid);
		emailutil = new EmailUtil(factory);
		this.factory = factory;
	}

	/**
	 * Adds an allergy to the patient's records
	 * 
	 * @param pid
	 * @param description
	 * @return "Allergy Added", exception message, a list of invalid fields, or "" (only if description is
	 *         null)
	 * @throws iTrustException
	 */
	public String updateAllergies(long pid, String description) throws iTrustException {
		
		emailutil.sendEmail(makeEmail());
		
		if (description != null && !description.equals("")) {
			try {
				AllergyBean bean = new AllergyBean();
				bean.setDescription(description);
				AllergyBeanValidator abv = new AllergyBeanValidator();
				abv.validate(bean);
				allergyDAO.addAllergy(pid, description);
				transDAO.logTransaction(TransactionType.ENTER_EDIT_PHR, loggedInMID, pid, "EditPHR - added allergy " + description);
				return "Allergy Added";
			} catch (FormValidationException e2) {
				return "<span class=error>Error: Invalid Fields--" + e2.getErrorList() + "</span><p />";
			}
		} else
			return "";
	}

	/**
	 * Returns a PatientBean for the patient
	 * 
	 * @return PatientBean
	 * @throws iTrustException
	 */
	public PatientBean getPatient() throws iTrustException {
		transDAO.logTransaction(TransactionType.VIEW_RECORDS, loggedInMID, pid, "EditPHR - view patient record ");
		return patientDAO.getPatient(pid);
	}

	/**
	 * Returns a list of AllergyBeans for the patient
	 * 
	 * @return list of AllergyBeans
	 * @throws iTrustException
	 */
	public List<AllergyBean> getAllergies() throws iTrustException {
		return allergyDAO.getAllergies(pid);
	}

	/**
	 * Returns a list of FamilyMemberBeans for the patient
	 * 
	 * @return list of FamilyMemberBeans
	 * @throws iTrustException
	 */
	public List<FamilyMemberBean> getFamily() throws iTrustException {
		List<FamilyMemberBean> fam = new ArrayList<FamilyMemberBean>();
		List<FamilyMemberBean> parents = null;
		parents = familyDAO.getParents(pid);
		fam.addAll(parents);
		fam.addAll(familyDAO.getSiblings(pid));
		fam.addAll(familyDAO.getChildren(pid));

		
		if(parents != null) {
			List<FamilyMemberBean> grandparents = new ArrayList<FamilyMemberBean>();
			for(FamilyMemberBean parent : parents) {
				grandparents.addAll(familyDAO.getParents(parent.getMid()));
			}
			
			fam.addAll(grandparents);
			
			for(FamilyMemberBean gp : grandparents) {
				gp.setRelation("Grandparent");
			}
		}
		transDAO.logTransaction(TransactionType.VIEW_RECORDS, loggedInMID, pid, "EditPHR - view patient family information ");
		return fam;
	}


	/**
	 * Returns a list of HealthRecords for the patient
	 * 
	 * @return
	 * @throws iTrustException
	 */
	public List<HealthRecord> getAllHealthRecords() throws iTrustException {
		List<HealthRecord> allHealthRecords = hrDAO.getAllHealthRecords(pid);
		transDAO.logTransaction(TransactionType.VIEW_HEALTH_RECORDS, loggedInMID, pid, "EditPHR - Viewed patient records");
		return allHealthRecords;
	}

	/**
	 * Returns a list of OfficeVisitBeans
	 * 
	 * @return
	 * @throws iTrustException
	 */
	public List<OfficeVisitBean> getAllOfficeVisits() throws iTrustException {
		transDAO.logTransaction(TransactionType.VIEW_OFFICE_VISIT, loggedInMID, pid, "EditPHR - view patient office visits ");
		return ovDAO.getAllOfficeVisits(pid);
	}

	/**
	 * Returns a complete OfficeVisitBean given a visitID
	 * 
	 * @return
	 * @throws iTrustException
	 */
	public OfficeVisitBean getCompleteOfficeVisit(long visitID) throws iTrustException {
		return ovDAO.getOfficeVisit(visitID);
	}
	
	/**
	 * Returns a list of diseases for which the patient is at risk
	 * 
	 * @return list of RiskCheckers
	 * @throws iTrustException
	 * @throws DBException
	 */
	public List<RiskChecker> getDiseasesAtRisk() throws NoHealthRecordsException,iTrustException,DBException {
		this.diseaseMediator = new ChronicDiseaseMediator(factory, pid);
		transDAO.logTransaction(TransactionType.IDENTIFY_RISK_FACTORS, loggedInMID, pid, "Check for risk factors");
		return diseaseMediator.getDiseaseAtRisk();
	}
	
	/**
	 * Checks to see if a particular family member has high blood pressure
	 * 
	 * @param member the family member to check
	 * @return true if the family member has high blood pressure.  False if there are no records or the family member does not have high blood pressure
	 * @throws iTrustException
	 */
	public boolean doesFamilyMemberHaveHighBP(FamilyMemberBean member) throws iTrustException {
		List<HealthRecord> records = hrDAO.getAllHealthRecords(member.getMid());
		if(records.size() == 0)
			return false;
		for(HealthRecord record : records) {
			if(record.getBloodPressureSystolic() > 240 || record.getBloodPressureDiastolic() > 120 )
				return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a particular family member has high cholesterol
	 * 
	 * @param member the family member to check
	 * @return true if the family member has high cholesterol.  False if there are no records or the family member does not
	 * @throws iTrustException
	 */	
	public boolean doesFamilyMemberHaveHighCholesterol(FamilyMemberBean member) throws iTrustException {
		List<HealthRecord> records = hrDAO.getAllHealthRecords(member.getMid());
		if(records.size() == 0)
			return false;
		for(HealthRecord record : records) {
			if(record.getCholesterolHDL() < 35 || record.getCholesterolLDL() > 250 )
				return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a particular family member has diabetes
	 * 
	 * @param member the family member to check
	 * @return true if the family member has diabetes.  False if there are no records or the family member does not
	 * @throws iTrustException
	 */	

	public boolean doesFamilyMemberHaveDiabetes(FamilyMemberBean member) throws iTrustException {
		List<DiagnosisBean> diagnoses = patientDAO.getDiagnoses(member.getMid());
		if(diagnoses.size() == 0)
			return false;
		for(DiagnosisBean diag : diagnoses) {
			if(diag.getICDCode().startsWith("250"))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a particular family member has cancer
	 * 
	 * @param member the family member to check
	 * @return true if the family member has cancer.  False if there are no records or the family member does not
	 * @throws iTrustException
	 */	

	public boolean doesFamilyMemberHaveCancer(FamilyMemberBean member) throws iTrustException {
		List<DiagnosisBean> diagnoses = patientDAO.getDiagnoses(member.getMid());
		if(diagnoses.size() == 0)
			return false;
		for(DiagnosisBean diag : diagnoses) {
			if(diag.getICDCode().startsWith("199"))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a particular family member has heart disease
	 * 
	 * @param member the family member to check
	 * @return true if the family member has heart disease.  False if there are no records or the family member does not
	 * @throws iTrustException
	 */	
	public boolean doesFamilyMemberHaveHeartDisease(FamilyMemberBean member) throws iTrustException {
		List<DiagnosisBean> diagnoses = patientDAO.getDiagnoses(member.getMid());
		if(diagnoses.size() == 0)
			return false;
		for(DiagnosisBean diag : diagnoses) {
			if(diag.getICDCode().startsWith("402"))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a particular family member smokes
	 * 
	 * @param member the family member to check
	 * @return true if the family member smokes.  False if there are no records or the family member does not
	 * @throws iTrustException
	 */	
	public boolean isFamilyMemberSmoker(FamilyMemberBean member) throws iTrustException {
		List<HealthRecord> records = hrDAO.getAllHealthRecords(member.getMid());
		if(records.size() == 0)
			return false;
		for(HealthRecord record : records) {
			if(record.isSmoker())
				return true;
		}
		return false;
	}
	

	/**
	 * Checks to see if the family member is dead and returns their cause of death if so
	 * 
	 * @param member the family member to check
	 * @return the cause of death if there is one; otherwise null
	 * @throws iTrustException
	 */
	public String getFamilyMemberCOD(FamilyMemberBean member) throws iTrustException {
		PatientBean patient = patientDAO.getPatient(member.getMid());
		if(patient.getCauseOfDeath() == null)
			return "";
		DiagnosisBean diag = icdDAO.getICDCode(patient.getCauseOfDeath());
		if(diag == null)
			return "";
		return diag.getDescription();
	}
	
	/**
	 * Creates a fake e-mail to notify the user that their records have been altered.
	 * 
	 * @return the e-mail to be sent
	 * @throws DBException
	 */
	private Email makeEmail() throws DBException{

		Email email = new Email();
		List<PatientBean> reps = patientDAO.getRepresenting(patient.getMID());
		
		List<String> toAddrs = new ArrayList<String>();
		toAddrs.add(patient.getEmail());
		for (PatientBean r: reps) {
			toAddrs.add(r.getEmail());
		}
		
		email.setFrom("no-reply@itrust.com");
    	email.setToList(toAddrs); // patient and personal representative
    	email.setSubject(String.format("Your medical records have been altered"));
    	email.setBody("Health care professional "+ HCPUAP.getFullName() +" has altered your medical records. " +
    				"She is not on your list of designated health care professionals.");
		return email;
	}
}
