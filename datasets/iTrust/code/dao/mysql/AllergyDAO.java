package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.AllergyBean;
import edu.ncsu.csc.itrust.beans.loaders.AllergyBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
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
public class AllergyDAO {
	private DAOFactory factory;
	private AllergyBeanLoader allergyBeanLoader = new AllergyBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public AllergyDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of patient's allergies.
	 * @param pid A long for the MID of the patient we are looking up.
	 * @return A java.util.List of AllergyBeans associated with this patient.
	 * @throws DBException
	 */
	public List<AllergyBean> getAllergies(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Allergies WHERE PatientID=? ORDER BY FirstFound DESC");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return allergyBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Adds an allergy to this patient's list.
	 * @param pid The MID of the patient whose allergy we are adding.
	 * @param description The name of the allergen.
	 * @throws DBException
	 */
	public void addAllergy(long pid, String description) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO Allergies(PatientID, Description) VALUES (?,?)");
			ps.setLong(1, pid);
			ps.setString(2, description);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
