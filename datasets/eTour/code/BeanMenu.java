package unisa.gps.etour.bean;

import java.io.Serializable;

/**
 * Bean containing information relating to a Menu
 *
 */
public class BeanMenu implements Serializable {
    private static final long serialVersionUID = -3112032222839565409L;
    private int id;
    private String day;
    private int idRefreshmentPoint;

    /**
     * Parameterized constructor
     *
     */
    public BeanMenu(int pId, String pDay, int pIdRefreshmentPoint) {
        setId(pId);
        setDay(pDay);
        setIdRefreshmentPoint(pIdRefreshmentPoint);
    }

    /**
     * Empty Constructor
     */
    public BeanMenu() {

    }

    /**
     * Returns the value of days
     *
     * @Return Value of the day.
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the new value of days
     *
     * @Param value New pDay day.
     */
    public void setDay(String pDay) {
        day = pDay;
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
     * Returns the value of idRefreshmentPoint
     *
     * @Return value idRefreshmentPoint.
     */
    public int getIdRefreshmentPoint() {
        return idRefreshmentPoint;
    }

    /**
     * Sets the new value of id
     *
     * @Param pId New value for id.
     */
    public void setId(int pId) {
        id = pId;
    }

    /**
     * Sets the new value of idRefreshmentPoint
     *
     * @Param value pIdRefreshmentPoint New idRefreshmentPoint.
     */
    public void setIdRefreshmentPoint(int pIdRefreshmentPoint) {
        idRefreshmentPoint = pIdRefreshmentPoint;
    }

}
