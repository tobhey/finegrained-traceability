/*
 * ConnectionWrapper
 *
 */

package smos.storage.connectionManagement;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.ltodayng.Logger;

/**
 * ConnectionWrapper is a class that adds to all methods of the class Connection
 * the setting of the time in which the operation on the particular connection
 * took place informing the pool of what happened.
 */
public class ConnectionWrapper implements Connection, Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(ConnectionWrapper.class.getName());

	private Connection connection;

	private SourceDataConnectionPool manager;

	private ArrayList<Statement> statements = new ArrayList<Statement>();

	/**
	 * @param pConnections
	 * @param pPoolManager
	 */
	public ConnectionWrapper(Connection pConnections, SourceDataConnectionPool pPoolManager) {
		this.connection = pConnections;
		this.manager = pPoolManager;
		LOGGER.fine("Creating ConnectionWrapper");
	}

	private PreparedStatement cachePreparedStatement(PreparedStatement pPrepSt) {
		this.manager.setLastTime(this);
		this.statements.add(pPrepSt);
		return pPrepSt;
	}

	private Statement cacheStatement(Statement pStatement) {
		this.manager.setLastTime(this);
		this.statements.add(pStatement);
		return pStatement;
	}

	/**
	 * @see java.sql.Connection#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		this.connection.clearWarnings();
	}

	/**
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		closeAndReleaseStatements();
		this.manager.release(this);
	}

	private synchronized void closeAndReleaseStatements() throws SQLException {
		final int n = this.statements.size();
		for (int i = 0; i < n; i++) {
			this.statements.get(i).close();
		}
		this.statements.clear();
	}

	/**
	 * Close the wrapped connection.
	 * 
	 * @throws SQLException
	 */
	void closeConnectionWrapper() throws SQLException {
		closeAndReleaseStatements();
		if (!this.connection.isClosed()) {
			LOGGER.fine("Closing db connection: " + this.getClass().getName() + " [" + this + "]");
		}
		this.connection.close();
	}

	/**
	 * @see java.sql.Connection#commit()
	 */
	@Override
	public void commit() throws SQLException {
		this.manager.setLastTime(this);
		this.connection.commit();
	}

	/**
	 * @see java.sql.Connection#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {
		this.manager.setLastTime(this);
		return cacheStatement(this.connection.createStatement());
	}

	/**
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	@Override
	public Statement createStatement(int pResultSetType, int pResultSetConcurrency) throws SQLException {
		this.manager.setLastTime(this);
		return cacheStatement(this.connection.createStatement(pResultSetType, pResultSetConcurrency));
	}

	/**
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	@Override
	public Statement createStatement(int pResultSetType, int pResultSetConcurrency, int pResultSetHoldability)
			throws SQLException {
		this.manager.setLastTime(this);
		return cacheStatement(
				this.connection.createStatement(pResultSetType, pResultSetConcurrency, pResultSetHoldability));
	}

	/**
	 * Closes the wrapped connection.
	 */
	@Override
	protected void finalize() throws Throwable {
		closeConnectionWrapper();
	}

	/**
	 * @see java.sql.Connection#getAutoCommit()
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getAutoCommit();
	}

	/**
	 * @see java.sql.Connection#getCatalog()
	 */
	@Override
	public String getCatalog() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getCatalog();
	}

	/**
	 * @see java.sql.Connection#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getHoldability();
	}

	/**
	 * @see java.sql.Connection#getMetaData()
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getMetaData();
	}

	/**
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getTransactionIsolation();
	}

	/**
	 * @see java.sql.Connection#getTypeMap()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map getTypeMap() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getTypeMap();
	}

	/**
	 * @see java.sql.Connection#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.getWarnings();
	}

	/**
	 * @see java.sql.Connection#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.isClosed();
	}

	/**
	 * @see java.sql.Connection#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.isReadOnly();
	}

	/**
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	@Override
	public String nativeSQL(String sql) throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.nativeSQL(sql);
	}

	/**
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.prepareCall(sql);
	}

	/**
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String pStatementSql, int pResultSetType, int pResultSetConcurrency)
			throws SQLException {

		this.manager.setLastTime(this);
		return this.connection.prepareCall(pStatementSql, pResultSetType, pResultSetConcurrency);
	}

	/**
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String pStatementSql, int pResultSetType, int pResultSetConcurrency,
			int pResultSetHoldability) throws SQLException {

		this.manager.setLastTime(this);
		return this.connection.prepareCall(pStatementSql, pResultSetType, pResultSetConcurrency, pResultSetHoldability);
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement prepareStatement(String pStatementSql) throws SQLException {
		this.manager.setLastTime(this);
		return cachePreparedStatement(this.connection.prepareStatement(pStatementSql));
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String pStatementSql, int pAutoGeneratedKeys) throws SQLException {

		this.manager.setLastTime(this);
		return cachePreparedStatement(this.connection.prepareStatement(pStatementSql, pAutoGeneratedKeys));
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String pStatementSql, int pResultSetType, int pResultSetConcurrency)
			throws SQLException {

		this.manager.setLastTime(this);
		return cachePreparedStatement(
				this.connection.prepareStatement(pStatementSql, pResultSetType, pResultSetConcurrency));
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String pStatementSql, int pResultSetType, int pResultSetConcurrency,
			int pResultSetHoldability) throws SQLException {

		this.manager.setLastTime(this);
		return cachePreparedStatement(this.connection.prepareStatement(pStatementSql, pResultSetType,
				pResultSetConcurrency, pResultSetHoldability));
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	@Override
	public PreparedStatement prepareStatement(String pStatementSql, int[] columnIndexes) throws SQLException {
		this.manager.setLastTime(this);
		return cachePreparedStatement(this.connection.prepareStatement(pStatementSql, columnIndexes));
	}

	/**
	 * @see java.sql.Connection#prepareStatement(java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public PreparedStatement prepareStatement(String pStatementSql, String[] pColumnNames) throws SQLException {
		this.manager.setLastTime(this);
		return cachePreparedStatement(this.connection.prepareStatement(pStatementSql, pColumnNames));
	}

	/**
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	@Override
	public void releaseSavepoint(Savepoint pSavepoint) throws SQLException {
		this.manager.setLastTime(this);
		this.connection.releaseSavepoint(pSavepoint);
	}

	/**
	 * @see java.sql.Connection#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		this.manager.setLastTime(this);
		this.connection.rollback();
	}

	/**
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	@Override
	public void rollback(Savepoint pSavepoint) throws SQLException {
		this.manager.setLastTime(this);
		this.connection.rollback(pSavepoint);
	}

	/**
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(boolean pAutoCommit) throws SQLException {
		this.manager.setLastTime(this);
		if (this.connection.getAutoCommit() != pAutoCommit) {
			this.connection.setAutoCommit(pAutoCommit);
		}
	}

	/**
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	@Override
	public void setCatalog(String pCatalog) throws SQLException {
		this.manager.setLastTime(this);
		this.connection.setCatalog(pCatalog);
	}

	/**
	 * @see java.sql.Connection#setHoldability(int)
	 */
	@Override
	public void setHoldability(int pHoldability) throws SQLException {
		this.manager.setLastTime(this);
		this.connection.setHoldability(pHoldability);
	}

	/**
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean pReadOnly) throws SQLException {
		this.manager.setLastTime(this);
		this.connection.setReadOnly(pReadOnly);
	}

	/**
	 * @see java.sql.Connection#setSavepoint()
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.setSavepoint();
	}

	/**
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	@Override
	public Savepoint setSavepoint(String pName) throws SQLException {
		this.manager.setLastTime(this);
		return this.connection.setSavepoint(pName);
	}

	/**
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	@Override
	public void setTransactionIsolation(int pLevel) throws SQLException {
		this.manager.setLastTime(this);
		this.connection.setTransactionIsolation(pLevel);
	}

	/**
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.connection.toString();
	}

}
