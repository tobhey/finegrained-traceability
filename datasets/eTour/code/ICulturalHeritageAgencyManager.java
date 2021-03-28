package unisa.gps.etour.control.CulturalHeritageManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanVisitBC;

/**
 * Interface for operations peculiar cultural heritage by Operator Agency.
 *
 */
public interface ICulturalHeritageAgencyManager extends ICulturalHeritageCommonManager {
    /**
     * Method for the insertion of a new cultural
     *
     * @Param pCulturalHeritage The raw bean to be included in the database
     * @Return boolean The result of the operation; true if was successful, false
     *         otherwise
     */
    public boolean insertCulturalHeritage(BeanCulturalHeritage pCulturalHeritage) throws RemoteException;

    /**
     * Method for the clearing of a cultural object by id
     *
     * @Param Id pCulturalHeritageID the bean to be deleted
     * @Return boolean The result of the operation; true if was successful, false
     *         otherwise
     */
    public boolean clearCulturalHeritage(int pCulturalHeritageID) throws RemoteException;

    /**
     * Method for the return of all cultural property in the Database
     *
     * @Return ArrayList all the beans in the database
     */
    public ArrayList<BeanCulturalHeritage> getCulturalHeritage() throws RemoteException;

    /**
     * Method for updatang (or change) the data of a cultural
     *
     * @Param pCulturalHeritage The bean with the new information of the cultural
     * @Return boolean The result of the operation; true if was successful, false
     *         otherwise
     */
    public boolean modifyCulturalHeritage(BeanCulturalHeritage pCulturalHeritage) throws RemoteException;

    /**
     * Method for setting a tag to a certain cultural
     *
     * @Param pCulturalHeritageID The identifier of the cultural object to which to add
     *        a tag
     * @Param pTagID The ID tag to add to the cultural indicated
     * @Return boolean The result of the operation; true if was successful, false
     *         otherwise
     */
    public boolean addTagCulturalHeritage(int pCulturalHeritageID, int pTagID) throws RemoteException;

    /**
     * Method for removing a tag from a certain cultural To ensure that 'the
     * operation is successful it is necessary that the cultural property has
     * Actually set the specified tag
     *
     * @Param pCulturalHeritageID The identifier of the cultural object from which to
     *        remove the tag
     * @Param pTagID The ID tag to be removed from the cultural indicated
     * @Return boolean The result of the operation; true if was successful, false
     *         otherwise
     */
    public boolean removeTagCulturalHeritage(int pCulturalHeritageID, int pTagID) throws RemoteException;
}