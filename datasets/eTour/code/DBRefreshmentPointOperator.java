package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import unisa.gps.etour.bean.BeanOperatorRefreshmentPoint;

/**
 * Class that implements the interface Operator Refreshment
 *
 */
public class DBRefreshmentPointOperator implements IDBRefreshmentPointOperator {
// Empty constructor
    public DBRefreshmentPointOperator() {

    }

    public boolean clearOperatorRefreshmentPoint(int pIdOperator) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query clearing
            String query = "DELETE FROM operatorepuntodiristoro WHERE Id =" + pIdOperator;
// You run the query Cancellation
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Always runs and takes care of closing the Statement and the
// Connect
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public boolean insertOperatorRefreshmentPoint(BeanOperatorRefreshmentPoint pOperator) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet single = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the insertion
            String query = "INSERT INTO operatorepuntodiristoro (Name, Surname,"
                    + "Username, Password, Email, IdRefreshmentPoint) VALUES ( '" + pOperator.getName() + "','"
                    + pOperator.getSurname() + pOperator.getUserName() + "','" + "','" + pOperator.getPassword()
                    + "','" + pOperator.getEmail() + " '," + pOperator.getIdRefreshmentPoint() + ")";
// Query for checking the ID of the RefreshmentPoint as
// The association is 1 to 1 between OPPR and PR
            String unico = "SELECT IdRefreshmentPoint FROM operatorepuntodiristoro WHERE IdRefreshmentPoint ="
                    + pOperator.getIdRefreshmentPoint();
// Execute the query to control
            single = stat.executeQuery(unico);
            int j = 0;
// Check if there are tuples
            while (single.next())
                j++;
// If it is empty
            if (j == 0) {
// You run the insert query
                int i = stat.executeUpdate(query);
// This returns the backup
                System.out.println("If you include the PR");
                return (i == 1);
            }
// If not already exist
            else {
                System.out.println("Operator PR already exists for the PR");
                return false;
            }
        }
// Always runs and takes care of closing the Statement and the
// Connect
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
            if (single != null) {
                single.close();
            }
        }
    }

    public boolean modifyOperatorRefreshmentPoint(BeanOperatorRefreshmentPoint pOperator) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for amendment
            String query = "UPDATE operatorepuntodiristoro SET" + "Name = '" + pOperator.getName() + " ', Name ='"
                    + pOperator.getSurname() + " ', password ='" + pOperator.getPassword() + " ', Email ='"
                    + pOperator.getEmail() + " 'WHERE IdRefreshmentPoint =" + pOperator.getIdRefreshmentPoint();
// You run the query for Change
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Always runs and takes care of closing the Statement and the
// Connect
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public BeanOperatorRefreshmentPoint getOperatorRefreshmentPoint(int pIdOperator) throws SQLException {

// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the extraction of the dot Refreshments required
            String query = "SELECT * FROM WHERE id = operatorepuntodiristoro" + pIdOperator;
// The query is executed
            result = stat.executeQuery(query);
// Get the bean Operator refreshment passing the id
            BeanOperatorRefreshmentPoint beanTemp = null;
            if (result.next()) {
// Built on BeanOPR
                beanTemp = new BeanOperatorRefreshmentPoint(result.getInt("Id"), result.getString("Name"),
                        result.getString("Name"), result.getString("Username"), result.getString("Password"),
                        result.getString("Email"), result.getInt("IdRefreshmentPoint"));
            }
            return beanTemp;
        }
// Exception if there is an error
        catch (Exception e) {
            throw new SQLException();
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

}