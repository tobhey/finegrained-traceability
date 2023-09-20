package DB;

import Bean.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * La classe DbEmployee si occupa di gestire le connessioni al db
 *
 * @author Antonio Leone
 * @version 1.0
 */

public class DbEmployee {

  private Connection connection;

  public DbEmployee() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException e) {
      throw new DbException("Error: connection failed");
    }
  }

  /**
   * Method that inserts an employee inside the db
   *
   * @param i
   *     Object of type Employee
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean insertEmployee(Employee i) throws DbException {
    String matr = i.getNumber();
    String name = i.getName();
    String surname = i.getSurname();
    String email = i.getEmail();
    String login = i.getLogin();
    int ret = 0;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("INSERT INTO employee VALUES (? ,? ,?,?,?)");
      statement.setString(1, matr);
      statement.setString(2, name);
      statement.setString(3, surname);
      statement.setString(4, email);
      statement.setString(5, login);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: employee insertion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: employee insertion failed");
      }
    }
  }

  /**
   * Method that deletes an employee from the db
   *
   * @param matr
   *     the string that is used as number
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean deleteEmployee(String matr) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("DELETE FROM employee WHERE number =?");
      statement.setString(1, matr);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: employee deletion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: employee deletion failed");
      }
    }
  }

  /**
   * Method that returns a collection of employees
   *
   * @param nameImp
   *     string that is used as the employee name
   * @param surnameImp
   *     string that is used as the employee's surname
   * @return Returns a Collection of Employees
   * @throws DbException
   */
  public Collection<Employee> getEmployeeByName(String nameImp, String surnameImp)
      throws DbException {
    ArrayList<Employee> ret = new ArrayList<Employee>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.prepareStatement(
          "SELECT * FROM employee WHERE name =? and surname =?");
      statement.setString(1, nameImp);
      statement.setString(2, surnameImp);
      rs = statement.executeQuery();
      while (rs.next()) {
        String matr = rs.getString("number");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String eMail = rs.getString("eMail");
        String login = rs.getString("login");
        ret.add(new Employee(name, surname, matr, eMail, login));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search employee via name and surname failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search employee via name and surname failed");
      }
    }
  }

  /**
   * Method that returns an employee
   *
   * @param matrImp
   *     string that is used as the employee number
   * @return Returns an object of type Employee
   * @throws DbException
   */

  public Employee getEmployeeByNumber(String matrImp) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Employee ret = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM employee WHERE number =?");
      statement.setString(1, matrImp);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      String matr = rs.getString("number");
      String name = rs.getString("name");
      String surname = rs.getString("surname");
      String eMail = rs.getString("eMail");
      String login = rs.getString("login");
      ret = new Employee(name, surname, matr, eMail, login);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search employee by number failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search employee by number failed");
      }
    }
  }

  /**
   * Method that returns all stored employees
   *
   * @return Returns a collection of employees
   * @throws DbException
   */

  public Collection<Employee> getEmployees() throws DbException {
    ArrayList<Employee> ret = new ArrayList<Employee>();
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.createStatement();
      rs = statement.executeQuery("SELECT * FROM employee");
      while (rs.next()) {
        String matr = rs.getString("number");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String eMail = rs.getString("eMail");
        String login = rs.getString("login");
        ret.add(new Employee(name, surname, matr, eMail, login));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: Employee search failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: Employee search failed");
      }
    }
  }

  /**
   * Method that returns an employee
   *
   * @param log
   *     string that is used as the employee login
   * @return Returns an object of type employee
   * @throws DbException
   */

  public Employee getEmployeeByLogin(String log) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Employee ret = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM employee WHERE login =?");
      statement.setString(1, log);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      String matr = rs.getString("number");
      String name = rs.getString("name");
      String surname = rs.getString("surname");
      String eMail = rs.getString("eMail");
      String login = rs.getString("login");
      ret = new Employee(name, surname, matr, eMail, login);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search employee via login failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search employee via login failed");
      }
    }
  }

  /**
   * Method that modify an employee
   *
   * @param matr
   *     the string that identifies the employee
   * @param to
   *     employee with updated data
   * @return True if a modify has been made on the db, False otherwise
   */

  public boolean modifyEmployee(String matr, Employee a) {
    String number = a.getNumber();
    String name = a.getName();
    String surname = a.getSurname();
    String email = a.getEmail();
    String login = a.getLogin();
    int ret = 0;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(
          "UPDATE employee SET number = ?,name = ?, surname = ?, email = ?, login = ? WHERE number = ?");
      statement.setString(1, number);
      statement.setString(2, name);
      statement.setString(3, surname);
      statement.setString(4, email);
      statement.setString(5, login);
      statement.setString(6, matr);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: modify employee failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: modify employee failed");
      }
    }
  }
}