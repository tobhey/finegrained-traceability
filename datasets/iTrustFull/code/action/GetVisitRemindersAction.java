package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import edu.ncsu.csc.itrust.beans.forms.VisitReminderReturnForm;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.dao.mysql.VisitRemindersDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.beans.VisitFlag;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.enums.Gender;

/**
 * Gets the VisitReminders for a given patient Used by visitReminders.jsp
 * 
 * @author laurenhayward
 * 
 */
public class GetVisitRemindersAction {

	/**
	 * Reminder Type enumeration.
	 */
	public static enum ReminderType {
		DIAGNOSED_CARE_NEEDERS("Diagnosed Care Needers"),
		FLU_SHOT_NEEDERS("Flu Shot Needers"),
		IMMUNIZATION_NEEDERS("Immunization Needers");

		private String typeName;

		private ReminderType(String typeName) {
			this.typeName = typeName;
		}

		private static final HashMap<String, ReminderType> map = new HashMap<String, ReminderType>();
		static {
			for (ReminderType rt : ReminderType.values()) {
				map.put(rt.getTypeName(), rt);
			}
		}

		/**
		 * Gets the ReminderType for the name passed as a param
		 * 
		 * @param name
		 * @return the ReminderType associated with the name
		 */
		public static ReminderType getReminderType(String name) {
			return map.get(name);
		}

		/**
		 * Returns the type name as a string
		 * 
		 * @return
		 */
		public String getTypeName() {
			return typeName;
		}
	}

	/**
	 * 
	 * Begin GetVisitRemindersAction code
	 * 
	 */
	private TransactionDAO transDAO;
	private VisitRemindersDAO visitReminderDAO;
	private PatientDAO patientDAO;
	private long loggedInMID;

	/**
	 * Set up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID MID of the person who is logged in
	 * @throws iTrustException
	 */
	public GetVisitRemindersAction(DAOFactory factory, long loggedInMID) throws iTrustException {
		this.transDAO = factory.getTransactionDAO();
		this.loggedInMID = loggedInMID;
		visitReminderDAO = factory.getVisitRemindersDAO();
		patientDAO = factory.getPatientDAO();
	}

	/**
	 * Returns a list of VisitReminderReturnForms for the type passed in as a param
	 * 
	 * @param type
	 *            the ReminderType
	 * @return the list of VisitReminderReturnForms
	 * @throws iTrustException
	 * @throws FormValidationException
	 */
	public List<VisitReminderReturnForm> getVisitReminders(ReminderType type) throws iTrustException, FormValidationException {
		
		if (null == type)
			throw new iTrustException("Reminder Type DNE");
		
		transDAO.logTransaction(TransactionType.PATIENT_REMINDERS, loggedInMID, 0l, type.getTypeName());
		switch (type) {
			case DIAGNOSED_CARE_NEEDERS:
				return stripDupes(visitReminderDAO.getDiagnosedVisitNeeders(loggedInMID));

			case FLU_SHOT_NEEDERS:
				return visitReminderDAO.getFluShotDelinquents(loggedInMID);
				
			case IMMUNIZATION_NEEDERS:
				return getImmunizationNeeders(loggedInMID);
				
			default:
				throw new iTrustException("Reminder Type DNE");
		}
	}
	
	/**
	 * Gets a list of anyone who need immunizations
	 * 
	 * @param mid the HCP whose patients are being checked
	 * @return a list of all the people who need immunizations--done in a visit reminder
	 * @throws iTrustException
	 */

	private List<VisitReminderReturnForm> getImmunizationNeeders(long mid) throws iTrustException {
		
		List<VisitReminderReturnForm> formList;
		List<VisitReminderReturnForm> needList = new ArrayList<VisitReminderReturnForm>();
		String reason = "";
		// Get list of patients that designate this HCP
		formList = visitReminderDAO.getPatients(mid);
		
		for (VisitReminderReturnForm r : formList) {
			reason = checkImmunizations(r.getPatientID());
			if (0 < reason.length()) {
				needList.add(r);
				r.addVisitFlag(new VisitFlag(VisitFlag.IMMUNIZATION, reason));
			}
		}	
		
		return needList;
	}
	
	

