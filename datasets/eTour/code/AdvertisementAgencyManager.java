
package unisa.gps.etour.control.AdvertisementManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanNews;/*Import the stub*/
import unisa.gps.etour.control.AdvertisementManager.test.stubs.DBNews;
import unisa.gps.etour.repository.IDBNews;
import unisa.gps.etour.util.ControlData;
import unisa.gps.etour.util.ErrorMessage;
import unisa.gps.etour.util.GlobalConstants;
/**
 * Implementing the management advertisement
 * For the operator agency. Contains methods for managing
 * News.
 *
 */
public class AdvertisementAgencyManager extends AdvertisementManager implements IAdvertisementAgencyManager {
    private static final long serialVersionUID = 1L;
    /** Contains the methods for collecting news in the database */
    private IDBNews dbNews;

    /**
     * Constructor. Instantiates an object of type (@link
     * unisa.gps.etour.repository.DBNews).
     *
     * @Throws RemoteException
     */
    public AdvertisementAgencyManager() throws RemoteException {
        super();
        dbNews = new DBNews();
    }

    /**
     * Method which removes news from the database. Uses the (@Link Boolean
     * unisa.gps.etour.repository.IDBNews # clearNews (int))
     *
     * @Param id pNewsID news be erased.
     * @Return true if the clearing was successful or FALSE otherwise.
     * @Throws RemoteException
     *
     */
    public boolean clearNews(int pNewsID) throws RemoteException {

        try {
            /* Check that the ID is valid */
            if (pNewsID > 0) {
                return (dbNews.clearNews(pNewsID));
            } else {
                return false;
            }

        }

        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        }

    }

    /**
     * Insert a new news in the database. Use the method (@link
     * unisa.gps.etour.repository.IDBNews # insertNews (BeanNews)) To insert the
     * news and the method (@link unisa.gps.etour.repository.IDBNews # getNews
     * ()) To count the number of news in the system.
     *
     * @Param pNews bean containing data news.
     * @Return true if the insertion is successful, false if it was Reached the
     *         maximum number of news stored or if the insertion fails.
     * @Throws RemoteException
     */
    public boolean insertNews(BeanNews pNews) throws RemoteException {
        try {
            /* Check the data of the news */
            if (!ControlData.checkBeanNews(pNews)) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            }
            /* Check that has not been exceeded the no. Max news presets */
            int numNews = dbNews.getNews().size();
            if (numNews < GlobalConstants.MAX_NEWS_ACTIVE) {
                /* Possible inclusion */
                return (dbNews.insertNews(pNews));
            } else {
                /*
                 * 
                 * Insertion is not possible
                 */return false;
            }
        }

        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        }

    }

    /**
     * Method to change data of a news. Use the method (@link
     * unisa.gps.etour.repository.IDBNews # modifyNews (BeanNews)).
     *
     * @Param pNews bean containing the data of news changed.
     * @Return true if the change goes through.
     * @Throws RemoteException
     */
    public boolean modifyNews(BeanNews pNews) throws RemoteException {

        try {
            /* Check the data of the news */
            if (!ControlData.checkBeanNews(pNews)) {
                throw new RemoteException(ErrorMessage.ERROR_DATA);
            }
            return (dbNews.modifyNews(pNews));
        }

        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        }

    }

    /**
     * Method that returns an array containing all the news stored in the system.
     * Use the method (@link unisa.gps.etour.repository.IDBNews # getNews ()).
     *
     * @Return ArrayList containing beans of news.
     * @Throws RemoteException
     */

    public ArrayList<BeanNews> getAllNews() throws RemoteException {

        try {
            return (dbNews.getNews());
        }

        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_CONNECTION_DBMS);
        }
    }
}