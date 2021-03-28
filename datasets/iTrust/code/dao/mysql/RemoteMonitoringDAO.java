package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.beans.loaders.RemoteMonitoringDataBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for the keeping track of remote monitoring data.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 */
public class RemoteMonitoringDAO {
	private DAOFactory factory;
	private RemoteMonitoringDataBeanLoader loader = new RemoteMonitoringDataBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public RemoteMonitoringDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns patient data for a given HCP
	 * 
	 * @return
	 * @throws DBException
	 */
	public List<RemoteMonitoringDataBean> getPatientsData(long loggedInMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringLists WHERE HCPMID=? ORDER BY PatientMID");
			ps.setLong(1, loggedInMID);
			ResultSet patientRS = ps.executeQuery();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringData WHERE timelogged >= CURRENT_DATE ORDER BY PatientID, timeLogged DESC");
			ResultSet dataRS = ps.executeQuery();
			
			List<String> patientList = new ArrayList<String>();
			while(patientRS.next()) {
				patientList.add(patientRS.getLong("PatientMID") + "");
			}
			List<RemoteMonitoringDataBean> dataList = loader.loadList(dataRS);			
			
			int i, j;
			//Go through all patients and remove any that aren't monitored by this HCP
			for(i = 0; i < dataList.size(); i++) {
				if(!patientList.contains(dataList.get(i).getPatientMID() + "")) {
					dataList.remove(i);
					i--;
				}
			}
			
			//Add values in patient list with no data for today to list.
			boolean itsThere;
			for(i = 0; i < patientList.size(); i++) {
				itsThere = false;
				for(j = 0; j < dataList.size(); j++) {
					if((dataList.get(j).getPatientMID() + "").equals(patientList.get(i))) {
						itsThere = true;
						break;
					}
				}
				if(!itsThere) {
					dataList.add(new RemoteMonitoringDataBean(Long.parseLong(patientList.get(i))));
				}
			}
			
			return dataList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	public List<RemoteMonitoringDataBean> getPatientDataByDate(long patientMID, Date lower, Date upper) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM remotemonitoringdata WHERE PatientID=? AND timeLogged >= ? AND timeLogged <= ? ORDER BY timeLogged DESC");
			ps.setLong(1, patientMID);
			ps.setTimestamp(2, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(3, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));
			ResultSet rs = ps.executeQuery();
			List<RemoteMonitoringDataBean> dataList = loader.loadList(rs);
			return dataList;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Store data for a given patient in the RemoteMonitoringData table
	 * 
	 * @param patientMID The MID of the patient
	 * @param systolicBloodPressure The systolic blood pressure of the patient
	 * @param diastolicBloodPressure The diastolic blood pressure of the patient
	 * @param glucoseLevel The glucose level of the patient
	 * @param reporterRole  The role of the person that reported these monitoring stats
	 * @param reporterMID  The MID of the person that reported these monitoring stats
	 * @throws DBException
	 */
	public void storePatientData(long patientMID, int systolicBloodPressure, int diastolicBloodPressure, int glucoseLevel, String reporterRole, long reporterMID)
			throws DBException, iTrustException {
		if(getNumberOfDailyEntries(patientMID) >= 10)
			throw new iTrustException("Patient entries for today cannot exceed 10.");
		
		if(reporterRole.equals("patient representative"))
			validatePR(reporterMID, patientMID);
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO RemoteMonitoringData(PatientID, systolicBloodPressure, "
					+ "diastolicBloodPressure, glucoseLevel, ReporterRole, ReporterID) VALUES(?,?,?,?,?,?)");
			ps.setLong(1, patientMID);
			ps.setLong(2, systolicBloodPressure);
			ps.setInt(3, diastolicBloodPressure);
			ps.setInt(4, glucoseLevel);
			ps.setString(5, reporterRole);
			ps.setLong(6, reporterMID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Store data for a given patient in the RemoteMonitoringData table
	 * 
	 * @param patientMID The MID of the patient
	 * @param systolicBloodPressure The systolic blood pressure of the patient
	 * @param diastolicBloodPressure The diastolic blood pressure of the patient
	 * @param glucoseLevel The glucose level of the patient
	 * @param reporterRole  The role of the person that reported these monitoring stats
	 * @param reporterMID  The MID of the person that reported these monitoring stats
	 * @throws DBException
	 */
	public void storePatientData(long patientMID, int glucoseLevel, String reporterRole, long reporterMID)
			throws DBException, iTrustException {
		if(getNumberOfDailyEntries(patientMID) >= 10)
			throw new iTrustException("Patient entries for today cannot exceed 10.");
		
		if(reporterRole.equals("patient representative"))
			validatePR(reporterMID, patientMID);
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO RemoteMonitoringData(PatientID, glucoseLevel, ReporterRole, ReporterID) VALUES(?,?,?,?)");
			ps.setLong(1, patientMID);
			ps.setInt(2, glucoseLevel);
			ps.setString(3, reporterRole);
			ps.setLong(4, reporterMID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Store data for a given patient in the RemoteMonitoringData table
	 * 
	 * @param patientMID The MID of the patient
	 * @param systolicBloodPressure The systolic blood pressure of the patient
	 * @param diastolicBloodPressure The diastolic blood pressure of the patient
	 * @param glucoseLevel The glucose level of the patient
	 * @param reporterRole  The role of the person that reported these monitoring stats
	 * @param reporterMID  The MID of the person that reported these monitoring stats
	 * @throws DBException
	 */
	public void storePatientData(long patientMID, int systolicBloodPressure, int diastolicBloodPressure, String reporterRole, long reporterMID)
			throws DBException, iTrustException {
		if(getNumberOfDailyEntries(patientMID) >= 10)
			throw new iTrustException("Patient entries for today cannot exceed 10.");
		
		if(reporterRole.equals("patient representative"))
			validatePR(reporterMID, patientMID);
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO RemoteMonitoringData(PatientID, systolicBloodPressure, "
					+ "diastolicBloodPressure, ReporterRole, ReporterID) VALUES(?,?,?,?,?)");
			ps.setLong(1, patientMID);
			ps.setLong(2, systolicBloodPressure);
			ps.setInt(3, diastolicBloodPressure);
			ps.setString(4, reporterRole);
			ps.setLong(5, reporterMID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Private method to get the number of entries for a certain patientID for today.
	 * @param patientMID
	 * @return the number of entries
	 * @throws DBException
	 */
	private int getNumberOfDailyEntries(long patientMID) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringData WHERE PatientID=? AND DATE(timeLogged)=CURRENT_DATE");
			ps.setLong(1, patientMID);
			ResultSet rs = ps.executeQuery();
			List<RemoteMonitoringDataBean> patients = loader.loadList(rs);
			return patients.size();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	public void validatePR(long representativeMID, long patientMID)
			throws iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Representatives WHERE RepresenterMID=? AND RepresenteeMID=?");
			ps.setLong(1, representativeMID);
			ps.setLong(2, patientMID);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) //no rows
				throw new iTrustException("Representer is not valid for patient " + patientMID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Add a patient to the list of HCPs' monitoring lists of Patients
	 * 
	 * @param patientMID The MID of the patient
	 * @param HCPMID The MID of the HCP
	 * @return true if added successfully, false if already in list
	 */
	public boolean addPatientToList(long patientMID, long HCPMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringLists WHERE PatientMID = ? AND HCPMID = ?");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return false;
			ps = conn.prepareStatement("INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID) VALUES(?,?)");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Remove a patient from the list of HCPs' monitoring lists of Patients
	 * 
	 * @param patientMID The MID of the patient
	 * @param HCPMID The MID of the HCP
	 * @return true if removed successfully, false if not in list
	 */
	public boolean removePatientFromList(long patientMID, long HCPMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			/*ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringList WHERE PatientMID = ? AND HCPMID = ?");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return false;*/
			ps = conn.prepareStatement("DELETE FROM RemoteMonitoringLists WHERE PatientMID = ? AND HCPMID = ?");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			if(ps.executeUpdate() == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
}
