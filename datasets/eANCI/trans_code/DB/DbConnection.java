package DB;

import Bean.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.*;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * This class provides a connection pool
 *
 * @author Antonio Leone
 */

public class DbConnection {

  private static Properties dbProperties;
  private static ComboPooledDataSource cpds = null;

  /**
   * This piece of code creates a connection pool and defines the db properties
   */
  static {
    try {
      DbConnection.loadDbProperties();
      DbConnection.createPool();
    } catch (IOException e) {
      throw new DbException("Error in defining DB propertiesd");
    } catch (SQLException e) {
      throw new DbException("Error creating the Connection Pool");
    } catch (PropertyVetoException e) {
      throw new DbException("Error creating the Connection Pool");
    }
  }

  private static void createPool() throws SQLException, PropertyVetoException {
    cpds = new ComboPooledDataSource();
    cpds.setJdbcUrl(DbConnection.dbProperties.getProperty("url"));
    cpds.setDriverClass(DbConnection.dbProperties.getProperty("driver"));
    cpds.setUser(DbConnection.dbProperties.getProperty("username"));
    cpds.setPassword(DbConnection.dbProperties.getProperty("password"));
  }

  /**
   * Returns a connection to the db, if it is not already available a new one is created
   *
   * @return a connection to the db
   * @throws SQLException
   */
  public static synchronized Connection getConnection() throws SQLException {
    return cpds.getConnection();
  }

  /**
   * Load the db properties
   *
   * @throws IOException
   */
  private static void loadDbProperties() throws IOException {
    InputStream fileProperties = new FileInputStream("database.properties");
    DbConnection.dbProperties = new Properties();
    DbConnection.dbProperties.load(fileProperties);
  }

  /**
   * Method that closes the connection pool
   */
  public void closePool() {
    cpds.close();
  }
}