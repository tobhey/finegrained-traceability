/**
  * FeedbackTableModel.java
  *
  * 1.0
  *
  * 21/05/2007
  *
  * 2007 eTour Project - Copyright by SE @SA Lab - DMI University of Salerno
*/
package unisa.gps.etour.gui.operatoragency.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Container model of data for feedback received To be a cultural or a
 * refreshment. 
 *
 *
 */
public class FeedBackTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] headers = { "rating", "Comment", "Release Date", "Issued by" };
    private static final Class[] columnClasses = { Integer.class, String.class, Date.class, String.class };
    private ArrayList<Object[]> data;

    /**
     * Default Constructor. We only provide the model but not Loads no data in it.
     *
     */
    public FeedBackTableModel() {
        data = new ArrayList<Object[]>();
    }

    /**
     * Constructor that takes as input an ArrayList of BeanVisitBC or BeanVisitPR
     * and copies of the data within the model Preparing for display.
     *
     */
    public FeedBackTableModel(HashMap<?, String> pDataFeedback) {
        this();
        if (pDataFeedback == null || pDataFeedback.size() == 0) {
            return;
        }
        Iterator<?> iter = pDataFeedback.keySet().iterator();
        while (iter.hasNext()) {
            Object current = iter.next();
            if (current instanceof BeanVisitBC) {
                insertVisitBC((BeanVisitBC) current, pDataFeedback.get(current));
            } else if (current instanceof BeanVisitPR) {
                insertVisitPR((BeanVisitPR) current, pDataFeedback.get(current));
            }
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
     * The method is inherited by setValueAt TableModel does not need to
     * SitoTableModel Because it provided the possibility to change a single cell.
     * 
     * @Deprecated
     *
     */
    public void setValueAt(Object value, int row, int col) {

    }

    /**
     *
     * Enter data on the feedback received from a cultural In the model from Bean.
     *
     * @Param BeanVisitBC PBC - the bean that contains the feedback from Cultural.
     * @Throws IllegalArgumentException - if the parameters supplied input not Are
     *         valid.
     *
     */
    public void insertVisitBC(BeanVisitBC pVisitBC, String pUsername) throws IllegalArgumentException {
        if (pVisitBC == null || pUsername == null || pUsername.equals("")) {
            throw new IllegalArgumentException("invalid parameters supplied input.");
        }
        Object[] aRow = new Object[6];
        aRow[0] = pVisitBC.getRating();
        aRow[1] = pVisitBC.getComment();
        aRow[2] = pVisitBC.getDataVisit();
        aRow[3] = pUsername;
        aRow[4] = pVisitBC.getIdCulturalHeritage();
        aRow[5] = pVisitBC.getIdTourist();
        data.add(aRow);
    }

    /**
     *
     * Enter data on the feedback received from a refreshment In the model from
     * Bean.
     *
     * @Param BeanVisitPR PBC - the bean that contains the feedback from
     *        Refreshment.
     * @Throws IllegalArgumentException - if the parameters supplied input not Are
     *         valid.
     *
     */
    public void insertVisitPR(BeanVisitPR pVisitPR, String pUsername) throws IllegalArgumentException {
        if (pVisitPR == null || pUsername == null || pUsername.equals("")) {
            throw new IllegalArgumentException("invalid parameters supplied input.");
        }
        Object[] aRow = new Object[6];
        aRow[0] = pVisitPR.getRating();
        aRow[1] = pVisitPR.getComment();
        aRow[2] = pVisitPR.getDataVisit();
        aRow[3] = pUsername;
        aRow[4] = pVisitPR.getIdRefreshmentPoint();
        aRow[5] = pVisitPR.getIdTourist();
        data.add(aRow);
    }

    /**
     *
     * Updates the comment feedback contained in the table row selected.
     *
     * PNewComment 
     * @param String - the new comment.
     * @Param pRow - the row to update.
     * @Throws IllegalArgumentException - if
     *         <ul>
     *         <li>the row index is not present in the model.
     *         <li>New comment supplied input is zero. </**
     */
    public void modifyComment(String pNewComment, int pRow) throws IllegalArgumentException {
        if (pRow >= getRowCount() || pRow < 0) {
            throw new IllegalArgumentException("The row index is not provided in the model.");
        }
        if (null == pNewComment) {
            throw new IllegalArgumentException("The new comment can not be null.");
        }
        data.get(pRow)[1] = pNewComment;
        fireTableDataChanged();
    }

    /**
     *
     * Returns the id of the row receive feedback provided on input.
     *
     * @Param pRow int - the row number.
     * @Return int [] - the id of the feedback.
     * @Throws IllegalArgumentException - if the row index does not pro In the
     *         model.
     */
    public int[] getIDFeedback(int pRow) throws IllegalArgumentException {
        if (pRow >= getRowCount() || pRow < 0) {
            throw new IllegalArgumentException("The row index is not provided in the model.");
        }
        int[] ids = new int[2];
        ids[0] = (Integer) data.get(pRow)[4];
        ids[1] = (Integer) data.get(pRow)[5];
        return ids;
    }

    /**
     *
     * Returns the id of feedback to line number provided as input and removes it
     * from the model.
     *
     * @Param pRow int - the row number.
     * @Return int [] - the id of the feedback.
     * @Exception IllegalArgumentException - if the row index does not pro In the
     *            model.
     *
     */
    public int[] removeFeedback(int pRow) throws IllegalArgumentException {
        int[] ids = getIDFeedback(pRow);
        data.remove(pRow);
        return ids;
    }
}
