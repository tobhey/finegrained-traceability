package unisa.gps.etour.repository;

import java.sql.SQLException;

import unisa.gps.etour.bean.BeanGenericPreference;

/**
 * Interface for handling general preferences in database
 *
 */
public interface IDBGenericPreference {
    /**
     * Add a preference General
     *
     * @Param pPreference preference to be added
     */
    public boolean insertGenericPreference(BeanGenericPreference pPreference) throws SQLException;

    /**
     * Edit a general preference
     *
     * @Param pPreference preference to change
     * @Return True if and 'been changed otherwise false
     */
    public boolean modifyGenericPreference(BeanGenericPreference pPreference) throws SQLException;

    /**
     * Delete a general preference
     *
     * @Param ID pIdPreference preference to delete
     * @Return True if and 'have been deleted false otherwise
     */
    public boolean clearGenericPreference(int pIdPreference) throws SQLException;

    /**
     * Returns the generic preference for tourists
     *
     * @Param Id pIdTourist tourists
     * @Return generic preference
     */
    public BeanGenericPreference getGenericPreference(int pIdTourist) throws SQLException;
}