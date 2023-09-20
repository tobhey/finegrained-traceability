package DB;

import java.sql.*;
import java.util.*;

import Bean.Citizen;

/**
 * Class that deals with managing connections with the database and to shield the servets with the
 * DBMS.
 *
 * @author Michelangelo Cianciulli
 */

public class DbCitizen {
  private Connection connection;

  public DbCitizen() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException exc) {
      throw new DbException("Error: connection failed");
    }
  }

  /**
   * Method that allows you to register a new citizen. (db update)
   *
   * @param city
   *     is the citizen instance
   * @return the id of the citizen entered.
   * @throws DbException
   */
  public int registraCitizen(Citizen city) throws DbException {
    PreparedStatement stmt = null;
    int a;
    String login = city.getLogin();
    String surname = city.getSurname();
    String name = city.getName();
    java.util.Date d = city.getDateOfBirth();
    String placeN = city.getPlaceOfBirth();
    String codF = city.getFiscalCode();
    String email = city.getEmail();
    boolean adv = city.isAdvertise();
    int nuclF = city.getFamily();
    if (adv == true)
      a = 1;
    else
      a = 0;
    try {

      stmt = connection.prepareStatement("INSERT INTO citizen VALUES (?,?,?,?,?,?,?,?,?,?)");
      stmt.setInt(1, 0);
      stmt.setString(2, codF);
      stmt.setString(3, surname);
      stmt.setString(4, name);
      stmt.setDate(5, new java.sql.Date(d.getYear() - 1900, d.getMonth() - 1, d.getDate()));
      stmt.setString(6, placeN);
      stmt.setString(7, email);
      stmt.setInt(8, a);
      if (nuclF != 0)
        stmt.setInt(9, nuclF);
      else
        stmt.setNull(9, java.sql.Types.NULL);
      stmt.setString(10, login);
      stmt.executeUpdate();
      return maxID();
    } catch (SQLException exc) {
      throw new DbException("Error inserting new citizen");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error inserting new citizen");
      }
    }
  }

  /**
   * Method that allows the modification of the name of a specific citizen. (db update)
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @param newname
   *     the new name to be assigned to the citizen
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyNameCitizen(int idCitt, String newname) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("UPDATE citizen SET name = ? WHERE idCitizen = ?");
      stmt.setInt(2, idCitt);
      stmt.setString(1, newname);
      return (stmt.executeUpdate() == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify citizen name ");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify citizen name ");
      }
    }
  }

  /**
   * Method that allows the modification of the surname of a specific citizen. (db update)
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @param newsurname
   *     is the new surname to assign to the citizen
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifySurnameCitizen(int idCitt, String newsurname) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("UPDATE citizen SET surname = ? WHERE idCitizen = ?");
      stmt.setInt(2, idCitt);
      stmt.setString(1, newsurname);
      return (stmt.executeUpdate() == 1);
    } catch (SQLException exc) {
      throw new DbException("Modify citizen surname error");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Modify citizen surname error");
      }
    }
  }

  /**
   * Method that allows the modification of the tax code of a specific citizen. (db update)
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @param newcf
   *     is the new tax code to be assigned to the citizen
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyFiscalCodeCitizen(int idCitt, String newcf) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement(
          "UPDATE citizen SET codicefiscale = ? WHERE idCitizen = ?");
      stmt.setInt(2, idCitt);
      stmt.setString(1, newcf);
      return (stmt.executeUpdate() == 1);
    } catch (SQLException exc) {
      throw new DbException("Modify citizen fiscal code error");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Modify citizen fiscal code error");
      }
    }
  }

  /**
   * Method that allows the modification of the e-mail address of a specific citizen. (db update)
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @param newmail
   *     is the new email to be assigned to citizen
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyEmailCitizen(int idCitt, String newmail) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("UPDATE citizen SET eMail = ? WHERE idCitizen = ?");
      stmt.setInt(2, idCitt);
      stmt.setString(1, newmail);
      return (stmt.executeUpdate() == 1);
    } catch (SQLException exc) {
      throw new DbException("Modify citizen mail error");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Modify citizen mail error");
      }
    }
  }

  /**
   * Method that allows the modify of the advertise field of a specific citizen. (db update)
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyAdvertise(int idCitt) throws DbException {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int ret;
    try {
      stmt = connection.prepareStatement("SELECT advertise FROM citizen WHERE idCitizen = ?");
      Statement statement = connection.createStatement();
      stmt.setInt(1, idCitt);
      rs = stmt.executeQuery();
      rs.next();
      int adv = rs.getInt("advertise");
      if (adv == 0)
        ret = statement.executeUpdate(
            "UPDATE citizen SET advertise = 1 WHERE idCitizen = " + idCitt);
      else
        ret = statement.executeUpdate(
            "UPDATE citizen SET advertise = 0 WHERE idCitizen =" + idCitt);

      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Modify citizen advertise error");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Modify citizen advertise error");
      }
    }
  }

  /**
   * Method that allows the deletion of a citizen. (db update)
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean deleteCitizen(int idCitt) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("DELETE FROM citizen WHERE idCitizen = ?");
      stmt.setInt(1, idCitt);
      return (stmt.executeUpdate() == 1);
    } catch (SQLException exc) {
      throw new DbException("Delete citizen error");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Delete citizen error");
      }
    }
  }

  /**
   * Method that allows the search of a citizen by his id.
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @return object of type citizen with id equal to the one passed as parameter
   * @throws DbException
   */

  public Citizen getCitizenByCodice(int idCitt) throws DbException {
    Citizen res = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = connection.prepareStatement("SELECT * FROM citizen WHERE idCitizen = ?");
      stmt.setInt(1, idCitt);
      rs = stmt.executeQuery();
      if (rs.next() == true) {
        boolean adv;
        int idCitizen = rs.getInt("idCitizen");
        String fiscalCode = rs.getString("fiscalCode");
        String surname = rs.getString("surname");
        String name = rs.getString("name");
        java.util.Date dateOfBirth = rs.getDate("dateOfBirth");
        String placeN = rs.getString("placeOfBirth");
        String eMail = rs.getString("eMail");
        int advertise = rs.getInt("advertise");
        int family = rs.getInt("family");
        String login = rs.getString("login");
        if (advertise == 1)
          adv = true;
        else
          adv = false;
        res = new Citizen(idCitizen, fiscalCode, surname, name, dateOfBirth, placeN, eMail, adv,
            family, login);
      }
      return res;
    } catch (SQLException exc) {
      throw new DbException("Search citizen by code error");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Search citizen by code error");
      }
    }
  }

  /**
   * Method that allows the search of a set of cityadins based on their name and surname.
   *
   * @param name
   *     parameter on which to search
   * @param surname
   *     parameter on which to search
   * @return a collection of cityadins with the name and surname passed as a parameter
   * @throws DbException
   */
  public Collection<Citizen> getCitizenByName(String name, String surname) throws DbException {

    Collection<Citizen> res = new ArrayList<Citizen>();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean adv;
    try {
      stmt = connection.prepareStatement("SELECT * FROM citizen WHERE name = ? AND surname = ?");
      stmt.setString(1, name);
      stmt.setString(2, surname);
      rs = stmt.executeQuery();
      while (rs.next()) {
        int idCitizen = rs.getInt("idCitizen");
        String fiscalCode = rs.getString("fiscalCode");
        String surname = rs.getString("surname");
        String name = rs.getString("name");
        java.util.Date dateOfBirth = rs.getDate("dateOfBirth");
        String placeN = rs.getString("placeOfBirth");
        String eMail = rs.getString("eMail");
        int advertise = rs.getInt("advertise");
        int family = rs.getInt("family");
        String login = rs.getString("login");
        if (advertise == 1)
          adv = true;
        else
          adv = false;
        res.add(new Citizen(idCitizen, fiscalCode, surname, name, dateOfBirth, placeN, eMail, adv,
            family, login));
      }
      return res;
    } catch (SQLException exc) {
      throw new DbException("Error search citizens by name and surname");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error search citizens by name and surname");
      }
    }
  }

  /**
   * Method that allows modifying the login for a specific citizen. (db update)
   *
   * @param idC
   *     is the id of the citizen
   * @param newLogin
   *     is citizen's new login
   * @return true if the operation was successful
   */
  public boolean modifyLogin(int idC, String newLogin) throws DbException {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = connection.prepareStatement("UPDATE citizen SET login = ? WHERE idCitizen = ?");
      stmt.setString(1, newLogin);
      stmt.setInt(2, idC);
      //rs = stmt.executeQuery();
			/*if(rs.next())
				{
				String l = rs.getString("login");
				stmt = connection.prepareStatement("UPDATE access SET login = ? WHERE login = ?");
				stmt.setString(1, newLogin);
				stmt.setString(2, l);*/
      return (stmt.executeUpdate() == 1);
      //}
      //return false;
    } catch (SQLException exc) {
      throw new DbException("Modify login error");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Modify login error");
      }
    }
  }

  /**
   * Method that allows the search of a citizen through his login.
   *
   * @param log
   *     is the login on which you want to search
   * @return the type citizen object
   * @throws DbException
   */
  public Citizen getCitizenByLogin(String log) throws DbException {
    Citizen res = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = connection.prepareStatement("SELECT * FROM citizen WHERE login = ?");
      stmt.setString(1, log);
      rs = stmt.executeQuery();
      if (rs.next() == true) {
        boolean adv;
        int idCitizen = rs.getInt("idCitizen");
        String fiscalCode = rs.getString("fiscalCode");
        String surname = rs.getString("surname");
        String name = rs.getString("name");
        java.util.Date dateOfBirth = rs.getDate("dateOfBirth");
        String placeN = rs.getString("placeOfBirth");
        String eMail = rs.getString("eMail");
        int advertise = rs.getInt("advertise");
        int family = rs.getInt("family");
        String login = rs.getString("login");
        if (advertise == 1)
          adv = true;
        else
          adv = false;
        res = new Citizen(idCitizen, fiscalCode, surname, name, dateOfBirth, placeN, eMail, adv,
            family, login);
      }
      return res;
    } catch (SQLException exc) {
      throw new DbException("Search citizen by login error");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Search citizen by login error");
      }
    }
  }

  /**
   * Private method that returns the id of the last citizen entered.
   *
   * @return the highest id of the citizen table
   * @throws DbException
   */
  private int maxID() throws DbException {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = connection.createStatement();
      rs = stmt.executeQuery("SELECT max(idCitizen) as m FROM citizen;");
      if (rs.next())
        return rs.getInt("m");
      throw new DbException("MaxID execution error");
    } catch (SQLException exc) {
      throw new DbException("MaxID execution error");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("MaxID execution error");
      }
    }
  }

  /**
   * Method that allows you to know the citizen's ID starting from his personal data (tax code,
   * surname, name)
   *
   * @param cf
   *     is the fiscal code parameter for the search
   * @param surname
   *     is the surname parameter for the search
   * @param name
   *     is the name parameter for the search
   * @return the id of the citizen if ok, -1 if the search is not successful
   */
  public int findIdCitizen(String cf, String surname, String name) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = connection.prepareStatement(
          "SELECT idCitizen AS id FROM citizen WHERE fiscalCode = ? AND name = ? AND surname = ?");
      stmt.setString(1, cf);
      stmt.setString(2, name);
      stmt.setString(3, surname);
      rs = stmt.executeQuery();
      if (rs.next())
        return rs.getInt("id");
      return -1;
    } catch (SQLException exc) {
      throw new DbException("Citizen search id error via cf / name / surname");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Citizen search id error via cf / name / surname");
      }
    }
  }

  /**
   * Method that allows the search of the citizen's tax code starting from his ID
   *
   * @param id
   *     is the id of the citizen
   * @return the citizen's tax code
   */
  public String findFiscalCode(int id) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String res = null;
    try {
      stmt = connection.prepareStatement(
          "SELECT fiscalCode as cf FROM citizen WHERE idCitizen = ?");
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      if (rs.next())
        res = rs.getString("cf");
      return res;
    } catch (SQLException exc) {
      throw new DbException("Fiscal code search error by ID");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Fiscal code search error by ID");
      }
    }
  }

  /**
   * Method that modifies the citizen's household given his id
   *
   * @param idCitt
   *     is the id of the citizen
   * @param newnucleo
   *     is the citizen's new household
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyFamilyCitizen(int idCitt, int newnucleo) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("UPDATE citizen SET family = ? WHERE idCitizen = ?");
      stmt.setInt(2, idCitt);
      stmt.setInt(1, newnucleo);
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify household citizen");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify household citizen");
      }
    }
  }
}
