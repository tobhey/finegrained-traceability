package DB;

import Bean.*;

import java.sql.*;

import Bean.IdentityCard;

/**
 * Class that deals with managing connections with the database and to shield the servlets from the
 * DBMS.
 *
 * @author Michelangelo Cianciulli
 */

public class DbIdentityCard {
  private Connection connection;

  public DbIdentityCard() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException exc) {
      throw new DbException("Error: connection failed");
    }
  }

  /**
   * Method that allows the search of an identity card by its number.
   *
   * @param cod
   *     is the citizen's identity card number.
   * @return the object of type CartaIdentit associated with the number passed as a parameter
   * @throws DbException
   */
  public IdentityCard searchIdentityCardByNumber(String cod) throws DbException {
    IdentityCard res = null;
    ResultSet rs = null;
    PreparedStatement stmt = null;
    boolean e;
    try {
      stmt = connection.prepareStatement("SELECT * FROM identitycard WHERE number = ?");
      stmt.setString(1, cod);
      rs = stmt.executeQuery();
      if (rs.next() == true) {
        String num = rs.getString("number");
        int city = rs.getInt("citizen");
        String citizenship = rs.getString("citizenship");
        String residence = rs.getString("residence");
        String street = rs.getString("street");
        int numciv = rs.getInt("civicNumber ");
        String maritalStatus = rs.getString("maritalStatus");
        String profession = rs.getString("profession");
        double height = rs.getDouble("height");
        String hair = rs.getString("hair");
        String eyes = rs.getString("eyes");
        String particularSigns = rs.getString("particularSigns");
        java.util.Date releaseDate = rs.getDate("releaseDate");
        java.util.Date expiryDate = rs.getDate("expiryDate");
        int esp = rs.getInt("validExpatriation");
        if (esp == 1)
          e = true;
        else
          e = false;
        res = new IdentityCard(num, city, citizenship, residence, street, numciv, maritalStatus,
            profession, height, hair, eyes, particularSigns, releaseDate, expiryDate, e);
      }
      return res;
    } catch (SQLException exc) {
      throw new DbException("Error searching for an identity card by its number");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e1) {
        throw new DbException("Error searching for an identity card by its number");
      }
    }
  }

  /**
   * Method that allows the deletion of an identity card. (db update)
   *
   * @param cod
   *     is the code of the identity card that is intended to be deleterious
   * @return true if the operation was successful
   * @throws DbException
   */
  public boolean deleteIdentityCard(String cod) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("DELETE FROM identitycard WHERE number = ?");
      stmt.setString(1, cod);
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Identity card deletion error");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Identity card deletion error");
      }
    }
  }

  /**
   * Method that allows the registration of a new identity card in the database
   *
   * @param c
   *     is the identity card type object
   * @return true if the operation was successful
   * @throws DbException
   */
  public boolean registerIdentityCard(IdentityCard c) throws DbException {
    int esp;
    String num = c.getNumber();
    int city = c.id();
    String citizenship = c.getCitizenship();
    String residence = c.getResidence();
    String street = c.getStreet();
    int numCiv = c.getCivicNumber();
    String stateCiv = c.getMaritalStatus();
    String profession = c.getProfession();
    double height = c.getHeight();
    String hair = c.getHair();
    String eyes = c.getEyes();
    String particularSigns = c.getParticularSigns();
    java.util.Date releaseDate = c.getReleaseDate();
    java.util.Date expiryDate = c.getExpiryDate();
    if (c.isValidExpatriation())
      esp = 1;
    else
      esp = 0;

    PreparedStatement stmt = null;

    try {
      stmt = connection.prepareStatement(
          "INSERT INTO identitycard VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      stmt.setString(1, num);
      stmt.setInt(2, city);
      stmt.setString(3, citizenship);
      stmt.setString(4, residence);
      stmt.setString(5, street);
      stmt.setInt(6, numCiv);
      stmt.setString(7, stateCiv);
      stmt.setString(8, profession);
      stmt.setDouble(9, height);
      stmt.setString(10, hair);
      stmt.setString(11, eyes);
      stmt.setString(12, particularSigns);
      stmt.setDate(13, new java.sql.Date(releaseDate.getYear() - 1900, releaseDate.getMonth() - 1,
          releaseDate.getDate()));
      stmt.setDate(14, new java.sql.Date(expiryDate.getYear() - 1900, expiryDate.getMonth() - 1,
          expiryDate.getDate()));
      stmt.setInt(15, esp);

      return (stmt.executeUpdate() == 1);
    } catch (SQLException exc) {
      throw new DbException("ID card insertion error� ");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e1) {
        throw new DbException("Error searching for an identity card by its number");
      }
    }
  }

  /**
   * Method that allows the modification of the street in a specific identity card. (db update)
   *
   * @param cod
   *     is the number of the identity card
   * @param v
   *     is the new street to be registered in the identity card
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyStreetIdentityCard(String cod, String v) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("UPDATE identitycard SET street = ? WHERE number = ?");
      stmt.setString(1, v);
      stmt.setString(2, cod);
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify street ID card");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify street ID card");
      }
    }
  }

  /**
   * Method that allows the modification of the house number in a specific identity card. (db
   * update)
   *
   * @param cod
   *     is the number of the identity card
   * @param nc
   *     is the new house number
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyCivicNumberIdentityCard(String cod, int nc) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement(
          "UPDATE identitycard SET civicNumber  = ? WHERE number = ?");
      stmt.setInt(1, nc);
      stmt.setString(2, cod);
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify civic number identity card");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify civic number identity card");
      }
    }
  }

  /**
   * Method that allows the modification of the residence (street and number civio) in a specific
   * identity card. (update of the db) �
   *
   * @param street
   *     is the new street to be registered in the identity card
   * @param nc
   *     is the house number to be registered in the identity card
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyResidenceIdentityCard(String cod, String street, int nc) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement(
          "UPDATE identitycard SET street = ?, civicNumber  = ? WHERE number = ?");
      stmt.setString(1, street);
      stmt.setInt(2, nc);
      stmt.setString(3, cod);
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify residence identity card� ");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify residence identity card� ");
      }
    }
  }

  /**
   * Method that allows the modification of the date of issue of a specific identity card. (db
   * update)
   *
   * @param cod
   *     is the number of the identity card
   * @param gives
   *     the new date of issue of the identity card
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyReleaseDateIdentityCard(String cod, java.util.Date d) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement(
          "UPDATE identitycard SET releaseDate = ? WHERE number = ?");
      stmt.setString(2, cod);
      stmt.setDate(1, new java.sql.Date(d.getYear() - 1900, d.getMonth() - 1, d.getDate()));
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify identity card release date ");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify identity card release date ");
      }
    }
  }

  /**
   * Method that allows the modification of the expiration date for a specific identity card. (db
   * update)
   *
   * @param cod
   *     is the number of the identity card
   * @param gives
   *     the new expiration date
   * @return true if the operation is successful
   * @throws DbException
   */

  public boolean modifyExpiryDateIdentityCard(String cod, java.util.Date d) throws DbException {
    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement("UPDATE identitycard SET expiryDate = ? WHERE number = ?");
      stmt.setString(2, cod);
      stmt.setDate(1, new java.sql.Date(d.getYear() - 1900, d.getMonth() - 1, d.getDate()));
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify identity card expiration date");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e) {
        throw new DbException("Error modify identity card expiration date");
      }
    }
  }

  /**
   * Method that allows the modification of the validity for expatriation of a specific identity
   * card. (db update)
   *
   * @param cod
   *     is the number of the identity card
   * @param esp
   *     is the boolean value that indicates the validity for expatriation for the specific identity
   *     card
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean modifyValidExpatriation(String cod, boolean esp) throws DbException {
    PreparedStatement stmt = null;
    int e;
    if (esp == true)
      e = 1;
    else
      e = 0;
    try {
      stmt = connection.prepareStatement(
          "UPDATE identitycard SET validExpatriation = ? WHERE number = ?");
      stmt.setString(2, cod);
      stmt.setInt(1, e);
      int ret = stmt.executeUpdate();
      return (ret == 1);
    } catch (SQLException exc) {
      throw new DbException("Error modify expatriation identity card validity");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException e1) {
        throw new DbException("Error modify expatriation identity card validity");
      }
    }
  }

  /**
   * Method that allows the search of an identity card� starting from the id of the owner * @param
   * idC � the id of the citizen * @return the object of type IdentityCard of the specific citizen.
   *
   * @throws DbException
   */
  public IdentityCard searchIdentityCardByOwner(int idC) throws DbException {
    IdentityCard res = null;
    ResultSet rs = null;
    PreparedStatement stmt = null;
    boolean e;
    try {
      stmt = connection.prepareStatement("SELECT * FROM identitycard WHERE citizen = ?");
      stmt.setInt(1, idC);
      rs = stmt.executeQuery();
      if (rs.next() == true) {
        String num = rs.getString("number");
        int city = rs.getInt("citizen");
        String citizenship = rs.getString("citizenship");
        String residence = rs.getString("residence");
        String street = rs.getString("street");
        int numciv = rs.getInt("civicNumber ");
        String maritalStatus = rs.getString("maritalStatus");
        String profession = rs.getString("profession");
        double height = rs.getDouble("height");
        String hair = rs.getString("hair");
        String eyes = rs.getString("eyes");
        String particularSigns = rs.getString("particularSigns");
        java.util.Date releaseDate = rs.getDate("releaseDate");
        java.util.Date expiryDate = rs.getDate("expiryDate");
        int esp = rs.getInt("validExpatriation");
        if (esp == 1)
          e = true;
        else
          e = false;
        res = new IdentityCard(num, city, citizenship, residence, street, numciv, maritalStatus,
            profession, height, hair, eyes, particularSigns, releaseDate, expiryDate, e);
      }
      return res;
    } catch (SQLException exc) {
      throw new DbException("ID card search error via citizen id");
    } finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
      } catch (SQLException e1) {
        throw new DbException("ID card search error via citizen id");
      }
    }
  }

}
