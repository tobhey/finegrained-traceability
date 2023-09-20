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
 * The DbFamily class takes care of managing the connections to the db
 *
 * @author Antonio Leone
 * @version 1.0
 */

public class DbFamily {

  private Connection connection;

  public DbFamily() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException e) {
      throw new DbException("Error: Connection failed");
    }
  }

  /**
   * Method that inserts a family object into the db
   *
   * @param nf
   *     Family unit type object
   * @return Returns the id of the household entered
   * @throws DbException
   */

  public int insertFamily(Family nf) throws DbException {
    PreparedStatement statement = null;
    int idC = nf.getCapoFamiglia();
    String note = nf.getNote();
    int nc = nf.getNComponenti();
    try {
      statement = connection.prepareStatement("INSERT INTO family VALUES (? ,? ,?,?)");
      statement.setInt(1, 0);
      statement.setInt(2, idC);
      statement.setString(3, note);
      statement.setInt(4, nc);
      statement.executeUpdate();
      return lastId();
    } catch (SQLException e) {
      throw new DbException("Error: household insertion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: household insertion failed");
      }
    }
  }

  /**
   * Method that delete a family from the db
   *
   * @param id
   *     the integer which is used as the id of the family
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean deleteFamily(int id) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("DELETE FROM family WHERE idFamily =?");
      statement.setInt(1, id);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: Household deletion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: Household deletion failed");
      }
    }
  }

  /**
   * Method that returns the members of a family status
   *
   * @param integer
   *     id which is used as the household id
   * @return Returns a collection of cityadins
   * @throws DbException
   */

  public Collection<Citizen> getFamilyStatus(int id) throws DbException {
    ArrayList<Citizen> ret = new ArrayList<Citizen>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    Family nf = getFamilyById(id);
    if (nf == null)
      return null;
    int nc = nf.getNComponenti();
    try {
      statement = connection.prepareStatement("SELECT * FROM familystatus WHERE family = ?");
      statement.setInt(1, id);
      rs = statement.executeQuery();
      for (int i = 0; i < nc; i++) {
        rs.next();
        String cc = rs.getString("fiscalCode");
        String surname = rs.getString("surname");
        String name = rs.getString("name");
        java.util.Date dateOfBirth = rs.getDate("dateOfBirth");
        String place = rs.getString("placeOfBirth");
        int family = rs.getInt("family");
        ret.add(new Citizen(family, cc, surname, name, dateOfBirth, place));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: family status search failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: family status search failed");
      }
    }
  }

  /**
   * Method that allows you to check the existence of a family unit
   *
   * @param id
   *     the integer which is used as the household id
   * @return True if the id is present, False otherwise
   * @throws DbException
   */

  public boolean checkFamilyId(int id) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    boolean ret = false;
    try {
      statement = connection.prepareStatement("SELECT * FROM family WHERE idFamily = ?");
      statement.setInt(1, id);
      rs = statement.executeQuery();
      ret = rs.next();
      return (ret);
    } catch (SQLException e) {
      throw new DbException("Error: Household check failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: Household check failed");
      }
    }
  }

  /**
   * Method that allows the search of a family unit to know any notes
   *
   * @param id
   *     the integer which is used as the household id
   * @return Returns the notes of the household
   * @throws DbException
   */

  public String familyNote(int id) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    String ret = "";
    try {
      statement = connection.prepareStatement("SELECT note FROM family WHERE idFamily = ?");
      statement.setInt(1, id);
      rs = statement.executeQuery();
      if (!rs.next())
        return null;
      ret = rs.getString("note");
      if (ret == null)
        return "";
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search note failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search note failed");
      }
    }
  }

  /**
   * Method that modifies a head of the family
   *
   * @param idF
   *     the integer which is used as the family id
   * @param idC
   *     the integer that is used as the id of the head of the family
   * @return True if the modify was successful, False otherwise
   * @throws DbException
   */

  public boolean setHeadOfFamily(int idF, int idC) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement(
          "UPDATE family SET headOfFamily = ? WHERE idFamily = ?");
      statement.setInt(1, idC);
      statement.setInt(2, idF);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: modify head of household failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: modify head of household failed");
      }
    }
  }

  /**
   * Method that returns a household
   *
   * @param id
   *     the integer that contains the household id
   * @return Returns an object of type Family
   * @throws DbException
   */

  public Family getFamilyById(int id) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Family ret = null;
    try {
      statement = connection.prepareStatement("SELECT* FROM family WHERE idFamily = ?");
      statement.setInt(1, id);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      int idN = rs.getInt("idFamily");
      int idC = rs.getInt("headOfFamily");
      String note = rs.getString("note");
      int nc = rs.getInt("nComponenti");
      ret = new Family(idN, idC, note, nc);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: household search by id failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: household search by id failed");
      }
    }
  }

  private int lastId() throws DbException {
    int ret = 0;
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.createStatement();
      rs = statement.executeQuery("SELECT max(idFamily)AS max FROM family");
      if (!rs.next())
        return ret;
      ret = rs.getInt(1);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search id of the last household failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search id of the last household failed");
      }
    }
  }

  /**
   * Method that modifies the number of family members
   *
   * @param idF
   *     the integer which is used as the family id
   * @param n
   *     the integer representing the new number of components
   * @return True if the modify was successful, False otherwise
   * @throws DbException
   */

  public boolean setNComponents(int idF, int n) {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement(
          "UPDATE family SET nComponenti = ? WHERE idFamily = ?");
      statement.setInt(1, n);
      statement.setInt(2, idF);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: modify number components failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: modify number components failed");
      }
    }
  }

}