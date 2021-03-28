/**
  * PriorityRenderer.java
  *
  * 1.0
  *
  * 22/05/2007
  *
  * 2007 eTour Project - Copyright by SE @SA Lab - DMI University of Salerno
*/
package unisa.gps.etour.gui.operatoragency.tables;

import java.awt.Component;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * This class creates a custom renderer JSlider as Of the priorities in the
 * table of news. 
 *
 *
 */
public class PriorityRenderer extends JSlider implements TableCellRenderer {
    /**
     * This default constructor.
     *
     */
    public PriorityRenderer() {
        setOpaque(true);
        setOrientation(SwingConstants.HORIZONTAL);
        setMinimum(1);
        setMaximum(5);
        setLabelTable(createStandardLabels(1));
        setMajorTickSpacing(1);
        setPaintTicks(true);
    }

    /**
     * This method returns the display component of the cell identified From the row
     * and column provided input.
     *
     * @Param pTable JTable - the table.
     * @Param pValue Object - the object contained in the selected cell.
     * @Param pIsSelected boolean - true if the object selected. False otherwise.
     * @Param pHasFocus boolean - true if the object has the focus. False otherwise.
     * @Param pRow int - the row index.
     * @Param pColumn int - the index of the column.
     * @Return Component - the component to display
     */
    public Component getTableCellRendererComponent(JTable pTable, Object pValue, boolean pIsSelected, boolean pHasFocus,
            int pRow, int pColumn) throws IllegalArgumentException {
        if ((pValue instanceof Integer)) {
            throw new IllegalArgumentException("unexpected value cell");
        }
        int value = (Integer) pValue;
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Illegal value cell.");
        }
        if (pIsSelected) {
            setBackground(pTable.getSelectionBackground());
        } else {
            setBackground(pTable.getBackground());
        }
        setValue(value);
        return this;
    }
}