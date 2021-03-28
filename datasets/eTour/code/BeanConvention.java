package unisa.gps.etour.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean containing information relating to a Convention
 *
 */
public class BeanConvention implements Serializable {

    private static final long serialVersionUID = -3255500940680220001L;
    private int id;
    private int maxBanner;
    private Date startDate;
    private Date endDate;
    private double price;
    private boolean active;
    private int idRefreshmentPoint;

    /**
     * Parameterized constructor
     */
    public BeanConvention(int pId, int pMaxBanner, Date pDataStart, Date pDataEnd, double pPrice, boolean active,
            int pidRefreshmentPoint) {
        setId(pId);
        setMaxBanner(pMaxBanner);
        setDataStart(pDataStart);
        setDataEnd(pDataEnd);
        setPrice(pPrice);
        setActive(active);
        setIdRefreshmentPoint(pidRefreshmentPoint);
    }

    /**
     * Empty Constructor
     *
     */
    public BeanConvention() {

    }

    /**
     * Returns the value of active
     *
     * @Return value of assets.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the new value of active
     *
     * @Param new value terms of assets.
     */
    public void setActive(boolean active) {
        active = active;
    }

    /**
     * Returns the value of EndDate
     *
     * @Return Value EndDate.
     */
    public Date getDataEnd() {
        return endDate;
    }

    /**
     * Sets the new value for EndDate
     *
     * @Param pDataEnd New value for EndDate.
     */
    public void setDataEnd(Date pDataEnd) {
        endDate = pDataEnd;
    }

    /**
     * Returns the value of StartDate
     *
     * @Return value StartDate.
     */
    public Date getDataStart() {
        return startDate;
    }

    /**
     * Sets the new value of StartDate
     *
     * @Param new value pDataStart StartDate.
     */
    public void setDataStart(Date pDataStart) {
        startDate = pDataStart;
    }

    /**
     * Returns the value of maxBanner
     *
     * @Return value maxBanner.
     */
    public int getMaxBanner() {
        return maxBanner;
    }

    /**
     * Sets the new value of maxBanner
     *
     * @Param value pMaxBanner New maxBanner.
     */
    public void setMaxBanner(int pMaxBanner) {
        maxBanner = pMaxBanner;
    }

    /**
     * Returns the value of money
     *
     * @Return value price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the new value of money
     *
     * @Param pPrice New value for money.
     */
    public void setPrice(double pPrice) {
        price = pPrice;
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