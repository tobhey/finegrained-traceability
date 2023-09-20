package Manager;

import Bean.Family;
import DB.DbException;
import DB.DbFamily;

/**
 * The FamilyManager class interacts with the database management classes The FamilyManager class
 * has no dependencies
 *
 * @author Federico Cinque
 */
public class FamilyManager {

  private DbFamily coreF;

  /**
   * Default constructor of the FamilyManager class
   */
  public FamilyManager() {
    coreF = new DbFamily();
  }

  /**
   * Method that inserts a family object into the db invoking the relative method of class db
   *
   * @param nf
   *     Object of type family
   * @return Returns the id of the household entered
   * @throws DbException
   */
  public int insertNucleo(Family nf) {
    return coreF.insertFamily(nf);
  }

  /**
   * Method that allows you to check the existence of a family unit invoking the relative method of
   * class db
   *
   * @param id
   *     the integer which is used as the household id
   * @return True if the id is present, False otherwise
   * @throws DbException
   */
  public boolean checkFamilyId(int id) {
    return coreF.checkFamilyId(id);
  }

  /**
   * Method that returns the number of members of the household invoking the relative method of
   * class db
   *
   * @param id
   *     of the household whose number of members you want
   * @return True if the id is present, False otherwise
   * @throws DbException
   */
  public int getNCoreComponents(int id) {
    return coreF.getFamilyById(id).getNComponents();
  }

  /**
   * Method that modifies a head of the family invoking the relative method of class db
   *
   * @param id
   *     the integer which is used as the family id
   * @param IdCitt
   *     the integer that is used as the id of the head of the family
   * @return True if the modify was successful, False otherwise
   * @throws DbException
   */
  public boolean setHeadOfFamily(int id, int IdCitt) {
    return coreF.setHeadOfFamily(id, IdCitt);
  }

  /**
   * Method that increases the number of family members by invoking a method of class db
   *
   * @param id
   *     of the household you want to modify
   * @return True if the modify was successful, False otherwise
   * @throws DbException
   */
  public boolean incrementComponents(int id) {
    int n = getNCoreComponents(id) + 1;
    return coreF.setNComponents(id, n);
  }

  /**
   * Method that decreases the number of family members by invoking a method of class db
   *
   * @param id
   *     of the household you want to modify
   * @return True if the modify was successful, False otherwise
   * @throws DbException
   */
  public boolean decrementComponents(int id) {
    int n = getNCoreComponents(id) - 1;
    return coreF.setNComponents(id, n);
  }

  public Family getNucleo(int id) {
    return coreF.getFamilyById(id);
  }
}