	/**
	 * Checks a patient to see what immunizations they need
	 * 
	 * @param pid patient to be checked
	 * @return patient list of those lacking immunizations according to the schedule
	 */
	private String checkImmunizations(long pid) throws iTrustException {
		
		String reason = "";
		List<ProcedureBean> procs = patientDAO.getProcedures(pid);
		PatientBean patient = patientDAO.getPatient(pid);
		long patientAge = patient.getAgeInWeeks();
		Gender gen = patient.getGender();
		
		int hepB = 0;
		long hepBTime = 0;

		int rota = 0;
		long rotaTime = 0;

		int diptet = 0;
		long deptetTime = 0;
		
		int haemoflu = 0;
		long haemofluTime = 0;
		long haemofluTimeFirst = 0;
		
		int pneumo = 0;
		long pneumoTime = 0;
		long pneumofluTimeFirst = 0;
		
		int polio = 0;
		long polioTime = 0;
		
		int measles = 0;
		long measlesTime = 0;
		
		int varicella = 0;
		long varicellaTime = 0;
		
		int hepA = 0;
		long hepATime = 0;
		
		int hpv = 0;
		long hpvTime = 0;
		
		for (ProcedureBean proc: procs) {
		
			String cpt = proc.getCPTCode();
			
			// Hep B (90371)
			if (cpt.equals("90371")) {
				hepB++;
				hepBTime = proc.getDate().getTime();
			}
				
			// Rotavirus (90681)
			else if (cpt.equals("90681")) {
				rota++;
				rotaTime = proc.getDate().getTime();
			}
			
			// Diptheria, Tetanus, Pertussis (90696)
			else if (cpt.equals("90696")) {
				diptet++;
				deptetTime = proc.getDate().getTime();
			}
			
			// Haemophilus influenza (90645)
			else if (cpt.equals("90645")) {
				if (0 == haemoflu)
					haemofluTimeFirst = proc.getDate().getTime();
				
				haemoflu++;
				haemofluTime = proc.getDate().getTime();
				
			}
			
			// Pneumococcal (90669)
			else if (cpt.equals("90669")) {
				if (0 == pneumo)
					pneumofluTimeFirst = proc.getDate().getTime();
				pneumo++;
				pneumoTime = proc.getDate().getTime();
			}
			
			// Poliovirus (90712)
			else if (cpt.equals("90712")) {
				polio++;
				polioTime = proc.getDate().getTime();
			}
			
			// Measles, Mumps, Rubella (90707)
			else if (cpt.equals("90707")) {
				measles++;
				measlesTime = proc.getDate().getTime();
			}
			
			// Varicella (90396)
			else if (cpt.equals("90396")) {
				varicella++;
				varicellaTime = proc.getDate().getTime();
			}
			
			// Hep A (90633)
			else if (cpt.equals("90633")) {
				hepA++;
				hepATime = proc.getDate().getTime();
			}
			
			// Human Papillomaavirus (90649)
			else if (cpt.equals("90649")) {
				hpv++;
				hpvTime = proc.getDate().getTime();
			}
		}
		
		if (3 > hepB) {
			reason += testHepB(hepB, patientAge, hepBTime);
		}
		
		if (3 > rota) {
			reason += testRotaVirus(rota, patientAge, rotaTime);
		}
		
		if (6 > diptet) {
			reason += testDipTet(diptet, patientAge, deptetTime);
		}
		
		if (3 > haemoflu) {
			reason += testHaemoFlu(haemoflu, patientAge, haemofluTime, haemofluTimeFirst);
		}
		
		if (4 > pneumo) {
			reason += testPneumo(pneumo, patientAge, pneumoTime, pneumofluTimeFirst);
		}
		
		if (3 > polio) {
			reason += testPolio(polio, patientAge, polioTime);
		}
		
		if (2 > measles) {
			reason += testMeasles(measles, patientAge, measlesTime);
		}
		
		if (2 > varicella) {
			reason += testVaricella(varicella, patientAge, varicellaTime);
		}
		
		if (2 > hepA) {
			reason += testHepA(hepA, patientAge, hepATime);
		}
		
		if (3 > hpv && gen.getName().equals("Female")) {
			reason += testHPV(hpv, patientAge, hpvTime);
		}
	
		return reason;
	}
	
	
	/**
	 * Checks to see if a patient needs the HPV immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the date is
	 * @return when the immunization should be given
	 */
	public static String testHPV(int count, long patientAge, long time) {
	
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (468 <= patientAge)
				reason += "90649 Human Papillomavirus (9 years) ";
		}
		else if (1 == count) {
			if (476 <= patientAge && 8 <= weeks)
				reason += "90649 Human Papillomavirus (9 years, 2 months) ";
		}
		else if (2 == count) {
			if (494 <= patientAge && 16 <= weeks)
				reason += "90649 Human Papillomavirus (9 years, 6 months) ";
		}
		
