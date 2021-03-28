package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.iTrustException;

public class ViewMyApptsAction {
	private long loggedInMID;
	private ApptDAO apptDAO;
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	
	public ViewMyApptsAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.apptDAO = factory.getApptDAO();
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
	}
	
	public List<ApptBean> getMyAppointments() throws SQLException {
		return apptDAO.getApptsFor(loggedInMID);
	}
	
	/**
	 * Gets a users's name from their MID
	 * 
	 * @param mid the MID of the user
	 * @return the user's name
	 * @throws iTrustException
	 */
	public String getName(long mid) throws iTrustException {
		if(mid < 7000000000L)
			return patientDAO.getName(mid);
		else
			return personnelDAO.getName(mid);
	}
	
	/**
	 * Get All Appointments for the admins use... may need to change!
	 * @return
	 * @throws SQLException
	 */
	public List<ApptBean> getAllAppts()  throws SQLException {
		return apptDAO.getAllAppts();
	}
}
