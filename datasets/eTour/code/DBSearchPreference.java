package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanSearchPreference;

/**
 * Class that implements the interface SearchPreference
 *
 */
public class DBSearchPreference implements IDBSearchPreference {
// Empty constructor
    public DBSearchPreference() {
    }

    public boolean clearSearchPreference(int pIdPreference) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
            ;
// Create the Statement
            stat = conn.createStatement();
// Query clearing
            String query = "DELETE FROM preferenzedisearch WHERE Id =" + pIdPreference;
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

    public boolean clearSearchPreferenceBC(int pIdCulturalHeritage, int pIdSearchPreference) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
            ;
// Create the Statement
            stat = conn.createStatement();
// Query clearing
            String query = "DELETE FROM associazionebc WHERE IdSearchPreference =" + pIdSearchPreference
                    + "AND IdCulturalHeritage =" + pIdCulturalHeritage;
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

    public boolean clearSearchPreferencePR(int pIdRefreshmentPoint, int pIdPreference) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
            ;
// Create the Statement
            stat = conn.createStatement();
// Query clearing
            String query = "DELETE FROM associazionepr WHERE IdSearchPreference =" + pIdPreference
                    + "AND IdRefreshmentPoint =" + pIdRefreshmentPoint;
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

    public boolean clearSearchPreferenceTourist(int pIdTourist, int pIdPreference) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query clearing
            String query = "DELETE FROM rating WHERE IdTourist =" + pIdTourist + "AND IdSearchPreference ="
                    + pIdPreference;
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

    public boolean insertSearchPreferenceDelBC(int pIdCulturalHeritage, int pIdSearchPreference)
            throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the insertion
            String query = "INSERT INTO associazionebc (IdSearchPreference, IdCulturalHeritage) VALUES ("
                    + pIdSearchPreference + "," + pIdCulturalHeritage + ")";
// You run the insert query
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

    public boolean insertSearchPreference(BeanSearchPreference pPreference) throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the insertion
            String query = "INSERT INTO preferenzedisearch (Id, Name) VALUES (" + pPreference.getId() + " '"
                    + pPreference.getName() + "')";
// You run the insert query
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

    public boolean insertSearchPreferenceDelPR(int pIdRefreshmentPoint, int pIdSearchPreference)
            throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the insertion
            String query = "INSERT INTO associazionepr (IdSearchPreference, IdRefreshmentPoint) VALUES ("
                    + pIdSearchPreference + "," + pIdRefreshmentPoint + ")";
// You run the insert query
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

    public boolean insertSearchPreferenceDelTourist(int pIdTourist, int pIdSearchPreference)
            throws SQLException {
// Variables for database connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for the insertion
            String query = "INSERT INTO rating (IdTourist, IdSearchPreference) VALUES (" + pIdTourist + ","
                    + pIdSearchPreference + ")";
// You run the insert query
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

    public ArrayList<BeanSearchPreference> getSearchPreferenceDelBC(int pIdCulturalHeritage)
            throws SQLException {
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
// Query to extract the list of search preferences
// A cultural
            String query = "SELECT * FROM associazionebc, preferenzedisearch WHERE IdCulturalHeritage ="
                    + pIdCulturalHeritage + "AND IdSearchPreference preferenzedisearch.Id =";
// The query is executed
            result = stat.executeQuery(query);
// List that will contain the BeanSearchPreference
            ArrayList<BeanSearchPreference> list = new ArrayList<BeanSearchPreference>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Add to the list BeanSearchPreference
                list.add(new BeanSearchPreference(result.getInt("Id"), result.getString("Name ")));
            }
            return list;
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

    public ArrayList<BeanSearchPreference> getSearchPreferenceDelPR(int pIdRefreshmentPoint)
            throws SQLException {
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
// Query to extract the list of search preferences
// A refreshment
            String query = "SELECT * FROM associazionepr, preferenzedisearch WHERE IdRefreshmentPoint ="
                    + pIdRefreshmentPoint + "AND IdSearchPreference preferenzedisearch.Id =";
// The query is executed
            result = stat.executeQuery(query);
// List that will contain the BeanSearchPreference
            ArrayList<BeanSearchPreference> list = new ArrayList<BeanSearchPreference>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Add to the list BeanSearchPreference
                list.add(new BeanSearchPreference(result.getInt("Id"), result.getString("Name ")));
            }
            return list;
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

    public ArrayList<BeanSearchPreference> getSearchPreferenceDelTourist(int pIdTourist) throws SQLException {
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
// Query to extract the list of search preferences
// A Tourist
            String query = "SELECT * FROM liking preferenzedisearch WHERE IdTourist =" + pIdTourist
                    + "AND IdSearchPreference preferenzedisearch.Id =";
// The query is executed
            result = stat.executeQuery(query);
// List that will contain the BeanSearchPreference
            ArrayList<BeanSearchPreference> list = new ArrayList<BeanSearchPreference>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Add to the list BeanSearchPreference
                list.add(new BeanSearchPreference(result.getInt("Id"), result.getString("Name ")));
            }
            return list;
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

    public ArrayList<BeanSearchPreference> getSearchPreference() throws SQLException {
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
// Query to extract the list of search preferences
// A Tourist
            String query = "SELECT * FROM preferenzedisearch";
// The query is executed
            result = stat.executeQuery(query);
// List that will contain the BeanSearchPreference
            ArrayList<BeanSearchPreference> list = new ArrayList<BeanSearchPreference>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Add to the list BeanSearchPreference
                list.add(new BeanSearchPreference(result.getInt("Id"), result.getString("Name ")));
            }
// Return the list of search preferences in the DB
            return list;
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