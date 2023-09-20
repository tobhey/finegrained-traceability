package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.OperationalProfile;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.beans.loaders.OperationalProfileLoader;
import edu.ncsu.csc.itrust.beans.loaders.TransactionBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * Used for the logging mechanism.
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
public class TransactionDAO {
	private DAOFactory factory;
	private TransactionBeanLoader loader = new TransactionBeanLoader();
	private OperationalProfileLoader operationalProfileLoader = new OperationalProfileLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public TransactionDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the whole transaction log
	 * 
	 * @return
	 * @throws DBException
	 */
	public List<TransactionBean> getAllTransactions() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM TransactionLog ORDER BY timeLogged DESC");
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
	 * Log a transaction, just giving it the person who is logged in and the type
	 * 
	 * @param type The {@link TransactionType} enum representing the type this transaction is.
	 * @param loggedInMID The MID of the user who is logged in.
	 * @throws DBException
	 */
	public void logTransaction(TransactionType type, long loggedInMID) throws DBException {
		logTransaction(type, loggedInMID, 0L, "");
	}

	/**
	 * Log a transaction, with all of the info. The meaning of secondaryMID and addedInfo changes depending on
	 * the transaction type.
	 * 
	 * @param type The {@link TransactionType} enum representing the type this transaction is.
	 * @param loggedInMID The MID of the user who is logged in.
	 * @param secondaryMID Typically, the MID of the user who is being acted upon.
	 * @param addedInfo A note about a subtransaction, or specifics of this transaction (for posterity).
	 * @throws DBException
	 */
	public void logTransaction(TransactionType type, long loggedInMID, long secondaryMID, String addedInfo)
			throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO TransactionLog(loggedInMID, secondaryMID, "
					+ "transactionCode, addedInfo) VALUES(?,?,?,?)");
			ps.setLong(1, loggedInMID);
			ps.setLong(2, secondaryMID);
			ps.setInt(3, type.getCode());
			ps.setString(4, addedInfo);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of all transactions in which an HCP accessed the given patient's record
	 * 
	 * @param patientID The MID of the patient in question.
	 * @return A java.util.List of transactions.
	 * @throws DBException
	 */
	public List<TransactionBean> getAllRecordAccesses(long patientID, boolean getByRole) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM TransactionLog WHERE secondaryMID=? AND transactionCode "
							+ "IN(" + TransactionType.patientViewableStr + ") ORDER BY timeLogged DESC");
			ps.setLong(1, patientID);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> tbList = loader.loadList(rs);

			tbList = addAndSortRoles(tbList, patientID, getByRole);
			
			return tbList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * The Most Thorough Fetch 
	 * @param loggedInRole Role of loggedIn
	 * @param secondaryRole Role of secondary
	 * @param begin Beginning of date range
	 * @param end End of date range
	 * @param type Type of transaction
	 * @return
	 * @throws DBException
	 */
	public List<TransactionBean> getTransactionsFor(String loggedInRole, String secondaryRole, Date begin, Date end, TransactionType type) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			
			
			
			if(loggedInRole.compareTo("any") == 0) {
				loggedInRole = "*";
			}
			if(secondaryRole.compareTo("any") == 0) {
				secondaryRole = "*";
			}
			
			ps = conn
					.prepareStatement("SELECT * FROM TransactionLog, Users  WHERE (MID=loggedInMID OR MID=secondaryMID) AND (Role=? OR Role=?) "
							+ " AND transactionCode=? AND timeLogged >= ? AND timeLogged <= ?" + " ORDER BY timeLogged DESC");
			ps.setString(1, loggedInRole);
			ps.setString(2, secondaryRole);
			ps.setInt(3, type.getCode());
			ps.setDate(4, begin);
			ps.setDate(5, end);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> tbList = loader.loadList(rs);

			//tbList = addAndSortRoles(tbList, patientID, getByRole);
			
			return tbList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return a list of all transactions in which an HCP accessed the given patient's record, within the dates
	 * 
	 * @param patientID The MID of the patient in question.
	 * @param lower The starting date as a java.util.Date
	 * @param upper The ending date as a java.util.Date
	 * @return A java.util.List of transactions.
	 * @throws DBException
	 */
	public List<TransactionBean> getRecordAccesses(long patientID, java.util.Date lower, java.util.Date upper, boolean getByRole) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("SELECT * FROM TransactionLog WHERE secondaryMID=? AND transactionCode IN ("
							+ TransactionType.patientViewableStr
							+ ") "
							+ "AND timeLogged >= ? AND timeLogged <= ? ORDER BY timeLogged DESC");
			ps.setLong(1, patientID);
			ps.setTimestamp(2, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(3, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> tbList = loader.loadList(rs);

			tbList = addAndSortRoles(tbList, patientID, getByRole);
			
			return tbList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns the operation profile
	 * 
	 * @return The OperationalProfile as a bean.
	 * @throws DBException
	 */
	public OperationalProfile getOperationalProfile() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT TransactionCode, count(transactionID) as TotalCount, "
					+ "count(if(loggedInMID<9000000000, transactionID, null)) as PatientCount, "
					+ "count(if(loggedInMID>=9000000000, transactionID, null)) as PersonnelCount "
					+ "FROM TransactionLog GROUP BY transactionCode ORDER BY transactionCode ASC");
			ResultSet rs = ps.executeQuery();
			return operationalProfileLoader.loadSingle(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * 
	 * @param tbList
	 * @param patientID
	 * @param sortByRole
	 * @return
	 * @throws DBException
	 */
	private List<TransactionBean> addAndSortRoles(List<TransactionBean> tbList, long patientID, boolean sortByRole) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = factory.getConnection();
			
			for(TransactionBean t : tbList) {
				
				ps = conn
						.prepareStatement("SELECT Role FROM Users WHERE MID=?");
				ps.setLong(1, t.getLoggedInMID());
				ResultSet rs = ps.executeQuery();
				String role = "";
				if(rs.next())
					role = rs.getString("Role");
				if(role.equals("er"))
					role = "Emergency Responder";
				else if(role.equals("uap"))
					role = "UAP";
				else if(role.equals("hcp")) {
					role = "LHCP";
					ps = conn
							.prepareStatement("SELECT PatientID FROM DeclaredHCP WHERE HCPID=?");
					ps.setLong(1, t.getLoggedInMID());
					ResultSet rs2 = ps.executeQuery();
					while(rs2.next()) {
						if (rs2.getLong("PatientID") == patientID){
							role = "DLHCP";
							break;
						}
					}
				}
				else if(role.equals("patient")){
					role = "Patient";
					ps = conn
							.prepareStatement("SELECT representeeMID FROM Representatives WHERE representerMID=?");
					ps.setLong(1, t.getLoggedInMID());
					ResultSet rs2 = ps.executeQuery();
					while(rs2.next()) {
						if (rs2.getLong("representeeMID") == patientID){
							role = "Personal Health Representative";
							break;
						}
					}
				}
					
				t.setRole(role);
				
			}
			
			if(sortByRole){
				TransactionBean[] array = new TransactionBean[tbList.size()];
				array[0] = tbList.get(0);
				TransactionBean t;
				for(int i = 1; i < tbList.size(); i++) {
					t = tbList.get(i);
					String role = t.getRole();
					int j = 0;
					while(array[j] != null && role.compareToIgnoreCase(array[j].getRole()) >= 0)
						j++;
					for(int k = i; k > j; k--) {
						array[k] = array[k-1];
					}
					array[j] = t;
				}
				int size = tbList.size();
				for(int i = 0; i < size; i++)
					tbList.set(i, array[i]);
				
			}
		
			return tbList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	
}
