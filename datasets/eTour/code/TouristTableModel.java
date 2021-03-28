/*
  * TouristTableModel.java
  *
  * 1.0
  *
  * 27/05/2007
  *
  * 2007 eTour Project - Copyright by SE @SA Lab - DMI University of Salerno
  */
package unisa.gps.etour.gui.operatoragency.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import unisa.gps.etour.bean.BeanTourist;

/**
 * <b> TouristTableModel </ b>
 * <p>
 * Acts as a container of data from the tourists who have Be displayed in a
 * JTable. </ P>
 * 
 */
public class TouristTableModel extends AbstractTableModel {
    private static final String[] headers = { "Status", "Name", "Name", "E-Mail", "Phone", "Date of Birth",
            "City of Birth", "Address", "City", "CPC", "test", "Save" };
    private static final Class[] columnClasses = { Boolean.class, String.class, String.class, String.class,
            String.class, Date.class, String.class, String.class, String.class, String.class, String.class,
            Date.class };
    private Vector<Object[]> data;

    /**
     * Default Constructor. We only provide the template without loading No data in
     * it.
     *
     */
    public TouristTableModel() {
        data = new Vector<Object[]>();
    }

    /**
     * Create a model of the table and loads the data provided through an array of
     * BeanCulturalHeritage.
     *
     * @Param pTourist java.util.ArrayList <BeanTourist> - an ArrayList of
     *        BeanTourist.
     *
     */
    public TouristTableModel(ArrayList<BeanTourist> pTourist) {
        this();
        if (null == pTourist) {
            return;
        }
        for (int i = 0; i < pTourist.size(); i++) {
            insertTourist(pTourist.get(i));
        }
    }

    /**
     * Returns the number of columns provided by the model.
     *
     * @Return int - the number of columns.
     *
     */
    public int getColumnCount() {
        return headers.length;
    }

    /**
     * Returns the number of rows currently in the model.
     *
     * @Return int - the number of rows.
     *
     */
    public int getRowCount() {
        return data.size();
    }

    /**
     * Returns the column name from the index provided.
     *
     * @Return String - the name of the column.
     * @Exception IllegalArgumentException - if the column index is not provided In
     *            the model.
     *
     */
    public String getColumnName(int pColumn) throws IllegalArgumentException {
        if (pColumn >= getColumnCount() || pColumn < 0) {
            throw new IllegalArgumentException("The column index is not provided in the model.");
        }
        return headers[pColumn];
    }

    /**
     * Returns the object in the model to the row and column provided.
     *
     * @Param pRow - the line number.
     * @Param pColumn - the column number.
     * @Return Object - the object contained in the selected cell.
     * @Exception IllegalArgumentException - if the index or the row or column not
     *            provided Are present in the model.
     *
     */
    public Object getValueAt(int pRow, int pColumn) throws IllegalArgumentException {
        if (pRow >= getRowCount() || pRow < 0) {
            throw new IllegalArgumentException("The row index is not provided in the model.");
        }
        if (pColumn >= getColumnCount() || pColumn < 0) {
            throw new IllegalArgumentException("The column index is not provided in the model.");
        }
        return data.get(pRow)[pColumn];
    }

    /**
     * Returns the class of objects in the column of which is provided in the index.
     *
     * @Param pColumn - the column number.
     * @Return Class - the class of objects of the selected column.
     * @Exception IllegalArgumentException - if the index column does not pro In the
     *            model.
     *
     */
    public Class getColumnClass(int pColumn) throws IllegalArgumentException {
        if (pColumn >= getColumnCount() || pColumn < 0) {
            throw new IllegalArgumentException("The column index is not provided in the model.");
        }
        return columnClasses[pColumn];
    }

    /**
     * Returns whether the selected cell editable.
     *
     * @Return boolean - true if the cell editable False otherwise
     * @Param pRow - the line number.
     * @Param pColumn - the column number.
     * @Exception IllegalArgumentException - if the index or the row or column are
     *            not provided In the model.
     *
     */
    public boolean isCellEditable(int pRow, int pColumn) throws IllegalArgumentException {
        return false;
    }

    /**
     * The method is inherited by the TableModel not setValueAt Necessary because
     * not provided for the possibility of amending a Single cell.
     *
     * @Deprecated
     */
    public void setValueAt(Object value, int row, int col) {

    }

