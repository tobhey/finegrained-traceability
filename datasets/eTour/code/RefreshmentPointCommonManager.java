package unisa.gps.etour.control.RefreshmentPointManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitPR;
import unisa.gps.etour.repository.DBRefreshmentPoint;
import unisa.gps.etour.repository.DBVisitPR;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.IDBRefreshmentPoint;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.repository.IDBVisitPR;
import unisa.gps.etour.util.GlobalConstants;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class that implements the common tasks for the operator of dining and For the
 * Operator Agency
 *
 */
public class RefreshmentPointCommonManager extends UnicastRemoteObject implements IRefreshmentPointCommonManager {

// Instance for database connections
    private static final long serialVersionUID = 1L;
    protected IDBRefreshmentPoint refreshmentPoint;
    protected IDBTag tag;
    protected IDBVisitPR feed;
    protected IDBTourist dbTourist;

    public RefreshmentPointCommonManager() throws RemoteException {
        super();
// Connect to the Database
        try {
            refreshmentPoint = new DBRefreshmentPoint();
            tag = new DBTag();
            feed = new DBVisitPR();
        }
// Note: no longer 'cause SQLException thrown
// Changes to the layer DB; changed Exception
        catch (Exception e) {
            System.out.println("Error:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
    }

    /*
     * Method which allows to obtain a BeanRefreshmentPoint through Connect to
     * database
     */
    public BeanRefreshmentPoint getRefreshmentPoint(int pRefreshmentPointID) throws RemoteException {
// Check identifier passed
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Return Instance
        BeanRefreshmentPoint toReturn = null;
        try {
// Revenue data through the instance of the database connection
            toReturn = refreshmentPoint.getRefreshmentPoint(pRefreshmentPointID);
        }
// Exception in database operations
        catch (SQLException e) {
            System.out.println("Error:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error:" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the variable return, so they do not pass bad data
// To the caller
// And triggers an exception if the format of the bean
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
// Return the bean that contains information about Refreshment
// Required
        return toReturn;
    }

// Method that returns an ArrayList containing the tag identification of a
// Some Refreshment

    public ArrayList<BeanTag> getTagRefreshmentPoint(int pRefreshmentPointID) throws RemoteException {
// Check identifier passed
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Return Instance
        ArrayList<BeanTag> toReturn = null;
        try {
// Revenue data through the instance of the database connection
            toReturn = tag.getTagRefreshmentPoint(pRefreshmentPointID);
        }
// Exception in operations on database
        catch (SQLException e) {
            System.out.println("Error:" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error:" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the variable return, so they do not pass bad data
// To the caller
// And triggers an exception if the format of the bean
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
// Return the ArrayList containing beans tag of refreshment
// Passed as parameter
        return toReturn;
    }

// Returns an array of 10 strings containing the last 10 comments
// Issued by tourists for Refreshment passed as parameter
    public String[] getLastComments(int pRefreshmentPointID) throws RemoteException {
// Check the validity identifier passed
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// String that contains only the last 10 comments made
        String[] toReturn = new String[10];
// ArrayList temporary then save the comments contained in the beans
// Inside
        ArrayList<BeanVisitPR> temp = null;
        try {
// Attempt to retrieve information from the database via
// The connection instance
            temp = feed.getListVisitPR(pRefreshmentPointID);
        }
// Exception in database operations
        catch (SQLException e) {
            System.out.println("Error in method getLastComments" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method getLastComments" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// The bean that I receive from the database are already sorted by date
// Implicitly, so there is no need for sorting algorithms
        for (int i = 0; i < 10; i++) {
// Insert the text of the comments in cyclically
// Array
            toReturn[i] = (temp.get(temp.size() - (i + 1))).getComment();
        }
// E 'unnecessary control the format of the array
// Then return the array directly with comments
        return toReturn;
    }

// Method that returns an ArrayList containing the number of votes
// Release from 1 to 5 for
// The Refreshment passed as parameter. The method inserts in order
// Counters in positions
// From 0 to 1 vote, 1 vote for 2, etc. ..
    public ArrayList<Integer> getStatisticRefreshmentPoint(int pRefreshmentPointID) throws RemoteException {
// Check the validity identifier passed
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// ArrayList that allows me to store the bean containing the feedback
        ArrayList<BeanVisitPR> bVisit = null;
// ArrayList that allows me to store the counters
// And then return to the calling method
        ArrayList<Integer> listResult = new ArrayList<Integer>(5);
// Initialize the array of counters
        for (int i = 0; i < 5; i++)
            listResult.add(Integer.valueOf(0));
// Data useful to verify that the issued date back thirty Feedback
// Days from current date
        Date ultimiTrentaGiorni = new Date(new Date().getTime() - GlobalConstants.THIRTY_DAYS);

        System.out.println("The date of 30 days ago:" + ultimiTrentaGiorni);

        try {
// All feedback Revenue issued for Refreshment
// Passed as parameter
            bVisit = feed.getListVisitPR(pRefreshmentPointID);
// Iterate the collection of elements to control the bean
// Visits
            for (Iterator<BeanVisitPR> iteratoreVisitPR = bVisit.iterator(); iteratoreVisitPR.hasNext();) {
// Recuperto the BeanVisitPR
                BeanVisitPR bVisitTemp = iteratoreVisitPR.next();
// ... Do not know what does ...
                System.out.println("The date of this visit is:" + bVisitTemp.getDataVisit());
                if (bVisitTemp.getDataVisit().after(ultimiTrentaGiorni))
                    listResult.set(bVisitTemp.getRating() - 1,
                            Integer.valueOf(listResult.get(bVisitTemp.getRating() - 1).intValue() + 1));
            }
        }
// Exception in database operations
        catch (SQLException e) {
            System.out.println("Error in method getStatisticRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method getStatisticRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
        if (null == listResult)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
// Return the list of results that should contain counters
// Comments
// Issued in the last 30 days
        return listResult;
    }

// Method that allows you to change the comment issued for a
// Refreshment
    public boolean modifyFeedbackRefreshmentPoint(int pRefreshmentPointId, BeanVisitPR newVisit)
            throws RemoteException {
// Check the validity of past data
        if ((pRefreshmentPointId < 0) || (!(newVisit instanceof BeanVisitPR)))
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Revenue from the database the bean and stores saved
// In order to verify that the vote has not changed
// Which had been previously released
        BeanVisitPR temp = null;
        try {
            temp = feed.getVisitPR(pRefreshmentPointId, newVisit.getIdTourist());
        }
// Exception running the operation on Database
        catch (SQLException e) {
            System.out.println("Error in method modifyFeedbackRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exception caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method modifyFeedbackRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If the vote was amended returns an exception
        if (temp.getRating() != newVisit.getRating()) {
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        }
// If the vote has not been amended shall save the new
// Comment in the database using the specific method of the bean
        else {
            try {
                feed.modifyVisitPR(newVisit);
                return true;
            }
// Exception running the operation on Database
            catch (SQLException e) {
                System.out.println("Error in method modifyFeedbackRefreshmentPoint" + e.toString());
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            }
// Unexpected exception caused by other factors
            catch (Exception ee) {
                System.out.println("Error in method modifyFeedbackRefreshmentPoint" + ee.toString());
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
    }
}
