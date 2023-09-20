package Manager;

import java.util.Collection;

import Bean.Citizen;
import DB.DbCitizen;
import DB.DbException;

/**
 * The CitizenManager class interacts with the database management classes The CitizenManager class
 * has no dependencies
 *
 * @author Federico Cinque
 */
public class CitizenManager {

  private DbCitizen dbCitizen;

  /**
   * Default constructor of the CIManager class
   */
  public CitizenManager() {
    dbCitizen = new DbCitizen();
  }

  /**
   * Method that allows the search of a citizen through his login invoking the relative method of
   * class db
   *
   * @param login
   *     is the login on which you want to search
   * @return the citizen type object
   * @throws DbException
   */
  public Citizen getCitizenByLogin(String login) throws DbException {
    return dbCitizen.getCitizenByLogin(login);
  }

  /**
   * Method that allows modifying the login for a specific citizen invoking the relative method of
   * class db
   *
   * @param idCitt
   *     is the citizen's id
   * @param newLogin
   *     is the citizen's new login
   * @return true if the operation was successful, flase otherwise
   */
  public boolean modifyLogin(int idCitt, String newLogin) throws DbException {
    return dbCitizen.modifyLogin(idCitt, newLogin);
  }

  /**
   * Method that allows the modification of the e-mail address of a specific citizen invoking the
   * relative method of class db
   *
   * @param idCitizen
   *     is the identifier of the citizen
   * @param email
   *     is the new email to be assigned to the citizen
   * @return true if successful, flase otherwise
   * @throws DbException
   */
  public boolean modifyEmail(int idCitizen, String email) throws DbException {
    return dbCitizen.modifyEmailCitizen(idCitizen, email);
  }

  /**
   * Method that allows you to insert a new citizen invoking the relative method of class db
   *
   * @param citizen
   *     is the citizen instance
   * @return the id of the citizen entered.
   * @throws DbException
   */
  public int insertCitizen(Citizen citizen) throws DbException {
    return dbCitizen.registraCitizen(citizen);
  }

  /**
   * Method that allows the search of a set of citizens based on their name and surname invoking the
   * relative method of class db
   *
   * @param name
   *     parameter on which to search
   * @param surname
   *     parameter to search on
   * @return a collection of citizens with the name and surname passed as a parameter
   * @throws DbException
   */
  public Collection<Citizen> searchCitizen(String name, String surname) throws DbException {
    return dbCitizen.getCitizenByName(name, surname);
  }

  /**
   * Method that allows the deletion of a citizen invoking the relative method of class db
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @return true if successful, flase otherwise
   * @throws DbException
   */
  public boolean deleteCitizen(int idCitt) throws DbException {
    return dbCitizen.deleteCitizen(idCitt);
  }

  /**
   * Method that allows the search of a citizen through his id invoking the relative method of class
   * db
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @return object of citizen type with id equal to the one passed as parameter
   * @throws DbException
   */
  public Citizen getCitizenById(int idCitt) throws DbException {
    return dbCitizen.getCitizenByCodice(idCitt);
  }

  /**
   * Method that modifies the citizen's family unit given his id invoking the relative method of
   * class db
   *
   * @param idCitt
   *     is the citizen's id
   * @param newid
   *     is the id of the citizen's new household
   * @return true if successful, flase otherwise
   * @throws DbException
   */
  public boolean modifyFamily(int idCitt, int newid) throws DbException {
    return dbCitizen.modifyFamilyCitizen(idCitt, newid);
  }

  /**
   * Method that allows the modification of the name of a specific citizen invoking the relative
   * method of class db
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @param name
   *     is the new name to be assigned to the citizen
   * @return true if successful, flase otherwise
   * @throws DbException
   */
  public boolean modifyName(int idCitt, String name) throws DbException {
    return dbCitizen.modifyNameCitizen(idCitt, name);
  }

  /**
   * Method that allows the modification of the surname of a specific citizen invoking the relative
   * method of class db
   *
   * @param idCitt
   *     is the identifier of the citizen
   * @param surname
   *     is the new surname to assign to the citizen
   * @return true if successful, flase otherwise
   * @throws DbException
   */
  public boolean modifySurname(int idCitt, String surname) throws DbException {
    return dbCitizen.modifySurnameCitizen(idCitt, surname);
  }
}
