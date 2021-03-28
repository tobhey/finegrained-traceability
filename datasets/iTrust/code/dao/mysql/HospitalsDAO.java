package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.HospitalBean;
import edu.ncsu.csc.itrust.beans.loaders.HospitalBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for managing hospitals
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
public class HospitalsDAO {
	private DAOFactory factory;
	private HospitalBeanLoader hospitalLoader = new HospitalBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public HospitalsDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all hospitals sorted alphabetically
	 * 
	 * @return A java.util.List of HospitalBeans.
	 * @throws DBException
	 */
	public List<HospitalBean> getAllHospitals() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Hospitals ORDER BY HospitalName");
			ResultSet rs = ps.executeQuery();
			return hospitalLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a particular hospital given its ID
	 * 
	 * @param id The String ID of the hospital.
	 * @return A HospitalBean representing this hospital.
	 * @throws DBException
	 */
	public HospitalBean getHospital(String id) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Hospitals WHERE HospitalID = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return hospitalLoader.loadSingle(rs);
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds a hospital
	 * 
	 * @param hosp The HospitalBean object to insert.
	 * @return A boolean indicating whether the insertion was successful.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean addHospital(HospitalBean hosp) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO Hospitals (HospitalID, HospitalName) " + "VALUES (?,?)");
			ps.setString(1, hosp.getHospitalID());
			ps.setString(2, hosp.getHospitalName());
			return (1 == ps.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
			if (1062 == e.getErrorCode())
				throw new iTrustException("Error: Hospital already exists.");
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Updates a particular hospital's description. Returns the number of rows affected (should be 1)
	 * 
	 * @param hosp The HospitalBean to update.
	 * @return An int indicating the number of affected rows.
	 * @throws DBException
	 */
	public int updateHospital(HospitalBean hosp) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE Hospitals SET HospitalName = ? " + "WHERE HospitalID = ?");
			ps.setString(1, hosp.getHospitalName());
			ps.setString(2, hosp.getHospitalID());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Assign an HCP to a hospital. If they have already been assigned to that hospital, then an
	 * iTrustException is thrown.
	 * 
	 * @param hcpID The HCP's MID to assign to the hospital.
	 * @param hospitalID The ID of the hospital to assign them to.
	 * @return A boolean indicating whether the assignment was a success.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean assignHospital(long hcpID, String hospitalID) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO HCPAssignedHos (HCPID, HosID) VALUES (?,?)");
			ps.setLong(1, hcpID);
			ps.setString(2, hospitalID);
			return (1 == ps.executeUpdate());
		} catch (SQLException e) {
			if (1062 == e.getErrorCode())
				throw new iTrustException("HCP " + hcpID + " already assigned to hospital " + hospitalID);
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Unassigns an HCP to a hospital. Returns whether or not any changes were made
	 * 
	 * @param hcpID The MID of the HCP to remove.
	 * @param hospitalID The ID of the hospital being removed from.
	 * @return A boolean indicating success.
	 * @throws DBException
	 */
	public boolean removeHospitalAssignment(long hcpID, String hospitalID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM HCPAssignedHos WHERE HCPID = ? AND HosID = ?");
			ps.setLong(1, hcpID);
			ps.setString(2, hospitalID);
			return (1 == ps.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Removes all hospital assignments for a particular HCP. Returns the number of rows affected.
	 * 
	 * @param hcpID The MID of the HCP.
	 * @return An int representing the number of hospital assignments removed.
	 * @throws DBException
	 */
	public int removeAllHospitalAssignmentsFrom(long hcpID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM HCPAssignedHos WHERE HCPID = ?");
			ps.setLong(1, hcpID);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
