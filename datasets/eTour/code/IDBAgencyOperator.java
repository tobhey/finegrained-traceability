package unisa.gps.etour.repository;

import java.sql.SQLException;

import unisa.gps.etour.bean.BeanOperatorAgency;

/**
  * Interface for managing the database OperatorAgency
  *
*/
public interface IDBAgencyOperator
{
/**
* Returns the data Operator Agency with ID equal to that given in
* Input
*
* @Param pUsername Username dell'OperatorAgency to find
* @Return OperatorAGenzia with id equal to the input, null if there is
*/
public BeanOperatorAgency getOperatorAgency (String pUsername) throws SQLException;

/**
* Returns the data Operator Agency with ID equal to that given in
* Input
*
* @Param pUsername Username dell'OperatorAgency to find
* @Return OperatorAGenzia with id equal to the input, null if there is
*/
public boolean modifyPassword (BeanOperatorAgency poa) throws SQLException;

}