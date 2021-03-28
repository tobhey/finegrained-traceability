package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.RandomPassword;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * AuthDAO is for anything that has to do with authentication. Most methods access the users table.
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
public class AuthDAO {
	public static final long LOGIN_TIMEOUT = 15 * 60 * 1000;// 15 min
	private DAOFactory factory;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public AuthDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Add a particular user to the system. Does not add user-specific information (e.g. Patient or HCP).
	 * Initially sets security question to a random set of characters, so that nobody should be able to guess
	 * its value.
	 * 
	 * @param mid The user's MID as a Long.
	 * @param role The role of the user as a Role enum {@link Role}
	 * @param password The password for the new user.
	 * @return A string representing the newly added randomly-generated password. 
	 * @throws DBException
	 */
	public String addUser(Long mid, Role role, String password) throws DBException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			pstmt = conn
					.prepareStatement("INSERT INTO Users (MID, PASSWORD, ROLE, sQuestion, sAnswer) VALUES (?,?,?,?,?)");
			pstmt.setLong(1, mid);
			pstmt.setString(2, password);
			pstmt.setString(3, role.toString());
			pstmt.setString(4, "Enter the random password given in your account email");
			String pwd = RandomPassword.getRandomPassword();
			pstmt.setString(5, pwd);
			pstmt.executeUpdate();
			return pwd;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstmt);
		}
	}

	/**
	 * Reset the security question and answer for a particular user
	 * 
	 * @param question The security question as a string.
	 * @param answer The security answer as a string.
	 * @param mid The MID of the user as a long.
	 * @throws DBException
	 */
	public void setSecurityQuestionAnswer(String question, String answer, long mid) throws DBException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement("UPDATE Users SET sQuestion = ?, sAnswer = ? WHERE MID = ?");
			pstmt.setString(1, question);
			pstmt.setString(2, answer);
			pstmt.setLong(3, mid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstmt);
		}
	}

	/**
	 * Returns the user name of a user from just the MID
	 * 
	 * @param mid The MID of the user to get the name of.
	 * @return The user's name as a String.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public String getUserName(long mid) throws DBException, iTrustException {
		Role role = getUserRole(mid);
		switch (role) {
			case HCP:
			case PHA:
			case ADMIN:
			case UAP:
			case ER:
				return factory.getPersonnelDAO().getName(mid);
			case PATIENT:
				return factory.getPatientDAO().getName(mid);
			case TESTER:
				return String.valueOf(mid);
			default:
				throw new iTrustException("Role " + role + " not supported");
		}
	}

	/**
	 * Returns the role of a particular MID
	 * 
	 * @param mid The MID of the user to look up.
	 * @return The {@link Role} of the user as an enum.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public Role getUserRole(long mid) throws DBException, iTrustException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = factory.getConnection();
			pstmt = conn.prepareStatement("SELECT role FROM Users WHERE MID=?");
			pstmt.setLong(1, mid);
			ResultSet rs;
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return Role.parse(rs.getString("role"));
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
	 * Change the password of a particular user
	 * 
	 * @param mid The MID of the user whose password we are changing.
	 * @param password The new password.
	 * @throws DBException
	 */
	public void resetPassword(long mid, String password) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("UPDATE Users SET password=? WHERE MID=?");
			ps.setString(1, password);
			ps.setLong(2, mid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return the security question for a particular user.
	 * 
	 * @param mid The MID of the user we are looking up.
	 * @return The security question of the user we are looking up.
	 * @throws iTrustException
	 */
	public String getSecurityQuestion(long mid) throws iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT sQuestion FROM Users WHERE MID=?");
			ps.setLong(1, mid);
			ResultSet r = ps.executeQuery();
			if (r.next())
				return r.getString("sQuestion");
			else
				throw new iTrustException("No security question set for MID: " + mid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return the security answer of a particular user
	 * 
	 * @param mid The MID of the user we are looking up.
	 * @return The security answer as a String.
	 * @throws iTrustException
	 */
	public String getSecurityAnswer(long mid) throws iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT sAnswer FROM Users WHERE MID=?");
			ps.setLong(1, mid);
			ResultSet r = ps.executeQuery();
			if (r.next())
				return r.getString("sAnswer");
			else
				throw new iTrustException("No security answer set for MID " + mid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Record a login failure, which blacklists the ipAddress. Uses the database table like a hash table where
	 * the key is the user's IP address. If the user's IP address is not in the table, a row with "1" is
	 * added.
	 * 
	 * @param ipAddr The IP address of the user as a String.
	 * @throws DBException
	 */
	public void recordLoginFailure(String ipAddr) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("UPDATE LoginFailures SET FailureCount=FailureCount+1, lastFailure=CURRENT_TIMESTAMP WHERE IPAddress=?");
					//.prepareStatement("INSERT INTO LoginFailures VALUES(?,?,?)");
			ps.setString(1, ipAddr);
			//ps.setInt(2, failures);
			//ps.setDate(3, Calendar.getInstance().getTime());
			int numUpdated = ps.executeUpdate();
			if (numUpdated == 0) // if there wasn't an empty row to begin with
				insertLoginFailureRow(ipAddr, 1, conn);// now they have a row AND a strike against
			// 'em
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Record a reset password failure, which blacklists the ipAddress. Uses the database table like a hash
	 * table where the key is the user's IP address. If the user's IP address is not in the table, a row with
	 * "1" is added.
	 * 
	 * @param ipAddr The IP address of the user as a String.
	 * @throws DBException
	 */
	public void recordResetPasswordFailure(String ipAddr) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("UPDATE ResetPasswordFailures SET failurecount=failurecount+1 WHERE ipaddress=?");
			ps.setString(1, ipAddr);
			int numUpdated = ps.executeUpdate();
			if (numUpdated == 0) // if there wasn't an empty row to begin with
				insertResetPasswordRow(ipAddr, 1, conn);// now they have a row AND a strike against
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return the number of failures from resetting a password, given an IP address.
	 * 
	 * @param ipAddr An IP address for the associated attempt as a String.
	 * @return An int representing the number of failures.
	 * @throws DBException
	 */
	public int getResetPasswordFailures(String ipAddr) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM ResetPasswordFailures WHERE IPADDRESS=?");
			ps.setString(1, ipAddr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// if we're more than X minutes out, clear the failure count
				if (System.currentTimeMillis() - rs.getTimestamp("lastFailure").getTime() > LOGIN_TIMEOUT) {
					updateResetFailuresToZero(ipAddr, conn);
					return 0;
				} else {
					return rs.getInt("failureCount");
				}
			} else {
				insertResetPasswordRow(ipAddr, 0, conn);
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Return the number of failures from login failures a password, given an IP address.
	 * 
	 * @param ipAddr The IP address for this attempt as a String.
	 * @return An int representing the number of failures which have occured.
	 * @throws DBException
	 */
	public int getLoginFailures(String ipAddr) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM LoginFailures WHERE IPADDRESS=?");
			ps.setString(1, ipAddr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// if we're more than X minutes out, clear the failure count
				if (System.currentTimeMillis() - rs.getTimestamp("lastFailure").getTime() > LOGIN_TIMEOUT) {
					updateFailuresToZero(ipAddr, conn);
					return 0;
				} else {
					return rs.getInt("failureCount");
				}
			} else {
				insertLoginFailureRow(ipAddr, 0, conn);
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	private void insertLoginFailureRow(String ipAddr, int failureCount, Connection conn) throws DBException,
			SQLException {
		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO LoginFailures(IPAddress, failureCount) VALUES(?,?)");
		ps.setString(1, ipAddr);
		ps.setInt(2, failureCount);
		ps.executeUpdate();
	}

	private void insertResetPasswordRow(String ipAddr, int failureCount, Connection conn) throws DBException,
			SQLException {
		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO ResetPasswordFailures(IPAddress, failureCount) VALUES(?,?)");
		ps.setString(1, ipAddr);
		ps.setInt(2, failureCount);
		ps.executeUpdate();
	}

	private void updateFailuresToZero(String ipAddr, Connection conn) throws DBException, SQLException {
		PreparedStatement ps = conn
				.prepareStatement("UPDATE LoginFailures SET failureCount=0 WHERE IPAddress=?");
		ps.setString(1, ipAddr);
		ps.executeUpdate();
	}
	
	public void resetLoginFailuresToZero(String ipAddr) throws DBException, SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = factory.getConnection();
			ps = conn
					.prepareStatement("UPDATE LoginFailures SET failureCount=0 WHERE IPAddress=?");
			ps.setString(1, ipAddr);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	private void updateResetFailuresToZero(String ipAddr, Connection conn) throws DBException, SQLException {
		PreparedStatement ps = conn
				.prepareStatement("UPDATE ResetPasswordFailures SET failureCount=0 WHERE IPAddress=?");
		ps.setString(1, ipAddr);
		ps.executeUpdate();
	}

	/**
	 * Check that a user actually exists.
	 * 
	 * @param mid
	 * @return
	 * @throws DBException
	 */
	public boolean checkUserExists(long mid) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Users WHERE MID=?");
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
