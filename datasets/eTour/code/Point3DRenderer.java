package unisa.gps.etour.gui.operatoragency.tables;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import unisa.gps.etour.util.Point3D;

/**
 * This class creates a custom renderer for Objects of type Punto3D. <
 *
 */
public class Point3DRenderer implements TableCellRenderer {
    /**
     * Method that returns the custom component for the Display of the data
     * contained in the cell of a table.
     *
     * @Param pTable JTable - the table.
     * @Param Object pValue - the data.
     * @Param boolean pSelected --
     * @Param boolean pHasFocus --
     * @Param int pRow - the line number.
     * @Param int pColumn - the column number.
     * @Return Component - the component that customizes render the cell.
     * @Throws IllegalArgumentException - if the value of the cell can not Be
     *         rendered by this renderer.
     */
    public Component getTableCellRendererComponent(JTable pTable, Object pValue, boolean pSelected, boolean pHasFocus,
            int prow, int pColumn) {
        if ((pValue instanceof Point3D)) {
            throw new IllegalArgumentException("Value Cella unexpected.");
        }
        Point3D point = (Point3D) pValue;
        String pointString = point.getLatitude() + ";" + point.getLongitude() + "  = " + point.getAltitude();
        JLabel aLabel = new JLabel(pointString, SwingConstants.CENTER);
        if (pSelected) {
            aLabel.setForeground(pTable.getSelectionForeground());
            aLabel.setBackground(pTable.getSelectionBackground());
        }
        return aLabel;

    }

}
