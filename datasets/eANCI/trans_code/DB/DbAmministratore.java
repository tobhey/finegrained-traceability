package DB;

import Bean.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The DbAdministrator class takes care of managing the connections to the db
 *
 * @author Antonio Leone
 * @version 1.0
 */

public class DbAdministrator {

  private Connection connection;

  public DbAdministrator() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException e) {
      throw new DbException("Error: connection failed");
    }
  }

  /**
   * Method that inserts an administrator into the database
   *
   * @param i
   *     Object of type Administrator
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean enterAdministrator(Administrator a) throws DbException {
    String matr = a.getNumber();
    String name = a.getName();
    String surname = a.getSurname();
    String email = a.getEmail();
    String login = a.getLogin();
    int ret = 0;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("INSERT INTO administrator VALUES (? ,? ,?,?,?)");
      statement.setString(1, matr);
      statement.setString(2, name);
      statement.setString(3, surname);
      statement.setString(4, email);
      statement.setString(5, login);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: administrator insertion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: administrator insertion failed");
      }
    }
  }

  /**
   * Method that deletes an Administrator from the db
   *
   * @param matr
   *     the integer which is used as number
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean deleteAdministrator(String matr) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("DELETE FROM administrator WHERE number =?");
      statement.setString(1, matr);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: administrator deletion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: administrator deletion failed");
      }
    }
  }

  /**
   * Method that returns a collection of administrators
   *
   * @param nameImp
   *     string that is used as the administrator name
   * @param surnameImp
   *     string that is used as the administrator surname
   * @return Returns a collection of Administrators
   * @throws DbException
   */

  public Collection<Administrator> getAdministratorByName(String nameAmm, String surnameAmm)
      throws DbException {
    ArrayList<Administrator> ret = new ArrayList<Administrator>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.prepareStatement(
          "SELECT * FROM administrator WHERE name =? and surname =?");
      statement.setString(1, nameAmm);
      statement.setString(2, surnameAmm);
      rs = statement.executeQuery();
      while (rs.next()) {
        String matr = rs.getString("number");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String eMail = rs.getString("eMail");
        String login = rs.getString("login");
        ret.add(new Administrator(name, surname, matr, eMail, login));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search administrator via name and surname failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search administrator via name and surname failed");
      }
    }
  }

  /**
   * Method that returns an administrator
   *
   * @param matrImp
   *     string that is used as the administrator number
   * @return Returns an object of type Administrator
   * @throws DbException
   */

  public Administrator getAdministratorByNumber(String matrAmm) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Administrator ret = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM administrator WHERE number =?");
      statement.setString(1, matrAmm);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      String matr = rs.getString("number");
      String name = rs.getString("name");
      String surname = rs.getString("surname");
      String eMail = rs.getString("eMail");
      String login = rs.getString("login");
      ret = new Administrator(name, surname, matr, eMail, login);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search administrator via number failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search administrator via number failed");
      }
    }
  }

  /**
   * Method that returns all stored administrators
   *
   * @return Returns a collection of Administrators
   * @throws DbException
   */

  public Collection<Administrator> getAdministrators() throws DbException {
    ArrayList<Administrator> ret = new ArrayList<Administrator>();
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.createStatement();
      rs = statement.executeQuery("SELECT * FROM administrator");
      while (rs.next()) {
        String matr = rs.getString("number");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String eMail = rs.getString("eMail");
        String login = rs.getString("login");
        ret.add(new Administrator(name, surname, matr, eMail, login));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search administrators failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search administrators failed");
      }
    }
  }

  /**
   * Method that returns an administrator
   *
   * @param log
   *     string that is used as the administrator login
   * @return Returns an object of type administrator
   * @throws DbException
   */

  public Administrator getAdministratorByLogin(String log) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Administrator ret = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM administrator WHERE login =?");
      statement.setString(1, log);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      String matr = rs.getString("number");
      String name = rs.getString("name");
      String surname = rs.getString("surname");
      String eMail = rs.getString("eMail");
      String login = rs.getString("login");
      ret = new Administrator(name, surname, matr, eMail, login);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search administrator via login failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search administrator via login failed");
      }
    }
  }

  /**
   * Method that modify an administrator
   *
   * @param matr
   *     the string that identifies the administrator
   * @param to
   *     Administrator with updated data
   * @return True if a modify has been made on the db, False otherwise
   */

  public boolean editAdministrator(String matr, Administrator a) {
    String number = a.getNumber();
    String name = a.getName();
    String surname = a.getSurname();
    String email = a.getEmail();
    String login = a.getLogin();
    int ret = 0;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(
          "UPDATE administrator SET number = ?,name = ?, surname = ?, email = ?, login = ? WHERE number = ?");
      statement.setString(1, number);
      statement.setString(2, name);
      statement.setString(3, surname);
      statement.setString(4, email);
      statement.setString(5, login);
      statement.setString(6, matr);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: modify administrator failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: modify administrator failed");
      }
    }
  }
}