package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.bean.BeanRefreshmentPoint;

/**
 * Interface for managing the database Business
 *
 */
public interface IDBConvention {

    /**
     * Add a convention in the database
     *
     * @Param pConvention Convention by adding
     */
    public boolean insertConvention(BeanConvention pConvention) throws SQLException;

    /**
     * Modify a convention in the database
     *
     * @Param data pConvention Convention of the Convention to be updated
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyConvention(BeanConvention pConvention) throws SQLException;

    /**
     * Delete an agreement by the database
     *
     * @Param pIdConvention ID of the Convention by removing
     * @Return True if been deleted false otherwise
     */
    public boolean clearConvention(int pIdConvention) throws SQLException;

    /**
     * Returns the historical conventions of a refreshment
     *
     * @Param idRefreshmentPoint point identification Refreshments
     * @Return List of conventions of Refreshment given as argument
     */
    public ArrayList<BeanConvention> getHistoricalConvention(int idRefreshmentPoint) throws SQLException;

    /**
     * Returns the Convention active a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Return Convention Turns
     * @Throws SQLException
     */
    public BeanConvention getConventionActive(int pIdRefreshmentPoint) throws SQLException;

    /**
     * Returns a list of all the PR that have a Convention active
     *
     * @Return list of all the PR with the Convention active
     */
    public ArrayList<BeanRefreshmentPoint> getListConventionActivePR() throws SQLException;

}