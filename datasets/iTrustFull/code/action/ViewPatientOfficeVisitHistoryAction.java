package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PatientVisitBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * 
 * Action class for ViewPatientOfficeVisitHistory.jsp
 *
 */
public class ViewPatientOfficeVisitHistoryAction {
	private long loggedInMID;
	private PatientDAO patientDAO;
	private TransactionDAO transDAO;
	private PersonnelDAO personnelDAO;
	private OfficeVisitDAO officevisitDAO;
	private ArrayList<PatientVisitBean> visits;

	/**
	 * Set up defaults
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the office visits.
	 */
	public ViewPatientOfficeVisitHistoryAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.personnelDAO = factory.getPersonnelDAO();
		this.transDAO = factory.getTransactionDAO();
		officevisitDAO = factory.getOfficeVisitDAO();
		this.patientDAO = factory.getPatientDAO();
		
		visits = new ArrayList<PatientVisitBean>();
		
	}
	
	/**
	 * Adds all the office visits for the logged in HCP to a list.
	 * 
	 * @throws iTrustException
	 */
	private void processOfficeVisits() throws iTrustException {
		try {
			List<OfficeVisitBean> ovlist = officevisitDAO.getAllOfficeVisitsForLHCP(loggedInMID);
			Iterator<OfficeVisitBean> it = ovlist.iterator();
			OfficeVisitBean ov;
			PatientVisitBean visitBean;
			
			while (it.hasNext()) {
				visitBean = new PatientVisitBean();
				ov = it.next();
				PatientBean pb;
				pb = patientDAO.getPatient(ov.getPatientID());
				visitBean.setPatient(pb);
				visitBean.setPatientName(pb.getFullName());
				String date = ov.getVisitDateStr();
				Scanner sc = new Scanner(date);
				sc.useDelimiter("/");
				String month = sc.next();
				String day = sc.next();
				String year = sc.next();
				visitBean.setLastOVDateM(month);
				visitBean.setLastOVDateD(day);
				visitBean.setLastOVDateY(year);
				visitBean.setLastOVDate(year +"-" + month +"-" + date);
				visitBean.setAddress1(pb.getStreetAddress1() +" " + pb.getStreetAddress2());
				visitBean.setAddress2(pb.getCity() + " " +pb.getState() +" " +pb.getZip());
				boolean b = PatientInList(pb,date);
				if(b == false)
					visits.add(visitBean);
				
			}
		}
		catch (DBException dbe) {
			throw new iTrustException(dbe.getMessage());
		}
	}
	
	/**
	 * Checks to see if a patient is in the list of  visits
	 * @param pb patient to check
	 * @param date date of the visit
	 * @return True if the patient is in the list of visits.
	 */
	private boolean PatientInList(PatientBean pb, String date) {
		Iterator<PatientVisitBean> it = visits.iterator();
		PatientVisitBean visitBean;
		while (it.hasNext()){
			visitBean = it.next();
		if(visitBean.getPatientName().equals(pb.getFullName())){
				
				if(visitBean.getLastOVDateY().compareTo(date) < 0){
					visitBean.setLastOVDate(date);
				}
					return true;
				}
		}		
		
		return false;
	}
/**
 * Get the list of patients an HCP has had office visits with
 * 
 * @return the list of patients an HCP has had office visits with
 * @throws DBException
 */
	public List<PatientVisitBean> getPatients() throws DBException {
		
		try {
			processOfficeVisits();
		}
		catch (iTrustException ie) {
			
		}
		//log transaction
		transDAO.logTransaction(TransactionType.VIEW_PATIENT_LIST,loggedInMID);
		
		return visits;
	}
	/**
	 * Returns a PersonnelBean for the logged in HCP
	 * @return PersonnelBean for the logged in HCP
	 * @throws iTrustException
	 */
	public PersonnelBean getPersonnel() throws iTrustException {
		return personnelDAO.getPersonnel(loggedInMID);
	}
}