package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanMenu;

/**
 * Class that implements the interface Menu
 *
 */
public class DBMenu implements IDBMenu {
// Empty constructor
    public DBMenu() {

    }

    public boolean clearMenu(int pIdMenu) throws SQLException {
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
            String query = "DELETE FROM menu WHERE id =" + pIdMenu;
// You run the query Cancellation
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Is always done and takes care of closing the Statement and the
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

    public boolean insertMenu(BeanMenu pMenu) throws SQLException {
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
            String query = "INSERT INTO menu (Day, IdRefreshmentPoint) VALUES ( '" + pMenu.getDay() + " ',"
                    + pMenu.getIdRefreshmentPoint() + ")";
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

    public boolean modifyMenu(BeanMenu pMenu) throws SQLException {
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
            String query = "UPDATE menu SET" + "Date = '" + pMenu.getDay() + " 'WHERE Id =" + pMenu.getId();
// You run the query for Change
            int i = stat.executeUpdate(query);
// This returns the backup
            return (i == 1);
        }
// Is always done and takes care of closing the Statement and the
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

    public ArrayList<BeanMenu> getMenu(int pIdRefreshmentPoint) throws SQLException {
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
// Query to extract the list of Menu
            String query = "SELECT * FROM menu WHERE IdRefreshmentPoint =" + pIdRefreshmentPoint;
// The query is executed
            result = stat.executeQuery(query);
// List that will contain all BeanMenu obtained
            ArrayList<BeanMenu> list = new ArrayList<BeanMenu>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Fill the list
                list.add(
                        new BeanMenu(result.getInt("Id"), result.getString("Day"), result.getInt("IdRefreshmentPoint ")));
            }
// Return the list
            return list;
        }
// Is always done and takes care to close the Result, the Statement
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

    public BeanMenu getMenuDelDay(int pIdRefreshmentPoint, String pDay) throws SQLException {
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
// Query for the extraction of Daily Menu
            String query = "SELECT * FROM menu WHERE IdRefreshmentPoint =" + pIdRefreshmentPoint + "AND day = '" + pDay
                    + "'";
// The query is executed
            result = stat.executeQuery(query);
// Get the bean of the daily menu based on the ID of the point of
// Dining and a day
            BeanMenu beanTemp = null;
            if (result.next()) {
// Create the proceeds Bean
                beanTemp = new BeanMenu(result.getInt("Id"), result.getString("Day"),
                        result.getInt("IdRefreshmentPoint"));
            }
// Return the Bean obtained
            return beanTemp;
        }
// Is always done and takes care to close the Result, the Statement
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