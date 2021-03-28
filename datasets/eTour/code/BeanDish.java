package unisa.gps.etour.bean;

import java.io.Serializable;

/**
 * Bean containing information relating to food
 *
 */

public class BeanDish implements Serializable {
    private int id;
    private String name;
    private double price;
    private int idMenu;
    private static final long serialVersionUID = -3775462843748984482L;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pName
     * @Param pPrice
     * @Param pIdMenu
     */
    public BeanDish(int pId, String pName, double pPrice, int pIdMenu) {
        setId(pId);
        setName(pName);
        setPrice(pPrice);
        setIdMenu(pIdMenu);
    }

    /**
     * Empty Constructor
     */
    public BeanDish() {

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
     * Returns the value of idMenu
     *
     * @Return value idMenu.
     */
    public int getIdMenu() {
        return idMenu;
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
     * Sets the new value of idMenu
     *
     * @Param value pIdMenu New idMenu.
     */
    public void setIdMenu(int pIdMenu) {
        idMenu = pIdMenu;
    }

}