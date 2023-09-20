package edu.ncsu.csc.itrust.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Handles retrieving the log of record accesses for a given user Used by viewAccessLog.jsp
 * 
 * @author laurenhayward
 * 
 */
public class ViewMyAccessLogAction {
	private TransactionDAO transDAO;
	private long loggedInMID;

	/**
	 * Set up
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person retrieving the logs.
	 */
	public ViewMyAccessLogAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.transDAO = factory.getTransactionDAO();
	}

	/**
	 * Returns a list of TransactionBeans between the two dates passed as params
	 * 
	 * @param lowerDate
	 *            the first date
	 * @param upperDate
	 *            the second date
	 * @return list of TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<TransactionBean> getAccesses(String lowerDate, String upperDate, boolean getByRole) throws DBException,
			FormValidationException {
		List<TransactionBean> accesses;
		if (lowerDate == null || upperDate == null)
			return transDAO.getAllRecordAccesses(loggedInMID, getByRole);
		String message = "";
		try {
			Date lower = new SimpleDateFormat("MM/dd/yyyy").parse(lowerDate);
			Date upper = new SimpleDateFormat("MM/dd/yyyy").parse(upperDate);
			if (lower.after(upper))
				throw new FormValidationException("Start date must be before end date!");
			message = "for dates between " + lowerDate + " and " + upperDate;
			transDAO.logTransaction(TransactionType.VIEW_ACCESS_LOG, loggedInMID, 0L, message);
			accesses = transDAO.getRecordAccesses(loggedInMID, lower, upper, getByRole);
		} catch (ParseException e) {
			throw new FormValidationException("Enter dates in MM/dd/yyyy");
		}
		return accesses;
	}

	/**
	 * Returns the date of the first Transaction in the list passed as a param if the list is not empty
	 * otherwise, returns today's date
	 * 
	 * @param accesses A java.util.List of TransactionBeans for the accesses.
	 * @return A String representing the date of the first transaction.
	 */
	public String getDefaultStart(List<TransactionBean> accesses) {
		String startDate = "";
		if (accesses.size() > 0) {
			startDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(accesses.get(accesses.size() - 1)
					.getTimeLogged().getTime()));
		} else {
			startDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		}
		return startDate;
	}

	/**
	 * Returns the date of the last Transaction in the list passed as a param if the list is not empty
	 * otherwise, returns today's date
	 * 
	 * @param accesses A java.util.List of TransactionBeans storing the access. 
	 * @return A String representation of the date of the last transaction.
	 */
	public String getDefaultEnd(List<TransactionBean> accesses) {
		String endDate = "";
		if (accesses.size() > 0) {
			endDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(accesses.get(0).getTimeLogged()
					.getTime()));
		} else {
			endDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		}
		return endDate;
	}
}
