package Bean;

/**
 * This class is in charge of managing the citizen's family state
 *
 * @author Christian Ronca
 */
public class Family {
  private int idFamily;
  private int headOfFamily;
  private String note;
  private int nComponents;

  /**
   * Standard manufacturer
   */
  public Family() {

  }

  /**
   * @param idFamily
   *     contains the family group id
   * @param headOfFamily
   *     of household contains the id of the head of household
   * @param notes
   *     any notes
   */
  public Family(int idFamily, int headOfFamily, String notes, int nc) {
    this.idFamily = idFamily;
    this.headOfFamily = headOfFamily;
    this.note = notes;
    this.nComponents = nc;
  }

  /**
   * Returns the id of the family state
   *
   * @return a string containing the family state id
   */
  public int getIdFamily() {
    return idFamily;
  }

  /**
   * Set the id of the family status
   *
   * @return a string that contains the new id of the family state
   */
  public int setIdFamily(int str) {
    idFamily = str;
    return idFamily;
  }

  /**
   * Returns the id of the householder
   *
   * @return a string containing the householder's id
   */
  public int getCapoFamiglia() {
    return headOfFamily;
  }

  /**
   * Set the id of the householder
   *
   * @return a string containing the new householder id
   */
  public int setHeadOfFamily(int str) {
    headOfFamily = str;
    return headOfFamily;
  }

  /**
   * Returns released notes
   *
   * @return a string that contains a previously released note
   */
  public String getNote() {
    return note;
  }

  /**
   * Insert a note
   *
   * @return a string that contains the released note
   */
  public String setNote(String str) {
    note = str;
    return note;
  }

  /**
   * Returns the number of family members
   *
   * @return an integer that contains the number of members of the household
   */
  public int getNComponents() {
    return nComponents;
  }

  /**
   * Set the number of family members
   *
   * @return an integer that contains the new number of members of the household
   */
  public int setNComponents(int str) {
    nComponents = str;
    return nComponents;
  }
}