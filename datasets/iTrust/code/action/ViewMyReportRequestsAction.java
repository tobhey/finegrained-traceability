package edu.ncsu.csc.itrust.action;

import java.util.Calendar;
import java.util.List;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.ReportRequestBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReportRequestDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Action class for ViewMyReports.jsp.  Allows the user to see all their reports
 */
public class ViewMyReportRequestsAction {
	private long loggedInMID;
	private ReportRequestDAO reportRequestDAO;
	private PersonnelDAO personnelDAO;
	private TransactionDAO transDAO;
	//private DAOFactory factory;

	/**
	 * Set up
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing their report requests.
	 */
	public ViewMyReportRequestsAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.reportRequestDAO = factory.getReportRequestDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.transDAO = factory.getTransactionDAO();
		//this.factory = factory;
	}

	/**
	 * Returns all the reports for the currently logged in HCP
	 * 
	 * @return list of all reports for the logged in HCP
	 * @throws iTrustException
	 */
	public List<ReportRequestBean> getAllReportRequestsForRequester() throws iTrustException {
		return reportRequestDAO.getAllReportRequestsForRequester(loggedInMID);
	}

//	/**
//	 * Returns a list of *all* reports
//	 * 
//	 * @return list of all reports
//	 * @throws iTrustException
//	 */
//	public List<ReportRequestBean> getAllReportRequests() throws iTrustException {
//		return reportRequestDAO.getAllReportRequests();
//	}

	/**
	 * Adds a report request to the list
	 * 
	 * @param patientMID ID of the patient that the report request is for
	 * @return
	 * @throws iTrustException
	 */
	public long addReportRequest(long patientMID) throws iTrustException {
		long id = reportRequestDAO
				.addReportRequest(loggedInMID, patientMID, Calendar.getInstance().getTime());
		transDAO.logTransaction(TransactionType.COMPREHENSIVE_REPORT_REQUEST, loggedInMID, patientMID,
				"Added comprehensive report request");
		return id;

	}

//	/**
//	 * Approves a report request from the list.  E-mail is sent when the request is approved.
//	 * 
//	 * @param ID id of the request
//	 * @throws iTrustException
//	 */
//	public void approveReportRequest(long ID) throws iTrustException {
//		ReportRequestBean rr = reportRequestDAO.getReportRequest(ID);
//		reportRequestDAO.approveReportRequest(ID, loggedInMID, Calendar.getInstance().getTime());
//		transDAO.logTransaction(TransactionType.COMPREHENSIVE_REPORT_REQUEST, loggedInMID,
//				rr.getPatientMID(), "Approved comprehensive report request");
//		new EmailUtil(factory).sendEmail(makeEmailApp(loggedInMID, rr.getRequesterMID(), rr.getPatientMID()));
//
//	}

//	/**
//	 * 
//	 * Sends e-mail regarding the approved request.
//	 * 
//	 * @param adminID admin who approved the request
//	 * @param hcpID HCP the request is for
//	 * @param pid ID of the patient the report is about
//	 * @return the sent e-mail
//	 * @throws DBException
//	 */
//	private Email makeEmailApp(long adminID, long hcpID, long pid) throws DBException {
//
//		PatientBean p = new PatientDAO(factory).getPatient(pid);
//
//		Email email = new Email();
//		email.setFrom("no-reply@itrust.com");
//		email.setToList(Arrays.asList(p.getEmail()));
//		email.setSubject("A Report has been generated in iTrust");
//		email
//				.setBody(String
//						.format(
//								"Dear %s, \n The iTrust Health Care Provider (%s) submitted a request to view your full medical records.  The iTrust administrator (%s) approved a one-time viewing of this report.  You will be notified when the HCP chooses to view it.",
//								p.getFullName(), hcpID, adminID));
//		return email;
//	}

//	/**
//	 * Rejects a request from the list.
//	 * 
//	 * @param ID id of the rejected request
//	 * @param comment why the request was rejected
//	 * @throws iTrustException
//	 */
//	public void rejectReportRequest(long ID, String comment) throws iTrustException {
//		reportRequestDAO.rejectReportRequest(ID, loggedInMID, Calendar.getInstance().getTime(), comment);
//		transDAO.logTransaction(TransactionType.COMPREHENSIVE_REPORT_REQUEST, loggedInMID, 0L,
//				"Rejected comprehensive report request");
//	}

	/**
	 * Returns the requested report
	 * 
	 * @param ID id of the requested report
	 * @return the requested report
	 * @throws iTrustException
	 */
	public ReportRequestBean getReportRequest(int ID) throws iTrustException {
		return reportRequestDAO.getReportRequest(ID);
	}
	
