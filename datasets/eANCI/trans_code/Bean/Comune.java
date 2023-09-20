package Bean;

/**
 * The Municipality class allows communication with other municipalities
 *
 * @author Antonio Leone
 * @version 1.0
 */
public class Municipality {

  private String name;
  private String IPAddress;

  /**
   * Default constructor
   */

  public Municipality() {

  }

  /**
   * Parametric constructor
   *
   * @param n
   *     name of the municipality
   * @param i
   *     ip address of the municipality
   */

  public Municipality(String n, String i) {
    this.name = n;
    this.IPAddress = i;
  }

  /**
   * Get the name of the municipality
   *
   * @return Returns a string containing the name of the municipality
   */

  public String getName() {
    return this.name;
  }

  /**
   * Set the name of the municipality
   *
   * @param n
   *     the string that contains the new name of the municipality
   * @return Returns the new name of the municipality
   */

  public String setName(String n) {
    this.name = n;
    return n;
  }

  /**
   * Get the ip address of the municipality
   *
   * @return Returns a string containing the ip address of the municipality
   */

  public String getIPAddress() {
    return this.IPAddress;
  }

  /**
   * Set the ip address of the municipality
   *
   * @param n
   *     the string that contains the new ip address of the municipality
   * @return Returns the new ip address of the municipality
   */

  public String setIPAddress(String i) {
    this.IPAddress = i;
    return i;
  }

}