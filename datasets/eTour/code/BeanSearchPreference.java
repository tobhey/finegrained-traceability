package unisa.gps.etour.bean;

import java.io.Serializable;

/**
 * Bean which contains data search preferences
 *
 */
public class BeanSearchPreference implements Serializable {
    private static final long serialVersionUID = 7576354037868937929L;
    private int id;
    private String name;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pName
     */
    public BeanSearchPreference(int pId, String pName) {
        setId(pId);
        setName(pName);
    }

    /**
     * Empty Constructor
     *
     */
    public BeanSearchPreference() {

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