    /**
     * Enables or disables the tourist in the selected row.
     *
     * @Param int pRow - the selected row.
     * @Return int - the id of the tourist on / off.
     *
     */
    public int activeTourist(int pRow) throws IllegalArgumentException {
        data.get(pRow)[0] = (isActiveto(pRow)) ? false : true;
        fireTableDataChanged();
        return getID(pRow);
    }

    /**
     * Determines if a visitor to the selected row is enabled or disabled.
     *
     * @Param int pRow - the selected row.
     * @Return
     *         <ul>
     *         <li><i> true </ i> - enabled </ li><li> <i> False </ i> - disabled </
     *         li> </ ul>
     */
    public boolean isActiveto(int pRow) throws IllegalArgumentException {
        return (Boolean) getValueAt(pRow, 0);
    }

    /**
     *
     * Enter data for a tourist in the model since its Bean.
     *
     * @Param pTourist BeanTurisa - the bean that contains the data of the tourist.
     *
     */
    public void insertTourist(BeanTourist pTourist) throws IllegalArgumentException {
        if (null == pTourist) {
            throw new IllegalArgumentException("The bean provided can not be null.");
        }
        Object[] aRow = new Object[13];
        aRow[0] = pTourist.isActive();
        aRow[1] = pTourist.getName();
        aRow[2] = pTourist.getSurname();
        aRow[3] = pTourist.getEmail();
        aRow[4] = pTourist.getPhone();
        aRow[5] = pTourist.getDateOfBirth();
        aRow[6] = pTourist.getCityNascita();
        aRow[7] = pTourist.getStreet();
        aRow[8] = pTourist.getCityResidenza();
        aRow[9] = pTourist.getCap();
        aRow[10] = pTourist.getProvince();
        aRow[11] = pTourist.getDataRegistration();
        aRow[12] = pTourist.getId();
        data.add(aRow);
    }

    /**
     *
     * Update the information of the tourist in the model (if any) With the bean
     * supplied input.
     *
     * @Param pTourist BeanTourist - the bean that contains the data of the tourist.
     *
     */
    public void updateTourist(BeanTourist pTourist) throws IllegalArgumentException {
        if (null == pTourist) {
            throw new IllegalArgumentException("The bean provided can not be null.");
        }
        int i;
        for (i = 0; i < data.size(); i++) {
            int id = (Integer) data.get(i)[12];
            if (id == pTourist.getId()) {
                break;
            }
        }
        if (i != data.size()) // Found
        {
            Object[] aRow = new Object[13];
            aRow[0] = pTourist.isActive();
            aRow[1] = pTourist.getName();
            aRow[2] = pTourist.getSurname();
            aRow[3] = pTourist.getEmail();
            aRow[4] = pTourist.getPhone();
            aRow[5] = pTourist.getDateOfBirth();
            aRow[6] = pTourist.getCityNascita();
            aRow[7] = pTourist.getStreet();
            aRow[8] = pTourist.getCityResidenza();
            aRow[9] = pTourist.getCap();
            aRow[10] = pTourist.getProvince();
            aRow[11] = pTourist.getDataRegistration();
            aRow[12] = pTourist.getId();
            data.set(i, aRow);
            fireTableDataChanged();
        }

    }

    /**
     *
     * Returns the id of the visitor whose data are displayed in row Provided input.
     *
     * @Param pRow - the line number.
     * @Return - the id of the tourist.
     * @Exception IllegalArgumentException - if the row index does not pro In the
     *            model.
     */
    public int getID(int pRow) throws IllegalArgumentException {
        if (pRow >= getRowCount() || pRow < 0) {
            throw new IllegalArgumentException("The row index is not provided in the model.");
        }
        return (Integer) data.get(pRow)[12];
    }

    /**
     *
     * Returns the id of the tourist at the line number provided as input and
     * removes it from the model.
     *
     * @Param pRow - the line number.
     * @Return - the id of the tourist.
     * @Exception IllegalArgumentException - if the row index does not pro In the
     *            model.
     *
     */
    public int removeTourist(int pRow) throws IllegalArgumentException {
        int id = getID(pRow);
        data.remove(pRow);
        return id;
    }

}