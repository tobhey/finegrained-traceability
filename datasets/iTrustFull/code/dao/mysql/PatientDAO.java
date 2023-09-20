package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.beans.loaders.DiagnosisBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.PatientLoader;
import edu.ncsu.csc.itrust.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.beans.loaders.PrescriptionBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.ProcedureBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.DateUtil;

/**
 * Used for managing all static information related to a patient. For other information related to all aspects
 * of patient care, see the other DAOs.
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
public class PatientDAO {
	private DAOFactory factory;
	private PatientLoader patientLoader;
	private PersonnelLoader personnelLoader;
	private DiagnosisBeanLoader diagnosisLoader;
	private PrescriptionBeanLoader prescriptionLoader;
	private ProcedureBeanLoader procedureLoader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public PatientDAO(DAOFactory factory) {
		this.factory = factory;
		this.patientLoader = new PatientLoader();
		this.personnelLoader = new PersonnelLoader();
		this.diagnosisLoader = new DiagnosisBeanLoader(true);
		this.prescriptionLoader = new PrescriptionBeanLoader();
		this.procedureLoader = new ProcedureBeanLoader(true);
	}

	/**
	 * Returns the name for the given MID
	 * 
	 * @param mid The MID of the patient in question.
	 * @return A String representing the patient's first name and last name.
	 * @throws iTrustException
	 * @throws DBException
	 */
	public String getName(long mid) throws iTrustException, DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT firstName, lastName FROM Patients WHERE MID=?");
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("firstName") + " " + rs.getString("lastName");
			} else {
				throw new iTrustException("User does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns the role of a particular patient - why is this in PatientDAO? It should be in AuthDAO
	 * 
	 * @param mid The MID of the patient in question.
	 * @param role A String representing the role of the patient.
	 * @return A String representing the patient's role.
	 * @throws iTrustException
	 * @throws DBException
	 */
	public String getRole(long mid, String role) throws iTrustException, DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT role FROM Users WHERE MID=? AND Role=?");
			ps.setLong(1, mid);
			ps.setString(2, role);
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("role");
			} else {
				throw new iTrustException("User does not exist with the designated role");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds an empty patient to the table, returns the new MID
	 * 
	 * @return The MID of the patient as a long.
	 * @throws DBException
	 */
	public long addEmptyPatient() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO Patients(MID) VALUES(NULL)");
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
	 * Returns the patient's information for a given ID
	 * 
	 * @param mid The MID of the patient to retrieve.
	 * @return A PatientBean representing the patient.
	 * @throws DBException
	 */
	public PatientBean getPatient(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Patients WHERE MID = ?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			// pdao.logTransaction(type, loggedInMID, secondaryMID, addedInfo)
			if (rs.next()) {
				return patientLoader.loadSingle(rs);
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Updates a patient's information for the given MID
	 * 
	 * @param p The patient bean representing the new information for the patient.
	 * @throws DBException
	 */
	public void editPatient(PatientBean p) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE Patients SET firstName=?,lastName=?,email=?,"
					+ "address1=?,address2=?,city=?,state=?,zip1=?, zip2=?,phone1=?,phone2=?,phone3=?,"
					+ "eName=?,ePhone1=?,ePhone2=?,ePhone3=?,iCName=?,iCAddress1=?,iCAddress2=?,iCCity=?,"
					+ "ICState=?,iCZip1=?, iCZip2=?, iCPhone1=?,iCPhone2=?,iCPhone3=?,iCID=?,DateOfBirth=?,"
					+ "DateOfDeath=?,CauseOfDeath=?,MotherMID=?,FatherMID=?,"
					+ "BloodType=?,Ethnicity=?,Gender=?,TopicalNotes=?, CreditCardType=?, CreditCardNumber=?, MessageFilter=? WHERE MID=?");
			patientLoader.loadParameters(ps, p);
			ps.setLong(40, p.getMID());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns whether or not the patient exists
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A boolean indicating whether the patient exists.
	 * @throws DBException
	 */
	public boolean checkPatientExists(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Patients WHERE MID=?");
			ps.setLong(1, pid);
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
	 * Returns a list of HCPs who are declared by the given patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of Personnel Beans.
	 * @throws DBException
	 */
	public List<PersonnelBean> getDeclaredHCPs(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pid == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM DeclaredHCP, Personnel "
					+ "WHERE PatientID=? AND Personnel.MID=DeclaredHCP.HCPID");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return personnelLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Declares an HCP for a particular patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @param hcpID The HCP's MID.
	 * @return A boolean as to whether the insertion was successful.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean declareHCP(long pid, long hcpID) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO DeclaredHCP(PatientID, HCPID) VALUES(?,?)");
			ps.setLong(1, pid);
			ps.setLong(2, hcpID);
			return 1 == ps.executeUpdate();
		} catch (SQLException e) {
			if (1062 == e.getErrorCode())
				throw new iTrustException("HCP " + hcpID + " has already been declared for patient " + pid);
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Undeclare an HCP for a given patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @param hcpID The MID of the HCP in question.
	 * @return A boolean indicating whether the action was successful.
	 * @throws DBException
	 */
	public boolean undeclareHCP(long pid, long hcpID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM DeclaredHCP WHERE PatientID=? AND HCPID=?");
			ps.setLong(1, pid);
			ps.setLong(2, hcpID);
			return 1 == ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Check if a patient has declared the given HCP
	 * 
	 * @param pid The MID of the patient in question as a long.
	 * @param hcpid The MID of the HCP in question as a long.
	 * @return
	 * @throws DBException
	 */
	public boolean checkDeclaredHCP(long pid, long hcpid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM DeclaredHCP WHERE PatientID=? AND HCPID=?");
			ps.setLong(1, pid);
			ps.setLong(2, hcpid);
			return ps.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of patients that the given patient represents
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of PatientBeans
	 * @throws DBException
	 */
	public List<PatientBean> getRepresented(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT Patients.* FROM Representatives, Patients "
					+ "WHERE RepresenterMID=? AND RepresenteeMID=Patients.MID");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return patientLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of patients that the given patient is represented by
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of PatientBeans.
	 * @throws DBException
	 */
	public List<PatientBean> getRepresenting(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT Patients.* FROM Representatives, Patients "
					+ "WHERE RepresenteeMID=? AND RepresenterMID=Patients.MID");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return patientLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Check if the given representer represents the representee
	 * 
	 * @param representer The MID of the representer in question.
	 * @param representee The MID of the representee in question.
	 * @return A boolean indicating whether represenation is in place.
	 * @throws DBException
	 */
	public boolean represents(long representer, long representee) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM Representatives WHERE RepresenterMID=? AND RepresenteeMID=?");
			ps.setLong(1, representer);
			ps.setLong(2, representee);
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
	 * Assign a representer to the representee
	 * 
	 * @param representer The MID of the representer as a long.
	 * @param representee The MID of the representee as a long.
	 * @return A boolean as to whether the insertion was correct.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean addRepresentative(long representer, long representee) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO Representatives(RepresenterMID,RepresenteeMID) VALUES (?,?)");
			ps.setLong(1, representer);
			ps.setLong(2, representee);
			return 1 == ps.executeUpdate();
		} catch (SQLException e) {
			if (1062 == e.getErrorCode())
				throw new iTrustException("Patient " + representer + " already represents patient "
						+ representee);
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Unassign the representation
	 * 
	 * @param representer The MID of the representer in question.
	 * @param representee The MID of the representee in question.
	 * @return A boolean indicating whether the unassignment was sucessful.
	 * @throws DBException
	 */
	public boolean removeRepresentative(long representer, long representee) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("DELETE FROM Representatives WHERE RepresenterMID=? AND RepresenteeMID=?");
			ps.setLong(1, representer);
			ps.setLong(2, representee);
			return 1 == ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of all diagnoses for a given patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of Diagnoses.
	 * @throws DBException
	 */
	public List<DiagnosisBean> getDiagnoses(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pid == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM OVDiagnosis ovd, OfficeVisits ov, ICDCodes icd "
					+ "WHERE ovd.VisitID=ov.ID and icd.Code=ovd.ICDCode and ov.PatientID=? "
					+ "ORDER BY ov.visitDate DESC");
			ps.setLong(1, pid);
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
	 * Return a list of all procedures for a given patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of all the procedures.
	 * @throws DBException
	 */
	public List<ProcedureBean> getProcedures(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pid == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OVProcedure ovp, OfficeVisits ov, CPTCodes cpt "
					+ "Where ovp.VisitID=ov.ID and cpt.code=ovp.cptcode and ov.patientID=? "
					+ "ORDER BY ov.visitDate desc");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return procedureLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of all immunization procedures for a given patient
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of the procedures.
	 * @throws DBException
	 */
	public List<ProcedureBean> getImmunizationProcedures(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (pid == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OVProcedure ovp, OfficeVisits ov, CPTCodes cpt "
					+ "Where ovp.VisitID=ov.ID and cpt.code=ovp.cptcode and ov.patientID=? and cpt.attribute='immunization'"
					+ "ORDER BY ov.visitDate desc");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return procedureLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	
	/**
	 * Return a list of all prescriptions for a patient
	 * 
	 * @param patientID The MID of the patient in question.
	 * @return A java.util.List of prescriptions.
	 * @throws DBException
	 */
	public List<PrescriptionBean> getPrescriptions(long patientID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (patientID == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OVMedication,NDCodes,OfficeVisits "
					+ "Where OfficeVisits.PatientID = ? AND OVMedication.VisitID = "
					+ "OfficeVisits.ID AND NDCodes.Code=OVMedication.NDCode "
					+ "ORDER BY OfficeVisits.visitDate DESC, OVMedication.NDCode ASC;");
			ps.setLong(1, patientID);
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
	 * Return a list of prescriptions which are currently prescribed for a patient
	 * 
	 * @param patientID The MID of the patient in question.
	 * @return A java.util.List of prescription beans.
	 * @throws DBException
	 */
	public List<PrescriptionBean> getCurrentPrescriptions(long patientID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (patientID == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			
			ps = conn.prepareStatement("Select * From OVMedication,NDCodes,OfficeVisits "
					+ "Where OfficeVisits.PatientID = ? AND OVMedication.VisitID = "
					+ "OfficeVisits.ID AND NDCodes.Code=OVMedication.NDCode AND "
					+ "OVMedication.EndDate >= ?" + "ORDER BY OVMedication.ID DESC;");
			ps.setLong(1, patientID);
			ps.setDate(2, DateUtil.getSQLdateXDaysAgoFromNow(91));
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
	 * Return a list of prescriptions which are expired prescription for a patient
	 * 
	 * @param patientID The MID of the patient in question.
	 * @return A java.util.List of prescriptions.
	 * @throws DBException
	 **/
	 
	public List<PrescriptionBean> getExpiredPrescriptions (long patientID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (patientID == 0L) throw new SQLException("pid cannot be 0");
			conn = factory.getConnection();
			ps = conn.prepareStatement("Select * From OVMedication,NDCodes,OfficeVisits "
					+ "Where OfficeVisits.PatientID = ? AND OVMedication.VisitID = "
					+ "OfficeVisits.ID AND NDCodes.Code=OVMedication.NDCode AND "
					+ "OVMedication.EndDate < ?" + "ORDER BY OVMedication.ID DESC;");
			ps.setLong(1, patientID);
			ps.setDate(2, DateUtil.getSQLdateXDaysAgoFromNow(0));
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
	 * Lists every patient in the database.
	 * 
	 * @return A java.util.List of PatientBeans representing the patients.
	 * @throws DBException
	 */
	public List<PatientBean> getAllPatients() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM patients ");
			ResultSet rs = ps.executeQuery();
			return patientLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Return a list of patients with a special-diagnosis-history who
	 * have the logged in HCP as a DHCP and whose medications are going to
	 * expire within seven days.
	 * 
	 * @param hcpMID The MID of the logged in HCP
	 * @return A list of patients satisfying the conditions.
	 * @throws DBException
	 */
	public List<PatientBean> getRenewalNeedsPatients(long hcpMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			
				
				ps = conn.prepareStatement("SELECT * FROM ( " + 

				"SELECT DISTINCT patients.* From patients, declaredhcp, ovdiagnosis, officevisits, ovmedication " + 
				"Where " + 
				
				"declaredHCP.HCPID = ? AND " + 
				"patients.MID = declaredhcp.PatientID AND " + 
				
				
				"( " + 
				"ovdiagnosis.VisitID = officevisits.ID AND officevisits.PatientID = declaredhcp.PatientID " + 
				"AND " + 
				
				"((ovdiagnosis.ICDCode >= ? AND ovdiagnosis.ICDCode < ?) " + 
				"OR (ovdiagnosis.ICDCode >= ? AND ovdiagnosis.ICDCode < ?) " + 
				"OR (ovdiagnosis.ICDCode >= ? AND ovdiagnosis.ICDCode < ?)) " + 
				") " + 
				
				
				
				"UNION ALL " + 
				
				
				"SELECT DISTINCT patients.* From patients, declaredhcp, ovdiagnosis, officevisits, ovmedication " + 
				"Where " + 
				
				"declaredHCP.HCPID = ? AND " + 
				"patients.MID = declaredhcp.PatientID AND " + 
				
				"( " + 
				"declaredhcp.PatientID = officevisits.PatientID AND officevisits.ID = ovmedication.VisitID " + 
				"AND " + 
				"ovmedication.EndDate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) " + 
				") " + 
				
				") AS final " + 
				
				"GROUP BY final.MID HAVING COUNT(*) = 2 " + 
				
				"ORDER BY final.lastname ASC, final.firstname ASC"); 
			
			ps.setLong(1, hcpMID);
			
			ps.setFloat(2, 250.0f);
			ps.setFloat(3, 251.0f);
				
			ps.setFloat(4, 493.0f);
			ps.setFloat(5, 494.0f);
			
			ps.setFloat(6, 390.0f);
			ps.setFloat(7, 460.99f);

			ps.setLong(8, hcpMID);
			
			ResultSet rs = ps.executeQuery();
			return patientLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Returns all patients with names "LIKE" (as in SQL) the passed in parameters.
	 * 
	 * @param first The patient's first name.
	 * @param last The patient's last name.
	 * @return A java.util.List of PatientBeans.
	 * @throws DBException
	 */
	public List<PatientBean> searchForPatientsWithName(String first, String last) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		if (first.equals("%") && last.equals("%")) return new Vector<PatientBean>();
		
		try {
			conn = factory.getConnection();
			
			ps = conn.prepareStatement("SELECT * FROM patients WHERE firstName LIKE ? AND lastName LIKE ?");
			ps.setString(1, first);
			ps.setString(2, last);
			ResultSet rs = ps.executeQuery();
			return patientLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	
}
