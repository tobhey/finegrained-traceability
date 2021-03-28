package unisa.gps.etour.bean;

import java.io.Serializable;
import java.util.Date;

import unisa.gps.etour.util.Point3D;

/**
 * Bean for the storage of data refreshment
 *
 */

public class BeanRefreshmentPoint implements Serializable {
    private static final long serialVersionUID = 8417686685147484931L;
    private int id;
    private int ratingNumber;
    private double averageRating;
    private String name;
    private String description;
    private String phone;
    private String location;
    private String city;
    private String street;
    private String cap;
    private String province;
    private String party;
    private Point3D position;
    private Date openingTime;
    private Date closingTime;
    private String closingDay;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pRatingNumber
     * @Param pAverageRating
     * @Param pName
     * @Param pDescription
     * @Param pPhone
     * @Param pLocation
     * @Param pCity
     * @Param pStreet
     * @Param pCap
     * @Param pProvince
     * @Param pParty
     * @Param pPosition
     * @Param pOpeningTime
     * @Param pClosingTime
     * @Param pClosingDay
     */
    public BeanRefreshmentPoint(int pId, int pRatingNumber, double pAverageRating, String pName, String pDescription,
            String pPhone, String pLocation, String pCity, String pStreet, String pCap, String pProvince,
            String pParty, Point3D pPosition, Date pOpeningTime, Date pClosingTime,
            String pClosingDay) {
        setId(pId);
        setRatingNumber(pRatingNumber);
        setAverageRating(pAverageRating);
        setName(pName);
        setDescription(pDescription);
        setPhone(pPhone);
        setLocation(pLocation);
        setCity(pCity);
        setStreet(pStreet);
        setCap(pCap);
        setProvince(pProvince);
        setParty(pParty);
        setPosition(pPosition);
        setOpeningTime(pOpeningTime);
        setClosingTime(pClosingTime);
        setClosingDay(pClosingDay);
    }

    /**
     * Empty Constructor
     *
     */
    public BeanRefreshmentPoint() {

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
     * Returns the value of city
     *
     * @Return Value of city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the new value of city
     *
     * @Param value New pCity city.
     */
    public void setCity(String pCity) {
        city = pCity;
    }

    /**
     * Returns the value of description
     *
     * @Return value of description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the new value of description
     *
     * @Param pDescription New value of description.
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    /**
     * Returns the value of closingDay
     *
     * @Return value closingDay.
     */
    public String getClosingDay() {
        return closingDay;
    }

    /**
     * Sets the new value of closingDay
     *
     * @Param value pClosingDay New closingDay.
     */
    public void setClosingDay(String pClosingDay) {
        closingDay = pClosingDay;
    }

    /**
     * Returns the value of location
     *
     * @Return locale values.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the new value of location
     *
     * @Param pLocation New locale values.
     */
    public void setLocation(String pLocation) {
        location = pLocation;
    }

    /**
     * Returns the value of averageRating
     *
     * @Return value averageRating.
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the new value of averageRating
     *
     * @Param value pAverageRating New averageRating.
     */
    public void setAverageRating(double pAverageRating) {
        averageRating = pAverageRating;
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
     * Returns the value of ratingNumber
     *
     * @Return value ratingNumber.
     */
    public int getRatingNumber() {
        return ratingNumber;
    }

    /**
     * Sets the new value of ratingNumber
     *
     * @Param value pRatingNumber New ratingNumber.
     */
    public void setRatingNumber(int pRatingNumber) {
        ratingNumber = pRatingNumber;
    }

    /**
     * Returns the value of openingTime
     *
     * @Return value openingTime.
     */
    public Date getOpeningTime() {
        return openingTime;
    }

    /**
     * Sets the new value of openingTime
     *
     * @Param value pOpeningTime New openingTime.
     */
    public void setOpeningTime(Date pOpeningTime) {
        openingTime = pOpeningTime;
    }

    /**
     * Returns the value of closingTime
     *
     * @Return value closingTime.
     */
    public Date getClosingTime() {
        return closingTime;
    }

    /**
     * Sets the new value of closingTime
     *
     * @Param value pClosingTime New closingTime.
     */
    public void setClosingTime(Date pClosingTime) {
        closingTime = pClosingTime;
    }

    /**
     * Returns the value of Party
     *
     * @Return value of a political party.
     */
    public String getParty() {
        return party;
    }

    /**
     * Sets the new value of Party
     *
     * @Param pParty New value of political parties.
     */
    public void setParty(String pParty) {
        party = pParty;
    }

    /**
     * Returns the value of position
     *
     * @Return value of position.
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     * Sets the new position value
     *
     * @Param pPosition New position value.
     */
    public void setPosition(Point3D pPosition) {
        position = pPosition;
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