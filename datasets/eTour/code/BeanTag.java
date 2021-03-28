package unisa.gps.etour.bean;

/**
  * Bean that contains the data for a Tag
  *
*/

import java.io.Serializable;

public class BeanTag implements Serializable {
    private static final long serialVersionUID = -6320421006595188597L;
    private int id;
    private String name;
    private String description;

    /**
     * Parameterized constructor
     *
     * @Param pId
     * @Param pName
     * @Param pDescription
     */
    public BeanTag(int pId, String pName, String pDescription) {
        setId(pId);
        setName(pName);
        setDescription(pDescription);
    }

    /**
     * Empty Constructor
     */
    public BeanTag() {

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
     * Returns the value of name
     *
     * @Return value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the new value of name
     *
     * @Param pName New value for name.
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
