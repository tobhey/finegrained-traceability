package unisa.gps.etour.bean;

import java.io.Serializable;

/**
 * Bean containing information relating to an Agency Operator
 *
 */
public class BeanOperatorAgency implements Serializable {

    /**
    *
    */
    private static final long serialVersionUID = -3489147679484477440L;
    private int id;
    private String username;
    private String name;
    private String lastName;
    private String password;

    /**
     * Parameterized constructor
     *
     * @Param pId @Param pUsername @Param pName @Param pSurname @Param pPassword
     */
    public BeanOperatorAgency(int pId, String pUsername, String pName, String pSurname, String pPassword) {
        setId(pId);
        setUsername(pUsername);
        setName(pName);
        setSurname(pSurname);
        setPassword(pPassword);
    }

    /**
     * Empty Constructor
     */
    public BeanOperatorAgency() {

    }

    /**
     * Returns the value of name
     *
     * @Return value of name.
     */
    public String getSurname() {
        return name;
    }

    /**
     * Sets the new value of name
     *
     * @Param value New pSurname surname.
     */
    public void setSurname(String pSurname) {
        lastName = pSurname;
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
     * Sets the new value of id
     *
     * @Param new value of id pId.
     */
    public void setId(int pId) {
        id = pId;
    }

    /**
     * Returns the value of id
     *
     * @Return value id.
     */
    public int getId() {
        return id;
    }

}