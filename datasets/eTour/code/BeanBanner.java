package unisa.gps.etour.bean;



import java.io.Serializable;
/**
 * Bean which contains data on the Banner
 */
public class BeanBanner implements Serializable {
    private static final long serialVersionUID = -872783211721655763L;
    private int id;
    private int idRefreshmentPoint;
    private String filepath;

    /**
     * Parameterized constructor
     *
     */

    public BeanBanner(int pId, String pPathFile, int pidRefreshmentPoint) {
        setId(pId);
        setPathFile(pPathFile);
        setIdRefreshmentPoint(pidRefreshmentPoint);
    }

    public BeanBanner() {

    }

    /**
     * Returns the value of FilePath
     *
     * @Return value of FilePath.
     */

    public String getPathFile() {
        return filepath;
    }

    /**
     * Sets the new value of filepath
     *
     * @Param pPathFile New value filepath.
     */
    public void setPathFile(String pPathFile) {
        filepath = pPathFile;
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