package edu.ncsu.csc.itrust.action;

import java.util.Arrays;
import edu.ncsu.csc.itrust.EmailUtil;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.ValidationFormat;

/**
 * Manages resetting the password Used by resetPassword.jsp
 * 
 * @author laurenhayward
 * 
 */
public class ResetPasswordAction {
	public static final int MAX_RESET_ATTEMPTS = 3;

	private AuthDAO authDAO;
	private PatientDAO patientDAO;
	private DAOFactory factory;

	/**
	 * Set up defaults
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 */
	public ResetPasswordAction(DAOFactory factory) {
		this.authDAO = factory.getAuthDAO();
		this.patientDAO = factory.getPatientDAO();
		this.factory = factory;
	}

	/**
	 * Checks to see if a user exists with the given mid
	 * 
	 * @param midString The user's MID to check for.
	 * @return 0 if the user does not exist, else the mid of the user as a long
	 */
	public long checkMID(String midString) {
		try {
			long mid = Long.valueOf(midString);
			if (!authDAO.checkUserExists(mid))
				return 0;
			return mid;
		} catch (NumberFormatException e) {
			return 0L;
		} catch (DBException e) {
			return 0L;
		}
	}

	/**
	 * Checks to see if the number of reset password attempts has been exceeded for the given ipAddress
	 * 
	 * @param ipAddress The IPv4 or IPv6 IP address as a String.
	 * @return true if the the number of reset attempts is greater than or equal to MAX_RESET_ATTEMPTS
	 * @throws DBException
	 */
	public boolean isMaxedOut(String ipAddress) throws DBException {
		return authDAO.getResetPasswordFailures(ipAddress) >= MAX_RESET_ATTEMPTS;
	}

	/**
	 * Checks if the given mid matches the given role
	 * 
	 * @param mid
	 *            the mid to be checked
	 * @param role
	 *            the role to be checked
	 * @return true if the mid and role match
	 * @throws iTrustException
	 */
	public String checkRole(long mid, String role) throws iTrustException {
		try {
			if (("patient".equals(role) && patientDAO.getRole(mid, role).equals("patient"))
					|| ("hcp".equals(role) && patientDAO.getRole(mid, role).equals("hcp"))
					|| ("uap".equals(role) && patientDAO.getRole(mid, role).equals("uap"))
					|| ("pha".equals(role) && patientDAO.getRole(mid, role).equals("pha"))
					|| ("er".equals(role) && patientDAO.getRole(mid, role).equals("er")))
				return role;
			else
				return null;
		} catch (DBException e) {
			e.printStackTrace();
		} catch (iTrustException e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	/**
	 * Checks if the answer param is null
	 * 
	 * @param answer the user's security answer
	 * @return answer if not null, else return null
	 */
	public String checkAnswerNull(String answer) {
		if (answer == null || "".equals(answer))
			return null;
		else
			return answer;
	}

	/**
	 * Returns the security question for the mid param
	 * 
	 * @param mid MID of the user
	 * @return the security question or "" if DBException thrown
	 * @throws iTrustException
	 */
	public String getSecurityQuestion(long mid) throws iTrustException {
		try {
			if (null == authDAO.getSecurityQuestion(mid) || authDAO.getSecurityQuestion(mid).equals(""))
				throw new iTrustException("No security question or answer for this user has been set.");
			else
				return authDAO.getSecurityQuestion(mid);
		} catch (DBException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Resets the password for the given mid
	 * 
	 * @param mid of the user to have their password reset
	 * @param role what role the user has in iTrust
	 * @param answer answers to their security question
	 * @param password their password
	 * @param confirmPassword their password again
	 * @param ipAddr the ip address the request is coming from
	 * @return status message
	 * @throws FormValidationException
	 * @throws DBException
	 * @throws iTrustException
	 */
	public String resetPassword(long mid, String role, String answer, String password,
			String confirmPassword, String ipAddr) throws FormValidationException, DBException,
			iTrustException {

		Role r = authDAO.getUserRole(mid);
		try {
			Role.parse(role);
		} catch (IllegalArgumentException e) {
			return "Invalid role";
		}

		if (r.equals(Role.ADMIN))
			return "This role cannot be changed here";
		if (!r.equals(Role.parse(role)))
			return "Role mismatch";

		if (authDAO.getResetPasswordFailures(ipAddr) >= MAX_RESET_ATTEMPTS) {
			return "Too many retries";
		}

		try {
			validatePassword(password, confirmPassword);

			if (answer.equals(authDAO.getSecurityAnswer(mid))) {
				authDAO.resetPassword(mid, password);
				new EmailUtil(factory).sendEmail(makeEmailApp(mid, role));
				return "Password changed";
				
			} else {
				authDAO.recordResetPasswordFailure(ipAddr);
				return "Answer did not match";
			}

		} catch (DBException e) {
			return "Error in validation of security answer";
		}
	}
	
	/**
	 * Creates and sends an e-mail about the change
	 * 
	 * @param mid the user who's password was changed
	 * @param role what role they have in iTrust
	 * @return the e-mial that is sent
	 * @throws DBException
	 */
	private Email makeEmailApp(long mid, String role) throws DBException{
		
		if(Role.parse(role) == Role.PATIENT){
			PatientBean p = new PatientDAO(factory).getPatient(mid);
			Email email = new Email();
			email.setFrom("no-reply@itrust.com");
			email.setToList(Arrays.asList(p.getEmail()));
			email.setSubject("Your password has been changed in iTrust");
			email.setBody(String.format("Dear %s, \n You have chosen to change your iTrust password for user %s", p.getFullName(), mid));

			return email;
		}
		else{ //UAP or HCP - admin taken out in "resetPassword"
			PersonnelBean p = new PersonnelDAO(factory).getPersonnel(mid);
			Email email = new Email();
			email.setFrom("no-reply@itrust.com");
			email.setToList(Arrays.asList(p.getEmail()));
			email.setSubject("Your password has been changed in iTrust");
			email.setBody(String.format("Dear %s, \n You have chosen to change your iTrust password for user %s", p.getFullName(), mid));

			return email;
		}
	}
	
	/**
	 * Checks to make sure the password is correctly entered twice.
	 * 
	 * @param password the password
	 * @param confirmPassword the password again for confirmation
	 * @throws FormValidationException
	 */

	private void validatePassword(String password, String confirmPassword) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (password == null || "".equals(password)) {
			errorList.addIfNotNull("Password cannot be empty");
		} else {
			if (!password.equals(confirmPassword))
				errorList.addIfNotNull("Passwords don't match");
			if (!ValidationFormat.PASSWORD.getRegex().matcher(password).matches()) {
				errorList.addIfNotNull("Password must be in the following format: "
						+ ValidationFormat.PASSWORD.getDescription());
			}
		}
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
