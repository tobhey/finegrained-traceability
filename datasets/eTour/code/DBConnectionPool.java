package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates the connection to the database using JDBC and Allows you
 * to query both read and edit the contents of Database. E 'implemented to
 * provide a pool of connections to Provide a connection to each thread.
 */
public class DBConnectionPool {
    private final static String driver = "com.mysql.jdbc.Driver";
    private final static String urlConnection = "jdbc: mysql: // localhost / eTour? user = & password = mauro mauro";
    private static List<Connection> freeConnections;

    /* private constructor that initiates the connection to the database */

    /*
     * Static initialization block is used to load the driver Memory
     */
    static {
        freeConnections = new ArrayList<Connection>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the connection to the server.
     *
     * @Return Returns the database connection
     * @Throws SQLException
     */
    public static synchronized Connection getConnection() throws SQLException {
        Connection connection;

        if (!freeConnections.isEmpty()) {
// Extract a connection from the free db connection queue
            connection = freeConnections.get(0);
            DBConnectionPool.freeConnections.remove(0);

            try {
// If the connection is not valid, a new connection will be
// Analyzed
                if (connection.isClosed())
                    connection = DBConnectionPool.getConnection();
            } catch (SQLException e) {
                connection = DBConnectionPool.getConnection();
            }
        } else
// The free db connection queue is empty, so a new connection will
// Be created
            connection = DBConnectionPool.creaDBConnection();

        return connection;
    }

    public static void releaseConnections(Connection pReleasedConnection) {
// Add the connection to the free db connection queue
        DBConnectionPool.freeConnections.add(pReleasedConnection);
    }

    private static Connection creaDBConnection() throws SQLException {
        Connection newConnections = null;
// Create a new db connection using the db properties
        newConnections = DriverManager.getConnection(urlConnection);
        newConnections.setAutoCommit(true);
        return newConnections;
    }
}