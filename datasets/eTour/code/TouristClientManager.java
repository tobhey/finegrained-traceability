package unisa.gps.etour.control.RegisteredUserManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanSearchPreference;
import unisa.gps.etour.bean.BeanGenericPreference;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;
import unisa.gps.etour.repository.DBCulturalHeritage;
import unisa.gps.etour.repository.DBSearchPreference;
import unisa.gps.etour.repository.DBGenericPreference;
import unisa.gps.etour.repository.DBRefreshmentPoint;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.repository.DBVisitBC;
import unisa.gps.etour.repository.DBVisitPR;
import unisa.gps.etour.repository.IDBCulturalHeritage;
import unisa.gps.etour.repository.IDBSearchPreference;
import unisa.gps.etour.repository.IDBGenericPreference;
import unisa.gps.etour.repository.IDBRefreshmentPoint;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.repository.IDBVisitBC;
import unisa.gps.etour.repository.IDBVisitPR;
import unisa.gps.etour.util.ControlData;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class on the Management of Tourist Information
 *
 */

public class TouristClientManager extends TouristCommonManager implements ITouristClientManager {
    private static final long serialVersionUID = -6161592850721537385L;
    private IDBTourist profileTourist; // Data Management for tourists
    private IDBGenericPreference prefGenTourist; // preferences, general manager of the tourist
    private IDBSearchPreference prefRicTourist; // Handle search preferences of tourists
    private IDBVisitBC visitedBC; // Managing cultural Visited
    private IDBVisitPR visitedPR; // Manager catering outlets visited
    private IDBCulturalHeritage culturalHeritage; // Managing cultural heritage (we need only obtain a bean CulturalHeritage x
// from its ID
    private IDBRefreshmentPoint refreshmentPoint; // Management refreshment areas (use the same object "culturalHeritage"

