package edu.ncsu.csc.itrust.action;


import edu.ncsu.csc.itrust.beans.ReferralBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ReferralDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.exception.DBException;
import java.util.List;

/**
 * Used for referring patients to other HCPs.  The call is made in /auth/hcp/hcpConsultation.jsp 
 */
public class ReferralManagementAction {
	private long loggedInMID;
	private TransactionDAO transDAO;
	private ReferralDAO referralDAO;

	/**
	 * Super class validates the patient id
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the user managing this referral.
	 * @param pidString The MID of the patient being referred.
	 * @throws iTrustException
	 */
	public ReferralManagementAction(DAOFactory factory, long loggedInMID) throws iTrustException {
		
		this.referralDAO = factory.getReferralDAO();
		this.loggedInMID = loggedInMID;
		this.transDAO = factory.getTransactionDAO();
	}

	/**
	 * Adds a referral bean to the database.
	 * @param r The referral bean to be added.
	 * @throws DBException
	 */
	public void sendReferral(ReferralBean r) throws DBException {
		referralDAO.addReferral(r);
		transDAO.logTransaction(TransactionType.SEND_REFERRAL, loggedInMID);
	}
	
	/**
	 * Updates an existing referral bean.
	 * @param r The current referral bean.
	 * @throws DBException
	 */
	public void updateReferral(ReferralBean r) throws DBException {
		referralDAO.editReferral(r);
	}
	
	/**
	 * Gets the referrals the currently logged in MID has sent.
	 * @return A java.util.List of ReferralBeans this MID has sent.
	 * @throws DBException
	 */
	public List<ReferralBean> getReferralsSentFromMe() throws DBException {
		return referralDAO.getReferralsSentFrom(loggedInMID);
	}
	
	/**
	 * Gets the referrals this MID has received.
	 * @return A java.util.List of the ReferralBeans this MID has received.
	 * @throws DBException
	 */
	public List<ReferralBean> getReferralsSentToMe() throws DBException {
		return referralDAO.getReferralsSentTo(loggedInMID);
	}

}
