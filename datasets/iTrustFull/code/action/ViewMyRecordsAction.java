package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.AllergyBean;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.FamilyMemberBean;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.ReportRequestBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.dao.mysql.FakeEmailDAO;
import edu.ncsu.csc.itrust.dao.mysql.FamilyDAO;
import edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReportRequestDAO;
import edu.ncsu.csc.itrust.dao.mysql.SurveyDAO;
import edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Handles patients viewing their own records Used by viewMyRecords.jsp
 * 
 * @author laurenhayward
 * 
 */
public class ViewMyRecordsAction {
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private AllergyDAO allergyDAO;
	private FamilyDAO familyDAO;
	private HealthRecordsDAO hrDAO;
	private OfficeVisitDAO ovDAO;
	private SurveyDAO surveyDAO;
	private LabProcedureDAO labDAO;
	private FakeEmailDAO emailDAO;
	private ICDCodesDAO icdDAO;
	private ReportRequestDAO reportRequestDAO;
	private long loggedInMID;

	/**
	 * Set up
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the records.
	 */
	public ViewMyRecordsAction(DAOFactory factory, long loggedInMID) {
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.allergyDAO = factory.getAllergyDAO();
		this.familyDAO = factory.getFamilyDAO();
		this.hrDAO = factory.getHealthRecordsDAO();
		this.ovDAO = factory.getOfficeVisitDAO();
		this.surveyDAO = factory.getSurveyDAO();
		this.labDAO = factory.getLabProcedureDAO();
		this.emailDAO = factory.getFakeEmailDAO();
		this.reportRequestDAO = factory.getReportRequestDAO();
		this.icdDAO = factory.getICDCodesDAO();
		this.loggedInMID = loggedInMID;
	}

	/**
	 * Takes the patient's representee as a param and returns it as a long if the patient represents the input
	 * param
	 * 
	 * @param input
	 *            the patient's representee mid
	 * @return representee's mid as a long
	 * @throws iTrustException
	 */
	public long representPatient(String input) throws iTrustException {

		try {
			long reppeeMID = Long.valueOf(input);
			if (patientDAO.represents(loggedInMID, reppeeMID)) {
				loggedInMID = reppeeMID;
				return reppeeMID;
			} else
				throw new iTrustException("You do not represent patient " + reppeeMID);
		} catch (NumberFormatException e) {
			throw new iTrustException("MID is not a number");
		}
	}

	/**
	 * Returns a PatientBean for the currently logged in patient
	 * 
	 * @return PatientBean for the currently logged in patient
	 * @throws iTrustException
	 */
	public PatientBean getPatient() throws iTrustException {
		return patientDAO.getPatient(loggedInMID);
	}
	
	/**
	 * Returns a PatientBean for the specified MID
	 * @param mid id of the requested bean
	 * @return PatientBean for the specified MID
	 * @throws iTrustException
	 */
	public PatientBean getPatient(long mid) throws iTrustException {
		return patientDAO.getPatient(mid);
	}

	/**
	 * Returns a PersonnelBean for the requested MID
	 * @param mid id of the requested bean
	 * @return a PersonnelBean for the requested MID
	 * @throws iTrustException
	 */
	public PersonnelBean getPersonnel(long mid) throws iTrustException {
		return personnelDAO.getPersonnel(mid);
	}

	/**
	 * Returns a PatientBean for the currently logged in patient
	 * 
	 * @return PatientBean for the currently logged in patient
	 * @throws iTrustException
	 */
	public List<Email> getEmailHistory() throws iTrustException {
		return emailDAO.getEmailsByPerson(getPatient().getEmail());
	}

	/**
	 * Returns a list of AllergyBeans for the currently logged in patient
	 * 
	 * @return a list of AllergyBeans for the currently logged in patient
	 * @throws iTrustException
	 */
	public List<AllergyBean> getAllergies() throws iTrustException {
		return allergyDAO.getAllergies(loggedInMID);
	}

	/**
	 * Returns a list of Parents, Siblings, and Children of the currently logged in patient
	 * 
	 * @return list of FamilyMemberBeans
	 */
	public List<FamilyMemberBean> getFamily() throws iTrustException {
		List<FamilyMemberBean> fam = new ArrayList<FamilyMemberBean>();
		List<FamilyMemberBean> parents = null;
		try {
			parents = familyDAO.getParents(loggedInMID);
			fam.addAll(parents);
			fam.addAll(familyDAO.getSiblings(loggedInMID));
			fam.addAll(familyDAO.getChildren(loggedInMID));
		} catch (DBException e) {
			throw new iTrustException(e.getMessage());
		}
		
		if(parents != null) {
			List<FamilyMemberBean> grandparents = new ArrayList<FamilyMemberBean>();
			for(FamilyMemberBean parent : parents) {
				try {
					grandparents.addAll(familyDAO.getParents(parent.getMid()));
				} catch (DBException e) {
					throw new iTrustException(e.getMessage());
				}
			}
			
			fam.addAll(grandparents);
			
			for(FamilyMemberBean gp : grandparents) {
				gp.setRelation("Grandparent");
			}
		}
		return fam;
	}
	
