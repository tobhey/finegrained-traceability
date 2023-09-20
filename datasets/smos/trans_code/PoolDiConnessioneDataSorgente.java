/*
 * ConnectionPoolDataSource
 *
 */

package smos.storage.connectionManagement;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.ltodayng.Logger;

import javax.sql.DataSource;

/**
 * Connection pooling through implementation
 * of the java.sql.DataSource interface. The pool periodically checks the
 * active connections and those that are pooled, i.e. those that are released but still
 * usable (i.e. present in memory). The release time of the
 * active and pooled connections is represented by two parameters present
 * inside the class and which are connectionPoolCloseTimeout and
 * inactiveMaxTimeout; these values as well as all the others inherent to the pool
 * have their own default value, which can be parameterized via the
 * properties connection.properties
 */
public class SourceDataConnectionPool implements DataSource {

	/**
	 * Thread inside of the ConnectionPoolDataSource class which establishes each
	 * connectionPoolCloseTimeout milliseconds the release of the connections pool.
	 */
	private class ConnectionsClose extends Thread {

		private long connectionActionTimestamp = 0;

		private int connectionPoolCloseTimeout = 300000;

		private long timeStamp = 0;

		/**
		 * Constructor that sets the release time of the pooled connections
		 *
		 * @author Di Giorgio Domenico, Melancholy Cris
		 * @param pTime time interval within which the pool empties the list of
		 * pool connections.
		 */
		private ConnectionsClose(int pTime) {
			setDaemon(true);
			setName("ConnectionPoolCloser");
			if (pTime > 0)
				this.connectionPoolCloseTimeout = pTime;
		}

		/**
		 * Whenever a connection raises an event with an invocation of
		 * getConnection () or release () the timestamp is set to the time value
		 * current via this method.
		 */
		public void connectionEvento() {
			this.connectionActionTimestamp = System.currentTimeMillis();
		}

		/**
		 * Check every connectionPoolCloseTimeout milliseconds if pool connections
		 * can be closed, freeing up memory in this case.
		 */
		@Override
		public void run() {
			boolean working = true;
			while (working) {
				try {
					this.timeStamp = System.currentTimeMillis();
					Thread.sleep(this.connectionPoolCloseTimeout);
					if (this.connectionActionTimestamp < this.timeStamp) {
						closeAllConnections(SourceDataConnectionPool.this.pool);
					}
				} catch (InterruptedException e) {
					working = false;
					e.printStackTrace();
				} catch (SQLException e) {
					working = false;
					e.printStackTrace();
				}
			}
		}
	}

	private List<SMOSConnections> active = new Vector<SMOSConnections>();

	private Properties config = new Properties();

	private ConnectionsClose connectionClose;

	private Driver driver;

	private String connectionCompleteString;

	private long maximumIdleTimeout = 20000;

	private int dimensionMaximumPool;

	private List<Connection> pool = new Vector<Connection>();

	/**
	 * Create a new instance of the connection pool.
	 *
	 * @param pJdbcDriverName name of the jdbc driver
	 * @param pConnectionsCompleteString connection string with the database
	 * @param pUser name user (database administrator)
	 * @param pPassword administrator password
	 * @param pSizeMaximumPool maximum number of active connections in
	 * pool, must be greater than 0
	 * @param pPoolTime time interval within which the pool
	 * will be emptied of its own each time
	 * pooled connections (in ms).
	 * @throws ClassNotFoundException if the jdbc driver cannot be found
	 * @throws SQLException if a problem occurs during the connection
	 * to the database
	 * @throws IllegalArgumentException if the parameters supplied in the input are not
	 * valid
	 */
	public SourceDataConnectionPool(String pJdbcDriverName, String pConnectionsCompleteString, String pUser,
			String pPassword, int pDimensionMaximumPool, int pPoolTime) throws ClassNotFoundException, SQLException {

		if (pDimensionMaximumPool < 1) {
			throw new IllegalArgumentException("maxPoolSize deve essere >0 ma �: " + pDimensionMaximumPool);
		}
		if (pConnectionsCompleteString == null) {
			throw new IllegalArgumentException("fullConnectionString " + "ha valore Null");
		}
		if (pUser == null) {
			throw new IllegalArgumentException("il name user ha valore Null");
		}
		this.dimensionMaximumPool = pDimensionMaximumPool;
		this.connectionCompleteString = pConnectionsCompleteString;
		this.config.put("user", pUser);
		if (pPassword != null) {
			this.config.put("password", pPassword);
		}
		Class.forName(pJdbcDriverName);
		this.driver = DriverManager.getDriver(pConnectionsCompleteString);
		this.connectionClose = new ConnectionsClose(pPoolTime);
		this.connectionClose.start();
	}