/**
 * Sets the viewed status of the report.  If the report is "viewed" the HCP must request a new one to see it again.
 * 
 * @param ID id of the report
 * @throws iTrustException
 */
	public void setViewed(int ID) throws iTrustException {
//		ReportRequestBean rr = reportRequestDAO.getReportRequest(ID);
		reportRequestDAO.setViewed(ID, Calendar.getInstance().getTime());
		transDAO.logTransaction(TransactionType.COMPREHENSIVE_REPORT_REQUEST, loggedInMID, 0L,
				"Viewed comprehensive report");
		//new EmailUtil(factory).sendEmail(makeEmailView(rr.getApproverMID(), rr.getRequesterMID(), rr
			//	.getPatientMID()));

	}

//	/**
//	 * 
//	 * Sends e-mail regarding the request to the patient.
//	 * 
//	 * @param adminID admin who approved the request
//	 * @param hcpID HCP the request is for
//	 * @param pid ID of the patient the report is about
//	 * @return the sent e-mail
//	 * @throws DBException
//	 */
//	private Email makeEmailView(long adminID, long hcpID, long pid) throws DBException {
//
//		PatientBean p = new PatientDAO(factory).getPatient(pid);
//
//		Email email = new Email();
//		email.setFrom("no-reply@itrust.com");
//		email.setToList(Arrays.asList(p.getEmail()));
//		email.setSubject("A Report has been generated in iTrust");
//		email
//				.setBody(String
//						.format(
//								"Dear %s, \n The iTrust Health Care Provider (%s) has chosen to view your full medical report, which was approved by an iTrust administrator (%s).  This report was only viewable one time and is no longer available.",
//								p.getFullName(), hcpID, adminID));
//		return email;
//	}

	/**
	 * Gets the status of the request
	 * 
	 * @param id id of the request
	 * @return the request's status
	 * @throws iTrustException
	 */
	public String getLongStatus(long id) throws iTrustException {
		StringBuilder s = new StringBuilder();
		ReportRequestBean r = reportRequestDAO.getReportRequest(id);
		if (r.getStatus().equals(ReportRequestBean.Requested)) {
			PersonnelBean p = personnelDAO.getPersonnel(r.getRequesterMID());
			s.append(String.format("Request was requested on %s by %s", r.getRequestedDateString(), p
					.getFullName()));
		}
//		if (r.getStatus().equals(ReportRequestBean.Approved)) {
//			PersonnelBean p = personnelDAO.getPersonnel(r.getRequesterMID());
//			PersonnelBean p2 = personnelDAO.getPersonnel(r.getApproverMID());
//			s.append(String.format("Request was requested on %s by %s ", r.getRequestedDateString(), p
//					.getFullName()));
//			s.append(String.format("and approved on %s by %s", r.getApprovedDateString(), p2.getFullName()));
//		}
//		if (r.getStatus().equals(ReportRequestBean.Rejected)) {
//			PersonnelBean p = personnelDAO.getPersonnel(r.getRequesterMID());
//			PersonnelBean p2 = personnelDAO.getPersonnel(r.getApproverMID());
//			s.append(String.format("Request was requested on %s by %s ", r.getRequestedDateString(), p
//					.getFullName()));
//			s.append(String.format("and rejected on %s by %s", r.getApprovedDateString(), p2.getFullName()));
//		}
		if (r.getStatus().equals(ReportRequestBean.Viewed)) {
			PersonnelBean p = personnelDAO.getPersonnel(r.getRequesterMID());
//			PersonnelBean p2 = personnelDAO.getPersonnel(r.getApproverMID());
			String fullName = "Unknown";
			if(p != null){
				fullName = p.getFullName();
				s.append(String.format("Request was requested on %s by %s, ", r.getRequestedDateString(), p
					.getFullName()));
			}
//			s.append(String.format("approved on %s by %s, ", r.getApprovedDateString(), fullName));
			s.append("");// removed "<br />" because it caused unit test to fail and seems to have no
			// purpose
			s.append(String.format("and viewed on %s by %s", r.getViewedDateString(), fullName));
		}

		return s.toString();
	}

}
