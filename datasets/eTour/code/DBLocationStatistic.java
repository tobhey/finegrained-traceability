
package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class that implements the local statistical
 *
 */
public class DBLocationStatistic implements IDBLocationStatistic {
// Empty constructor
    public DBLocationStatistic() {

    }

    public ArrayList<String> getListLocation() throws SQLException {
// Variable for the connection
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
// Query for the extraction of location between the PR and BC
            String query = "(SELECT DISTINCT Locations" + "FROM beneculturale) UNION"
                    + "(SELECT DISTINCT FROM Location puntodiristoro)";
// The query is executed
            result = stat.executeQuery(query);
// We extract the results from the result set and moves in
// List
// To be returned
// List that includes the results obtained
            ArrayList<String> list = new ArrayList<String>();
            while (result.next()) {
// Add to the list the locations obtained
                list.add(result.getString("Location"));
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

    public double getAverageRatingLocation(String pLocation) throws SQLException {
// Variable for the connection
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
// Query to extract the average of the votes of catering outlets
// According to a Passo
            String query = "SELECT avg (AverageRating) as AverageRating FROM puntodiristoro WHERE Location = '"
                    + pLocation + " '";
// The query is executed
            result = stat.executeQuery(query);
// We extract the results from the result set
            double point = 0.0;
            if (result.next()) {
                point = result.getDouble("AverageRating");
            }
// Query to extract the average of the votes of cultural
// According to a Passo
            query = "SELECT avg (AverageRating) AS AverageRating FROM beneculturale WHERE Location = '" + pLocation
                    + " '";
// The query is executed
            result = stat.executeQuery(query);
// We extract the results from the result set
            double good = 0.0;
            if (result.next()) {
                good = result.getDouble("AverageRating");
            }
// It returns the average of the refreshment and heritage
            return (good + point) / 2;

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