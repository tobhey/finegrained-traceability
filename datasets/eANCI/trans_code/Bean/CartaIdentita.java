package Bean;

import java.util.Date;

/**
 * is a JavaBean that manages the setting methods and return of card attributes a citizen's
 * identity
 *
 * @author Francesco
 */
public class IdentityCard {
  /**
   * represents the code of the identity card which is entered by the employee
   */
  private String Number;
  private int idCitizen;

  private String Citizenship;
  private String Residence;
  private String Street;
  private String MaritalStatus;
  private String Profession;
  private double Height;
  private String Hair;
  private String Eyes;
  private String ParticularSigns;
  private Date ReleaseDate;
  private Date ExpiryDate;
  private boolean ValidExpatriation;
  private int civicNumber;

  /**
   * Empty default constructor
   */

  public IdentityCard() {
    this.civicNumber = 0;
    this.Hair = null;
    this.Citizenship = null;
    this.ReleaseDate = null;
    this.ExpiryDate = null;

    this.Number = null;
    this.Eyes = null;
    this.Profession = null;
    this.Residence = null;
    this.ParticularSigns = null;
    this.MaritalStatus = null;
    this.Height = 0.00;
    this.ValidExpatriation = false;
    this.Street = null;
  }

  /**
   * object that is returned from the database
   *
   * @param cardCode
   * @param city
   * @param res
   * @param street
   * @param stciv
   * @param prof
   * @param stat
   * @param cap
   * @param eyes
   * @param sp
   * @param dr
   * @param ds
   * @param validEsp
   * @param num_civ
   */
  public IdentityCard(String cardCode, int idC, String citizenship, String res, String street,
      int num, String stciv, String prof, double stat, String cap, String eyes, String sp, Date dr,
      Date ds, boolean validEsp) {
    this.Number = cardCode;
    this.idCitizen = idC;

    this.Citizenship = citizenship;
    this.Residence = res;
    this.Street = street;
    this.civicNumber = num;
    this.MaritalStatus = stciv;
    this.Profession = prof;
    this.Height = stat;
    this.Hair = cap;
    this.Eyes = eyes;
    this.ParticularSigns = sp;
    this.ReleaseDate = dr;
    this.ExpiryDate = ds;
    this.ValidExpatriation = validEsp;
  }
  // TODO Auto-generateted constructor stub

  /**
   * create the identity card of the citizen you are registered in the municipal system taking from
   * the class citizen information needed for creation of the identification document
   */
  public IdentityCard(String cardCode, String surname, String name, Date borndate, String city,
      String res, String street, int num, String stciv, String prof, double stat, String cap,
      String eyes, String sp, Date dr, Date ds, boolean validEsp) {
    /**
     * the unique code of the identity card requested by the citizen is
     * inserted by the employee at the time of the paper creation of the document and in the moment
     * where information must be kept in the database.
     *
     */
    this.Number = cardCode;
    /**
     * I assign the identity card I am creating to the citizen
     * who has requested it and who is present in the
     * municipal database
     */

    this.Citizenship = city;
    this.Residence = res;
    this.Street = street;
    this.civicNumber = num;
    this.MaritalStatus = stciv;
    this.Profession = prof;
    this.Height = stat;
    this.Hair = cap;
    this.Eyes = eyes;
    this.ParticularSigns = sp;
    this.ReleaseDate = dr;
    this.ExpiryDate = ds;
    this.ValidExpatriation = validEsp;

  }

  public int id() {
    return idCitizen;
  }

  public void setNumber(String code) {
    Number = code;
  }

  public String getNumber() {
    return Number;
  }

  public void setCitizenship(String citya) {
    Citizenship = citya;
  }

  public String getCitizenship() {
    return Citizenship;
  }

  public String getResidence() {
    return Residence;
  }

  public void setResidence(String res) {
    Residence = res;
  }

  public void setStreet(String list) {
    Street = list;
  }

  public String getStreet() {
    return Street;
  }

  public int getCivicNumber() {
    return civicNumber;
  }

  public void setCivicNumber(int n) {
    civicNumber = n;
  }

  public void setMaritalStatus(String stat) {
    MaritalStatus = stat;
  }

  public String getMaritalStatus() {
    return MaritalStatus;
  }

  public void setProfession(String prof) {
    Profession = prof;
  }

  public String getProfession() {
    return Profession;
  }

  public void setHeight(double stat) {
    Height = stat;
  }

  public double getHeight() {
    return Height;
  }

  public void setHair(String hair) {
    Hair = hair;
  }

  public String getHair() {
    return Hair;
  }

  public void setEyes(String eyes) {
    Eyes = eyes;
  }

  public String getEyes() {
    return Eyes;
  }

  public String getParticularSigns() {
    return ParticularSigns;
  }

  public void setParticularSigns(String listSp) {
    ParticularSigns = listSp;
  }

  public void setReleaseDate(Date data) {
    ReleaseDate = data;
  }

  public Date getReleaseDate() {
    return ReleaseDate;
  }

  public void setExpiryDate(Date datas) {
    ExpiryDate = datas;
  }

  public Date getExpiryDate() {
    return ExpiryDate;
  }

  public void setValidityExpatriation(boolean val) {
    ValidExpatriation = val;
  }

  public boolean isValidExpatriation() {
    return ValidExpatriation;
  }

  @SuppressWarnings({ "deprecation", "deprecation", "deprecation" })
  public String toString() {

    int month2 = 0;
    month2 = getReleaseDate().getMonth();
    int month3 = 0;
    month3 = getExpiryDate().getMonth();
    return  //I have to recover my surname, first name, and date of birth from class db

        "Citizenship:" + getCitizenship() + "\n" + "Residence:" + getResidence() + "\n" + "Street:"
            + getStreet() + "\n" + "Civic Number:" + getCivicNumber() + "\n" + "Marital Status:"
            + getMaritalStatus() + "\n" + "Profession:" + getProfession() + "\n"
            + "\nCONNOTATIONS AND HIGHLIGHTS \n \n" + "Height:" + getHeight() + "\n" + "Hair:"
            + getHair() + "\n" + "Eyes:" + getEyes() + "\n" + "Particular Signs:"
            + getParticularSigns().ToString() + "\n" + "li:" + getReleaseDate().getDate() + "/"
            + month2 + 1 + "/" + getReleaseDate().getYear() + "\n" + "Expiry Date:"
            + getExpiryDate().GetDate() + "/" + month3 + 1 + "/" + getExpiryDate().GetYear() + "\n";
  }

}