    public TouristClientManager() throws RemoteException {
        super();

        profileTourist = new DBTourist();
        prefGenTourist = new DBGenericPreference();
        prefRicTourist = new DBSearchPreference();
        visitedBC = new DBVisitBC();
        visitedPR = new DBVisitPR();
        culturalHeritage = new DBCulturalHeritage();
        refreshmentPoint = new DBRefreshmentPoint();
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # clearSearchPreference (int, Int)
     */
    public BeanSearchPreference clearSearchPreference(int pIdTourist, int pIdSearchPreference)
            throws RemoteException {
        try {
            boolean delete = true;
            BeanSearchPreference PrefRic = new BeanSearchPreference();
            ArrayList<BeanSearchPreference> listPrefRic = new ArrayList<BeanSearchPreference>();

            listPrefRic = prefRicTourist.getSearchPreferenceDelTourist(pIdTourist);
            Iterator<BeanSearchPreference> list = listPrefRic.iterator();

            while (list.hasNext() && delete == true) {
                PrefRic = list.next();
                if (PrefRic.getId() == pIdSearchPreference)
                    delete = false; // Find the anniversary with the id
// We leave the interested
// Cycle
            }

            delete = prefRicTourist.clearSearchPreferenceTourist(pIdTourist, pIdSearchPreference);
            return PrefRic;
        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }

    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # clearGenericPreference (int)
     */
    public BeanGenericPreference clearGenericPreference(int pIdTourist) throws RemoteException {
        try {
            BeanGenericPreference temp = prefGenTourist.getGenericPreference(pIdTourist);
            boolean canceled = prefGenTourist.clearGenericPreference(temp.getId());

            if (canceled)
                return temp;
            else
                return null; // The clearing occurred
        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }

    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertSearchPreference (int,
     * Unisa.gps.etour.bean.BeanSearchPreference)
     */
    public boolean insertSearchPreference(int pIdTourist, BeanSearchPreference pSearchPreference)
            throws RemoteException {
        try {
            boolean checkdate = ControlData.checkBeanSearchPreference(pSearchPreference);

            if (checkdate) {// If the data control is positive
                return prefRicTourist.insertSearchPreferenceDelTourist(pIdTourist, pSearchPreference.getId());
            } else {
                return false; // Data error
            }

        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertGenericPreference (unisa.gps.etour.bean.BeanGenericPreference)
     */
    public boolean insertGenericPreference(BeanGenericPreference pGenericPreference) throws RemoteException {
        try {
            boolean checkdate = ControlData.checkBeanGenericPreference(pGenericPreference);

            if (checkdate) {// If the data control is positive
                return prefGenTourist.insertGenericPreference(pGenericPreference);
            } else {
                return false; // Data error
            }

        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertTourist (unisa.gps.etour.bean.BeanTourist)
     */
    public boolean insertTourist(BeanTourist pTourist) throws RemoteException {
        try {

// Check if the username entered is already present in DB
            BeanTourist temp = profileTourist.getTourist(pTourist.getUserName());

// If there is no choice all'username Tourist Offices
            if (temp == null) {
                boolean checkdate = ControlData.checkBeanTourist(pTourist);

                if (checkdate) {// If the data control is positive
                    return profileTourist.insertTourist(pTourist);
                } else {
                    return false; // Data error
                }
            } else {
                return false; // Username already exists in DB
            }

        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # modifyGenericPreference (unisa.gps.etour.bean.BeanGenericPreference)
     */
    public boolean modifyGenericPreference(BeanGenericPreference pGenericPreferenceNuove)
            throws RemoteException {
        try {
            boolean checkdate = ControlData.checkBeanGenericPreference(pGenericPreferenceNuove);

            if (checkdate) {// If the data control is positive
                return prefGenTourist.modifyGenericPreference(pGenericPreferenceNuove);
            } else {
                return false; // Data error
            }

        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # getSearchPreference (int)
     */
    public BeanSearchPreference[] getSearchPreference(int pIdTourist) throws RemoteException {
// Array containing the list of search preferences tourists ID = pIdTourist
        BeanSearchPreference[] preferences = null;

        try {
//** Convert ArrayList (return type of method "getSearchPreference") to simple array **
            ArrayList<BeanSearchPreference> listPreference = prefRicTourist
                    .getSearchPreferenceDelTourist(pIdTourist);
            preferences = new BeanSearchPreference[listPreference.size()];
            preferences = listPreference.toArray(preferences);
//** ** End Conversion
        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }

        return preferences;

    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # getGenericPreference (int)
     */
    public BeanGenericPreference getGenericPreference(int pIdTourist) throws RemoteException {
        try {
            return prefGenTourist.getGenericPreference(pIdTourist);
        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.ManagerTouristCommon #
     * modifyTourist (unisa.gps.etour.bean.BeanTourist)
     */
    public boolean modifyTourist(BeanTourist pprofileTourist) throws RemoteException {
        try {
// Boolean variable that will hold true if the bean is correct, false otherwise
            boolean checkdate = ControlData.checkBeanTourist(pprofileTourist);

            if (checkdate) {// If the data control is positive
                return profileTourist.modifyTourist(pprofileTourist);
            } else {
                return false; // Data error
            }

        } catch (SQLException e) {
// If the data layer is thrown an exception SQLException
// It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }

    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # getCulturalHeritageVisit (int)
     */
    public BeanVisitBC[] getCulturalHeritageVisit(int pIdTourist) throws RemoteException {
        BeanVisitBC[] visited = null;
        if (pIdTourist > 0) {
            try {
                ArrayList<BeanVisitBC> listvisited = visitedBC.getListVisitBCTourist(pIdTourist);
                visited = new BeanVisitBC[listvisited.size()];
                visited = listvisited.toArray(visited);
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }

        }
        return visited;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # getRefreshmentPointVisit (int)
     */
    public BeanVisitPR[] getRefreshmentPointVisit(int pIdTourist) throws RemoteException {
// Array containing the list of catering outlets visited by tourists with id "pIdTourist"
        BeanVisitPR[] visited = null;
        if (pIdTourist > 0) {
            try {
                ArrayList<BeanVisitPR> listvisited = visitedPR.getListVisitPRTourist(pIdTourist);
                visited = new BeanVisitPR[listvisited.size()];
                visited = listvisited.toArray(visited);
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }

        }
        return visited;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertCulturalHeritageVisit (unisa.gps.etour.bean.BeanVisitBC)
     */
    public boolean insertCulturalHeritageVisit(BeanVisitBC pVisitBC) throws RemoteException {
        if (ControlData.checkBeanVisitBC(pVisitBC)) {
            try {

                return visitedBC.insertVisitBC(pVisitBC);
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return false;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertRefreshmentPointVisit (unisa.gps.etour.bean.BeanVisitPR)
     */
    public boolean insertRefreshmentPointVisit(BeanVisitPR pVisitPR) throws RemoteException {
        if (ControlData.checkBeanVisitPR(pVisitPR)) {
            try {
                return visitedPR.insertVisitPR(pVisitPR);
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return false;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertCulturalHeritagePreference (int, Int)
     */
    public boolean insertCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws RemoteException {
        if (pIdTourist > 0 && pIdCulturalHeritage > 0) {
            try {
                return profileTourist.insertCulturalHeritagePreference(pIdTourist, pIdCulturalHeritage);
            } catch (SQLException e) {

                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return false;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # insertRefreshmentPointPreference (int, Int)
     */
    public boolean insertRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws RemoteException {
        if (pIdTourist > 0 && pIdRefreshmentPoint > 0) {
            try {
                return profileTourist.insertRefreshmentPointPreference(pIdTourist, pIdRefreshmentPoint);
            } catch (SQLException e) {

                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return false;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # clearCulturalHeritagePreference (int, Int)
     */
    public boolean clearCulturalHeritagePreference(int pIdTourist, int pIdCulturalHeritage) throws RemoteException {
        if (pIdTourist > 0 && pIdCulturalHeritage > 0) {
            try {
                return profileTourist.clearCulturalHeritagePreference(pIdTourist, pIdCulturalHeritage);
            } catch (SQLException e) {

                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return false;
    }

    /*
     * (Non-Javadoc)
     *
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # clearRefreshmentPointPreference (int, Int)
     */
    public boolean clearRefreshmentPointPreference(int pIdTourist, int pIdRefreshmentPoint) throws RemoteException {
        if (pIdTourist > 0 && pIdRefreshmentPoint > 0) {
            try {
                return profileTourist.clearRefreshmentPointPreference(pIdTourist, pIdRefreshmentPoint);
            } catch (SQLException e) {

                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return false;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # getCulturalHeritagePreference (int)
     */
    public BeanCulturalHeritage[] getCulturalHeritagePreference(int pIdTourist) throws RemoteException {
        if (pIdTourist > 0) {
            try {
// List of cultural favorites of tourists cn id = "pIdTourist"
                ArrayList<Integer> listPreference = profileTourist.getCulturalHeritagePreference(pIdTourist);
// Convert ArrayList a simple array
                BeanCulturalHeritage[] favorites = null;
                favorites[listPreference.size()] = new BeanCulturalHeritage();
                favorites = listPreference.toArray(favorites);

                int k = 0; // Cycle counter
                for (Integer i : listPreference) {
// Fill the array with the favorite bean of all cultural favorites from the tourist
                    favorites[k++] = culturalHeritage.getCulturalHeritage(i);
                }
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return null;
    }

    /*
     * (Non-Javadoc)
     * 
     * @See unisa.gps.etour.control.ManagerRegisteredUsers.IManagerTouristClient
     * # getRefreshmentPointPreference (int)
     */
    public BeanRefreshmentPoint[] getRefreshmentPointPreference(int pIdTourist) throws RemoteException {
        if (pIdTourist > 0) {
            try {
// List of places for refreshment favorite tourist id = "pIdTourist"
                ArrayList<Integer> listPreference = profileTourist.getRefreshmentPointPreference(pIdTourist);

// Convert ArrayList a simple array
                BeanRefreshmentPoint[] favorites = null;
                favorites[listPreference.size()] = new BeanRefreshmentPoint();
                favorites = listPreference.toArray(favorites);

                int k = 0; // Cycle counter
                for (Integer i : listPreference) {
// Fill the array with the favorite bean of all cultural favorites from the tourist
                    favorites[k++] = refreshmentPoint.getRefreshmentPoint(i);
                }
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
        return null;
    }
}
