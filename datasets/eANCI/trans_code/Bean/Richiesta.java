package Bean;

/**
 * Class that manages the methods of the Request object
 *
 * @author Christian Ronca
 * @version 1.0
 */
public class Request {
  private int idRequest;
  private String type;
  private String data;
  private int applicant;
  private String state;
  private String document;

  /**
   * Default constructor
   */
  public Request() {

  }

  /**
   * Parameter manufacturer
   *
   * @param idRequest
   *     id of the request
   * @param type
   *     type of the request
   * @param data
   *     date on which the request was made
   * @param applicant
   *     id of the applicant
   * @param state
   *     progress of the request
   * @param document
   *     link to the required document
   */
  public Request(int idRequest, String type, String data, int applicant, String state,
      String document) {
    this.idRequest = idRequest;
    this.type = type;
    this.data = data;
    this.applicant = applicant;
    this.state = state;
    this.document = document;
  }

  /**
   * Parameter manufacturer
   *
   * @param type
   *     type of the request
   * @param data
   *     date on which the request was made
   * @param applicant
   *     id of the applicant
   * @param state
   *     progress of the request
   * @param document
   *     link to the required document
   */
  public Request(String type, String data, int applicant, String state, String document) {
    this.type = type;
    this.data = data;
    this.applicant = applicant;
    this.state = state;
    this.document = document;
  }

  /**
   * Get the request id
   *
   * @return a string with the request id
   */
  public int getIdRequest() {
    return idRequest;
  }

  /**
   * Set a new id at the request
   *
   * @param str
   *     takes as input a string that contains the new id of the request
   * @return a string containing the new id
   */
  public int setIdRequest(int str) {
    idRequest = str;
    return str;
  }

  /**
   * Get the type of request made
   *
   * @return a string that contains the type of request
   */
  public String getType() {
    return type;
  }

  /**
   * Set the type of the request
   *
   * @param str
   *     takes as input a string that contains the type of the request
   * @return a string containing the new type
   */
  public String setType(String str) {
    type = str;
    return str;
  }

  /**
   * Get the date the request was made
   *
   * @return a string that contains the date of the request
   */
  public String getData() {
    return data;
  }

  /**
   * Set the date at the request
   *
   * @param str
   *     takes as input a string that contains the date of the request
   * @return a string that contains the new date
   */
  public String setData(String str) {
    data = str;
    return str;
  }

  /**
   * Get the id of the applicant who made the request
   *
   * @return a string with the request id
   */
  public int getApplicant() {
    return applicant;
  }

  /**
   * Set the applicant for the request
   *
   * @param str
   *     takes as input a string that contains the request applicant
   * @return a string that contains the applicant
   */
  public int setApplicant(int str) {
    applicant = str;
    return str;
  }

  /**
   * Get the state of the request
   *
   * @return a string with the request id
   */
  public String getState() {
    return state;
  }

  /**
   * Set the state of the request
   *
   * @param str
   *     takes as input a string that contains the date of the request
   * @return a string that contains the new date
   */
  public String setState(String str) {
    state = str;
    return str;
  }

  /**
   * Download the link to the required document
   *
   * @return a string with the request id
   */
  public String getDocument() {
    return document;
  }

  /**
   * Set the link to the document
   *
   * @param str
   *     takes as input a string that contains the link to the requested document
   * @return a string that contains the new document
   */
  public String setDocument(String str) {
    document = str;
    return str;
  }

  /**
   * Returns a Boolean value in case the request has been accepted
   *
   * @return true if the request was accepted
   */
  public boolean isAccepted() {
    return state.equalsIgnoreCase("accepted");
  }

  /**
   * Returns a Boolean value in case the request has been rejected
   *
   * @return false if the request was refused
   */
  public boolean isRejected() {
    return state.equalsIgnoreCase("rejected");
  }
}