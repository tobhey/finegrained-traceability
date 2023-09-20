package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.PrescriptionReportBean;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.beans.loaders.DiagnosisBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.OfficeVisitLoader;
import edu.ncsu.csc.itrust.beans.loaders.PrescriptionBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.PrescriptionReportBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.ProcedureBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for doing tasks related to office visits. Use this for linking diagnoses to office visits, and similar
 * tasks.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * @author Andy
 * 
 */
public class OfficeVisitDAO {
	private DAOFactory factory;
	private OfficeVisitLoader officeVisitLoader = new OfficeVisitLoader();
	private DiagnosisBeanLoader diagnosisLoader = new DiagnosisBeanLoader(true);
	private PrescriptionBeanLoader prescriptionLoader = new PrescriptionBeanLoader();
	private PrescriptionReportBeanLoader prescriptionReportBeanLoader = new PrescriptionReportBeanLoader();
	private ProcedureBeanLoader procedureBeanLoader = new ProcedureBeanLoader(true);

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public OfficeVisitDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Adds an visit and return its ID
	 * 
	 * @param ov The OfficeVisitBean to be added.
	 * @return A long indicating the unique ID for the office visit.
	 * @throws DBException
	 */
	public long add(OfficeVisitBean ov) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO OfficeVisits (VisitDate, Notes, HCPID, PatientID, HospitalID) VALUES (?,?,?,?,?)");
			setValues(ps, ov);
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	private void setValues(PreparedStatement ps, OfficeVisitBean ov) throws SQLException {
		ps.setDate(1, new java.sql.Date(ov.getVisitDate().getTime()));
		ps.setString(2, ov.getNotes());
		ps.setLong(3, ov.getHcpID());
		ps.setLong(4, ov.getPatientID());
		ps.setString(5, ov.getHospitalID());
	}