		return reason;
	}
	
	/**
	 * Checks to see if a patient needs the Hepatits A immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testHepA(int count, long patientAge, long time) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (52 <= patientAge)
				reason += "90633 Hepatits A (12 months) ";
		}
		else if (1 == count) {
			if (78 <= patientAge && 26 <= weeks)
				reason += "90633 Hepatits A (18 months) ";
		}
		
		return reason;	
	}
	
	/**
	 * Checks to see if a patient needs the Varicella immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testVaricella(int count, long patientAge, long time) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (52 <= patientAge)
				reason += "90396 Varicella (12 months) ";
		}
		else if (1 == count) {
			if (208 <= patientAge && 12 <= weeks)
				reason += "90396 Varicella (4 years) ";
		}
		
		return reason;
	}
	
	/**
	 * Checks to see if a patient needs the Measles, Mumps, and Rubekka immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testMeasles(int count, long patientAge, long time) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (52 <= patientAge)
				reason += "90707 Measles, Mumps, Rubekka (12 months) ";
		}
		else if (1 == count) {
			if (208 <= patientAge && 12 <= weeks)
				reason += "90707 Measles, Mumps, Rubekka (4 years) ";
		}
		
		return reason;
	}
	
	/**
	 * Checks to see if a patient needs the Polio immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testPolio(int count, long patientAge, long time) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (6 <= patientAge)
				reason += "90712 Poliovirus (6 weeks) ";
		}
		else if (1 == count) {
			if (16 <= patientAge && 4 <= weeks)
				reason += "90712 Poliovirus (4 months) ";
		}
		else if (2 == count) {
			if (26 <= patientAge)
				reason += "90712 Poliovirus (6 months) ";				
		}
		
		return reason;	
	}
	
	/**
	 * Checks to see if a patient needs the Pneumococcal immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testPneumo(int count, long patientAge, long time, long firstDoseTime) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		long ageFirst = patientAge - (firstDoseTime / (1000 * 60 * 60 * 24 * 7)); 
		
		if (0 == count) {
			if (6 <= patientAge)
				reason += "90669 Pneumococcal (6 weeks) ";
		}
		else if (1 == count) {
			if (16 <= patientAge && 52 > ageFirst && 4 <= weeks)
				reason += "90669 Pneumococcal (4 months) ";
			else if (16 <= patientAge && 52 <= ageFirst && 60 >= ageFirst && 8 <= weeks)
				reason += "90669 Pneumococcal (4 months) ";
		}
		else if (2 == count) {
			if (26 <= patientAge && 4 <= weeks && 52 >= ageFirst)
				reason += "90669 Pneumococcal (6 months) ";				
		}
		else if (3 == count) {
			if (52 <= patientAge && 8 <= weeks && 52 >= ageFirst)
				reason += "90669 Pneumococcal (12 months) ";				
		}
		return reason;
	}
	
	/**
	 * Checks to see if a patient needs the Haemophilus Infulenzae immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testHaemoFlu(int count, long patientAge, long time, long firstDoseTime) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		long ageFirst = patientAge - (firstDoseTime / (1000 * 60 * 60 * 24 * 7)); 
			
		if (0 == count) {
			if (6 <= patientAge)
				reason += "90645 Haemophilus influenzae (6 weeks) ";
		}
		else if (1 == count) {
			if (16 <= patientAge && 52 > ageFirst && 4 <= weeks)
				reason += "90645 Haemophilus influenzae (4 months) ";
			else if (16 <= patientAge && 52 <= ageFirst && 60 >= ageFirst && 8 <= weeks)
				reason += "90645 Haemophilus influenzae (4 months) ";
		}
		else if (2 == count) {
			if (26 <= patientAge && 4 <= weeks && 52 > ageFirst)
				reason += "90645 Haemophilus influenzae (6 months) ";
		}
		
		return reason;
	}
	
	/**
	 * Checks to see if a patient needs the Diphtheria, Tetanus, Pertussis immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testDipTet(int count, long patientAge, long time) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (6 <= patientAge)
				reason += "90696 Diphtheria, Tetanus, Pertussis (6 weeks) ";
		}
		else if (1 == count) {
			if (16 <= patientAge && 4 <= weeks )
				reason += "90696 Diphtheria, Tetanus, Pertussis (4 months) ";
		}
		else if (2 == count) {
			if (26 <= patientAge && 4 <= weeks)
				reason += "90696 Diphtheria, Tetanus, Pertussis (6 months) ";				
		}
		else if (3 == count) {
			if (15 <= patientAge && 26 <= weeks)
				reason += "90696 Diphtheria, Tetanus, Pertussis (15 weeks) ";
		}
		else if (4 == count) {
			if (208 <= patientAge && 26 <= weeks)
				reason += "90696 Diphtheria, Tetanus, Pertussis (4 years) ";
		}
		else if (5 == count) {
			if (572 <= patientAge && 260 <= weeks)
				reason += "90696 Diphtheria, Tetanus, Pertussis (11 years) ";				
		}
		
		return reason;
	}
	
	/**
	 * Checks to see if a patient needs the Rotavirus immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	
	public static String testRotaVirus(int count, long patientAge, long time) {
	
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (6 <= patientAge)
				reason += "90681 Rotavirus (6 weeks) ";
		}
		else if (1 == count) {
			if (16 <= patientAge && 4 <= weeks)
				reason += "90681 Rotavirus (4 months) ";
		}
		else if (2 == count) {
			if (26 <= patientAge && 4 <= weeks )
				reason += "90681 Rotavirus (6 months) ";				
		}
		
		return reason;
	}

	
	/**
	 * Checks to see if a patient needs the Hepatitis B immunization
	 * 
	 * @param count which immunization they are on
	 * @param patientAge how old the patient is
	 * @param time what the current date is
	 * @return when the immunization should be given
	 */
	public static String testHepB(int count, long patientAge, long time) {
		
		String reason = "";
		long weeks = (Calendar.getInstance().getTimeInMillis() - time) / (1000 * 60 * 60 * 24 * 7);
		
		if (0 == count) {
			if (0 < patientAge)
				reason += "90371 Hepatitis B (birth) ";
		}
		else if (1 == count) {
			if (4 <= patientAge && 4 <= weeks)
				reason += "90371 Hepatitis B (1 month) ";
		}
		else if (2 == count) {
			if (26 <= patientAge && 8 <= weeks)
				reason += "90371 Hepatitis B (6 months) ";				
		}
		
		return reason;
	}
	
	
	/**
	 * Removes duplicates from a list of VisitReminderReturnForms
	 * 
	 * @param patients list of visit remindersto be cleaned up
	 * @return cleaned up list of visit reminders
	 */
	private List<VisitReminderReturnForm> stripDupes(List<VisitReminderReturnForm> patients) {
		if (null == patients)
			return null;
		if (0 == patients.size())
			return patients;
		List<VisitReminderReturnForm> retPatients = new ArrayList<VisitReminderReturnForm>();
		VisitReminderReturnForm temp = patients.get(0);
		retPatients.add(temp);
		for (VisitReminderReturnForm vr : patients) {
			if (vr.getPatientID() != temp.getPatientID())
				retPatients.add(vr);
			temp = vr;
		}
		return retPatients;
	}
}
