package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.loaders.HealthRecordsBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for all health records where a whole history is kept.
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
public class HealthRecordsDAO {
	private HealthRecordsBeanLoader loader = new HealthRecordsBeanLoader();
	private DAOFactory factory;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public HealthRecordsDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all health records for a particular patient
	 * 
	 * @param mid The MID of the patient to look up.
	 * @return A java.util.List of HealthRecords.
	 * @throws DBException
	 */
	public List<HealthRecord> getAllHealthRecords(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM PersonalHealthInformation "
					+ "WHERE PatientID=? ORDER BY ASOFDATE DESC");
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			return loader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds a health record for a particular patient
	 * 
	 * @param record The HealthRecord object to insert.
	 * @return A boolean indicating whether the insert was successful.
	 * @throws DBException
	 */
	public boolean add(HealthRecord record) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO PersonalHealthInformation(PatientID,Height,Weight,"
					+ "Smoker,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,"
					+ "HCPID) VALUES(?,?,?,?,?,?,?,?,?,?)");
			loader.loadParameters(ps, record);
			int numInserted = ps.executeUpdate();
			return (numInserted == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
