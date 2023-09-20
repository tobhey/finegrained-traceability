package Manager;

import java.util.Collection;

import Bean.Access;
import DB.DbAccess;
import DB.DbException;

/**
 * The AccessManager class interacts with the database management classes The AccessManager class
 * has no dependencies
 *
 * @author Federico Cinque
 */
public class AccessManager {
  private DbAccess dbAccess;

  /**
   * Default constructor of the AccessManager class
   */
  public AccessManager() {
    dbAccess = new DbAccess();
  }

  /**
   * Method that allows you to check the correctness of the login and the password of an access
   * invoking the relative method of the class db
   *
   * @param login
   *     String that is used as login
   * @param password
   *     String that is used as a password
   * @return True if access is present, False otherwise
   * @throws DbException
   */
  public boolean checkAccess(String login, String password) throws DbException {
    return dbAccess.checkAccess(login, password);
  }

  /**
   * Method that allows you to check the existence of the login invoking the relative method of
   * class db
   *
   * @param login
   *     String that is used as login
   * @return True if login is present, False otherwise
   * @throws DbException
   */
  public boolean checkLogin(String login) throws DbException {
    return dbAccess.checkLogin(login);
  }

  /**
   * Method that returns an access by invoking the relative method of the db class
   *
   * @param login
   *     String that is used as login
   * @return Returns an object of type Access
   * @throws DbException
   */
  public Access getAccess(String login) throws DbException {
    return dbAccess.getAccess(login);
  }

  public boolean editAccess(String login, Access newAccess) throws DbException {
    return dbAccess.editAccess(login, newAccess);
  }

  /**
   * Method that inserts an access inside the db invoking the relative method of class db
   *
   * @param ac
   *     Object of type Access
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean enterAccess(Access ac) throws DbException {
    return dbAccess.enterAccess(ac);
  }

  /**
   * Method that deletetes an access from the db by invoking the relative method of the db class
   *
   * @param login
   *     String that is used as login
   * @return True if a deletion has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean deleteAccess(String login) throws DbException {
    return dbAccess.deleteAccess(login);
  }

  /**
   * Method that returns all stored logins invoking the relative method of class db
   *
   * @return Returns a Collection of Logins
   * @throws DbException
   */
  public Collection<Access> getAccesses() throws DbException {
    return dbAccess.getAccesses();
  }
}
