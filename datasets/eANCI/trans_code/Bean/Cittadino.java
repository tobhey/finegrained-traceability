package Bean;

import java.util.Date;

/**
 * � a JavaBean that manages methods get and set of a Citizen's attributes
 *
 * @author Francesco
 */
public class Citizen {
  private IdentityCard ci;
  private int idCitizen;
  private String FiscalCode;
  private String Surname;
  private String Name;
  private Date DateOfBirth;
  private String PlaceOfBirth;
  private String Email;
  private boolean Advertise;
  private int family;
  private String Login;

  /**
   * empty default constructor
   */
  public Citizen() {
    this.Advertise = false;
    this.FiscalCode = "";
    this.Surname = "";
    this.DateOfBirth = null;
    this.PlaceOfBirth = "";
    this.Email = "";

    this.Login = "";
    this.Name = "";

  }

  public Citizen(String code, String surname, String name, String res, String street) {
    ci.setNumber(code);
    this.Surname = surname;
    this.Name = name;
    ci.setResidence(res);
    ci.setStreet(street);
  }

  public Citizen(int nf, String codfis, String surname, String name, Date data, String place) {
    this.family = nf;
    this.FiscalCode = codfis;
    this.Surname = surname;
    this.Name = name;
    this.DateOfBirth = data;
    this.PlaceOfBirth = place;

  }
  // TODO Auto-generateted constructor stub

  /**
   * parametric constructor that creates the object citizen with the data entered by the latter upon
   * registration in the municipal system
   */
  public Citizen(int id, String cod_fis, String surname, String name, Date data, String place,
      String mail, boolean adv, int nf, String l) {
    this.idCitizen = id;
    this.FiscalCode = cod_fis;
    this.Surname = surname;
    this.Name = name;
    this.DateOfBirth = data;
    this.PlaceOfBirth = place;
    this.Email = mail;
    this.Advertise = adv;
    this.family = nf;
    this.Login = l;
  }

  public String getLogin() {
    return Login;
  }

  public void setLogin(String log) {
    Login = log;
  }

  public int getIdCitizen() {
    return idCitizen;
  }

  public void setIdCitizen(int idCitizen) {
    this.idCitizen = idCitizen;
  }

  public String getSurname() {
    return Surname;
  }

  public void setSurname(String surname) {
    Surname = surname;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public Date getDateOfBirth() {
    return DateOfBirth;
  }

  public void setDateOfBirth(Date data) {
    DateOfBirth = data;
  }

  public void setPlaceOfBirth(String place) {
    PlaceOfBirth = place;
  }

  public String getPlaceOfBirth() {
    return PlaceOfBirth;
  }

  public String getEmail() {
    return Email;
  }

  public void setEmail(String mail) {
    Email = mail;
  }

  public boolean isAdvertise() {
    return Advertise;
  }

  public void setIsAdvertise(boolean ad) {
    Advertise = ad;
  }

  public void setFamily(int nf) {
    family = nf;
  }

  public int getFamily() {
    return family;
  }

  public String getFiscalCode() {
    return FiscalCode;
  }

  public void setFiscalCode(String cod_fis) {
    FiscalCode = cod_fis;
  }

  public String toString() {
    return "ID:" + getIdCitizen() + "\n" + "Login:" + getLogin() + "\n" + "Tax code:"
        + getFiscalCode() + "\n" + "Name:" + getName() + "\n" + "Surname:" + getSurname() + "\n"
        + "Date of birth:" + getDateOfBirth() + "\n" + "Place of Birth:" + getPlaceOfBirth() + "\n"
        + "e-mail:" + getEmail() + ". \n";
  }

}

