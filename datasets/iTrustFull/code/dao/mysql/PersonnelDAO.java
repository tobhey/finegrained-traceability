package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.HospitalBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.loaders.HospitalBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.enums.Role;


/**
 * Used for managing information related to personnel: HCPs, UAPs, Admins
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
public class PersonnelDAO {
	private DAOFactory factory;
	private PersonnelLoader personnelLoader;
	private HospitalBeanLoader hospitalBeanLoader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public PersonnelDAO(DAOFactory factory) {
		this.factory = factory;
		personnelLoader = new PersonnelLoader();
		hospitalBeanLoader = new HospitalBeanLoader();
	}

	/**
	 * Returns the name for a given MID
	 * 
	 * @param mid The MID of the personnel in question.
	 * @return A String representing the name of the personnel.
	 * @throws iTrustException
	 * @throws DBException
	 */
	public String getName(long mid) throws iTrustException, DBException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement("SELECT firstName, lastName FROM Personnel WHERE MID=?");
			pstmt.setLong(1, mid);
			ResultSet rs;

			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("firstName") + " " + rs.getString("lastName");
			} else {
				throw new iTrustException("User does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstmt);
		}
	}

	/**
	 * Adds an empty personnel, and returns the MID.
	 * 
	 * @return A long indicating the new MID.
	 * @param role A {@link Role} enum indicating the personnel's specific role.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public long addEmptyPersonnel(Role role) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		long newID;

		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO Personnel(Role) VALUES(?)");
			ps.setString(1, role.name());
			ps.executeUpdate();
			newID = DBUtil.getLastInsert(conn);
			return newID;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Retrieves a PersonnelBean with all of the specific information for a given employee.
	 * 
	 * @param mid The MID of the personnel in question.
	 * @return A PersonnelBean representing the employee.
	 * @throws DBException
	 */
	public PersonnelBean getPersonnel(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Personnel WHERE MID = ?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return personnelLoader.loadSingle(rs);
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
	 * Updates the demographics for a personnel.
	 * 
	 * @param p The personnel bean with the updated information.
	 * @throws DBException
	 */
	public void editPersonnel(PersonnelBean p) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE Personnel SET AMID=?,firstName=?,lastName=?,"
					+ "phone1=?,phone2=?,phone3=?, address1=?,address2=?,city=?, state=?, zip=?, zip1=?, zip2=?, email=?, MessageFilter=?"
					+ " WHERE MID=?");
			personnelLoader.loadParameters(ps, p);
			ps.setLong(16, p.getMID());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Indicates whether a certain personnel is in the database.
	 * 
	 * @param pid The MID of the personnel in question.
	 * @return A boolean indicating whether this personnel exists.
	 * @throws DBException
	 */
	public boolean checkPersonnelExists(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Personnel WHERE MID=?");
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
	 * Returns all of the hospitals this LHCP is associated with.
	 * 
	 * @param mid The MID of the personnel in question.
	 * @return A java.util.List of HospitalBeans.
	 * @throws DBException
	 */
	public List<HospitalBean> getHospitals(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM HCPAssignedHos hah,Hospitals h "
					+ "WHERE hah.HCPID=? AND hah.HosID=h.HospitalID ORDER BY HospitalName ASC");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			return hospitalBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	
	
	/**
	 * Returns all personnel in the database.
	 * 
	 * @return A java.util.List of personnel.
	 * @throws DBException
	 */
	public List<PersonnelBean> getAllPersonnel() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM personnel where role in ('hcp','uap','er') ");
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
	 * Returns a list of UAPs who work for this LHCP.
	 * 
	 * @param hcpid The MID of the personnel in question.
	 * @return A java.util.List of UAPs.
	 * @throws DBException
	 */
	public List<PersonnelBean> getUAPsForHCP(long hcpid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Personnel WHERE MID IN (SELECT UAP FROM HCPRelations WHERE HCP=?)");
			ps.setLong(1, hcpid);
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
	 * Given a prescription that has been given, this method returns all the information for the
	 * doctor who authorized that prescription.
	 * 
	 * @param prescription The PrescriptionBean describing the prescription in question.
	 * @return The PersonnelBean describing the doctor who authorized it.
	 * @throws DBException
	 */
	public PersonnelBean getPrescribingDoctor(PrescriptionBean prescription) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Personnel WHERE MID IN (SELECT HCPID FROM OfficeVisits WHERE ID=?)");
			ps.setLong(1, prescription.getVisitID());
			ResultSet rs = ps.executeQuery();
			return personnelLoader.loadList(rs).get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Matches all personnel who have names LIKE (as in SQL) the first and last names passed in.
	 * 
	 * @param first The first name to be searched for.
	 * @param last The last name to be searched for.
	 * @return A java.util.List of personnel who match these names.
	 * @throws DBException
	 */
	public List<PersonnelBean> searchForPersonnelWithName(String first, String last) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		if (first.equals("%") && last.equals("%")) return new Vector<PersonnelBean>();
		
		try {
			conn = factory.getConnection();
			
			ps = conn.prepareStatement("SELECT * FROM personnel WHERE firstName LIKE ? AND lastName LIKE ?");
			ps.setString(1, first);
			ps.setString(2, last);
			ResultSet rs = ps.executeQuery();
			return personnelLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
