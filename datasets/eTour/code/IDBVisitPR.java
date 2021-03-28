package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Interface for managing feedback related to a specific point Refreshments
 *
 */
public interface IDBVisitPR {

    /**
     * Add a visit to a refreshment
     *
     * @Param PVIS visit to add
     */
    public boolean insertVisitPR(BeanVisitPR PVIS) throws SQLException;

    /**
     * Modify a visit
     *
     * @Param PVIS Visit to edit
     * @Return True if and 'been changed otherwise false
     */
    public boolean modifyVisitPR(BeanVisitPR PVIS) throws SQLException;

    /**
     * Extract the list of visits to a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Return List of visits
     */
    public ArrayList<BeanVisitPR> getListVisitPR(int pIdRefreshmentPoint) throws SQLException;

    /**
     * Extract a visit by a tourist at a refreshment
     *
     * @Param pIdRefreshmentPoint point identification Refreshments
     * @Param ID pIdTourist tourists
     * @Return visit
     */
    public BeanVisitPR getVisitPR(int pIdRefreshmentPoint, int pIdTourist) throws SQLException;

    /**
     * Extract the list of visits of a tourist
     *
     * @Param ID pIdTourist tourists
     * @Return List of visits
     */
    public ArrayList<BeanVisitPR> getListVisitPRTourist(int pIdTourist) throws SQLException;

}