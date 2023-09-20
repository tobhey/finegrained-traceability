package edu.ncsu.csc.itrust;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.ncsu.csc.itrust.dao.DAOFactory;

/**
 * Provides a few database utilties
 * 
 * @author Andy
 * 
 */
public class DBUtil {
	/**
	 * Used to check if we can actually obtain a connection.
	 * 
	 * @return
	 */
	public static boolean canObtainProductionInstance() {
		try {
			DAOFactory.getProductionInstance().getConnection().close();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * Close the prepared statement and the connection in a proper way
	 * 
	 * @param conn
	 * @param ps
	 */
	public static void closeConnection(Connection conn, PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			System.err.println("Error closing connections");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the last ID that was generated for an auto-increment column. Please note that this does NOT
	 * cause transaction problems! last_insert_id() returns the last generated ID on a per-connection basis.
	 * See the MySQL documentation at the following location to confirm this:
	 * {@link http://dev.mysql.com/doc/refman/5.0/en/getting-unique-id.html}
	 * 
	 * Don't believe me? see {@link AutoIncrementTest}
	 * 
	 * @param conn
	 * @return last generated id
	 * @throws SQLException
	 */
	public static long getLastInsert(Connection conn) throws SQLException {
		ResultSet rs = conn.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
		rs.next();
		return rs.getLong(1);
	}
}
