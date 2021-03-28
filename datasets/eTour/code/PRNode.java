package unisa.gps.etour.gui.operatoragency.tables;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class creates a node in a JTree to store Information for a
 * refreshment.
 *
 */
public class PRNode extends DefaultMutableTreeNode {
    private int id;

    public PRNode() {
        super();
    }

    /**
     * Create a node with the name of refreshment and Your id supplied as
     * parameters.
     *
     * PNamePR 
     * @param String - the name of refreshment. pId 
     * @param int - the id
     * of refreshment.
     * 
     * @Throws IllegalArgumentException - if the name provided as input is invalid.
     */
    public PRNode(String pName, int pId) throws IllegalArgumentException {
        super();
        if (pName == null || pName.equals("")) {
            throw new IllegalArgumentException("Name of refreshment supplied invalid input.");
        }
        setUserObject(pName);
        id = pId;
    }

    /**
     *
     * Returns the id of the point of comfort for which information Are stored in
     * this node.
     *
     * @Return int - the id of refreshment.
     */
    public int getID() {
        return id;
    }

    /**
     *
     * Stores the id of the refreshment provided input.
     *
     * @Param int pId - an ID of an eating place.
     */
    public void setID(int pId) {
        id = pId;
    }

    /**
     *
     * Return the name of refreshment.
     *
     * @Return String - the name of refreshment.
     */
    public String getName() {
        return (String) super.getUserObject();
    }

    /**
     *
     * Stores the name of the refreshment provided input.
     *
     * pName * @param String - the name of a refreshment.
     * 
     * @Throws IllegalArgumentException - if the name provided as input is invalid.
     */
    public void setName(String pName) throws IllegalArgumentException {
        if (pName == null || pName.equals("")) {
            throw new IllegalArgumentException("Name of refreshment supplied invalid input.");
        }
        setUserObject(pName);
    }
}