package edu.ncsu.csc.itrust.action;

import java.util.Calendar;
import edu.ncsu.csc.itrust.beans.SurveyBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.SurveyDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * This class is used to add patient survey data to the database.  The office visit ID is linked with the survey ID.  Once the
 * survey is added, the transaction is logged
 *
 */
public class SurveyAction {
	private TransactionDAO transDAO;
	private SurveyDAO surveyDAO;
	long loggedInMID;
	
	/**
	 * Sets up defaults
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the user taking the survey.
	 */
	public SurveyAction(DAOFactory factory, long loggedInMID) {
		transDAO = factory.getTransactionDAO();
		surveyDAO = factory.getSurveyDAO();
		this.loggedInMID = loggedInMID;
	}


	/**
	 * Pass the OfficeVistBean along with SurveyBean
	 * @param surveyBean contains data to be added to database
	 * @param visitID The Office Visit ID corresponding to this Survey.
	 * @throws DBException
	 */
	public void addSurvey(SurveyBean surveyBean, long visitID) throws DBException {
		
		surveyBean.setVisitID(visitID); //now set visit ID in the survey bean
		surveyDAO.addCompletedSurvey(surveyBean, Calendar.getInstance().getTime());
		//add to transaction log
		transDAO.logTransaction(TransactionType.ADD_PATIENT_SURVEY, loggedInMID, 0L, "office visit ID for completed survey is: " + visitID);
		
	}
}
