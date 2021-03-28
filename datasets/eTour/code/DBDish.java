package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanDish;

/**
 * Class that implements the interface plate
 *
 */
public class DBDish implements IDBDish {
// Empty constructor
    public DBDish() {

    }

    public boolean clearDish(int pIdDish) throws SQLException {
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
            String query = "DELETE FROM courses WHERE ID =" + pIdDish;
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

    public boolean insertDish(BeanDish pDish) throws SQLException {
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
            String query = "INSERT INTO courses (Name, Price, IdMenu) VALUES ( '" + pDish.getName() + " ',"
                    + pDish.getPrice() + "' " + pDish.getIdMenu() + ")";
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

    public boolean modifyDish(BeanDish pDish) throws SQLException {
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
            String query = "UPDATE courses SET" + "Name = '" + pDish.getName() + " ', Price =" + pDish.getPrice()
                    + ", IdMenu =" + pDish.getIdMenu() + "WHERE Id =" + pDish.getId();
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

    public ArrayList<BeanDish> getDish(int pIdMenu) throws SQLException {
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
// Query to extract the list of dishes given the id of the Menu
            String query = "SELECT * FROM courses WHERE IdMenu =" + pIdMenu;
// The query is executed
            result = stat.executeQuery(query);
// List that contains all the plates obtained
            ArrayList<BeanDish> list = new ArrayList<BeanDish>();
// We extract the results from the result set and moves in
// List
// To be returned
            while (result.next()) {
// Add the list BeanDish
                list.add(new BeanDish(result.getInt("Price"), result.getString("Name"), result.getInt("IdMenu"),
                        result.getInt("id ")));
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

}
