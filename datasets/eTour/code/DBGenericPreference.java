package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import unisa.gps.etour.bean.BeanGenericPreference;

/**
 * Implementing the IDBGenericPreference
 *
 */
public class DBGenericPreference implements IDBGenericPreference {
// Constructor without parameters
    public DBGenericPreference() {

    }

    public boolean clearGenericPreference(int pIdPreference) throws SQLException {
// Connect to database
        Connection conn = null;
// Statement for running queries
        Statement stat = null;
// Try block which performs the query and the database connection
        try {
// You get the database connection from the pool
            conn = DBConnectionPool.getConnection();
// Create the statement
            stat = conn.createStatement();
// Query
            String query = "DELETE FROM preferenzegeneriche WHERE Id =" + pIdPreference;
// You run the query
            int i = stat.executeUpdate(query);

            return (i == 1);
        }
// Finally block that contains the instructions to close the connections
// Hyenas run in any case
        finally {
// This closes the if statement and 'opened
            if (stat != null) {
                stat.close();
            }
// It returns the connection to the pool if and 'opened
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public boolean insertGenericPreference(BeanGenericPreference pPreference) throws SQLException {
// Connect to database
        Connection conn = null;
// Statement for running queries
        Statement stat = null;
// Try block which performs the query and the database connection
        try {
// You get the database connection from the pool
            conn = DBConnectionPool.getConnection();
// Create the statement
            stat = conn.createStatement();
// Query
            String query = "INSERT INTO preferenzegeneriche (IdTourist, Font, Theme, DimensioneFont)" + "VALUES ("
                    + pPreference.getIdTourist() + " '" + pPreference.getFont() + "','" + pPreference.getTheme() + " ',"
                    + pPreference.getDimensioneFont() + ")";
// You run the query
            int i = stat.executeUpdate(query);
            return (i == 1);
        }
// Finally block that contains the instructions to close the connections
// Hyenas run in any case
        finally {
// This closes the if statement and 'opened
            if (stat != null) {
                stat.close();
            }
// It returns the connection to the pool if and 'opened
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public boolean modifyGenericPreference(BeanGenericPreference pPreference) throws SQLException {
// Connect to database
        Connection conn = null;
// Statement for running queries
        Statement stat = null;
// Try block which performs the query and the database connection
        try {
// You get the database connection from the pool
            conn = DBConnectionPool.getConnection();
// Create the statement
            stat = conn.createStatement();
// Query
            String query = "UPDATE preferenzegeneriche SET" + "= IdTourist" + pPreference.getIdTourist() + ", font = '"
                    + pPreference.getFont() + " ', theme ='" + pPreference.getTheme() + " ', DimensioneFont ="
                    + pPreference.getDimensioneFont() + "WHERE Id =" + pPreference.getId();
// You run the query
            int i = stat.executeUpdate(query);

            return (i == 1);
        }
// Finally block that contains the instructions to close the connections
// Hyenas run in any case
        finally {
// This closes the if statement and 'opened
            if (stat != null) {
                stat.close();
            }
// It returns the connection to the pool if and 'opened
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

    public BeanGenericPreference getGenericPreference(int pIdTourist) throws SQLException {
// Connect to database
        Connection conn = null;
// Statement for running queries
        Statement stat = null;
// Resut set where the output of the query is inserted
        ResultSet result = null;
// Try block which performs the query and the database connection
        try {
// You get the database connection from the pool
            conn = DBConnectionPool.getConnection();
// Create the statement
            stat = conn.createStatement();
// Query
            String query = "SELECT * FROM preferenzegeneriche WHERE IdTourist =" + pIdTourist;
// Run the query
            result = stat.executeQuery(query);
            BeanGenericPreference pref = null;
// Check that the query returns at least one result
            if (result.next()) {
                pref = new BeanGenericPreference();
                pref.setId(result.getInt("Id"));
                pref.setIdTourist(result.getInt("IdTourist "));
                pref.setDimensioneFont(result.getInt("DimensioneFont "));
                pref.setFont(result.getString("Font"));
                pref.setTheme(result.getString("Theme"));
            }
            return pref;
        }
// Finally block that contains the instructions to close the connections
// Hyenas run in any case
        finally {
// This closes the result set only if and 'the query was made
            if (result != null) {
                result.close();
            }
// This closes the if statement and 'opened
            if (stat != null) {
                stat.close();
            }
// It returns the connection to the pool if and 'opened
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }
    }

}
