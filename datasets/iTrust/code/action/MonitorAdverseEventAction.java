package edu.ncsu.csc.itrust.action;

/**
 * Used by PHAs to view the reported adverse events
 */
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.beans.AdverseEventBean;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.MessageBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AdverseEventDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;

public class MonitorAdverseEventAction {
	 
	private long loggedInMID;
	private EmailUtil emailer;
	private PatientDAO patientDAO;
	private AdverseEventDAO adverseEventDAO;
	private TransactionDAO transactionDAO;
	private SendMessageAction messenger;
	
	/**
	 * Constructor 
	 * @param factory
	 * @param loggedInMID
	 */
	public MonitorAdverseEventAction(DAOFactory factory, long loggedInMID){
		this.loggedInMID = loggedInMID;
		this.transactionDAO = factory.getTransactionDAO();
		this.patientDAO = factory.getPatientDAO();
		this.emailer = new EmailUtil(factory);
		this.adverseEventDAO = factory.getAdverseEventDAO();
		this.messenger = new SendMessageAction(factory, loggedInMID);
		
	}
	/**
	 * Returns a list of reports between specified dates
	 * @param isPrescription Boolean to return prescriptions or immunizations
	 * @param start The starting date
	 * @param end The ending date
	 * @return the list of events
	 * @throws iTrustException
	 * @throws FormValidationException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public List<AdverseEventBean> getReports(boolean isPrescription, String start, String end)throws iTrustException, FormValidationException, SQLException, ParseException {
		if(isPrescription) {
			transactionDAO.logTransaction(TransactionType.ADVERSE_EVENT, loggedInMID, 0L, "Adverse Prescription Reports Requested.");
			return adverseEventDAO.getPerscriptions(start, end);
		} else { //is Immunization
			transactionDAO.logTransaction(TransactionType.ADVERSE_EVENT, loggedInMID, 0L, "Adverse Immunization Report Requested.");
			return adverseEventDAO.getImmunizations(start, end);
		}
		
	}
	
	/**
	 * Returns the patient's name
	 * @param MID the MID of the patient to return the name for.
	 * @return
	 */
	public String getName(long MID) {
		try {
			return patientDAO.getName(MID);
		} catch(DBException e) {
			return "";
		} catch(iTrustException e) {
			return "Patient no longer exists";
		}
	}
	
	/**
	 * Sends an e-mail to a patient requesting more information
	 * @param patientMID The patient to receive the message
	 * @param message The message
	 * @return string for testing purposes
	 * @throws DBException
	 */
	public String sendEmail(long patientMID, String message) throws DBException{
		String rValue;
		List<String> toList = new ArrayList<String>();
		PatientBean receiver = patientDAO.getPatient(patientMID);
		toList.add(receiver.getEmail());
		Email mail = new Email();
		mail.setBody(message);
		mail.setFrom(loggedInMID + "");
		mail.setToList(toList);
		emailer.sendEmail(mail);
		transactionDAO.logTransaction(TransactionType.ADVERSE_EVENT, loggedInMID, 0L, "Requested more information");
		rValue = "" + mail.getFrom() + " " + mail.getBody();
		return rValue;
	}
	
	/**
	 * Method used to remove an adverse event report
	 * 
	 * @param id the id of the report to be removed
	 * @throws DBException
	 * @throws iTrustException
	 */
	public void remove(int id) throws DBException, iTrustException, FormValidationException{
		long HCPMID;
		try{
		AdverseEventBean aeBean = adverseEventDAO.getReport(id);
		adverseEventDAO.removeReport(id);
		HCPMID = adverseEventDAO.getHCPMID(id);
		MessageBean mBeanTwo = new MessageBean();
		MessageBean mBeanOne = new MessageBean();
		String body = "An adverse event for " + aeBean.getDrug() + " perscribed to " + patientDAO.getName(Long.parseLong(aeBean.getMID())) +" with description: (" + aeBean.getDescription()+ ") was removed.";
		mBeanOne.setTo(Long.parseLong(aeBean.getMID()));
		mBeanTwo.setTo(HCPMID);
		mBeanOne.setSubject("Subject");
		mBeanTwo.setSubject("Subject");
		mBeanOne.setBody(body);
		mBeanTwo.setBody(body);
		mBeanOne.setFrom(loggedInMID);
		mBeanTwo.setFrom(loggedInMID);
		messenger.sendMessage(mBeanOne);
		messenger.sendMessage(mBeanTwo);
		transactionDAO.logTransaction(TransactionType.ADVERSE_EVENT, loggedInMID, 0L, "Adverse Event Report Removed");
		}catch(SQLException e){
			throw new DBException(e);
		} 
	}
}