	/**
	 * Returns a list of Parents, Siblings, and Grand Parents of the currently logged in patient
	 * 
	 * @return list of FamilyMemberBeans
	 */
	public List<FamilyMemberBean> getFamilyHistory() throws iTrustException {
		List<FamilyMemberBean> fam = new ArrayList<FamilyMemberBean>();
		List<FamilyMemberBean> parents = null;
		try {
			parents = familyDAO.getParents(loggedInMID);
			fam.addAll(parents);
			fam.addAll(familyDAO.getSiblings(loggedInMID));
		} catch (DBException e) {
			throw new iTrustException(e.getMessage());
		}
		
		if(parents != null) {
			List<FamilyMemberBean> grandparents = new ArrayList<FamilyMemberBean>();
			for(FamilyMemberBean parent : parents) {
				try {
					grandparents.addAll(familyDAO.getParents(parent.getMid()));
				} catch (DBException e) {
					throw new iTrustException(e.getMessage());
				}
			}
			
			fam.addAll(grandparents);
			
			for(FamilyMemberBean gp : grandparents) {
				gp.setRelation("Grandparent");
			}
		}
		return fam;
	}

	/**
	 * Returns a list of HealthRecords for the currently logged in patient
	 * 
	 * @return a list of HealthRecords for the currently logged in patient
	 * @throws iTrustException
	 */
	public List<HealthRecord> getAllHealthRecords() throws iTrustException {
		return hrDAO.getAllHealthRecords(loggedInMID);
	}
	
	/**
	 * Returns a list of HealthRecords for the specified family member
	 * 
	 * @return a list of HealthRecords for the specified family member
	 * @throws iTrustException
	 */
	public List<HealthRecord> getFamilyHealthRecords(long mid) throws iTrustException {
		return hrDAO.getAllHealthRecords(mid);
	}

	/**
	 * Returns a list of OfficeVisitBeans for all office visits for the currently logged in patient
	 * 
	 * @return  a list of OfficeVisitBeans for all office visits for the currently logged in patient
	 * @throws iTrustException
	 */
	public List<OfficeVisitBean> getAllOfficeVisits() throws iTrustException {
		return ovDAO.getAllOfficeVisits(loggedInMID);
	}

	/**
	 * Returns a complete OfficeVisitBean given a visitID
	 * 
	 * @return a complete OfficeVisitBean given a visitID
	 * @throws iTrustException
	 */
	public OfficeVisitBean getCompleteOfficeVisit(long visitID) throws iTrustException {
		return ovDAO.getOfficeVisit(visitID);
	}
	
	/**
	 * Returns a list of PatientBeans of all patients the currently logged in patient represents
	 * 
	 * @return a list of PatientBeans of all patients the currently logged in patient represents
	 * @throws iTrustException
	 */
	public List<PatientBean> getRepresented() throws iTrustException {
		return patientDAO.getRepresented(loggedInMID);
	}

	/**
	 * Returns a list of PatientBeans of all patients the currently logged in patient represents
	 * 
	 * @return a list of PatientBeans of all patients the currently logged in patient represents
	 * @throws iTrustException
	 */
	public List<PatientBean> getRepresenting() throws iTrustException {
		return patientDAO.getRepresenting(loggedInMID);
	}

	/**
	 * Has a survey been created for this office visit
	 * 
	 * @param visitID ID of the office visit in question
	 * @return true if the survey has been completed, otherwise false
	 * @throws iTrustException
	 */
	public boolean isSurveyCompleted(long visitID) throws iTrustException {
		return surveyDAO.isSurveyCompleted(visitID);
	}

	/**
	 * Returns a list of lab procedures
	 * 
	 * @return a list of lab procedures for the logged in patient
	 * @throws DBException
	 */
	public List<LabProcedureBean> getLabs() throws iTrustException {
		return labDAO.getLabProceduresForPatient(loggedInMID);

	}

	/**
	 * Returns all the report requests for the logged in patient
	 * @return the report requests for the logged in patient
	 * @throws iTrustException
	 */
	public List<ReportRequestBean> getReportRequests() throws iTrustException {
		return reportRequestDAO.getAllReportRequestsForPatient(loggedInMID);
	}
	
	/**
	 * Checks to see if family members have high blood pressure
	 * 
	 * @param member the family member in question
	 * @return true if the family member has the risk factor, otherwise false.
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
	 * Checks to see if family members have high cholesterol
	 * 
	 * @param member the family member in question
	 * @return true if the family member has the risk factor, otherwise false.
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
	 * Checks to see if family members have diabetes
	 * 
	 * @param member the family member in question
	 * @return true if the family member has the risk factor, otherwise false.
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
	 * Checks to see if family members have cancer
	 * 
	 * @param member the family member in question
	 * @return true if the family member has the risk factor, otherwise false.
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
	 * Checks to see if family members have heart disease
	 * 
	 * @param member the family member in question
	 * @return true if the family member has the risk factor, otherwise false.
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
	 * Checks to see if family members smoke
	 * 
	 * @param member the family member in question
	 * @return true if the family member has the risk factor, otherwise false.
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
	 * Checks to see if family member is dead, and if so, what their cause of death was
	 * 
	 * @param member the family member in question
	 * @return the cause of death for the family member
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
}
