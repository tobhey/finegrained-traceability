package edu.ncsu.csc.itrust.action;

import java.util.List;
import java.util.ArrayList;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.HCPVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Action class for ViewVisitedHCPs.jsp
 *
 */
public class ViewVisitedHCPsAction {

	private long patientMID;
	private PersonnelDAO docDAO;
	private OfficeVisitDAO visitDAO;
	private PatientDAO patientDAO;
	private ArrayList<HCPVisitBean> visits;
	private DeclareHCPAction declareAction; 
	private ArrayList<PersonnelBean> filterList;
	
	/**
	 * Set up defaults 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing the visited HCPs.
	 */
	public ViewVisitedHCPsAction(DAOFactory factory, long loggedInMID) {
		patientMID = loggedInMID;
		docDAO = factory.getPersonnelDAO();
		visitDAO = factory.getOfficeVisitDAO();
		patientDAO = factory.getPatientDAO();
		
		visits = new ArrayList<HCPVisitBean>();
		declareAction = new DeclareHCPAction(factory, loggedInMID);
		filterList = new ArrayList<PersonnelBean>();
	}
	
	/**
	 * Adds all the office visits to a global array
	 * 
	 * @throws iTrustException
	 */
	private void processOfficeVisits() throws iTrustException {
		
		try {
			List<OfficeVisitBean> ovlist = visitDAO.getAllOfficeVisits(patientMID);
			HCPVisitBean visitBean;
			List<PersonnelBean> dhcps = patientDAO.getDeclaredHCPs(patientMID);
			boolean tmp;
			PersonnelBean pb;
			ArrayList<PersonnelBean> removeIDs = new ArrayList<PersonnelBean>();
			
			for (OfficeVisitBean ov: ovlist) {
				visitBean = new HCPVisitBean();
				pb = docDAO.getPersonnel(ov.getHcpID());
				
				visitBean.setHCPMID(ov.getHcpID());
				visitBean.setHCPName(pb.getFullName());
				visitBean.setOVDate(ov.getVisitDateStr());
				visitBean.setHCPSpecialty(pb.getSpecialty());
				visitBean.setHCPAddr(pb.getStreetAddress1() +" "+ pb.getStreetAddress2() +" "+ pb.getCity() +", "+ pb.getState() +" "+ pb.getZip());
								
				if (true == (tmp = patientDAO.checkDeclaredHCP(patientMID, ov.getHcpID()))) {
					visitBean.setDesignated(tmp);
					if (!dhcps.isEmpty()) {
						for (PersonnelBean hcp : dhcps) {
							if (hcp.getMID() == ov.getHcpID()) {
								removeIDs.add(hcp);
							}
						}
						if (!removeIDs.isEmpty()) {
							for (PersonnelBean pbean: removeIDs) {
								dhcps.remove(pbean);
							}
						}
					}
				}
				
				visits.add(visitBean);
				
			}
			
			for (PersonnelBean hcp : dhcps) {
				visitBean = new HCPVisitBean();
				visitBean.setHCPMID(hcp.getMID());
				visitBean.setHCPName(hcp.getFullName());
				visitBean.setOVDate("");
				visitBean.setHCPSpecialty(hcp.getSpecialty());
				visitBean.setHCPAddr(hcp.getStreetAddress1() +" "+ hcp.getStreetAddress2() +" "+ hcp.getCity() +", "+ hcp.getState() +" "+ hcp.getZip());
				visitBean.setDesignated(true);
				visits.add(visitBean);
			}
			
			
		}
		catch (DBException dbe) {
			throw new iTrustException(dbe.getMessage());
		}
	}
	
	/**
	 * Returns a list of all the visited HCPs
	 * @return list of all the visited HCPs
	 */
	
	public List<HCPVisitBean> getVisitedHCPs() {
		
		try {
			processOfficeVisits();
		
			for(int i = 0; i<visits.size(); i++){
				for(int j = i+1; j<visits.size(); j++){
					if(visits.get(i).getHCPMID()==visits.get(j).getHCPMID()){
						visits.remove(visits.get(j));
						j--;
					}
				}
			}
		}
		catch (iTrustException ie) {
			
		}
			
		return visits;
	}
	
