package unisa.gps.etour.control.CulturalHeritageManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.DBCulturalHeritage;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.repository.DBVisitBC;
import unisa.gps.etour.repository.IDBCulturalHeritage;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.repository.IDBVisitBC;
import unisa.gps.etour.util.GlobalConstants;
import unisa.gps.etour.util.ErrorMessage;


/**
 * Class management of cultural heritage for operations common to all actors
 *
 */
public class CulturalHeritageCommonManager extends UnicastRemoteObject implements ICulturalHeritageCommonManager {
// Connect to DB for Cultural Heritage
    protected IDBCulturalHeritage dbbc;

// Connect to DB Tag
    protected IDBTag dbtag;

// Connect to DB for the Feedback / Visits
    protected IDBVisitBC dbvisita;

// Connect to DB for Tourists
    protected IDBTourist dbturista;

    /**
     * Constructor; you instantiate all fields relevant to data management; Fields
     * are initialized for each instance of the class.
     *
     */
    public CulturalHeritageCommonManager() throws RemoteException {
        super();
        try {
// We instantiate objects
            dbbc = new DBCulturalHeritage();
            dbtag = new DBTag();
            dbvisita = new DBVisitBC();
            dbturista = new DBTourist();
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

    /*
     * Implements the method for obtaining a cultural object by Id
     *
     * @See
     * unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageCommon #
     * getCulturalHeritage (int)
     */
    public BeanCulturalHeritage getCulturalHeritage(int pCulturalHeritageID) throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        BeanCulturalHeritage bbc = null;

        try {
            bbc = dbbc.getCulturalHeritage(pCulturalHeritageID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

        return bbc;
    }

    /*
     * Implements the method for obtaining all the tags of a cultural object.
     *
     * @See
     * unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageCommon #
     * getTagCulturalHeritage (int)
     */

    public ArrayList<BeanTag> getTagCulturalHeritage(int pCulturalHeritageID) throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        ArrayList<BeanTag> btag = null;

        try {
            btag = dbtag.getTagCulturalHeritage(pCulturalHeritageID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

        return btag;
    }

    /*
     * Implements the method to obtain the list of feedback and their username on a
     * property Cultural specified by Id
     *
     * @See
     * unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageCommon #
     * getFeedbackCulturalHeritage (int)
     */

    public HashMap<BeanVisitBC, String> getFeedbackCulturalHeritage(int pCulturalHeritageID) throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        HashMap<BeanVisitBC, String> mapReturn;

        try {
// Instantiate the map of the same size as the list of BeanVisitBC
            mapReturn = new HashMap<BeanVisitBC, String>(dbvisita.getListVisitBC(pCulturalHeritageID).size());

// For each visit by adding their username
// Here we begin to iterate to add to any visit their username
            for (BeanVisitBC b : dbvisita.getListVisitBC(pCulturalHeritageID))
                mapReturn.put(b, dbturista.getTourist(b.getIdTourist()).getUserName());

        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

        return mapReturn;
    }

    /*
     * Implements the method to obtain statistics about a cultural past Through Id
     *
     * @See
     * unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageCommon #
     * getStatisticCulturalHeritage (int)
     */

    public ArrayList<Integer> getStatisticCulturalHeritage(int pCulturalHeritageID) throws RemoteException {
        /*
         * This method returns an ArrayList containing 5 elements (0 .. 4). For each
         * index more 'one is the number of equivalent value your feedback Index number
         * more 'one. Even in this case the method is not 'particularly attractive but
         * it does its dirty work End.
         */

        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        ArrayList<Integer> listResult = new ArrayList<Integer>(5);

// Set all the indices to 0
        for (int i = 0; i < 5; i++)
            listResult.add(i, Integer.valueOf(0));

// Calculate the date for the last thirty days
        Date lastThirtyDays = new Date(new Date().getTime() - GlobalConstants.THIRTY_DAYS);
        try {
// Get all visits of a certain cultural
// Loop looking for the requests / feedback obtained within the last thirty days
            for (BeanVisitBC b : dbvisita.getListVisitBC(pCulturalHeritageID)) {
                if (b.getDataVisit().after(lastThirtyDays)) {
                    listResult.set(b.getRating() - 1,
                    Integer.valueOf(listResult.get(b.getRating() - 1).intValue() + 1));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

        return listResult;
    }

    /*
     * Implement the method for changing a feedback on a cultural past Through Id
     *
     * @See
     * unisa.gps.etour.control.ManagerCulturalHeritage.IManagerCulturalHeritageCommon #
     * modifyFeedbackCulturalHeritage (int, unisa.gps.etour.bean.BeanVisitBC)
     */
    public boolean modifyFeedbackCulturalHeritage(int pCulturalHeritageID, BeanVisitBC pBeanVisitBC)
            throws RemoteException {
        if (!CulturalHeritageChecker.checkIdCulturalHeritage(pCulturalHeritageID)
                || !CulturalHeritageVisitChecker.checkDataVisitCulturalHeritage(pBeanVisitBC)) {
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        }
        /*
         * Please check that the vote has not changed. If the vote is changed to an
         * exception is raised
         */
        boolean ratingOk = true;

        try {
            ratingOk = dbvisita.getVisitBC(pCulturalHeritageID, pBeanVisitBC.getIdTourist()).getRating() == pBeanVisitBC
                    .getRating();
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }

        /*
         * If the vote is not changed we proceed to send the message to the method of
         * Change the layer's database.
         */
        if (ratingOk)
            try {
                return (dbvisita.modifyVisitBC(pBeanVisitBC));
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }

        return false;
    }
}