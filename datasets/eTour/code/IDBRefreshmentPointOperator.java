package unisa.gps.etour.repository;

import java.sql.SQLException;

import unisa.gps.etour.bean.BeanOperatorRefreshmentPoint;

/**
 * Interface for the operator to the point of comfort in the database
 *
 */
public interface IDBRefreshmentPointOperator {
    /**
     * Adds an operator refreshment
     *
     * @Param pOperator Additional operating
     */
    public boolean insertOperatorRefreshmentPoint(BeanOperatorRefreshmentPoint pOperator) throws SQLException;

    /**
     * Modify an operator in the database
     *
     * @Param pOperator New data Operator
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyOperatorRefreshmentPoint(BeanOperatorRefreshmentPoint pOperator) throws SQLException;

    /**
     * Delete an operator
     *
     * @Param pIdOperator Operator ID to delete
     * @Throws SQLException
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearOperatorRefreshmentPoint(int pIdOperator) throws SQLException;

    /**
     * Returns data operator
     *
     * @Param pIdOperator Operation ID
     * @Throws SQLException
     * @Return Operator refreshment
     */
    public BeanOperatorRefreshmentPoint getOperatorRefreshmentPoint(int pIdOperator) throws SQLException;
}