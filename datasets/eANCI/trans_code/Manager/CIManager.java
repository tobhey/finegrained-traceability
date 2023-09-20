package Manager;

import Bean.IdentityCard;
import DB.*;

/**
 * The CIManager class interacts with the database management classes The CIManager class has no
 * dependencies
 *
 * @author Federico Cinque
 */
public class CIManager {
  private DbIdentityCard dbIdentityCard;

  /**
   * Default constructor of the CIManager class
   */
  public CIManager() {
    dbIdentityCard = new DbIdentityCard();
  }

  /**
   * Method that allows you to search for an identity card by its number invoking the relative
   * method of class db
   *
   * @param cod
   *     the number of the citizen's identity card.
   * @return the object of type IdentityCard associated with the number passed as a parameter
   * @throws DbException
   */
  public IdentityCard getCardByNumber(String cod) throws DbException {
    return dbIdentityCard.searchIdentityCardByNumber(cod);
  }

  public IdentityCard getCardByIdCStri(int id) throws DbException {
    return dbIdentityCard.searchIdentityCardByOwner(id);
  }

}
