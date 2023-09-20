package edu.ncsu.csc.itrust.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.RemoteMonitoringDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Handles retrieving the patient data for a certain HCP as used by viewTelemedicineData.jsp
 * 
 */
public class ViewMyRemoteMonitoringListAction {
	private RemoteMonitoringDAO rmDAO;
	private TransactionDAO transDAO;
	private AuthDAO authDAO;
	private long loggedInMID;

	/**
	 * Constructor
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the HCP retrieving the patient data.
	 */
	public ViewMyRemoteMonitoringListAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.rmDAO = factory.getRemoteMonitoringDAO();
		this.transDAO = factory.getTransactionDAO();
		this.authDAO = factory.getAuthDAO();
	}

	/**
	 * Returns a list of RemoteMonitoringDataBeans for the logged in HCP
	 * 
	 * @return list of TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<RemoteMonitoringDataBean> getPatientsData() throws DBException {
		
		transDAO.logTransaction(TransactionType.TELEMEDICINE_MONITORING, loggedInMID);
		return rmDAO.getPatientsData(loggedInMID);
		
	}
	
	/**
	 * Returns a list of RemoteMonitoringDataBeans for the logged in HCP
	 * 
	 * @return list of TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<RemoteMonitoringDataBean> getPatientDataByDate(long patientMID, String startDate, String endDate) throws DBException,FormValidationException {
		Date lower;
		Date upper;
		try {
			lower = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
			upper = new SimpleDateFormat("MM/dd/yyyy").parse(endDate);
			if (lower.after(upper))
				throw new FormValidationException("Start date must be before end date!");
		} catch (ParseException e) {
			throw new FormValidationException("Enter dates in MM/dd/yyyy");
		}		
		
		transDAO.logTransaction(TransactionType.TELEMEDICINE_MONITORING, loggedInMID);
		return rmDAO.getPatientDataByDate(patientMID, lower, upper);
		
	}
	
	public List<RemoteMonitoringDataBean> getPatientDataWithoutLogging() throws DBException {
		return rmDAO.getPatientsData(loggedInMID);
	}
	
	/**
	 * returns the patient name
	 * 
	 * @return patient name
	 * @throws DBException
	 * @throws iTrustException
	 */
	public String getPatientName(long pid) throws DBException, iTrustException {
		return authDAO.getUserName(pid);
	}
	
}
