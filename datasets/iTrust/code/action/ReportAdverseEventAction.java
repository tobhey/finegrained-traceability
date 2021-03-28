package edu.ncsu.csc.itrust.action;
/**
 * Used for the patient to report adverse events.
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.AdverseEventBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AdverseEventDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.AdverseEventValidator;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ReportAdverseEventAction {
	
	private long loggedInMID;
	private EmailUtil emailer;
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private AdverseEventDAO adverseEventDAO;
	private TransactionDAO transactionDAO;
	private long hcpID;
	private AdverseEventValidator validator;
	
	public ReportAdverseEventAction(String hcpID, DAOFactory factory, long loggedInMID){
		this.loggedInMID = loggedInMID;
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.emailer = new EmailUtil(factory);
		this.adverseEventDAO = factory.getAdverseEventDAO();
		this.transactionDAO = factory.getTransactionDAO();
		this.hcpID = Long.parseLong(hcpID);
		this.validator = new AdverseEventValidator();
		
		
	}
	
	/**
	 * Constructor used to send e-mails only
	 */
	public ReportAdverseEventAction(DAOFactory factory, long loggedInMID){
		this.loggedInMID = loggedInMID;
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.emailer = new EmailUtil(factory);
		this.adverseEventDAO = factory.getAdverseEventDAO();
		this.transactionDAO = factory.getTransactionDAO();
		this.validator = new AdverseEventValidator();
	}
	/**
	 * Method that sends exactly one e-mail to each MID in the list of Adverse Events
	 * @param aeList The list of adverse Event Beans that need to be acted upon
	 * @return EList The list of e-mails sent for testing purposes
	 */
	public Email sendMails(List<AdverseEventBean> aeList) throws iTrustException, FormValidationException, DBException {
		List<String> MIDlist = new ArrayList<String>();
		String patientID = "";
		
		for(AdverseEventBean beaner : aeList){
			patientID = beaner.getMID();
			String newDesc = " Drug: " + beaner.getDrug() + " (" + beaner.getCode() + ") Description: " + beaner.getDescription();
			beaner.setDescription(newDesc);
		}
		for(AdverseEventBean beano : aeList){
			if(!MIDlist.contains(beano.getPrescriber())){
				MIDlist.add(beano.getPrescriber());
			}
		}
		Email email = new Email();
		
		for(String num : MIDlist){
			String message = " Patient: " + patientDAO.getName(Long.parseLong(patientID)) 
				+ " (MID " + patientID + ") Has Reported the following adverse event(s)";
			for(AdverseEventBean beano : aeList){
				if(beano.getPrescriber().equals(num)){
					message = message + beano.getDescription();
				}
			}
			
			String fromEmail;
			email.setFrom("noreply@itrust.com");
			PatientBean sender = patientDAO.getPatient(loggedInMID);
			PersonnelBean receiver = personnelDAO.getPersonnel(Long.parseLong(num));
			List<String> toList = new ArrayList<String>();
			toList.add(receiver.getEmail());
			fromEmail = sender.getEmail();
			
			email.setToList(toList);
			email.setFrom(fromEmail);
			email.setSubject(String.format("Adverse Event Report(Prescription)"));
			email.setBody(message);
			emailer.sendEmail(email);
			transactionDAO.logTransaction(TransactionType.SEND_MESSAGE, loggedInMID);
			
		}
		return email;
	}
	/**
	 * A method used to send a single e-mail. Used in immunizations.
	 * @param aeBean
	 * @return Email returns the Email for testing purposes.
	 */
	public Email sendMail (AdverseEventBean aeBean)throws iTrustException, FormValidationException, DBException{
		Email email = new Email();
		String fromEmail;
		email.setFrom("noreply@itrust.com");
		List<String> toList = new ArrayList<String>();
		
		PatientBean sender = patientDAO.getPatient(loggedInMID);
		PersonnelBean receiver = personnelDAO.getPersonnel(hcpID);
		
		toList.add(receiver.getEmail());
		fromEmail = sender.getEmail();
		
		email.setToList(toList);
		email.setFrom(fromEmail);
		email.setSubject(String.format("Adverse Event Report (Immunization)"));
		email.setBody(String.format(
				" Patient: " + patientDAO.getName(Long.parseLong(aeBean.getMID())) 
				+ " (MID " + aeBean.getMID() + ") Has Reported the following adverse event" +
				" Drug: " + aeBean.getDrug() + "(" + aeBean.getCode() + ") Description: " + aeBean.getDescription()
				));
		emailer.sendEmail(email);
		
		transactionDAO.logTransaction(TransactionType.SEND_MESSAGE, loggedInMID);
		
		return email;
	}
	
	/**
	 * Method used to add a report to the data base
	 * @param aeBean The adverse event to add
	 * @return a string for testing purposes only
	 * @throws iTrustException
	 * @throws FormValidationException
	 * @throws DBException
	 */
	public String addReport(AdverseEventBean aeBean)throws iTrustException, FormValidationException, DBException {
		
		try{
			validator.validate(aeBean);
		}catch (FormValidationException e){
			e.printStackTrace();
			return e.getMessage();
			}
		try{
			adverseEventDAO.addReport(aeBean, hcpID);
		}
		catch( DBException e ){
			throw new iTrustException(e.getMessage());
		}
		/**
		 * Old code used to send a single e-mail. Keep for reference
		Email email = new Email();
		String senderName;
		String fromEmail;
		email.setFrom("noreply@itrust.com");
		List<String> toList = new ArrayList<String>();
		
		PatientBean sender = patientDAO.getPatient(loggedInMID);
		PersonnelBean receiver = personnelDAO.getPersonnel(hcpID);
		
		toList.add(receiver.getEmail());
		senderName = sender.getFullName();
		fromEmail = sender.getEmail();
		
		email.setToList(toList);
		email.setFrom(fromEmail);
		email.setSubject(String.format("Adverse Event Report"));
		email.setBody(String.format(
				" Patient: " + patientDAO.getName(Long.parseLong(aeBean.getMID())) 
				+ " (MID " + aeBean.getMID() + ") Has Reported the following adverse event(s)" +
				" Drug: " + aeBean.getDrug() + "(" + aeBean.getCode() + ") Description: " + aeBean.getDescription()
				));
		emailer.sendEmail(email);
		
		transactionDAO.logTransaction(TransactionType.SEND_MESSAGE, loggedInMID);*/
		transactionDAO.logTransaction(TransactionType.ADVERSE_EVENT, loggedInMID);
		return "";
	}
}
