package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ReportRequestBean;
import edu.ncsu.csc.itrust.beans.loaders.ReportRequestBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for handling data related to report requests.
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
public class ReportRequestDAO {
	private DAOFactory factory;
	private ReportRequestBeanLoader loader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public ReportRequestDAO(DAOFactory factory) {
		this.factory = factory;
		loader = new ReportRequestBeanLoader();
	}
	
	/**
	 * Returns a full bean describing a given report request.
	 * 
	 * @param id The unique ID of the bean in the database.
	 * @return The bean describing this report request.
	 * @throws DBException
	 */
	public ReportRequestBean getReportRequest(long id) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (id == 0L) throw new SQLException("ID cannot be null");
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ReportRequests WHERE ID = ?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return loader.loadSingle(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Returns all report requests associated with a given requester.
	 * 
	 * @param mid The MID of the personnel in question.
	 * @return A java.util.List of report requests.
	 * @throws DBException
	 */
	public List<ReportRequestBean> getAllReportRequestsForRequester(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (mid == 0L) throw new SQLException("RequesterMID cannot be null");
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ReportRequests WHERE RequesterMID = ?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			return loader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns all of the report requests associated with a specific patient.
	 * 
	 * @param pid The MID of the patient in question.
	 * @return A java.util.List of report requests.
	 * @throws DBException
	 */
	public List<ReportRequestBean> getAllReportRequestsForPatient(long pid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (pid == 0L) throw new SQLException("PatientMID cannot be null");
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ReportRequests WHERE PatientMID = ?");
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			return loader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
//	/**
//	 * Returns every report request in the database.
//	 * @return A java.util.List of report requests.
//	 * @throws DBException
//	 */
//	public List<ReportRequestBean> getAllReportRequests() throws DBException {
//		Connection conn = null;
//		PreparedStatement ps = null;
//
//		try {
//			conn = factory.getConnection();
//			ps = conn.prepareStatement("SELECT * FROM ReportRequests");
//			ResultSet rs = ps.executeQuery();
//			return loader.loadList(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DBException(e);
//		} finally {
//			DBUtil.closeConnection(conn, ps);
//		}
//	}
	
	/**
	 * Adds a request for a report.
	 * 
	 * @param requesterMID The MID of the requester.
	 * @param patientMID The MID of the patient in question.
	 * @param date The date the request was made.
	 * @return A long of the unique ID of the report request.
	 * @throws DBException
	 */
	public long addReportRequest(long requesterMID, long patientMID, Date date) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (requesterMID == 0L) throw new SQLException("RequesterMID cannot be null");
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO ReportRequests (ID, RequesterMID, PatientMID, RequestedDate, Status) VALUES (null,?,?,?,'Requested')");
			ps.setLong(1, requesterMID);
			ps.setLong(2, patientMID);
			ps.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
//	/**
//	 * Sets the status of a request to 'Approved'
//	 * 
//	 * @param ID The unique ID of the report.
//	 * @param approverMID The MID of the person approving the request.
//	 * @param date The date the approval was made.
//	 * @throws DBException
//	 */
//	public void approveReportRequest(long ID, long approverMID, Date date) throws DBException {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		try {
//			if (ID == 0L) throw new SQLException("ID cannot be null");
//			conn = factory.getConnection();
//			ps = conn.prepareStatement("UPDATE ReportRequests set ApproverMID = ?, ApprovedDate = ?, Status = 'Approved' where ID = ?");
//			ps.setLong(1, approverMID);
//			ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
//			ps.setLong(3, ID);
//			ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DBException(e);
//		} finally {
//			DBUtil.closeConnection(conn, ps);
//		}
//	}
//
//	/**
//	 * Sets the status of a report request to 'Rejected'
//	 * 
//	 * @param ID The unique ID of the request in question.
//	 * @param approverMID The MID of the rejecter in question.
//	 * @param date The date the rejection was made.
//	 * @param comment A comment describing why the request was rejected.
//	 * @throws DBException
//	 */
//	public void rejectReportRequest(long ID, long approverMID, Date date, String comment) throws DBException {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		try {
//			if (ID == 0L) throw new SQLException("ID cannot be null");
//			conn = factory.getConnection();
//			ps = conn.prepareStatement("UPDATE ReportRequests set ApproverMID = ?, ApprovedDate = ?, Status = 'Rejected', comment = ? where ID = ?");
//			ps.setLong(1, approverMID);
//			ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
//			ps.setString(3, comment);
//			ps.setLong(4, ID);
//			ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new DBException(e);
//		} finally {
//			DBUtil.closeConnection(conn, ps);
//		}
//	}
	
	/**
	 * Sets the status of a report request to 'Viewed'
	 * 
	 * @param ID The unique ID of the request in question.
	 * @param date The date the request was viewed.
	 * @throws DBException
	 */
	public void setViewed(long ID, Date date) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if (ID == 0L) throw new SQLException("ID cannot be null");
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE ReportRequests set ViewedDate = ?, Status = 'Viewed' where ID = ?");
			ps.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
			ps.setLong(2, ID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		
	}

}
