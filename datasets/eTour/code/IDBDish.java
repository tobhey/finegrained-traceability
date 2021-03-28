package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanDish;

/**
 * Interface for the management of food in the database
 *
 */
public interface IDBDish {
    /**
     * Add a dish
     *
     * @Param pDish plate add
     */
    public boolean insertDish(BeanDish pDish) throws SQLException;

    /**
     * Modify the data in a flat in the database
     *
     * @Param pPiatti data plate to be inserted in database
     * @Return True if there 'was a modified false otherwise
     */
    public boolean modifyDish(BeanDish pDish) throws SQLException;

    /**
     * Delete a dish from the database
     *
     * @Param pIdDish ID plate eliminre
     * @Throws SQLException
     * @Return True if and 'was deleted false otherwise
     */
    public boolean clearDish(int pIdDish) throws SQLException;

    /**
     * Returns a list of dishes on a menu
     *
     * @Param ID pIdMenu menu
     * @Throws SQLException
     * @Return list of dishes in the menu
     */
    public ArrayList<BeanDish> getDish(int pIdMenu) throws SQLException;

}
