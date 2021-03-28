package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.MedicationBean;
import edu.ncsu.csc.itrust.beans.loaders.MedicationBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for managing the ND Codes.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * The National Drug Code (NDC) is a universal product identifier used in the
 * United States for drugs intended for human use.
 * 
 * @see http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm
 * @author Andy
 * 
 */
public class NDCodesDAO {
	private DAOFactory factory;
	private MedicationBeanLoader medicationLoader = new MedicationBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public NDCodesDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all ND codes
	 * 
	 * @return A java.util.List of MedicationBeans.
	 * @throws DBException
	 */
	public List<MedicationBean> getAllNDCodes() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM NDCodes ORDER BY CODE");
			ResultSet rs = ps.executeQuery();
			return medicationLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a particular description for a given code.
	 * 
	 * @param code The ND code to be looked up.
	 * @return A bean representing the Medication that was looked up.
	 * @throws DBException
	 */
	public MedicationBean getNDCode(String code) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM NDCodes WHERE Code = ?");
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return medicationLoader.loadSingle(rs);
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds a new ND code, returns whether or not the change was made. If the code already exists, an
	 * iTrustException is thrown.
	 * 
	 * @param med The medication bean to be added.
	 * @return A boolean indicating success or failure.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean addNDCode(MedicationBean med) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO NDCodes (Code, Description) " + "VALUES (?,?)");
			ps.setString(1, med.getNDCode());
			ps.setString(2, med.getDescription());
			return (1 == ps.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
			if (1062 == e.getErrorCode())
				throw new iTrustException("Error: Code already exists.");
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Updates a particular code's description
	 * 
	 * @param med A bean representing the particular medication to be updated.
	 * @return An int representing the number of updated rows.
	 * @throws DBException
	 */
	public int updateCode(MedicationBean med) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE NDCodes SET Description = ? " + "WHERE Code = ?");
			ps.setString(1, med.getDescription());
			ps.setString(2, med.getNDCode());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