	/**
	 * Updates the information in a particular office visit.
	 * 
	 * @param ov The Office Visit bean representing the changes.
	 * @throws DBException
	 */
	public void update(OfficeVisitBean ov) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE OfficeVisits SET VisitDate=?, Notes=?, HCPID=?, "
					+ "PatientID=?, HospitalID=? WHERE ID=?");
			setValues(ps, ov);
			ps.setLong(6, ov.getID());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a particular office visit given an ID
	 * 
	 * @param visitID The unique ID of the office visit.
	 * @return An OfficeVisitBean with the specifics for that office visit.
	 * @throws DBException
	 */
	public OfficeVisitBean getOfficeVisit(long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OfficeVisits Where ID = ?");
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return loadFullOfficeVist(rs, visitID);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	private OfficeVisitBean loadFullOfficeVist(ResultSet rs, long visitID) throws SQLException, DBException {
		OfficeVisitBean ov = new OfficeVisitBean(visitID);
		ov.setVisitDateStr(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("VisitDate")
				.getTime())));
		ov.setHcpID(rs.getLong("HCPID"));
		ov.setNotes(rs.getString("notes"));
		ov.setPatientID(rs.getLong("PatientID"));
		ov.setHospitalID(rs.getString("HospitalID"));
		ov.setDiagnoses(getDiagnoses(visitID));
		ov.setPrescriptions(getPrescriptions(visitID));
		ov.setProcedures(getProcedures(visitID));

		return ov;
	}

	/**
	 * Returns all of the procedures associated with the given office visit
	 * 
	 * @param visitID The unique ID of the office visit.
	 * @return A java.util.List of procedures.
	 * @throws DBException
	 */
	public List<ProcedureBean> getProcedures(long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		List<ProcedureBean> procs;
		
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("select * from ovprocedure ovp, officevisits ov, cptcodes cpt where ov.id=? and ovp.visitid=? and cpt.code=ovp.cptcode");
			ps.setLong(1, visitID);
			ps.setLong(2, visitID);
			ResultSet rs = ps.executeQuery();
			procs = procedureBeanLoader.loadList(rs);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} 
		finally {
			DBUtil.closeConnection(conn, ps);
		}
		
		return procs;
	}

	
	/**
	 * Returns all of the prescriptions associated with the given office visit
	 * 
	 * @param visitID The ID of the office visit.
	 * @return A java.util.List of prescriptions.
	 * @throws DBException
	 */
	public List<PrescriptionBean> getPrescriptions(long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OVMedication,NDCodes Where OVMedication.VisitID = ? "
					+ "AND NDCodes.Code=OVMedication.NDCode");
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			return prescriptionLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns all of the diagnoses associated with the given office visit
	 * 
	 * @param visitID The unique ID of the office visit.
	 * @return A java.util.List of diagnoses.
	 * @throws DBException
	 */
	public List<DiagnosisBean> getDiagnoses(long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OVDiagnosis,ICDCodes Where OVDiagnosis.VisitID = ? "
					+ "AND ICDCodes.Code=OVDiagnosis.ICDCode");
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			return diagnosisLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds the given CPT codes to the given office visit
	 * 
	 * @param cptCode
	 * @param visitID
	 * @return
	 * @throws DBException
	 */
	public long addProcedureToOfficeVisit (String cptCode, long visitID, String hcpid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO OVProcedure (CPTCode,VisitID,HCPID) VALUES (?,?,?)");
			ps.setString(1, cptCode);
			ps.setLong(2, visitID);
			ps.setString(3, hcpid);
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Removes the given procedure from its office visit
	 * 
	 * @param ovProcedureID The unique ID of the office visit procedure.
	 * @throws DBException
	 */
	public void removeProcedureFromOfficeVisit(long ovProcedureID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM OVProcedure WHERE ID=? ");
			ps.setLong(1, ovProcedureID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds a prescription bean to the database.
	 * @param pres The prescription bean to be added.
	 * @return The unique ID of the newly added bean.
	 * @throws DBException
	 */
	public long addPrescription(PrescriptionBean pres) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO OVMedication (VisitID,NDCode,StartDate,EndDate,Dosage,Instructions) VALUES (?,?,?,?,?,?)");
			prescriptionLoader.loadParameters(ps, pres);
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	
	/**
	 * Edits an existing prescription bean.
	 * 
	 * @param pres The newly updated prescription bean.
	 * @return A long indicating the ID of the newly updated prescription bean.
	 * @throws DBException
	 */
	public long editPrescription(PrescriptionBean pres) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			//ps = conn.prepareStatement("UPDATE OVMedication (VisitID,NDCode,StartDate,EndDate,Dosage,Instructions) VALUES (?,?,?,?,?,?)");
			String statement = "UPDATE OVMedication " +
				"SET VisitID=?, NDCode=?, StartDate=?, EndDate=?, Dosage=?, Instructions=? " +
				"WHERE ID=?";
			ps = conn.prepareStatement(statement);
			prescriptionLoader.loadParameters(ps, pres);
			ps.setLong(7, pres.getId());
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Removes the given medication from its office visit
	 * 
	 * @param ovMedicationID The unique ID of the medication to be removed.
	 * @throws DBException
	 */
	public void removePrescription(long ovMedicationID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM OVMedication WHERE ID=? ");
			ps.setLong(1, ovMedicationID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds the given CPT codes to the given office visit
	 * @param icd A double representing the ICD code to be added.
	 * @param visitID The ID of the office visit we are adding the code to.
	 * @return A long for the new ICD code's ID.
	 * @throws DBException
	 */
	public long addDiagnosisToOfficeVisit(double icd, long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO OVDiagnosis (ICDCode,VisitID) VALUES (?,?)");
			ps.setDouble(1, icd);
			ps.setLong(2, visitID);
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	

	/**
	 * Removes a particular diagnosis from its office visit
	 * 
	 * @param ovDiagnosisID The ID for the office visit diagnosis to be removed. 
	 * @throws DBException
	 */
	public void removeDiagnosisFromOfficeVisit(long ovDiagnosisID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM OVDiagnosis WHERE ID=? ");
			ps.setLong(1, ovDiagnosisID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Adds a LOINCCode to an office visit.
	 * 
	 * @param LOINCCode A string of the code being added.
	 * @param visitID The ID of the office visit the code is being added to.
	 * @param pid The patient's MID associated with this transaction.
	 * @return The unique ID of the code that was added.
	 * @throws DBException
	 */
	public long addLabProcedureToOfficeVisit(String LOINCCode, long visitID, long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO LabProcedure (LaboratoryProcedureCode,OfficeVisitID," +
					"Commentary, Results, PatientMID, Status, Rights) VALUES (?,?,?,?,?,?,?)");
			ps.setString(1, LOINCCode);
			ps.setLong(2, visitID);
			ps.setString(3, "");
			ps.setString(4, "");
			ps.setLong(5, pid);
			ps.setString(6, LabProcedureBean.Not_Received);
			ps.setString(7, "ALLOWED");
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Removes a given lab procedure.
	 * 
	 * @param labProcedureID The unique ID of the lab procedure.
	 * @throws DBException
	 */
	public void removeLabProcedureFromOfficeVisit(long labProcedureID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM LabProcedure WHERE LaboratoryProcedureID=?");
			ps.setLong(1, labProcedureID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Returns whether or not an office visit actually exists
	 * 
	 * @param ovID The ID of the office visit to be checked.
	 * @param pid The MID of the patient associated with this transaction.
	 * @return A boolean indicating its existence.
	 * @throws DBException
	 */
	public boolean checkOfficeVisitExists(long ovID, long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM OfficeVisits WHERE ID=? AND PatientID=?");
			ps.setLong(1, ovID);
			ps.setLong(2, pid);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of all office visits for a given patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of OfficeVisitBeans.
	 * @throws DBException
	 */
	public List<OfficeVisitBean> getAllOfficeVisits(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM OfficeVisits WHERE PatientID=? ORDER BY VisitDate DESC");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return officeVisitLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Returns a list of all office visits for a given patient
	 * 
	 * @param mid The MID of the LHCP you are looking up.
	 * @return A java.util.List of Office Visits.
	 * @throws DBException
	 */
	public List<OfficeVisitBean> getAllOfficeVisitsForLHCP(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (mid == 0L) throw new SQLException("HCPID cannot be null");
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM OfficeVisits WHERE HCPID=? ORDER BY VisitDate DESC");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			return officeVisitLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of all office visits for a given patient
	 * 
	 * @param pid The MID of the patient.
	 * @return A java.util.List of Office Visits.
	 * @throws DBException
	 */
	public List<OfficeVisitBean> getOfficeVisitsWithNoSurvey(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM OfficeVisits where id not in (select visitid from OVSurvey) and PatientID = ? ORDER BY VisitDate DESC");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return officeVisitLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of information related to prescription reports given all of the office visits and the
	 * patient ID. The patient ID is necessary in case the office visit IDs are for different patients (the
	 * disambiguation is for security reasons).
	 * 
	 * @param ovIDs A java.util.List of Longs for the office visits.
	 * @param patientID A long representing the MID of the patient in question.
	 * @return A java.util.List of prescription reports.
	 * @throws DBException
	 */
	public List<PrescriptionReportBean> getPrescriptionReportsByDate(long patientID, String startDate, String endDate)
			throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM NDCodes, OVMedication, OfficeVisits "
					+ "WHERE NDCodes.Code=OVMedication.NDCode AND OVMedication.VisitID=OfficeVisits.ID "
					+ "AND PatientID=? AND ((DATE(?) < OVMedication.EndDate AND DATE(?) > OVMedication.StartDate)"
					+ "OR (DATE(?) > OVMedication.StartDate  AND DATE(?) < OVMedication.EndDate ) OR "
					+ "(DATE(?) <= OVMedication.StartDate AND DATE(?) >= OVMedication.StartDate)) "
					+ "ORDER BY VisitDate DESC");
			ps.setLong(1, patientID);
			ps.setString(2, startDate);
			ps.setString(3, startDate);
			ps.setString(4, endDate);
			ps.setString(5, endDate);
			ps.setString(6, startDate);
			ps.setString(7, endDate);
			ResultSet rs = ps.executeQuery();
			return prescriptionReportBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of information related to prescription reports given all of the office visits and the
	 * patient ID. The patient ID is necessary in case the office visit IDs are for different patients (the
	 * disambiguation is for security reasons).
	 * 
	 * @param ovIDs A java.util.List of Longs for the office visits.
	 * @param patientID A long representing the MID of the patient in question.
	 * @return A java.util.List of prescription reports.
	 * @throws DBException
	 */
	public List<PrescriptionReportBean> getPrescriptionReports(List<Long> ovIDs, long patientID)
			throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			String preps = buildPreps(ovIDs.size());
			ps = conn.prepareStatement("SELECT * FROM NDCodes, OVMedication, OfficeVisits "
					+ "WHERE NDCodes.Code=OVMedication.NDCode AND OVMedication.VisitID=OfficeVisits.ID "
					+ "AND PatientID=? AND VisitID IN(" + preps + ") ORDER BY VisitDate DESC");
			ps.setLong(1, patientID);
			prepareOVIDs(ps, ovIDs);
			ResultSet rs = ps.executeQuery();
			return prescriptionReportBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	private void prepareOVIDs(PreparedStatement ps, List<Long> ovIDs) throws SQLException {
		for (int i = 0; i < ovIDs.size(); i++) {
			ps.setLong(i + 2, ovIDs.get(i));
		}
	}

	private String buildPreps(int size) {
		String prep = "";
		for (int i = 0; i < size; i++) {
			prep += "?,";
		}
		if (prep.length() > 0)
			return prep.substring(0, prep.length() - 1);
		else
			return prep;
	}
	
	/**
	 * Returns a list of all office visits at a given hospital
	 * @param hospitalID the id of the hospital
	 * @return a list of the OfficeVisitBeans that hold the office visits
	 * @throws DBException in the event of a database error
	 */
	public List<OfficeVisitBean> getOfficeVisitsFromHospital(String hospitalID) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM officevisits WHERE hospitalID = ? ORDER BY ID DESC");
			ps.setString(1, hospitalID);
			ResultSet rs = ps.executeQuery();
			return officeVisitLoader.loadList(rs);
		} catch (SQLException e){
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		
	}
	
	/**
	 * Gets all office visits corresponding to a particular ICD diagnosis.
	 * 
	 * @param icdcode A string represnting the ICD diagnosis to look up.
	 * @return A java.util.List of Office visits.
	 * @throws DBException
	 */
	public List<OfficeVisitBean> getAllOfficeVisitsForDiagnosis(String icdcode) throws DBException {
		
		List<DiagnosisBean> diags = null;
		List<OfficeVisitBean> ovs = new ArrayList<OfficeVisitBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs= null;
		
		try {
			if (icdcode == null) 
				throw new SQLException("icdcode cannot be null");
			
			conn = factory.getConnection();
			
			ps = conn.prepareStatement("SELECT * FROM ovdiagnosis ovd, icdcodes icd WHERE ovd.ICDCode=? and icd.Code=?");
			ps.setString(1, icdcode);
			ps.setString(2, icdcode);
			rs = ps.executeQuery();
			diags = diagnosisLoader.loadList(rs);
			rs.close();
			ps.close();
			ps = null;
			rs = null;
			
			for (DiagnosisBean bean: diags) {
				ps = conn.prepareStatement("SELECT * FROM officevisits ov WHERE ov.ID=?");
				ps.setInt(1, (int)bean.getVisitID());
				rs = ps.executeQuery();
				
				if (rs.next()) {
					ovs.add(loadFullOfficeVist(rs, bean.getVisitID()));
				}
				rs.close();
				ps.close();
			}
			
			return ovs;
		
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}	
}
