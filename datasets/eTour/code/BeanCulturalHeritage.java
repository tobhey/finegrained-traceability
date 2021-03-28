package unisa.gps.etour.bean;

/**
  * Bean containing information relating to a cultural heritage
  *
  */

import java.io.Serializable;
import java.util.Date;

import unisa.gps.etour.util.Point3D;

public class BeanCulturalHeritage implements Serializable {

    private static final long serialVersionUID = -460705346474984466L;

    private int id;
    private int ratingNumber;
    private String name;
    private String city;
    private String phone;
    private String description;
    private String location;
    private String street;
    private String cap;
    private String province;
    private String closingDay;
    private Point3D position;
    private Date openingTime;
    private Date closingTime;
    private double ticketCost;
    private double averageRating;

    public BeanCulturalHeritage(int pId, int pRatingNumber, String pName, String pCity, String pPhone,
            String pDescription, String pLocation, String pStreet, String pCap, String pProvince, Point3D pPosition,
            Date pOpeningTime, Date pClosingTime, String pClosingDay, double pTicketCost,
            double pAverageRating) {
        setId(pId);
        setRatingNumber(pRatingNumber);
        setName(pName);
        setCity(pCity);
        setPhone(pPhone);
        setDescription(pDescription);
        setLocation(pLocation);
        setStreet(pStreet);
        setCap(pCap);
        setProvince(pProvince);
        setPosition(pPosition);
        setOpeningTime(pOpeningTime);
        setClosingTime(pClosingTime);
        setClosingDay(pClosingDay);
        setTicketCost(pTicketCost);
        setAverageRating(pAverageRating);
    }

    public BeanCulturalHeritage() {

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
     * Returns the value of ticketCost
     *
     * @Return value ticketCost.
     */
    public double getTicketCost() {
        return ticketCost;
    }

    /**
     * Sets the new value of ticketCost
     *
     * @Param value pTicketCost ticketCost.
     */
    public void setTicketCost(double pTicketCost) {
        ticketCost = pTicketCost;
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
     * Returns the value of location
     *
     * @Return location values.
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
     * @Param value pAverageRating averageRating.
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
     * @Param value pString New openingTime.
     */
    public void setOpeningTime(Date pString) {
        openingTime = pString;
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