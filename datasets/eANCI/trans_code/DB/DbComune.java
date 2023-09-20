package DB;

import Bean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DbMunicipality class takes care of managing the connections to the db
 *
 * @author Antonio Leone
 * @version 1.0
 */

public class DbMunicipality {

  private Connection connection;

  public DbMunicipality() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException e) {
      throw new DbException("Error: Connection failed");
    }
  }

  /**
   * Method that inserts a municipality within the db
   *
   * @param c
   *     object of type Municipality
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean insertMunicipality(Municipality c) throws DbException {
    int ret = 0;
    PreparedStatement statement = null;
    String name = c.getName();
    String streetAddress = c.getIndirizzoId();
    try {
      statement = connection.prepareStatement("INSERT INTO municipality VALUES (?, ?)");
      statement.setString(1, name);
      statement.setString(2, streetAddress);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: Common entry failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: Common entry failed");
      }
    }
  }

  /**
   * Method that deletetes a municipality from the database
   *
   * @param name
   *     that identifies the municipality
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean deleteMunicipality(String name) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("DELETE FROM municipality WHERE name = ?");
      statement.setString(1, name);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: Common delete failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: Common delete failed");
      }
    }
  }

  /**
   * Method that returns a common
   *
   * @param name
   *     the string representing the name of the municipality
   * @return Returns an object of type Municipality
   * @throws DbException
   */

  public Municipality getMunicipalityByName(String name) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Municipality ret = null;
    try {
      statement = connection.prepareStatement("SELECT* FROM municipality WHERE name = ?");
      statement.setString(1, name);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      String nameC = rs.getString("name");
      String streetAddress = rs.getString("IPAddress");
      ret = new Municipality(nameC, streetAddress);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: Search of municipality by name failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: Search of municipality by name failed");
      }
    }
  }
}