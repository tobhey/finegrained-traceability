package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Class that implements the interface IDBVisitPR
 *
 */
public class DBVisitPR implements IDBVisitPR {
// Empty constructor
    public DBVisitPR() {

    }

    public boolean insertVisitPR(BeanVisitPR pVisit) throws SQLException {
// Variable for the connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
// Variable for the query results
        ResultSet result = null;
        try {
// Create the date of visit
            java.sql.Date dataVisit = new Date(pVisit.getDataVisit().getTime());
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query to get the average rating of a property
            String queryAverageRating = "SELECT AverageRating, RatingNumber FROM puntodiristoro WHERE Id ="
                    + pVisit.getIdRefreshmentPoint();
            result = stat.executeQuery(queryAverageRating);
// Variable for the average rating
            double average = 0;
// Variable for the number of votes
            int ratingNumber = 0;
            if (result.next()) {
                average = result.getDouble("AverageRating");
                ratingNumber = result.getInt("RatingNumber ");
                average = ((average * ratingNumber) + pVisit.getRating()) / ratingNumber;
            }
// Query for the insertion
            String query = "INSERT INTO visitapr (IdTourist, IdRefreshmentPoint, DataVisit, Vote, Comment) VALUES ("
                    + pVisit.getIdTourist() + "" + pVisit.getIdRefreshmentPoint() + " '" + dataVisit + " ',"
                    + pVisit.getRating() + " '" + pVisit.getComment() + "')";
            String query2 = "UPDATE puntodiristoro September AverageRating =" + queryAverageRating + ", RatingNumber ="
                    + ratingNumber + "WHERE Id =" + pVisit.getIdRefreshmentPoint();
// You run the insert query
            stat.executeQuery("BEGIN");
            int i = stat.executeUpdate(query);
            i = i * stat.executeUpdate(query2);
            stat.executeQuery("COMMIT");
// This returns the backup
            return (i == 1);
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

    public boolean modifyVisitPR(BeanVisitPR pVisit) throws SQLException {
// Variable for the connection
        Connection conn = null;
// Variable for the query
        Statement stat = null;
        try {
// Create the date of visit
            java.sql.Date dataVisit = new Date(pVisit.getDataVisit().getTime());
// Get the connection
            conn = DBConnectionPool.getConnection();
// Create the Statement
            stat = conn.createStatement();
// Query for amendment
            String query = "UPDATE visitapr SET" + "DataVisit = '" + dataVisit + " ', Comment ='"
                    + pVisit.getComment() + " 'WHERE IdRefreshmentPoint =" + pVisit.getIdRefreshmentPoint()
                    + "AND IdTourist =" + pVisit.getIdTourist();
// You run the query for Change
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Always runs and takes care to close the Result, the Statement
// And Connection
        finally {
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnections(conn);
            }
        }

    }

    public ArrayList<BeanVisitPR> getListVisitPR(int pIdRefreshmentPoint) throws SQLException {
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
// Query to extract the list of requests for a
// Refreshment
            String query = "SELECT * FROM visitapr WHERE IdRefreshmentPoint =" + pIdRefreshmentPoint;
// The query is executed
            result = stat.executeQuery(query);
            ArrayList<BeanVisitPR> list = new ArrayList<BeanVisitPR>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
                java.util.Date dataVisit = new java.util.Date(result.getDate("DataVisit").getTime());
                list.add(new BeanVisitPR(result.getInt("Customer"), result.getInt("IdRefreshmentPoint"),
                        result.getString("Comment"), result.getInt("IdTourist"), dataVisit));
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

    public ArrayList<BeanVisitPR> getListVisitPRTourist(int pIdTourist) throws SQLException {
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
// Query to extract the list of requests for a
// Eating place for tourists
            String query = "SELECT * FROM visitapr WHERE IdTourist =" + pIdTourist;
// The query is executed
            result = stat.executeQuery(query);
// List that will contain the BeanVisitPR
            ArrayList<BeanVisitPR> list = new ArrayList<BeanVisitPR>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Add to the list BeanVisitPR
                java.util.Date dataVisit = new java.util.Date(result.getDate("DataVisit").getTime());
                list.add(new BeanVisitPR(result.getInt("Customer"), result.getInt("IdRefreshmentPoint"),
                        result.getString("Comment"), result.getInt("IdTourist"), dataVisit));
            }
// Return the list
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

    public BeanVisitPR getVisitPR(int pIdRefreshmentPoint, int pIdTourist) throws SQLException {
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
// Query for the extraction of the visit made by a tourist to
// A given point of comfort
            String query = "SELECT * FROM visitapr WHERE IdRefreshmentPoint =" + pIdRefreshmentPoint + "AND IdTourist ="
                    + pIdTourist;
// The query is executed
            result = stat.executeQuery(query);
// Get the bean's visit sought based on the ID of the tourist and
// Of refreshment
            BeanVisitPR beanTemp = null;
            if (result.next()) {
// Create the BeanVisitPR
                java.util.Date dataVisit = new java.util.Date(result.getDate("DataVisit").getTime());
                beanTemp = new BeanVisitPR(result.getInt("Customer"), result.getInt("IdRefreshmentPoint"),
                        result.getString("Comment"), result.getInt("IdTourist"), dataVisit);
            }
// Return the BeanTemp
            return beanTemp;
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
