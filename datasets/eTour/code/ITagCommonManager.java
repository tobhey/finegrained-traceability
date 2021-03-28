
package unisa.gps.etour.control.TagManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTag;

/**
 * Common interface for managing Tag
 *
 * @Author Joseph Morelli @Version 0.1 2007 eTour Project - Copyright by SE @
 * SA Lab DMI University Of Salerno
 */
public interface ITagCommonManager extends Remote {

    /**
     * Method which returns all the tags stored in the database
     *
     * @Return an ArrayList of BeanTag @Throws RemoteException Exception Remote
     */
    public ArrayList<BeanTag> getTags() throws RemoteException;

}