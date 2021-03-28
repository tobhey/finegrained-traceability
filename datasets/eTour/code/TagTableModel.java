package unisa.gps.etour.gui.operatoragency;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import unisa.gps.etour.bean.BeanTag;

/**
 * <p>
 * <B> Title: </ B> TagTableModel </ P>
 * <p>
 * <B> Description: </ B> TableModel for dynamic management of Table Within the
 * section ManagerTag </ P>
 *
 */

public class TagTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] headers = { "Name", "Description" };
    private static final Class[] columnClasses = { String.class, String.class };
    private Vector<Object[]> data;

    /**
     * Constructor for class TagTableModel
     *
     * @Param BeanTag []
     *
     */
    public TagTableModel(BeanTag[] tag) {
        data = new Vector<Object[]>();
        for (int i = 0; i < tag.length; i++) {
            Object[] new_ = new Object[10];
            new_[0] = tag[i].getId();
            new_[1] = tag[i].getName();
            new_[2] = tag[i].getDescription();

        }
    }

    /**
     * Returns the number of columns
     *
     */
    public int getColumnCount() {
        return headers.length;
    }

    /**
     * Returns the number of rows
     *
     */
    public int getRowCount() {
        return data.size();
    }

    /**
     * Returns the column heading i_esima
     *
     * @Param pCol
     *
     */
    public String getColumnName(int pCol) {
        return headers[pCol];
    }

    /**
     * Returns the coordinates given by the pair of row, column
     *
     * @Param pCol
     * @Param pRow
     *
     */
    public Object getValueAt(int pRow, int pCol) {
        return data.get(pRow)[pCol];
    }

    /**
     * Returns the column pCol
     *
     * @Param pCol
     *
     */
    public Class getColumnClass(int pCol) {
        return columnClasses[pCol];
    }

    /**
     * Always returns false because the cells in the table are not editable
     *
     * @Param pCol
     * @Param pRow
     *
     * @Return false
     *
     */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * This method is empty. Can not be included an element within a cell
     *
     * @Deprecated
     *
     */
    public void setValueAt(Object value, int row, int col) {

    }

}