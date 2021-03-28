package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;


public class SearchUsersAction {
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;


	/**
	 * Set up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the user who is performing the search.
	 */
	public SearchUsersAction(DAOFactory factory, long loggedInMID) {
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
	}
	

	/**
	 * Searches for all personnel with the first name and last name specified in the parameter list.
	 * @param firstName The first name to be searched.
	 * @param lastName The last name to be searched.
	 * @return A java.util.List of PersonnelBeans for the users who matched.
	 */
	public List<PersonnelBean> searchForPersonnelWithName(String firstName, String lastName) {
		
		try {	
			if("".equals(firstName))
				firstName = "%";
			if("".equals(lastName))
				lastName = "%";
			return personnelDAO.searchForPersonnelWithName(firstName, lastName);
		}
		catch (DBException e) {
			System.out.println("DB Exception from SearchUsersAction");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Search for all patients with first name and last name given in parameters.
	 * @param firstName The first name of the patient being searched.
	 * @param lastName The last name of the patient being searched.
	 * @return A java.util.List of PatientBeans
	 */
	public List<PatientBean> searchForPatientsWithName(String firstName, String lastName) {
	
		try {	
			if("".equals(firstName))
				firstName = "%";
			if("".equals(lastName))
				lastName = "%";
			return patientDAO.searchForPatientsWithName(firstName, lastName);
		}
		catch (DBException e) {
			System.out.println("DB Exception from SearchUsersAction");
			e.printStackTrace();
			return null;
		}
	}
}
