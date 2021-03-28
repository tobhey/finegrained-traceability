package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ReferralBean;
import edu.ncsu.csc.itrust.beans.loaders.ReferralBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used to update referrals, and fetch lists of referrals sent to and
 * from HCPs.
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
public class ReferralDAO {
	private DAOFactory factory;
	private ReferralBeanLoader referralLoader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public ReferralDAO(DAOFactory factory) {
		this.factory = factory;
		referralLoader = new ReferralBeanLoader();
	}

	

	/**
	 * Gets a list of all referrals sent from an HCP
	 * @param mid The HCP's mid.
	 * @return The list of the referrals they sent.
	 * @throws DBException
	 */
	public List<ReferralBean> getReferralsSentFrom(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM referrals WHERE SenderID = ?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			
			
			return referralLoader.loadList(rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	
	/**
	 * Gets a list of all referrals sent to an HCP
	 * @param mid The HCP's mid.
	 * @return The list of the referrals sent to them.
	 * @throws DBException
	 */
	public List<ReferralBean> getReferralsSentTo(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM referrals WHERE ReceiverID = ?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			
			
			return referralLoader.loadList(rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}


	/**
	 * Updates a given referral in the database.
	 * @param r The referral to update.
	 * @throws DBException
	 */
	public void editReferral(ReferralBean r) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE referrals SET PatientID=?,SenderID=?,ReceiverID=?,"
					+ "ReferralDetails=?,ConsultationDetails=?,Status=?  WHERE ID=?");
			referralLoader.loadParameters(ps, r);
			ps.setLong(7, r.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Adds a given referral to the database.
	 * @param r The referral to add.
	 * @throws DBException
	 */
	public void addReferral(ReferralBean r) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO referrals (PatientID,SenderID,ReceiverID,"
					+ "ReferralDetails,ConsultationDetails,Status)  "
					+ "VALUES (?,?,?,?,?,?)");
			referralLoader.loadParameters(ps, r);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
}
