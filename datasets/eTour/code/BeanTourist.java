package unisa.gps.etour.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean containing information relating to a tourist
 *
 */
public class BeanTourist implements Serializable {
    private static final long serialVersionUID = 4214744963263090577L;
    private int id;
    private String username;
    private String name;
    private String surname;
    private String cityNascita;
    private String cityResidenza;
    private String phone;
    private String cap;
    private String street;
    private String province;
    private String email;
    private String password;
    private Date dob;
    private Date dataRegistration;
    private boolean active;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pUsername
     * @Param pName
     * @Param pSurname
     * @Param pCityNascita
     * @Param pCityResidenza
     * @Param pPhone
     * @Param pCap
     * @Param pStreet
     * @Param pProvince
     * @Param pEmail
     * @Param pPassword
     * @Param pDateOfBirth
     * @Param pDataRegistration
     * @Param pacts
     */
    public BeanTourist(int pId, String pUsername, String pName,String pSurname , String pCityNascita,
            String pCityResidenza, String pPhone , String pCap, String pStreet , String pProvince,String pEmail ,
            String pPassword, Date pDateOfBirth, Date pDataRegistration, boolean Patti) {
        setId(pId);
        setUsername(pUsername);
        setName(pName);
        setSurname(pSurname);
        setCityNascita(pCityNascita);
        setCityResidenza(pCityResidenza);
        setPhone(pPhone);
        setCap(pCap);
        setStreet(pStreet);
        setProvince(pProvince);
        setEmail(pEmail);
        setPassword(pPassword);
        setDateOfBirth(pDateOfBirth);
        setDataRegistration(pDataRegistration);
        setActive(Patti);
    }

    /**
     * Empty Constructor
     */
    public BeanTourist() {

    }

    /**
     * Returns the value of cap
     *
     * @Return value cap.
     */
    public String getCap() {
        return cap;
    }

    /**
     * Sets the new value of cap
     *
     * New pCap * @param value cap.
     */
    public void setCap(String pCap) {
        cap = pCap;
    }

/**
* Returns the value of cityNascita
*
* @Return value cityNascita.
*/
public String getCityNascita ()
{
    return cityNascita ;
}

    /**
     * Sets the new value of cityNascita
     *
     * @Param value pCityNascita New cityNascita.
     */
    public void setCityNascita(String pCityNascita) {
        cityNascita = pCityNascita;
    }

/**
* Returns the value of cityResidenza
*
* @Return value cityResidenza.
*/
public String getCityResidenza ()
{
    return cityResidenza ;
}

    /**
     * Sets the new value of cityResidenza
     *
     * @Param value pCityResidenza New cityResidenza.
     */
    public void setCityResidenza(String pCityResidenza) {
        cityResidenza = pCityResidenza;
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
     * Returns the value of dob
     *
     * @Return value dob.
     */
    public Date getDateOfBirth() {
        return dob;
    }

    /**
     * Sets the new value of dob
     *
     * @Param value New pDateOfBirth dob.
     */
    public void setDateOfBirth(Date pDateOfBirth) {
        dob = pDateOfBirth;
    }

/**
* Returns the value of dataRegistration
*
* @Return value dataRegistration.
*/
public Date getDataRegistration ()
{
    return dataRegistration ;
}

    /**
     * Sets the new value of dataRegistration
     *
     * @Param value pDataRegistration New dataRegistration.
     */
    public void setDataRegistration(Date pDataRegistration) {
        dataRegistration = pDataRegistration;
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
     * Returns the value of the province
     *
     * @Return value of the province.
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the new value of the province
     *
     * @Param pProvince New value for the province.
     */
    public void setProvince(String pProvince) {
        province = pProvince;
    }

    /**
     * Returns the value of telephone
     *
     * @Return Value of the phone.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the new value of telephone
     *
     * @Param value New pPhone phone.
     */
    public void setPhone(String pPhone) {
        phone = pPhone;
    }

    /**
     * Returns the value of street
     *
     * @Return value on.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the new value via
     *
     * @Param value New pStreet on.
     */
    public void setStreet(String pStreet) {
        street = pStreet;
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
     * Returns to be 1 or 0, indicating whether a tourist or not Active
     *
     * @Return value of activation
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the new value of active
     *
     * @Param new value terms of assets.
     */
    public void setActive(boolean Patti) {
        active = Patti;
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