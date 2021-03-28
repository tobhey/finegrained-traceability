package unisa.gps.etour.control.TagManager;

import java.rmi.RemoteException;

import unisa.gps.etour.bean.BeanTag;

/**
  * Interface for the tag handler by the Operator Agency
  *
*/
public interface ITagAgencyOperatorManager extends ITagCommonManager
{

/**
* Method to insert a new tag
*
* @Param pTagNew containing all the data of the new Tag
* @Return Boolean: true if the operation is successful, false otherwise
*/
public boolean insertTag (BeanTag pTagNew) throws RemoteException;

/**
* Method for the clearing of an existing tag
*
* @Param pTagID to identify the tags in question
* @Return Boolean: true if the operation is successful, false otherwise
*/
public boolean clearTag (int pTagID) throws RemoteException;

/**
* Method for editing a Tag
*
* @Param pTagChanged containing the details of the new Tag
* @Return Boolean: true if the operation is successful, false otherwise
*/
public boolean modifyTag (BeanTag pTagChanged) throws RemoteException;

/**
* Method which returns a tag identified by its ID
*
* @Param pTagID to identify a particular tag
* @Return a BeanTag containing data Tag concerned
*/
public BeanTag getTag (int pTagID) throws RemoteException;

} 