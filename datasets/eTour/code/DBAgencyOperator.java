
package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import unisa.gps.etour.bean.BeanOperatorAgency;
/**
 * Class that implements the Agency's Operator
 */
public class DBAgencyOperator implements IDBAgencyOperator {

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBOperatorAgency # getOperatorAgency
     * (int)
     */
    public BeanOperatorAgency getOperatorAgency(String pUsername) throws SQLException {
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
            String query = "SELECT * FROM operatoreagenzia WHERE Username = '" + pUsername + " '";
            result = stat.executeQuery(query);
            BeanOperatorAgency oa = null;
            if (result.next()) {
// Build the bean when the query returns a
// Value
// Otherwise will return null
                oa = new BeanOperatorAgency();
                oa.setId(result.getInt("Id"));
                oa.setUsername(result.getString("Username"));
                oa.setName(result.getString("Name"));
                oa.setSurname(result.getString("Name"));
                oa.setPassword(result.getString("Password"));
            }
            return oa;
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

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.repository.IDBOperatorAgency # modifyPassword
     * (java.lang.String)
     */
    public boolean modifyPassword(BeanOperatorAgency pOa) throws SQLException {
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
            String query = "UPDATE operatoreagenzia SET" + "Password = '" + pOa.getPassword() + " 'WHERE Id ="
                    + pOa.getId();
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
}
