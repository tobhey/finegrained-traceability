package unisa.gps.etour.control.AdvertisementManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.ImageIcon;

import unisa.gps.etour.bean.BeanBanner;

/**
 * Interface General Manager of Banner and news.
 *
 */
public interface IAdvertisementManager extends Remote {
    /**
     * Inserts a new banner.
     *
     * @Param pBanner Bean contains the data of the banner
     * @Throws RemoteException
     */
    public boolean insertBanner(int pIdRefreshmentPoint, ImageIcon pImageBanner) throws RemoteException;

    /**
     * Delete a banner from the system.
     *
     * @Param pBannerID ID banner to be deleted.
     * @Return true if the operation is successful false otherwise.
     * @Throws RemoteException
     */
    public boolean clearBanner(int pBannerID) throws RemoteException;

    /**
     * Modify the data of the banner or the image associated.
     *
     * @Param pBanner Bean contains the data of the banner.
     * @Return true if the operation is successful, false otherwise.
     * @Throws RemoteException
     */
    public boolean modifyBanner(int pBannerID, ImageIcon pImage) throws RemoteException;

    /**
     * Returns a list of Banner of a particular point of comfort.
     *
     * @Param Id pRefreshmentPointID of refreshment owner of banner
     * @Return ArrayList containing the list of banner refreshment
     * @Throws RemoteException
     */
    public HashMap<BeanBanner, ImageIcon> getBannersID(int pIdRefreshmentPoint) throws RemoteException;

}
