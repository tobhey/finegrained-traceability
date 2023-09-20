package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.action.EditApptTypeAction;
import edu.ncsu.csc.itrust.action.ViewMyApptsAction;
import edu.ncsu.csc.itrust.action.ViewMyRecordsAction;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Calendar;

/**
 * Action class for calendar.jsp
 * @author Software Engineering Team 13, 2010 "Project Zephyr"
 *
 */
public class GenerateCalendarAction {
	private ViewMyApptsAction a_action;
	private EditApptTypeAction types;
	private ViewMyRecordsAction r_action;
	private List<ApptBean> send;
	
	/**
	 * Set up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the user who is viewing the calendar
	 */
	public GenerateCalendarAction(DAOFactory factory, long loggedInMID) {
		a_action = new ViewMyApptsAction(factory, loggedInMID);
		types = new EditApptTypeAction(factory, loggedInMID);
		r_action = new ViewMyRecordsAction(factory, loggedInMID);
		send = new ArrayList<ApptBean>();
	}
	
	/**
	 * Return the send request for an AppointmentBean
	 * @return the send request for an AppointmentBean
	 */
	public List<ApptBean> getSend() {
		return send;
	}
	
	/**
	 * Check appointments appearing on the calendar for conflicts 
	 * with other appointments on the calendar. 
	 * 
	 * The array from this method is used to determine what appointments
	 * will appear in bold on the calendar.
	 * 
	 * @return An array of items that are in conflict with other items.
	 * @throws SQLException
	 */
	public boolean[] getConflicts() throws SQLException {
		boolean conflicts[] = new boolean[send.size()];
		for(int i=0; i<send.size(); i++) {
			ApptBean ab = send.get(i);
			long t = ab.getDate().getTime();
			long m = types.getDurationByType(ab.getApptType()) * 60L * 1000L;
			Timestamp time = new Timestamp(t+m);
			for(int j=i+1; j<send.size(); j++) {
				if(send.get(j).getDate().before(time)) {
					conflicts[i] = true;
					conflicts[j] = true;
				}
			}
		}
		return conflicts;
	}
	
	/**
	 * Creates a hash table with all of the Appointments to be 
	 * displayed on the calendar for the month and year being viewed.
	 * 
	 * @param thisMonth The month of the calendar to be rendered
	 * @param thisYear The year of the calendar to be rendered
	 * @return A Hashtable containing the AppointmentBeans to be rendered
	 * @throws SQLException
	 */
	public Hashtable<Integer, ArrayList<ApptBean>> getApptsTable(int thisMonth, int thisYear) throws SQLException {
		List<ApptBean> appts = a_action.getMyAppointments();
		Hashtable<Integer, ArrayList<ApptBean>> atable = new Hashtable<Integer, ArrayList<ApptBean>>();
		Calendar a = Calendar.getInstance();
		for(ApptBean b : appts) {
			a.setTimeInMillis(b.getDate().getTime());
			if(a.get(Calendar.MONTH) == thisMonth && a.get(Calendar.YEAR) == thisYear) {
				if(!atable.containsKey(a.get(Calendar.DAY_OF_MONTH)))
					atable.put(a.get(Calendar.DAY_OF_MONTH), new ArrayList<ApptBean>());
				ArrayList<ApptBean> l = atable.get(a.get(Calendar.DAY_OF_MONTH));
				l.add(b);
				send.add(b);
				atable.put(a.get(Calendar.DAY_OF_MONTH), l);
			}
		}
		return atable;
	}
	
	/**
	 * Creates a hash table with all of the Office Visits to be 
	 * displayed on the calendar for the month and year being viewed.
	 * 
	 * @param thisMonth The month of the calendar to be rendered
	 * @param thisYear The year of the calendar to be rendered
	 * @return A Hashtable containing the OfficeVisitBeans to be rendered
	 * @throws SQLException
	 */
	public Hashtable<Integer, ArrayList<OfficeVisitBean>> getOfficeVisitsTable(int thisMonth, int thisYear) throws iTrustException {
		List<OfficeVisitBean> officeVisits = r_action.getAllOfficeVisits();
		Hashtable<Integer, ArrayList<OfficeVisitBean>> rtable = new Hashtable<Integer, ArrayList<OfficeVisitBean>>();
		Calendar a = Calendar.getInstance();
		for(OfficeVisitBean b : officeVisits) {
			a.setTimeInMillis(b.getVisitDate().getTime());
			if(a.get(Calendar.MONTH) == thisMonth && a.get(Calendar.YEAR) == thisYear) {
				if(!rtable.containsKey(a.get(Calendar.DAY_OF_MONTH)))
					rtable.put(a.get(Calendar.DAY_OF_MONTH), new ArrayList<OfficeVisitBean>());
				ArrayList<OfficeVisitBean> l = rtable.get(a.get(Calendar.DAY_OF_MONTH));
				l.add(b);
				rtable.put(a.get(Calendar.DAY_OF_MONTH), l);
			}
		}
		return rtable;
	}
	
	/**
	 * Creates a hash table with all of the lab procedures to be 
	 * displayed on the calendar for the month and year being viewed.
	 * 
	 * @param thisMonth The month of the calendar to be rendered
	 * @param thisYear The year of the calendar to be rendered
	 * @return A Hashtable containing the LabProcedureBeans to be rendered
	 * @throws SQLException
	 */
	public Hashtable<Integer, ArrayList<LabProcedureBean>> getLabProceduresTable(int thisMonth, int thisYear) throws iTrustException {
		List<LabProcedureBean> procs = r_action.getLabs();
		Hashtable<Integer, ArrayList<LabProcedureBean>> ptable = new Hashtable<Integer, ArrayList<LabProcedureBean>>();
		Calendar a = Calendar.getInstance();
		for(LabProcedureBean b : procs) {
			a.setTimeInMillis(b.getTimestamp().getTime());
			if(a.get(Calendar.MONTH) == thisMonth && a.get(Calendar.YEAR) == thisYear) {
				if(!ptable.containsKey(a.get(Calendar.DAY_OF_MONTH)))
					ptable.put(a.get(Calendar.DAY_OF_MONTH), new ArrayList<LabProcedureBean>());
				ArrayList<LabProcedureBean> l = ptable.get(a.get(Calendar.DAY_OF_MONTH));
				l.add(b);
				ptable.put(a.get(Calendar.DAY_OF_MONTH), l);
			}
		}
		return ptable;
	}
}
