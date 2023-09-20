package DB;

import Bean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The DbAccess class takes care of managing the connections to the db to allow logins.
 *
 * @author Antonio Leone
 * @version 1.0
 */

public class DbAccess {

  private Connection connection;

  public DbAccess() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException e) {
      throw new DbException("Error: Connection failed");
    }
  }

  /**
   * Method that inserts an access inside the db
   *
   * @param a
   *     Object of type Access
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean enterAccess(Access a) throws DbException {
    int ret = 0;
    PreparedStatement statement = null;
    String login = a.getLogin();
    String password = a.getPassword();
    String type = a.getType();
    try {
      statement = connection.prepareStatement("INSERT INTO access VALUES (? ,? ,?)");
      statement.setString(1, login);
      statement.setString(2, password);
      statement.setString(3, type);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: Access failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: Access failed");
      }
    }
  }

  /**
   * Method that deletetes an access from the db
   *
   * @param log
   *     String that is used as login
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean deleteAccess(String log) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("DELETE FROM access WHERE login =?");
      statement.setString(1, log);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: delete access failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: delete access failed");
      }
    }
  }

  /**
   * Method that returns an access
   *
   * @param log
   *     String that is used as login
   * @return Returns an object of type Access
   * @throws DbException
   */

  public Access getAccess(String log) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Access ret = null;
    try {
      statement = connection.prepareStatement("SELECT* FROM access WHERE login=?");
      statement.setString(1, log);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      String login = rs.getString("login");
      String password = rs.getString("password");
      String type = rs.getString("type");
      ret = new Access(login, password, type);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search access failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search access failed");
      }
    }
  }

  /**
   * Method that returns all stored logins
   *
   * @return Returns an Access Collection
   * @throws DbException
   */

  public Collection<Access> getAccesses() throws DbException {
    ArrayList<Access> ret = new ArrayList<Access>();
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.createStatement();
      rs = statement.executeQuery("SELECT * FROM access");
      while (rs.next()) {
        String login = rs.getString("login");
        String password = rs.getString("password");
        String type = rs.getString("type");
        ret.add(new Access(login, password, type));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search accesses failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search accesses failed");
      }
    }
  }

  /**
   * Method that allows you to check the existence of the login
   *
   * @param login
   *     String that is used as login
   * @return True if login is present, False otherwise
   * @throws DbException
   */

  public boolean checkLogin(String login) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    boolean ret = false;
    try {
      statement = connection.prepareStatement("SELECT * FROM access WHERE login = ?");
      statement.setString(1, login);
      rs = statement.executeQuery();
      ret = rs.next();
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: login check failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: login check failed");
      }
    }
  }

  /**
   * Method that allows you to check the correctness of the login and the password of an access to
   * guarantee the opening of an authenticated session
   *
   * @param login
   *     String that is used as login
   * @param password
   *     String that is used as a password
   * @return True if access is present, False otherwise
   * @throws DbException
   */

  public boolean checkAccess(String login, String password) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    boolean ret = false;
    try {
      statement = connection.prepareStatement(
          "SELECT * FROM access WHERE login = ? and password = ?");
      statement.setString(1, login);
      statement.setString(2, password);
      rs = statement.executeQuery();
      ret = rs.next();
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: Login verification failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: Login verification failed");
      }
    }
  }

  /**
   * Method that modify an access
   *
   * @param log
   *     the login that identifies an access
   * @param to
   *     Access with updated data
   * @return True if the database has been modified, False otherwise
   */

  public boolean editAccess(String log, Access a) {
    String login = a.getLogin();
    String password = a.getPassword();
    String type = a.getType();
    int ret = 0;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(
          "UPDATE access SET login = ?,password = ?, type = ? WHERE login = ?");
      statement.setString(1, login);
      statement.setString(2, password);
      statement.setString(3, type);
      statement.setString(4, log);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: modify access failed ");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: modify access failed ");
      }
    }
  }
}