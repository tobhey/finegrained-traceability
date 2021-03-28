package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.SecurityQA;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.SecurityQAValidator;

/**
 * Handles setting and retrieving the security questions/answers for users Used by
 * patient/editMyDemographics.jsp, staff/editMyDemographics.jsp, staff/editPersonnell.jsp
 * 
 * @author laurenhayward
 * 
 */
public class SetSecurityQuestionAction {

	private AuthDAO authDAO;
	private long loggedInMID;

	/**
	 * Sets up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param rLoggedInMID The MID of the user who is setting their security question.
	 * @throws iTrustException
	 */
	public SetSecurityQuestionAction(DAOFactory factory, long rLoggedInMID) throws iTrustException {
		this.authDAO = factory.getAuthDAO();
		loggedInMID = checkMID(rLoggedInMID);
	}

	/**
	 * Updates information in the database from the information held in the SecurityQA bean passed as a param
	 * 
	 * @param a
	 *            SecurityQuestionBean that holds new information
	 * @throws Exception
	 */
	public void updateInformation(SecurityQA a) throws Exception {
		SecurityQAValidator sqav = new SecurityQAValidator();
		sqav.validate(a);
		authDAO.setSecurityQuestionAnswer(a.getQuestion(), a.getAnswer(), loggedInMID);
	}

	/**
	 * Returns a SecurityQA bean holding the security info for the currently logged in user
	 * 
	 * @return SecurityQA for loggedInMid
	 * @throws iTrustException
	 */
	public SecurityQA retrieveInformation() throws iTrustException {
		SecurityQA toRet = new SecurityQA();
		toRet.setAnswer(authDAO.getSecurityAnswer(loggedInMID));
		toRet.setQuestion(authDAO.getSecurityQuestion(loggedInMID));
		return toRet;
	}
	/**
	 * Checks to make sure the MID exists in iTrust
	 * 
	 * @param mid MID to check
	 * @return returns the MID if the user is valid, otherwise, throws an exception
	 * @throws iTrustException
	 */

	private long checkMID(long mid) throws iTrustException {
		if (!authDAO.checkUserExists(mid))
			throw new iTrustException("MID " + mid + " is not a user!");
		return mid;
	}

}
