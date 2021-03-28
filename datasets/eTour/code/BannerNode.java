package unisa.gps.etour.gui.operatoragency.tables;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class creates a node in a JTree to store Information about a banner.
 *
 */
public class BannerNode extends DefaultMutableTreeNode {
    private int id;

    /**
     * This is the default constructor.
     *
     */
    public BannerNode() {
        super();
    }

    /**
     * Create a node with the image of the banner and Your id supplied as
     * parameters.
     *
     * @Param pBanner Imagelcon - the image of the banner. pId * @param int - the id
     *        of the banner.
     * @Throws IllegalArgumentException - if the image provided as input is invalid.
     *
     */
    public BannerNode(ImageIcon pBanner, int pId) throws IllegalArgumentException {
        super();
        if (null == pBanner) {
            throw new IllegalArgumentException("Image is invalid.");
        }
        setUserObject(pBanner);
        id = pId;
    }

    /**
     *
     * Returns the node type PRNode father.
     *
     * @Return PRNode - the parent node.
     *
     */
    public PRNode getParent() {
        return (PRNode) super.getParent();
    }

    /**
     *
     * Returns the id of the banner for which information Are stored in this node.
     *
     * @Return int - the id of the banner.
     *
     */
    public int getID() {
        return id;
    }

    /**
     *
     * Stores the id of the banner supplied input.
     *
     * @Param int pId - an ID of a banner.
     *
     */
    public void setID(int pId) {
        id = pId;
    }

    /**
     *
     * Returns the banner image.
     *
     * @Return ImageIcon - the image.
     */
    public ImageIcon getBanner() {
        return (ImageIcon) getUserObject();
    }

    /**
     *
     * Save the image provided as input.
     *
     * @Param pImage Imagelcon - the image to be stored.
     * @Throws IllegalArgumentException - if the supplied parameter is null.
     */
    public void setBanner(ImageIcon pImage) throws IllegalArgumentException {
        if (null == pImage) {
            throw new IllegalArgumentException("Image is invalid.");
        }
        setUserObject(pImage);
    }
}