	/**
	 * Returns the size of the list of active connections.
	 *
	 * @return the size of the list of currently active connections.
	 */
	public int activeSize() {
		return this.active.size();
	}

	/**
	 * Empty the connection pool of active ones that have not run anymore
	 * operations for inactiveMaxTimeout milliseconds.
	 * 
	 */
	protected void clearActive() {
		long temp = 0;
		long TIME = System.currentTimeMillis();
		SMOSConnections adc = null;

		for (int count = 0; count < this.active.size(); count++) {
			adc = (SMOSConnections) this.active.get(count);
			temp = TIME - adc.getLastTime();
			if (temp >= this.maximumIdleTimeout) {
				this.release(adc.getConnection());
			}
		}
	}

	/**
	 * Closes all connections in the pool, both active and existing ones
	 * pool.
	 *
	 * @author Di Giorgio Domenico, Melancholy Cris
	 * @throws SQLException
	 */
	public synchronized void closeAllConnections() throws SQLException {
		closeAllConnections(this.pool);
		closeAllConnections(this.active);
	}

	/**
	 * Closes all connections indicated in the connection list.
	 *
	 * @author Di Giorgio Domenico, Melancholy Cris
	 * @param pConnections the list of connections that must be closed.
	 * @throws SQLException when unable to close a connection.
	 */
	private synchronized void closeAllConnections(List pConnections) throws SQLException {

		while (pConnections.size() > 0) {
			ConnectionWrapper conn = (ConnectionWrapper) pConnections.remove(0);
			conn.closeConnectionWrapper();
		}
	}

	/**
	 * Closes all pool connections that are in the pool list.
	 *
	 * @throws SQLException when unable to close a connection.
	 */
	public synchronized void closeAllInPoolConnections() throws SQLException {
		closeAllConnections(this.pool);
	}

	/**
	 * Method used by getConnection () to create a new connection if
	 * in the pool list are not present.
	 *
	 * @return a new connection to the DataBase.
	 */
	private synchronized Connection createNewConnection() {
		Connection rawConn = null;
		try {
			rawConn = this.driver.connect(this.connectionCompleteString, this.config);
			Connection conn = new ConnectionWrapper(rawConn, this);
			SMOSConnections ac = new SMOSConnections();
			ac.setConnection(conn);
			ac.setLastTime(System.currentTimeMillis());
			this.active.add(ac);
			return conn;
		} catch (SQLException e) {
			System.out.println("Creazione della connection fallita " + "in ConnectionPoolDataSource:" + e);
			return null;
		}
	}

	/**
	 * Return a connection if the pool is not full, the check occurs first
	 * in the list of pooled connections to avoid unnecessary creation
	 * otherwise a new connection will be created.
	 *
	 * @return the connection to the database if otherwise possible
	 * an exception is thrown
	 * @see javax.sql.DataSource getConnection ()
	 * @throws SQLException If a problem occurs while connecting to the database
	 * including the fact that the maximum connection limit
	 * active is reached.
	 */
	@Override
	public synchronized Connection getConnection() throws SQLException {

		Connection connection = getPooledConnection(0);

		if (connection == null) {
			if (this.active.size() >= this.dimensionMaximumPool) {
				throw new SQLException("Connection pool limit of " + this.dimensionMaximumPool + " exceeded");
			} else {
				connection = createNewConnection();
			}
		}
		this.connectionClose.connectionEvento();
		// System.out.println("GET CONNECTION: " + active.size() + "/" + pool.size());
		return connection;
	}

	/**
	 * Method not implemented
	 *
	 * @param pArg1
	 * @param pArg2
	 * @return Connection
	 * @throws SQLException
	 *
	 * @throws NotYetImplementedException
	 */
	@Override
	public Connection getConnection(String pArg1, String pArg2) throws SQLException {
		throw new NotYetImplementedException();
	}

