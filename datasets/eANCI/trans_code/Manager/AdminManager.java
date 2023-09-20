package Manager;

import java.util.Collection;

import Bean.Administrator;
import DB.DbAdministrator;
import DB.DbException;

/**
 * The AdminManager class interacts with the database management classes The AdminManager class has
 * no dependencies
 *
 * @author Federico Cinque
 */
public class AdminManager {
  private DbAdministrator dbAdministrator;

  /**
   * Default constructor della classe AdminManager
   */
  public AdminManager() {
    dbAdministrator = new DbAdministrator();
  }

  /**
   * Method that modifies an administrator invoking the relative method of class db
   *
   * @param number
   *     the string that identifies the administrator
   * @param newAdmin
   *     Administrator with updated data
   * @return True if an entry has been made in the db, False otherwise
   */
  public boolean editAdmin(String number, Administrator newAdmin) throws DbException {
    return dbAdministrator.editAdministrator(number, newAdmin);
  }

  /**
   * Method that returns an administrator invoking the relative method of class db
   *
   * @param number
   *     string that is used as the administrator number
   * @return Returns an object of type Administrator
   * @throws DbException
   */
  public Administrator searchAdminByNumber(String number) throws DbException {
    return dbAdministrator.getAdministratorByNumber(number);
  }

  /**
   * Method that inserts an administrator into the database invoking the relative method of class
   * db
   *
   * @param newAdmin
   *     Object of type Administrator
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean enterAdmin(Administrator newAdmin) throws DbException {
    return dbAdministrator.enterAdministrator(newAdmin);
  }

  /**
   * Method that deletes an Administrator from the db invoking the relative method of class db
   *
   * @param number
   *     the integer which is used as number
   * @return True if a deletion has been made in the db, False otherwise
   * @throws DbException
   */
  public String deleteAdministrator(String number) throws DbException {
    Collection<Administrator> Administrators = dbAdministrator.getAdministrators();
    if (Administrators.size() > 1) {
      if (dbAdministrator.deleteAdministrator(number))
        return "ok";
      else
        return "error";
    } else
      return "unique";
  }

  /**
   * Method that returns an administrator invoking the relative method of class db
   *
   * @param login
   *     string that is used as the administrator login
   * @return Returns an object of type administrator
   * @throws DbException
   */
  public Administrator getAdministratorByLogin(String login) throws DbException {
    return dbAdministrator.getAdministratorByLogin(login);
  }

  /**
   * Method that returns a collection of administrators invoking the relative method of class db
   *
   * @param nameAmm
   *     string that is used as the administrator name
   * @param surnameAmm
   *     string that is used as the surname of the administrator
   * @return Returns a collection of Administrators
   * @throws DbException
   */
  public Collection<Administrator> getAdministratorByName(String nameAmm, String surnameAmm)
      throws DbException {
    return dbAdministrator.getAdministratorByName(nameAmm, surnameAmm);
  }

  /**
   * Method that returns all stored administrators
   *
   * @return Returns a collection of Administrators
   * @throws DbException
   */
  public Collection<Administrator> getAdministrators() throws DbException {
    return dbAdministrator.getAdministrators();
  }
}
