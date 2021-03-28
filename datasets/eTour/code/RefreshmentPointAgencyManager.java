package unisa.gps.etour.control.RefreshmentPointManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitPR;
import unisa.gps.etour.repository.DBConvention;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.repository.IDBConvention;
import unisa.gps.etour.util.ErrorMessage;

/**
 * Class  methods for managing Refreshments by Operator Agency
 *
 */
public class RefreshmentPointAgencyManager extends RefreshmentPointCommonManager
        implements IRefreshmentPointAgencyManager {

    private static final long serialVersionUID = 1L;

// Constructor
    public RefreshmentPointAgencyManager() throws RemoteException {
// Call the constructor of the inherited class to instantiate
// Database connections
        super();
        dbTourist = new DBTourist();
    }

// Method that allows the operator to cancel an agency point of
// Refreshment
// Passing as parameter the ID of the same Refreshment
    public boolean clearRefreshmentPoint(int pRefreshmentPointID) throws RemoteException {
// Check the validity identifier
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        try {
// Execute the method that clears the Refreshment from the Database
// And in case of operation successful return true
            if (refreshmentPoint.clearRefreshmentPoint(pRefreshmentPointID))
                return true;
        }
// Exception in operations on database
        catch (SQLException e) {
            System.out.println("Error in method clearRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method clearRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If no operations were successful return false end
        return false;
    }

// Method that allows the operator to include in the Agency database
// The new Refreshment with the information contained in the bean
    public boolean insertRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPoint) throws RemoteException {
// Check the validity of the bean as a parameter and if
// Triggers except remote
        if ((pRefreshmentPoint == null) || (!(pRefreshmentPoint instanceof BeanRefreshmentPoint)))
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        try {
// Calling the method of the class that operates on the database
// Insert the new Refreshment
            if (refreshmentPoint.insertRefreshmentPoint(pRefreshmentPoint))
// In the case where the operations were successful end
// Returns true
                return true;
        }
// Exception in database operations
        catch (SQLException e) {
            System.out.println("Error in method insertRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method insertRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If the operation is not successful return false
        return false;
    }

// Method for obtaining an ArrayList with all the points Bean
// Refreshments
    public ArrayList<BeanRefreshmentPoint> getRefreshmentPoint() throws RemoteException {
// ArrayList to return to the end of the method
        ArrayList<BeanRefreshmentPoint> toReturn = null;
        try {
// Get the list of Refreshments through the class
// Connect to database
// And save the list itself nell'ArrayList
            toReturn = refreshmentPoint.getListPR();
        }
// Exception in operations on database
        catch (SQLException e) {
            System.out.println("Error in method getRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method getRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the ArrayList to return so as not to pass null values
// To the caller
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
// Return the ArrayList with all the refreshment
        return toReturn;
    }

// Method that allows you to get all the refreshment that have
// A Convention on or off depending on the parameter passed
    public ArrayList<BeanRefreshmentPoint> getRefreshmentPoint(boolean statusConvention) throws RemoteException {
// Array that allows me to store all the refreshment and
// Which will remove
// Depending on the parameter passed to the refreshment active or not
        ArrayList<BeanRefreshmentPoint> toReturn = null;
// Array that allows me to store all the refreshment active
// Using the database connection
        ArrayList<BeanRefreshmentPoint> active = null;
// Instance to connect to the database
        IDBConvention conv = new DBConvention();
        try {
// Connect all proceeds from the refreshment Assets
            active = conv.getListConventionActivePR();
        }
// Exception in operations on database
        catch (SQLException e) {
            System.out.println("Error in method getRefreshmentPoint (boolean)" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method getRefreshmentPoint (boolean)" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If you want to get the refreshment active, then return
// Directly to those passed by the connection to the database
        if (statusConvention) {
// Check the contents dell'ArrayList so as not to return
// Null values to the caller
            if (active == null)
                throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
            return active;
        } else {
            try {
// Connect all proceeds from the refreshment then
// Perform comparisons
                toReturn = refreshmentPoint.getListPR();
            }
// Exception in operations on database
            catch (SQLException e) {
                System.out.println("Error in method getRefreshmentPoint (boolean)" + e.toString());
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            }
// Unexpected exceptions due to other factors
            catch (Exception ee) {
                System.out.println("Error in method getRefreshmentPoint (boolean)" + ee.toString());
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
// Size ArrayList containing all of gourmet
// Could
// Change size if you remove some element
            int dim = toReturn.size();
// Variable that allows me to understand whether to remove a Point
// Refreshments
// From array that then I must return
            boolean present = false;
// First loop to loop through all the ArrayList elements of
// All Refreshments
            for (int i = 0; i < dim; i++) {
// Second loop to loop through all the ArrayList elements
// Cones just Refreshments active
                for (int j = 0; j < active.size(); j++) {
// If the catering points in question has the ID equal to one
// Of those assets, then set this to true
                    if (active.get(j).getId() == toReturn.get(i).getId())
                        present = true;
                }
// If the catering points in question has a Convention active
// Removes it from those to be returned
                if (present)
                    toReturn.remove(i);
            }
        }
// Return the ArrayList obtained
        return toReturn;
    }

// Method that allows you to change the past as a refreshment
// Parameter
    public boolean modifyRefreshmentPoint(BeanRefreshmentPoint pRefreshmentPointupdated) throws RemoteException {
// Check the validity of the bean as a parameter and if
// Trigger an exception remote
        if (null == pRefreshmentPointupdated || (!(pRefreshmentPointupdated instanceof BeanRefreshmentPoint)))
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        try {
// Call the method to change the database connection
// The Refreshment
            if (refreshmentPoint.modifyRefreshmentPoint(pRefreshmentPointupdated))
// Return a positive value if the operation was successful
// End
                return true;
        }
// Exception in operations on database
        catch (SQLException e) {
            System.out.println("Error in method modifyRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions due to other factors
        catch (Exception ee) {
            System.out.println("Error in method modifyRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Return false if the operation is successful you should
        return false;
    }

// Method to obtain the Bean a particular point
// Refreshment whose
// Identifier is passed as parameter
    public BeanRefreshmentPoint getRefreshmentPoint(int pRefreshmentPointID) throws RemoteException {
// Check the validity identifier
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Bean to return to the caller
        BeanRefreshmentPoint toReturn = null;
        try {
// Revenue catering points in the issue by connecting to
// Database
            toReturn = refreshmentPoint.getRefreshmentPoint(pRefreshmentPointID);
        }
// Exception in the database opearazioni
        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the bean to be returned in order not to return null values
// To the caller
        if (null == toReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
// Return the bean of Refreshment
        return toReturn;
    }

// Method that allows you to activate a particular convention to a Point
// Passed as parameter Refreshments
    public boolean activeConvention(int pRefreshmentPointID, BeanConvention pConv) throws RemoteException {
// Check the validity of parameters passed
        if ((pRefreshmentPointID < 0) || (pConv == null) || (!(pConv instanceof BeanConvention)))
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Check the data further
        if (pConv.getIdRefreshmentPoint() != pRefreshmentPointID)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
        IDBConvention conv = null;
        try {
// Instantiate the class to connect to the database
            conv = new DBConvention();
// If the Convention is not yet active, previously provided to
// Activate it locally and then pass the bean to the database changed
            if (conv.getConventionActive(pRefreshmentPointID) == null) {
                pConv.setActive(true);
                conv.modifyConvention(pConv);
                return true;
            }
        }
// Exception in operations on database
        catch (SQLException e) {
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method activeConvention" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// If the operation is successful you should return false
        return false;
    }

// Method that returns a HashMap containing, for Refreshment
// Passed as a parameter, the feedback associated with it
    public HashMap<BeanVisitPR, String> getFeedbackRefreshmentPoint(int pRefreshmentPointID) throws RemoteException {
// Check the ID passed as a parameter
        if (pRefreshmentPointID < 0)
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// Instantiate the map and the performance of ArrayList that I will use
// Method
        HashMap<BeanVisitPR, String> mapReturn = null;
        ArrayList<BeanVisitPR> bvisita = null;
        try {
// Here I take the list of all visits to the PR passed as
// Parameter
            bvisita = feed.getListVisitPR(pRefreshmentPointID);
// Instantiate the map of the same size as the list of
// BeanVisitPR
            mapReturn = new HashMap<BeanVisitPR, String>(bvisita.size());
// Here we begin to iterate on each visit to add its
// Username
            for (Iterator<BeanVisitPR> iteratoreVisitPR = bvisita.iterator(); iteratoreVisitPR.hasNext();) {
//  the BeanVisitPR
                BeanVisitPR bVisitTemp = iteratoreVisitPR.next();
// Retrieve the tourist who left the comment that I
// Examining
                BeanTourist bTouristTemp = dbTourist.getTourist(bVisitTemp.getIdTourist());
// Get the username of the Tourist
                String usernameTouristTemp = bTouristTemp.getUserName();
// Put the pair in the map
                mapReturn.put(bVisitTemp, usernameTouristTemp);
            }
        }
// Exception in database operations
        catch (SQLException e) {
            System.out.println("Error in method getFeedbackPR" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exceptions caused by other factors
        catch (Exception ee) {
            System.out.println("Error in method getFeedbackPR" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check the return parameter so as not to pass null values
// To the database
        if (null == mapReturn)
            throw new RemoteException(ErrorMessage.ERROR_FORMAT_BEAN);
        return mapReturn;
    }

// Method to insert a tag from those of a refreshment
    public boolean clearTagRefreshmentPoint(int pRefreshmentPointId, int pTagId) throws RemoteException {
// Check the validity of past data
        if ((pRefreshmentPointId < 0) || (pTagId < 0))
            throw new RemoteException(ErrorMessage.ERROR_DATA);
// ArrayList which stores all the tags
        ArrayList<BeanTag> tags;
// Boolean variable to check if the Refreshment
// Holds the tag you want to delete
        boolean present = false;
        try {
// Use the method through the class of database connection
            tags = tag.getTagRefreshmentPoint(pRefreshmentPointId);
        }
// Exception in the execution of transactions in database
        catch (SQLException e) {
            System.out.println("Error in method clearTagRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        }
// Unexpected exception due to other factors
        catch (Exception ee) {
            System.out.println("Error in method clearTagRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
// Check if the tag is present cycle currently
// Between those of Refreshment
        for (BeanTag t : tags)
            if (t.getId() == pTagId)
                present = true;
// If the tag is present among those of eateries, then
// Provides for executing the erase operation
        if (present) {
            try {
                return tag.clearTagRefreshmentPoint(pRefreshmentPointId, pTagId);
            }
// Exception in implementing the operation on the database
            catch (SQLException e) {
                System.out.println("Error in method clearTagRefreshmentPoint" + e.toString());
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            }
// Unexpected exception due to other factors
            catch (Exception ee) {
                System.out.println("Error in method clearTagRefreshmentPoint" + ee.toString());
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
// In case something did not come to fruition
// Return false
        return false;
    }

// Method to delete a tag from those of a refreshment
// The operations are identical to those above, except for
// Control over the presence of the tag from those of Refreshment
// Which should give negative results, and the call here is the method of
// Insert
    public boolean insertTagRefreshmentPoint(int pRefreshmentPointId, int pTagId) throws RemoteException {
        if ((pRefreshmentPointId < 0) || (pTagId < 0))
            throw new RemoteException(ErrorMessage.ERROR_DATA);

        ArrayList<BeanTag> tags;
        boolean present = false;
        try {
            tags = tag.getTagRefreshmentPoint(pRefreshmentPointId);
        } catch (SQLException e) {
            System.out.println("Error in method insertTagRefreshmentPoint" + e.toString());
            throw new RemoteException(ErrorMessage.ERROR_DBMS);
        } catch (Exception ee) {
            System.out.println("Error in method insertTagRefreshmentPoint" + ee.toString());
            throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
        }
        for (BeanTag t : tags)
            if (t.getId() == pTagId)
                present = true;
// Check that the Refreshment has not already specified tag
        if (present) {
            try {
// Calling the method of adding the class via
// Connect to database
                return tag.addTagRefreshmentPoint(pRefreshmentPointId, pTagId);
            } catch (SQLException e) {
                System.out.println("Error in method insertTagRefreshmentPoint" + e.toString());
                throw new RemoteException(ErrorMessage.ERROR_DBMS);
            } catch (Exception ee) {
                System.out.println("Error in method insertTagRefreshmentPoint" + ee.toString());
                throw new RemoteException(ErrorMessage.ERROR_UNKNOWN);
            }
        }
// Return false if some operation is not successful you should
        return false;
    }
}
