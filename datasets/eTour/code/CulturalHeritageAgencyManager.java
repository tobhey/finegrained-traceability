package unisa.gps.etour.control.CulturalHeritageManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class management of cultural heritage unique to Agency
 *
 */
public class CulturalHeritageAgencyManager extends CulturalHeritageCommonManager implements ICulturalHeritageAgencyManager {
    /**
     * Constructor of class, richicama and initializes the class of common
     * management
     */
    public CulturalHeritageAgencyManager() throws RemoteException {
        super();
    }

    /**
     * Implements the method for the elimination of a cultural system.
     *
     * @See unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageAgency
     *      # clearCulturalHeritage (int)
     */
    public boolean clearCulturalHeritage(int pCulturalHeritageID) throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID))
            throw new RemoteException(ErrorMessage.ERROR_DATA);

        try {
            return (dbbc.clearCulturalHeritage(pCulturalHeritageID));
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

    /**
     * Implement the method for the insertion of a new cultural object.
     *
     * @See unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageAgency
     *      # insertCulturalHeritage (unisa.gps.etour.bean.BeanCulturalHeritage)
     */
    public boolean insertCulturalHeritage(BeanCulturalHeritage pCulturalHeritage) throws RemoteException {
        if (!CulturalHeritageChecker.checkDataCulturalHeritage(pCulturalHeritage))
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);

        try {
            return (dbbc.insertCulturalHeritage(pCulturalHeritage));
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

    /**
     * Implements the method for obtaining all the cultural assets currently in the
     * system.
     *
     * @See unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageAgency
     *      # getCulturalHeritage ()
     */

    public ArrayList<BeanCulturalHeritage> getCulturalHeritage() throws RemoteException {
        try {
            return (dbbc.getListBC());
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

    /**
     * Implement the method for changing a cultural asset in the system.
     *
     * @See unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageAgency
     *      # modifyCulturalHeritage (unisa.gps.etour.bean.BeanCulturalHeritage)
     */
    public boolean modifyCulturalHeritage(BeanCulturalHeritage pCulturalHeritage) throws RemoteException {
        if (!CulturalHeritageChecker.checkDataCulturalHeritage(pCulturalHeritage)) {
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        }
        try {
            return (dbbc.modifyCulturalHeritage(pCulturalHeritage));
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

    /**
     * Implements the method for adding a tag to a cultural object.
     *
     * @See unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageAgency
     *      # addTagCulturalHeritage (int, int)
     */
    public boolean addTagCulturalHeritage(int pCulturalHeritageID, int pTagID) throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID) || !(pTagID > 0)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        /**
         * This segment of code that actually controls the cultural speficiato Have the
         * tag defined.
         */

        /**
         * Get all tags to the cultural past for parameter
         */
        ArrayList<BeanTag> tempTag = null;

        boolean contieneTag = false;

        try {
            tempTag = dbtag.getTagCulturalHeritage(pCulturalHeritageID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }

        /**
         * Here we iterate to find the tag that speficiato, if it is you set a sentry In
         * order not to add a tag twice for the same cultural object.
         */
        for (BeanTag t : tempTag)
            if (t.getId() == pTagID)
                contieneTag = true;

        if (!contieneTag)
            try {
                return (dbtag.addTagCulturalHeritage(pCulturalHeritageID, pTagID));
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }

        return false;
    }

    /**
     * Implement the method for removing a tag from a cultural object.
     *
     * @See unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageAgency
     *      # removeTagCulturalHeritage (int, int)
     */
    public boolean removeTagCulturalHeritage(int pCulturalHeritageID, int pTagID) throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID) || !(pTagID > 0)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        /**
         * This segment of code that actually controls the cultural specified Has the
         * specified tag.
         */

        /**
         * Get all tags to the cultural past for parameter
         */
        ArrayList<BeanTag> tempTag = null;

        try {
            tempTag = dbtag.getTagCulturalHeritage(pCulturalHeritageID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }

        /**
         * Here we iterate to find the tag that specified, if you found the transaction
         * is made Removal of the tag and returns control
         */
        for (BeanTag t : tempTag) {
            if (t.getId() == pTagID) {
                try {
                    return (dbtag.clearTagCulturalHeritage(pCulturalHeritageID, pTagID));
                } catch (SQLException e) {
                    throw new RemoteException(ErrorMessage.ERROR_DBMS);
                } catch (Exception e) {
                    throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
                }
            }
        }

        return false;
    }
}