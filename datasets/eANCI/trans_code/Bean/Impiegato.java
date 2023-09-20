package Bean;

/**
 * The Employee class allows only the administrator to manage employees The Employee class has no
 * dependencies
 *
 * @author Federico Cinque
 */
public class Employee {

  private String Name;
  private String Surname;
  private String Number;
  private String Email;
  private String Login;

  /**
   * Empty constructor of the Employee class
   */
  public Employee() {
    Name = null;
    Surname = null;
    Number = null;
    Email = null;
    Login = null;
  }

  /**
   * Constructor of the Employee class
   *
   * @param Name
   * @param Surname
   * @param Number
   * @param Email
   * @param Login
   */
  public Employee(String Name, String Surname, String Number, String Email, String Login) {
    this.Name = Name;
    this.Surname = Surname;
    this.Number = Number;
    this.Email = Email;
    this.Login = Login;
  }

  /**
   * Method that returns the employee name
   *
   * @return Name
   */
  public String getName() {
    return Name;
  }

  /**
   * Method that sets the employee name
   *
   * @param name
   */
  public void setName(String name) {
    Name = name;
  }

  /**
   * Method that returns the employee's surname
   *
   * @return Surname
   */
  public String getSurname() {
    return Surname;
  }

  /**
   * Method that sets the employee surname
   *
   * @param surname
   */
  public void setSurname(String surname) {
    Surname = surname;
  }

  /**
   * Method that returns the employee number
   *
   * @return Number
   */
  public String getNumber() {
    return Number;
  }

  /**
   * Method that sets the employee number
   *
   * @param number
   */
  public void setNumber(String number) {
    Number = number;
  }

  /**
   * Method that returns the employee's email
   *
   * @return Email
   */
  public String getEmail() {
    return Email;
  }

  /**
   * Method that sets up the employee's email
   *
   * @param email
   */
  public void setEmail(String email) {
    Email = email;
  }

  /**
   * Method that returns the employee's login
   *
   * @return Login
   */
  public String getLogin() {
    return Login;
  }

  /**
   * Method that sets the employee login
   *
   * @param login
   */
  public void setLogin(String login) {
    Login = login;
  }

  /**
   * Method that converts the information of an access into a string
   *
   * @return String
   */
  public String toString() {
    return "Name: " + Name + ", Surname: " + Surname + ", Number: " + Number + ", e-mail: " + Email
        + ", Login: " + Login;
  }
}