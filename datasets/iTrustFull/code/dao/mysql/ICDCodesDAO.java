package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.loaders.DiagnosisBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for managing all ICD codes.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * The International Statistical Classification of Diseases and Related Health Problems 
 * (most commonly known by the abbreviation ICD) provides codes to classify diseases and a 
 * wide variety of signs, symptoms, abnormal findings, complaints, social circumstances and 
 * external causes of injury or disease. 
 * 
 * @see http://www.cdc.gov/nchs/icd9.htm
 * @author Andy
 * 
 */
public class ICDCodesDAO {
	private DAOFactory factory;
	private DiagnosisBeanLoader diagnosisLoader = new DiagnosisBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public ICDCodesDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns all ICD9CM codes sorted by code
	 * 
	 * @return java.util.List of DiagnosisBeans
	 * @throws DBException
	 */
	public List<DiagnosisBean> getAllICDCodes() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ICDCodes ORDER BY CODE");
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
	 * Returns a particular description for a given code
	 * 
	 * @param code The String representation of the code.
	 * @return A DiagnosisBean of the code.
	 * @throws DBException
	 */
	public DiagnosisBean getICDCode(String code) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ICDCodes WHERE Code = ?");
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return diagnosisLoader.loadSingle(rs);
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds an ICD9CM code. Returns whether or not the change was made.
	 * 
	 * @param diag The DiagnosisBean representing the changes.
	 * @return A boolean indicating success.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean addICDCode(DiagnosisBean diag) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO ICDCodes (Code, Description, Chronic) " + "VALUES (?,?,?)");
			ps.setString(1, diag.getICDCode());
			ps.setString(2, diag.getDescription());
			ps.setString(3, diag.getClassification());
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
	 * Changes a the description of a particular code.
	 * 
	 * @param diag A DiagnosisBean representing the changes.
	 * @return A boolean indicating the number of updated rows.
	 * @throws DBException
	 */
	public int updateCode(DiagnosisBean diag) throws DBException {
		int rows;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE ICDCodes SET Description = ?, Chronic = ? WHERE Code = ?");
			ps.setString(1, diag.getDescription());
			ps.setString(2, diag.getClassification());
			ps.setString(3, diag.getICDCode());
			rows = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		return rows;
	}

}
