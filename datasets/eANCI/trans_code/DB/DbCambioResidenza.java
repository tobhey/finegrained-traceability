package DB;

import Bean.*;

import java.sql.Connection;
import java.sql.SQLException;

import Bean.IdentityCard;

/**
 * Class that deals with managing connections with the database and to shield the servets with the
 * DBMS.
 *
 * @author Michelangelo Cianciulli
 */
public class DbCambioResidence {
  private Connection connection;

  public DbCambioResidence() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException exc) {
      throw new DbException("Error: connection failed");
    }
  }

  /**
   * Method that allows the deletion of the citizen's identity card in the moment in which it
   * changes residence to an external municipality (update of the db)
   *
   * @param cod
   *     is the identity card number of the person requesting the change of residence to an external
   *     municipality
   * @return true if the operation is successful
   * @throws DbException
   */
  public boolean changeResidence(String cod) throws DbException {
    return new DbIdentityCard().deleteIdentityCard(cod);
  }

  /**
   * Method that allows the updating of the residence saved in the identity card of the citizen who
   * has made a change of residence in the same municipality in which he currently resides. (update
   * of the db)
   *
   * @param cod
   *     is the number of the identity card
   * @param v
   *     is the new street where the citizen will reside
   * @param nc
   *     is the new house number of the citizen
   * @return the updated Identity Card object
   * @throws DbException
   */

  public IdentityCard changeResidenceIn(String cod, String v, int nc) throws DbException {
    if (new DbIdentityCard().modifyResidenceIdentityCard(cod, v, nc))
      return new DbIdentityCard().searchIdentityCardByNumber(cod);
    else
      return null;
  }
}