	
/**
  * Interface that provides management services dell'advertisement
  * Operator agency.
  *
*/
package unisa.gps.etour.control.AdvertisementManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanNews;

public interface IAdvertisementAgencyManager extends IAdvertisementManager
{
/**
* Method that inserts a new news system.
*
* @Param pNews Bean containing data news
* @Throws RemoteException
*/
public boolean insertNews (BeanNews pNews) throws RemoteException;
/**
* Method which removes from the news system.
*
* @Param ID pNewsID news be erased.
* @Throws RemoteException
*/
public boolean clearNews (int pNewsID) throws RemoteException;
/**
* Method amending the text of a news.
*
* @Param pNews Bean containing data news
* @Throws RemoteException
*/
public boolean modifyNews (BeanNews pNews) throws RemoteException;
/**
* Returns the list of active news.
*
* @Return ArrayList of ArrayList News
* @Throws RemoteException
*/
public ArrayList<BeanNews> getAllNews  () throws RemoteException;
}