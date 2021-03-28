package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.RemoteMonitoringDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.RemoteMonitoringDataBeanValidator;

/**
 * Handles adding remote monitoring patient data to the database
 * 
 */
public class AddRemoteMonitoringDataAction {
	private RemoteMonitoringDataBeanValidator validator = new RemoteMonitoringDataBeanValidator();
	private RemoteMonitoringDAO rmDAO;
	private TransactionDAO transDAO;
	private AuthDAO authDAO;
	private long loggedInMID;
	private long patientMID;

	/**
	 * Constructor
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person recording the patient's data.
	 * @param patientMID The MID of the patient
	 */
	public AddRemoteMonitoringDataAction(DAOFactory factory, long loggedInMID, long patientMID) {
		this.loggedInMID = loggedInMID;
		this.rmDAO = factory.getRemoteMonitoringDAO();
		this.transDAO = factory.getTransactionDAO();
		this.authDAO = factory.getAuthDAO();
		this.patientMID = patientMID;
	}

	/**
	 * Adds a patients remote monitoring data to the database.
	 * 
	 * @param glucoseLevel
	 * @throws DBException
	 */
	public void addRemoteMonitoringData(int glucoseLevel)
	  throws DBException, FormValidationException,iTrustException {
		//Validation - Only need to validate the three integer parameters
		RemoteMonitoringDataBean m = new RemoteMonitoringDataBean();
		m.setGlucoseLevel(glucoseLevel);
		m.setDiastolicBloodPressure(60);
		m.setSystolicBloodPressure(60);
		validator.validate(m);
		m.setDiastolicBloodPressure(-1);
		m.setSystolicBloodPressure(-1);
		//Log transaction
		transDAO.logTransaction(TransactionType.TELEMEDICINE_MONITORING, loggedInMID);
		String role;
		if (loggedInMID == patientMID){
			role = "self-reported";
		} else if (authDAO.getUserRole(loggedInMID).getUserRolesString().equals("uap")){
			role = "case-manager";
		} else {
			role = "patient representative";
		}		
		//Store in DB
		rmDAO.storePatientData(patientMID, glucoseLevel, role, loggedInMID);
	}
	
	/**
	 * Adds a patients remote monitoring data to the database.
	 * 
	 * @param systolicBloodPressure
	 * @param diastolicBloodPressure
	 * @throws DBException
	 */
	public void addRemoteMonitoringData(int systolicBloodPressure, int diastolicBloodPressure)
	  throws DBException, FormValidationException,iTrustException {
		//Validation - Only need to validate the three integer parameters
		RemoteMonitoringDataBean m = new RemoteMonitoringDataBean();
		m.setSystolicBloodPressure(systolicBloodPressure);
		m.setDiastolicBloodPressure(diastolicBloodPressure);
		validator.validate(m);
		
		//Log transaction
		transDAO.logTransaction(TransactionType.TELEMEDICINE_MONITORING, loggedInMID);
		String role;
		if (loggedInMID == patientMID){
			role = "self-reported";
		} else if (authDAO.getUserRole(loggedInMID).getUserRolesString().equals("uap")){
			role = "case-manager";
		} else {
			role = "patient representative";
		}		
		//Store in DB
		rmDAO.storePatientData(patientMID, systolicBloodPressure, diastolicBloodPressure, role, loggedInMID);
	}
	
	/**
	 * Adds a patients remote monitoring data to the database.
	 * 
	 * @param systolicBloodPressure
	 * @param diastolicBloodPressure
	 * @param glucoseLevel
	 * @throws DBException
	 */
	public void addRemoteMonitoringData(int systolicBloodPressure, int diastolicBloodPressure, int glucoseLevel)
	  throws DBException, FormValidationException,iTrustException {
		//Validation - Only need to validate the three integer parameters
		RemoteMonitoringDataBean m = new RemoteMonitoringDataBean();
		m.setSystolicBloodPressure(systolicBloodPressure);
		m.setDiastolicBloodPressure(diastolicBloodPressure);
		m.setGlucoseLevel(glucoseLevel);
		validator.validate(m);
		
		//Log transaction
		transDAO.logTransaction(TransactionType.TELEMEDICINE_MONITORING, loggedInMID);
		String role;
		if (loggedInMID == patientMID){
			role = "self-reported";
		} else if (authDAO.getUserRole(loggedInMID).getUserRolesString().equals("uap")){
			role = "case-manager";
		} else {
			role = "patient representative";
		}		
		//Store in DB
		rmDAO.storePatientData(patientMID, systolicBloodPressure, diastolicBloodPressure, glucoseLevel, role, loggedInMID);
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
