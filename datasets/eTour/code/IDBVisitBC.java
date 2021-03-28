package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanVisitBC;

/**
 * Interface for handling feedback on a given asset Cultural
 *
 */
public interface IDBVisitBC {

    /**
     * Inserts a visit
     *
     * @Param PVIS Visit to insert
     */
    public boolean insertVisitBC(BeanVisitBC PVIS) throws SQLException;

    /**
     * Modify a visit
     *
     * @Param PVIS Visit to edit
     * @Return True if and 'been changed otherwise false
     */
    public boolean modifyVisitBC(BeanVisitBC PVIS) throws SQLException;

    /**
     * Extract the list of visits to a cultural
     *
     * @Param pIdCulturalHeritage ID of the cultural
     * @Return list of visits of the cultural
     */
    public ArrayList<BeanVisitBC> getListVisitBC(int pIdCulturalHeritage) throws SQLException;

    /**
     * Extract the list of cultural visited by a tourist
     *
     * @Param ID pIdTourist tourists
     * @Return ArrayList of all feedback issued by a tourist for a Specified
     *         cultural
     */
    public ArrayList<BeanVisitBC> getListVisitBCTourist(int pIdTourist) throws SQLException;

    /**
     * Extract a visit by a tourist to a cultural
     *
     * @Param pIdCulturalHeritage ID of the cultural
     * @Param ID pIdTourist tourists
     * @Return visit
     */
    public BeanVisitBC getVisitBC(int pIdCulturalHeritage, int pIdTourist) throws SQLException;
}
