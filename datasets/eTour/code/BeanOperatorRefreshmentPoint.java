package unisa.gps.etour.bean;

import java.io.Serializable;

/**
 * Bean containing information relating to food
 *
 */
public class BeanOperatorRefreshmentPoint implements Serializable {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private int idRefreshmentPoint;
    private static final long serialVersionUID = -6485826396352557404L;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pName
     * @Param pSurname
     * @Param pUsername
     * @Param pPassword
     * @Param pEmail
     * @Param pIdRefreshmentPoint
     */
    public BeanOperatorRefreshmentPoint(int pId, String pName, String pSurname, String pUsername, String pPassword,
            String pEmail, int pIdRefreshmentPoint) {
        setId(pId);
        setName(pName);
        setSurname(pSurname);
        setUsername(pUsername);
        setPassword(pPassword);
        setEmail(pEmail);
        setIdRefreshmentPoint(pIdRefreshmentPoint);
    }

    /**
     * Empty Constructor
     */
    public BeanOperatorRefreshmentPoint() {

    }

    /**
     * Returns the value of name
     *
     * @Return value of name.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the new value of name
     *
     * @Param value New pSurname surname.
     */
    public void setSurname(String pSurname) {
        surname = pSurname;
    }

    /**
     * Returns the value of email
     *
     * @Return value of email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the new value of email
     *
     * @Param pEmail New value of email.
     */
    public void setEmail(String pEmail) {
        email = pEmail;
    }

    /**
     * Returns the value of name
     *
     * @Return value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the new name value
     *
     * @Param name New value pName.
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Returns the value of password
     *
     * @Return value of password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the new password value
     *
     * @Param pPassword new password value.
     */
    public void setPassword(String pPassword) {
        password = pPassword;
    }

    /**
     * Returns the value of username
     *
     * @Return value of username.
     */
    public String getUserName() {
        return username;
    }

    /**
     * Sets the new value of username
     *
     * @Param pUsername New value for username.
     */
    public void setUsername(String pUsername) {
        username = pUsername;
    }

    /**
     * Returns the value of idRefreshmentPoint
     *
     * @Return value idRefreshmentPoint.
     */
    public int getIdRefreshmentPoint() {
        return idRefreshmentPoint;
    }

    /**
     * Sets the new value of idRefreshmentPoint
     *
     * @Param value pIdRefreshmentPoint New idRefreshmentPoint.
     */
    public void setIdRefreshmentPoint(int pIdRefreshmentPoint) {
        idRefreshmentPoint = pIdRefreshmentPoint;
    }

    /**
     * Returns the value of id
     *
     * @Return value id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the new value of id
     *
     * @Param pId New value for id.
     */
    public void setId(int pId) {
        id = pId;
    }

}