	/**
	 * Set a given HCP as undeclared
	 * 
	 * @param name HCP to undeclare
	 * @return An empty string.
	 * @throws iTrustException
	 */
	public String undeclareHCP(String name) throws iTrustException {

		HCPVisitBean remove = null;
		
		for (HCPVisitBean visit: visits) {
			if (0 == visit.getHCPName().toLowerCase().compareTo(name.toLowerCase())) {
				Long mid = Long.valueOf(visit.getHCPMID());

				//if (patientDAO.checkDeclaredHCP(patientMID, visit.getHCPMID())) {
					declareAction.undeclareHCP(mid.toString());
				//}
				visit.setDesignated(false);
				
				if (0 == visit.getOVDate().compareTo("")) {
					remove = visit;
				}
			}
		}
		
		if (null != remove) {
			visits.remove(remove);
		}
				
		return "";
	}
	
	/**
	 * Set a given HCP as declared
	 * 
	 * @param name HCP to declare
	 * @return An empty string.
	 * @throws iTrustException
	 */
	public String declareHCP(String name) throws iTrustException {
		boolean match = false;
		for (HCPVisitBean visit: visits) {
			if (0 == visit.getHCPName().toLowerCase().compareTo(name.toLowerCase())) {
				match = true;
				Long mid = Long.valueOf(visit.getHCPMID());
				if (!patientDAO.checkDeclaredHCP(patientMID, visit.getHCPMID())) {
					declareAction.declareHCP(mid.toString());
				}
				visit.setDesignated(true);
			}
		}
		
		if (!match) {
				List<PersonnelBean> doclist = docDAO.getAllPersonnel();
				for (PersonnelBean ele: doclist) {
					if (0 == name.compareTo(ele.getFullName())) {
						HCPVisitBean visitBean;
						visitBean = new HCPVisitBean();
						visitBean.setHCPMID(ele.getMID());
						visitBean.setHCPName(ele.getFullName());
						visitBean.setOVDate("");
						visitBean.setHCPSpecialty(ele.getSpecialty());
						visitBean.setHCPAddr(ele.getStreetAddress1() +" "+ ele.getStreetAddress2() +" "+ ele.getCity() +", "+ ele.getState() +" "+ ele.getZip());
						
						visitBean.setDesignated(true);
				
						Long mid = Long.valueOf(ele.getMID());
						if (!patientDAO.checkDeclaredHCP(patientMID, mid)) {
							declareAction.declareHCP(mid.toString());
							visits.add(visitBean);
						}
					}
				}
			
		}
		return "";
	}
	
	/**
	 * Check to see if a given HCP is declared
	 * @param mid HCP to check
	 * @return true if the HCP is declared, otherwise false
	 */
	public boolean checkDeclared(long mid) {
		
		try {
			return patientDAO.checkDeclaredHCP(patientMID, mid);
		} catch (DBException dbe) {
			return false;
		}
	}
	
	/**
	 * Filter the list of HCPs by last name, specialty, or zip code. 
	 * @param doc sort by last name
	 * @param specialty sort by specialty
	 * @param zip sort by zip
	 * @return sorted list of HCPs
	 */
	public List<PersonnelBean> filterHCPList(String doc, String specialty, String zip) {
		List<PersonnelBean> doclist;
			
		try {
			doclist = docDAO.getAllPersonnel();
			for (PersonnelBean ele: doclist) {
				if (ele.getLastName().toLowerCase().contains(doc.toLowerCase())) {
					if (null != specialty && !specialty.equals("")) {
						if (0 == specialty.toLowerCase().compareTo(ele.getSpecialty().toLowerCase())) {
							if (null != zip && !zip.equals("")) {
								if (ele.getZip().contains(zip.substring(0, 2))) {
									filterList.add(ele);
								}
							}
							else {	
								filterList.add(ele);
							}
						}
					}
					else if (null != zip && !zip.equals("")) {
						if (ele.getZip().contains(zip.substring(0, 2))) {
							filterList.add(ele);
						}
					}
					else {
						filterList.add(ele);
					}
				}
			}
		}
		catch (DBException dbe) {
			
		}
		return filterList;
	}

}
