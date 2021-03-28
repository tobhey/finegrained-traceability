package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanBanner;

/**
 * Interface for managing the banner on the database
 *
 */

public interface IDBBanner {
    /**
     * Add a banner in the database
     *
     * @Param pBanner bean containing the information of the banner
     */
    public boolean insertBanner(BeanBanner pBanner) throws SQLException;

    /**
     * Modify the contents of the advertisement, and returns the contents before
     * Edit
     *
     * @Param pBanner Bean that contains the new information of the banner
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyBanner(BeanBanner pBanner) throws SQLException;

    /**
     * Delete a banner from the database and returns
     *
     * @Param pIdBanner ID BeanBanner
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearBanner(int pIdBanner) throws SQLException;

    /**
     * Returns a list of banners for a refreshment point, if the id of Refreshment
     * and 'equal to -1 will' return the complete list Banners
     *
     * @Param Id pIdRefreshmentPoint of refreshment point from which to obtain the
     *        list Banner
     * @Return list of banners linked to Refreshment
     */
    public ArrayList<BeanBanner> getBanner(int pIdRefreshmentPoint) throws SQLException;

    /**
     * Method which returns a banner given its id
     *
     * @Param ID pIdBanner the banner to return
     * @Return Banner found in the database, null if there is' match
     */
    public BeanBanner getBannerDaID(int pIdBanner) throws SQLException;
}
