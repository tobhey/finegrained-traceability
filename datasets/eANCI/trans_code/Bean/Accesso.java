package Bean;

/**
 * The Access class allows access management The Access class has no dependencies
 *
 * @author Federico Cinque
 */
public class Access {

  private String Login;
  private String Password;
  private String Type;

  /**
   * Empty constructor of the Access class
   */
  public Access() {
    Login = null;
    Password = null;
    Type = null;
  }

  /**
   * Constructor of the Access class
   *
   * @param Login
   * @param Password
   * @param Type
   */
  public Access(String Login, String Password, String Type) {
    this.Login = Login;
    this.Password = Password;
    this.Type = Type;
  }

  /**
   * Method that returns a login
   *
   * @return Login
   */
  public String getLogin() {
    return Login;
  }

  /**
   * Method that sets up a login
   *
   * @param login
   */
  public void setLogin(String login) {
    Login = login;
  }

  /**
   * Method that returns a password
   *
   * @return Password
   */
  public String getPassword() {
    return Password;
  }

  /**
   * Method that sets a password
   *
   * @param password
   */
  public void setPassword(String password) {
    Password = password;
  }

  /**
   * Method that returns the type of user who logs on
   *
   * @return Type
   */
  public String getType() {
    return Type;
  }

  /**
   * Method that sets the type of user who logs in
   *
   * @param type
   */
  public void setType(String type) {
    Type = type;
  }

  /**
   * Method that converts login information into a string
   *
   * @return String
   */
  public String toString() {
    return "Login:" + Login + ", Password:" + Password + ", Type:" + Type;
  }
}