	/**
	 * Method not implemented
	 *
	 * @return int
	 * @throws SQLException
	 *
	 * @throws NotYetImplementedException
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		throw new NotYetImplementedException();
	}

	/**
	 * Method not implemented
	 *
	 * @return PrintWriter
	 * @throws SQLException
	 *
	 * @throws NotYetImplementedException
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new NotYetImplementedException();
	}

	/**
	 * Returns the maximum number of active connections
	 *
	 * @return the maximum number of active connections.
	 */
	public int getDimensionMaximumPool() {
		return this.dimensionMaximumPool;
	}

	/**
	 * Method used by getConnection () to determine if in the list of
	 * pool connections there are any that can be reused.
	 *
	 * @param pPoolIndex index of the list of pooled connections (always 0).
	 * @return a connection from the list of those pools if one exists.
	 */
	private synchronized Connection getPooledConnection(int pPoolIndex) {
		SMOSConnections ac = new SMOSConnections();
		Connection connection = null;
		if (this.pool.size() > 0) {
			connection = this.pool.remove(pPoolIndex);
			ac.setConnection(connection);
			ac.setLastTime(System.currentTimeMillis());
			this.active.add(ac);
		}
		return ac.getConnection();
	}

	/**
	 * Returns the size of the pooled connection list
	 *
	 * @return the size of the connection pool list.
	 */
	public int poolDimension() {
		return this.pool.size();
	}

	/**
	 * Release a connection, deleting it from the active ones and inserting it in
	 * those pools to be subsequently reused.
	 *
	 * @param pConnections The connection to be returned to the pool.
	 */
	public synchronized void release(Connection pConnections) {
		boolean exists = false;
		int activeIndex = 0;

		if (pConnections != null) {
			SMOSConnections adc = null;
			while ((activeIndex < this.active.size()) && (!exists)) {
				adc = (SMOSConnections) this.active.get(activeIndex);
				if (adc.equals(pConnections)) {
					this.active.remove(adc);
					this.pool.add(adc.getConnection());
					exists = true;
				}
				activeIndex++;
			}
			this.connectionClose.connectionEvento();
			// System.out.println("RELEASE CONNECTION: " + active.size() + "/" +
			// pool.size());
		}
	}

	/**
	 * Sets the life time of active connections in milliseconds.
	 *
	 * @param pTimeOut life time of the connection.
	 */
	public void setActiveTimeout(long pTimeOut) {
		if (pTimeOut > 0) {
			this.maximumIdleTimeout = pTimeOut;
		}
	}

	/**
	 * Reset connection life time due to running
	 * an operation. From now on the connection can be active without
	 * take no action for other inactiveMaxTimeout milliseconds.
	 *
	 * @param pConnections the connection that performed an operation and therefore can
	 * stay active.
	 */
	void setLastTime(Connection pConnections) {
		boolean exists = false;
		int count = 0;
		SMOSConnections adc = null;

		while ((count < this.active.size()) && (!exists)) {
			adc = (SMOSConnections) this.active.get(count);
			count++;
			if (adc.equals(pConnections)) {
				adc.setLastTime(System.currentTimeMillis());
				exists = true;
			}
		}
	}

	/**
	 * Method not implemented
	 *
	 * @param pArg0
	 * @throws SQLException
	 *
	 * @throws NotYetImplementedException
	 */
	@Override
	public void setLoginTimeout(int pArg0) throws SQLException {
		throw new NotYetImplementedException();
	}

	/**
	 * Method not implemented
	 *
	 * @param pArg0
	 * @throws SQLException
	 *
	 * @throws NotYetImplementedException
	 */
	@Override
	public void setLogWriter(PrintWriter pArg0) throws SQLException {
		throw new NotYetImplementedException();
	}

	/**
	 * Converts an object of the ConnectionPoolDataSource class to a String
	 *
	 * @return the representation in the String type of the connection pool.
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("[");
		buf.append("maxPoolSize=").append(this.dimensionMaximumPool);
		buf.append(", activeSize=").append(activeSize());
		buf.append(", poolSize=").append(poolDimension());
		buf.append(", fullConnectionString=").append(this.connectionCompleteString);
		buf.append("]");
		return (buf.toString());
	}

}
