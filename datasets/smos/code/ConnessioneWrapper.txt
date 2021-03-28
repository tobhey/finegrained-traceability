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
import java.util.logging.Logger;

/**
 * ConnessioneWrapper e una classe che aggiunge a tutti i metodi della classe
 * Connection il settaggio del tempo in cui l'operazione sulla particolare
 * connessione e avvenuta informando il pool di quanto accaduto.
 */
public class ConnessioneWrapper implements Connection, Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(ConnessioneWrapper.class.getName());

    private Connection connessione;

    private PoolDiConnessioneDataSorgente manager;

    private ArrayList<Statement> dichiarazioni = new ArrayList<Statement>();

    /**
     * @param pConnessione
     * @param pPoolManager
     */
    public ConnessioneWrapper(Connection pConnessione,
            PoolDiConnessioneDataSorgente pPoolManager) {
        this.connessione = pConnessione;
        this.manager = pPoolManager;
        LOGGER.fine("Creating ConnectionWrapper");
    }

    private PreparedStatement cachePreparedStatement(PreparedStatement pPrepSt) {
        this.manager.settareUltimaVolta(this);
        this.dichiarazioni.add(pPrepSt);
        return pPrepSt;
    }

    private Statement cacheStatement(Statement pStatement) {
        this.manager.settareUltimaVolta(this);
        this.dichiarazioni.add(pStatement);
        return pStatement;
    }

    /**
     * @see java.sql.Connection#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        this.connessione.clearWarnings();
    }

    /**
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        closeAndReleaseStatements();
        this.manager.rilasciare(this);
    }

    private synchronized void closeAndReleaseStatements() throws SQLException {
        final int n = this.dichiarazioni.size();
        for (int i = 0; i < n; i++) {
            ((Statement) this.dichiarazioni.get(i)).close();
        }
        this.dichiarazioni.clear();
    }

    /**
     * Close the wrapped connection.
     * @throws SQLException 
     */
    void chiudereConnessioneAvvolta() throws SQLException {
        closeAndReleaseStatements();
        if (!this.connessione.isClosed()) {
            LOGGER.fine("Closing db connection: " + this.getClass().getName()
                    + " [" + this + "]");
        }
        this.connessione.close();
    }

    /**
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.commit();
    }

    /**
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return cacheStatement(this.connessione.createStatement());
    }

    /**
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int pResultSetType,
            int pResultSetConcurrency) throws SQLException {
        this.manager.settareUltimaVolta(this);
        return cacheStatement(this.connessione.createStatement(pResultSetType,
                pResultSetConcurrency));
    }

    /**
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int pResultSetType,
            int pResultSetConcurrency, int pResultSetHoldability)
            throws SQLException {
        this.manager.settareUltimaVolta(this);
        return cacheStatement(this.connessione.createStatement(pResultSetType,
                pResultSetConcurrency, pResultSetHoldability));
    }

    /**
     * Closes the wrapped connection.
     */
    protected void finalize() throws Throwable {
        chiudereConnessioneAvvolta();
    }

    /**
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getAutoCommit();
    }

    /**
     * @see java.sql.Connection#getCatalog()
     */
    public String getCatalog() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getCatalog();
    }

    /**
     * @see java.sql.Connection#getHoldability()
     */
    public int getHoldability() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getHoldability();
    }

    /**
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getMetaData();
    }

    /**
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getTransactionIsolation();
    }

    /**
     * @see java.sql.Connection#getTypeMap()
     */
    @SuppressWarnings("unchecked")
	public Map getTypeMap() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getTypeMap();
    }

    /**
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.getWarnings();
    }

    /**
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.isClosed();
    }

    /**
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.isReadOnly();
    }

    /**
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public String nativeSQL(String sql) throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.nativeSQL(sql);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.prepareCall(sql);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(String pStatementSql,
            int pResultSetType, int pResultSetConcurrency) throws SQLException {

        this.manager.settareUltimaVolta(this);
        return this.connessione.prepareCall(pStatementSql, pResultSetType,
                pResultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public CallableStatement prepareCall(String pStatementSql,
            int pResultSetType, int pResultSetConcurrency,
            int pResultSetHoldability) throws SQLException {

        this.manager.settareUltimaVolta(this);
        return this.connessione.prepareCall(pStatementSql, pResultSetType,
                pResultSetConcurrency, pResultSetHoldability);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(String pStatementSql)
            throws SQLException {
        this.manager.settareUltimaVolta(this);
        return cachePreparedStatement(this.connessione
                .prepareStatement(pStatementSql));
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public PreparedStatement prepareStatement(String pStatementSql,
            int pAutoGeneratedKeys) throws SQLException {

        this.manager.settareUltimaVolta(this);
        return cachePreparedStatement(this.connessione.prepareStatement(
                pStatementSql, pAutoGeneratedKeys));
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(String pStatementSql,
            int pResultSetType, int pResultSetConcurrency) throws SQLException {

        this.manager.settareUltimaVolta(this);
        return cachePreparedStatement(this.connessione.prepareStatement(
                pStatementSql, pResultSetType, pResultSetConcurrency));
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public PreparedStatement prepareStatement(String pStatementSql,
            int pResultSetType, int pResultSetConcurrency,
            int pResultSetHoldability) throws SQLException {

        this.manager.settareUltimaVolta(this);
        return cachePreparedStatement(this.connessione.prepareStatement(
                pStatementSql, pResultSetType, pResultSetConcurrency,
                pResultSetHoldability));
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public PreparedStatement prepareStatement(String pStatementSql,
            int[] columnIndexes) throws SQLException {
        this.manager.settareUltimaVolta(this);
        return cachePreparedStatement(this.connessione.prepareStatement(
                pStatementSql, columnIndexes));
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public PreparedStatement prepareStatement(String pStatementSql,
            String[] pColumnNames) throws SQLException {
        this.manager.settareUltimaVolta(this);
        return cachePreparedStatement(this.connessione.prepareStatement(
                pStatementSql, pColumnNames));
    }

    /**
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    public void releaseSavepoint(Savepoint pSavepoint) throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.releaseSavepoint(pSavepoint);
    }

    /**
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.rollback();
    }

    /**
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    public void rollback(Savepoint pSavepoint) throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.rollback(pSavepoint);
    }

    /**
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean pAutoCommit) throws SQLException {
        this.manager.settareUltimaVolta(this);
        if (this.connessione.getAutoCommit() != pAutoCommit) {
            this.connessione.setAutoCommit(pAutoCommit);
        }
    }

    /**
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    public void setCatalog(String pCatalog) throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.setCatalog(pCatalog);
    }

    /**
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(int pHoldability) throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.setHoldability(pHoldability);
    }

    /**
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean pReadOnly) throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.setReadOnly(pReadOnly);
    }

    /**
     * @see java.sql.Connection#setSavepoint()
     */
    public Savepoint setSavepoint() throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.setSavepoint();
    }

    /**
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    public Savepoint setSavepoint(String pNome) throws SQLException {
        this.manager.settareUltimaVolta(this);
        return this.connessione.setSavepoint(pNome);
    }

    /**
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(int pLevel) throws SQLException {
        this.manager.settareUltimaVolta(this);
        this.connessione.setTransactionIsolation(pLevel);
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
    public String toString() {
        return this.connessione.toString();
    }
	
	

}
