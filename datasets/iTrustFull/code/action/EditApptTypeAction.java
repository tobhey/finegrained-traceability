package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.validate.ApptTypeBeanValidator;

public class EditApptTypeAction {
	private long loggedInMID;
	private ApptTypeDAO apptTypeDAO;
	private TransactionDAO transDAO;
	private ApptTypeBeanValidator validator = new ApptTypeBeanValidator();
	
	public EditApptTypeAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.apptTypeDAO = factory.getApptTypeDAO();
		this.transDAO = factory.getTransactionDAO();
	}
	
	public List<ApptTypeBean> getApptTypes() throws SQLException {
		return apptTypeDAO.getApptTypes();
	}
	
	public String addApptType(ApptTypeBean apptType) throws SQLException, FormValidationException {
		validator.validate(apptType);
		
		List<ApptTypeBean> list = this.getApptTypes();
		for(ApptTypeBean a : list) {
			if(a.getName().equals(apptType.getName()))
				return "Appointment Type: "+apptType.getName()+" already exists.";
		}
		
		try {
			if (apptTypeDAO.addApptType(apptType)) {
				transDAO.logTransaction(TransactionType.ADD_APPT_TYPE, loggedInMID, 0L,
						"Added Appointment Type " + apptType.getName());
				return "Success: " + apptType.getName() + " - Duration: " + apptType.getDuration() + " added";
			} else
				return "The database has become corrupt. Please contact the system administrator for assistance.";
		} catch (DBException e) {
			e.printStackTrace();
			return e.getMessage();
		} 
	}
	
	public String editApptType(ApptTypeBean apptType) throws SQLException, FormValidationException {
		validator.validate(apptType);
		
		List<ApptTypeBean> list = this.getApptTypes();
		int flag = 0;
		for(ApptTypeBean a : list) {
			if(a.getName().equals(apptType.getName())) {
				flag = 1;
				if(a.getDuration() == apptType.getDuration())
					return "Appointment Type: "+apptType.getName()+" already has a duration of "+apptType.getDuration()+" minutes.";
				break;
			}
		}
		if(flag == 0) {
			return "Appointment Type: "+apptType.getName()+" you are trying to update does not exist.";
		}
		
		try {
			if (apptTypeDAO.editApptType(apptType)) {
				transDAO.logTransaction(TransactionType.UPDATE_APPT_TYPE, loggedInMID, 0L,
						"Updated Appointment Type " + apptType.getName());
				return "Success: " + apptType.getName() + " - Duration: " + apptType.getDuration() + " updated";
			} else
				return "The database has become corrupt. Please contact the system administrator for assistance.";
		} catch (DBException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public int getDurationByType(String at) throws SQLException {
		List<ApptTypeBean> list = this.getApptTypes();
		for(ApptTypeBean a : list) {
			if(a.getName().equals(at))
				return a.getDuration();
		}
		return 0;
	}
}
