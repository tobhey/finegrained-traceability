
package unisa.gps.etour.control.AdvertisementManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import unisa.gps.etour.bean.BeanBanner;
import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.control.AdvertisementManager.test.stubs.DBBanner;
import unisa.gps.etour.repository.DBConvention;
import unisa.gps.etour.repository.IDBBanner;
import unisa.gps.etour.repository.IDBConvention;
import unisa.gps.etour.util.ControlData;
import unisa.gps.etour.util.ErrorMessage;
import unisa.gps.etour.util.GlobalConstants;

/**
 * Class that implements the general Management advertisement.
 *
 */
public class AdvertisementManager extends UnicastRemoteObject implements IAdvertisementManager {
    private static final long serialVersionUID = 1L;
    /** An object that handles operations on the banner */
    private IDBBanner dbBanner;

    /**
     * Constructor, instantiates an object of type DBBanner that Contains methods
     * that perform operations on data Entity banner.
     *
     */
    public AdvertisementManager() throws RemoteException {
        super();
        dbBanner = new DBBanner();
    }

    /**
     * Delete a banner and the image associated with the system.
     *
     * @Param id of the banner to remove pBannerID
     */
    public boolean clearBanner(int pIdBanner) throws RemoteException {
        /* Bean containing the data of the banner */
        BeanBanner banner;

        try {
            /* Load the banner and check the data */
            banner = dbBanner.getBannerDaID(pIdBanner);
            if (!ControlData.checkBeanBanner(banner)) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            }
            /* Remove the image associated with the banner */
            File file = new File(banner.getPathFile());
            file.delete();
            /* Clear the whole bean banner */
            return (dbBanner.clearBanner(pIdBanner));
        } catch (

        SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        }
    }

    /**
     * Method to change the image associated with the banner: Delete the old image
     * and stores the new Enjoy the same path as the old image.
     *
     * @Param id pBannerID banner
     * @Param pImage ImageIcon object containing the new image of the banner
     * @Return returns true if the operation is successful
     */
    public boolean modifyBanner(int pBannerID, ImageIcon pImage) throws RemoteException {

        try {
            /* Performs a check on the image of the banner */
            if (!ControlData.checkImmagine(pImage)) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            }
            /* Load the icon image */
            BufferedImage buffImg = (BufferedImage) pImage.getImage();
            /* Load the data of the banner */
            BeanBanner banner = dbBanner.getBannerDaID(pBannerID);
            /* Check the banner on the data uploaded */
            if (!ControlData.checkBeanBanner(banner)) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            }

            /* Rewrite the image file and returns the result of the operation */
            File imgfile = new File(banner.getPathFile());
            return (ImageIO.write(buffImg, "jpg", imgfile));
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        } catch (Exception e) {
            if (e instanceof RemoteException) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            } else {
                throw new RemoteException(ErrorMessage.ERROR_FILE);
            }
        }
    }

    /**
     * Returns a list of banners for a refreshment. Use the method (@link)
     * DBBanner.getBanner
     *
     * @Param pIdRefreshmentPoint refreshment
     */

    public HashMap<BeanBanner, ImageIcon> getBannersID(int pIdRefreshmentPoint) throws RemoteException {
        /* Create a HashMap that will hold the banner and associated images */
        HashMap<BeanBanner, ImageIcon> toReturn = new HashMap<BeanBanner, ImageIcon>();

        try {
            /* Get the list of banners for a refreshment point */
            ArrayList<BeanBanner> listBanner = dbBanner.getBanner(pIdRefreshmentPoint);
            /* For each banner valid list */
            for (BeanBanner banner : listBanner) {
                /* If the banner is a valid charge in the HashMap with the image file */
                if (ControlData.checkBeanBanner(banner)) {
                    /* Create the image file */
                    File imageFile = new File(banner.getPathFile());
                    /* Create an object ImageIcon from the image file */
                    ImageIcon icon = new ImageIcon(ImageIO.read(imageFile));
                    /* Add the banner and the image all'HashMap */
                    toReturn.put(banner, icon);
                }
            }
            return toReturn;
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_FILE);
        }
    }

    /**
     * Create and insert a new banner, making sure the num. maximum Banner displayed
     * for a refreshment point is Already been reached in this case throws an
     * exception to the calling method.
     *
     * @Param Id pIdRefreshmentPoint of refreshment
     * @Param pImageBanner ImageIcon object containing the banner image
     * @Return Returns true if the insertion has been successful.
     */
    public boolean insertBanner(int pIdRefreshmentPoint, ImageIcon pImageBanner) throws RemoteException {

        try {/* Performs a check on the image */
            if (!ControlData.checkImmagine(pImageBanner)) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            }
            /* Create an object that handles the conventions */
            IDBConvention dbConvention = new DBConvention();
            /* Load the data of the convention of a refreshment point */
            BeanConvention convention = dbConvention.getConventionActive(pIdRefreshmentPoint);
            /* Get the number of banners displayed by the Convention Masssimo */
            int maxBanner = convention.getMaxBanner();
            /* Load the list of banners for a refreshment and stores the number */
            int numBanner = (dbBanner.getBanner(pIdRefreshmentPoint)).size();
            /* Check that the maximum number of banners is not reached */
            if (!(numBanner < maxBanner)) {
                throw new Exception(ErrorMessage.ERROR_NUM_BANNER);
            }
            /* Create a blank banner */
            BeanBanner banner = new BeanBanner();
            /* Create a unique file name */
            String path = GlobalConstants.SERVER_IMAGE_PATH + pIdRefreshmentPoint;
            int i = 0;
            File fileImg = new File(path + "_" + i + ". Jpg");
            while (fileImg.exists()) {
                i++;
                fileImg = new File(path + "_" + i + ". jpg");
            }

            /* Stores the image in the file system */
            BufferedImage im = (BufferedImage) pImageBanner.getImage();
            if (!ImageIO.write(im, "jpg", fileImg)) {
                /*
                 * Write failed
                 */
                throw new IOException();
            }
            /*
             * Loads the data in the banner
             */
            banner.setIdRefreshmentPoint(pIdRefreshmentPoint);
            banner.setPathFile(fileImg.getPath());
            /*
             * Insert the banner in the database
             */
            return (dbBanner.insertBanner(banner));
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        } catch (Exception e) {
            if (e.getMessage().equals(ErrorMessage.ERROR_NUM_BANNER)) {
                /*
                 * Has been reached on most of banner inserted num
                 */
                throw new RemoteException(ErrorMessage.ERROR_NUM_BANNER);
            } else {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }

        }

    }

}
