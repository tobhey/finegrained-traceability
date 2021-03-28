package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.FamilyMemberBean;
import edu.ncsu.csc.itrust.beans.loaders.FamilyBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for queries related to families.
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
public class FamilyDAO {
	private DAOFactory factory;
	private FamilyBeanLoader familyBeanLoader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public FamilyDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Return the information of the mother and father of this patient
	 * 
	 * @param pid -
	 *            this patient
	 * @return
	 * @throws DBException
	 */
	public List<FamilyMemberBean> getParents(long pid) throws DBException {
		return getFamilyMembers(pid, "Parent",
				"SELECT p2.FirstName AS FirstName, p2.LastName AS LastName, p2.MID as MID "
						+ "FROM Patients p1, Patients p2 "
						+ "WHERE p1.MID=? AND (p1.MotherMID=p2.MID OR p1.FatherMID=p2.MID)", false);
	}

	/**
	 * Return a list of patients who share at least one parent (and that parent is not '0') with this patient
	 * 
	 * @param pid -
	 *            this patient
	 * @return A java.util.list of FamilyMemberBeans.
	 * @throws DBException
	 */
	public List<FamilyMemberBean> getSiblings(long pid) throws DBException {
		return getFamilyMembers(pid, "Sibling",
				"SELECT p2.FirstName AS FirstName, p2.LastName AS LastName, p2.MID as MID "
						+ "FROM Patients p1, Patients p2 " + "WHERE p1.MID=? AND p1.MID<>p2.MID "
						+ "AND( (p1.MotherMID=p2.MotherMID AND p2.MotherMID<>0)"
						+ "  OR (p1.FatherMID=p2.FatherMID AND p1.FatherMID<>0))", false);
	}

	/**
	 * Return a list of patients whose mother or father is this patient
	 * 
	 * @param pid -
	 *            this patient
	 * @return A java.util.List of FamilyMemberBeans.
	 * @throws DBException
	 */
	public List<FamilyMemberBean> getChildren(long pid) throws DBException {
		return getFamilyMembers(pid, "Child", "SELECT FirstName, LastName, MID FROM Patients "
				+ "WHERE MotherMID=? or FatherMID=?", true);
	}

	/**
	 * Private helper method (since all three are alike)
	 * 
	 * @param pid
	 * @param relation
	 * @param query
	 * @param secondParam -
	 *            add the pid as the second parameter (the 3rd query was a little different)
	 * 
	 * @return A java.util.List of FamilyMemberBeans.
	 * @throws DBException
	 */
	private List<FamilyMemberBean> getFamilyMembers(long pid, String relation, String query,
			boolean secondParam) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		familyBeanLoader = new FamilyBeanLoader(relation);
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement(query);
			ps.setLong(1, pid);
			if (secondParam)
				ps.setLong(2, pid);
			ResultSet rs = ps.executeQuery();
			return familyBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
