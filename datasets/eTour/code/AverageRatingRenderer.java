package unisa.gps.etour.gui.operatoragency.tables;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import unisa.gps.etour.gui.operatoragency.Home;

/**
 * This class creates a custom renderer for the average ratings of a Site. <
 *
 */
public class AverageRatingRenderer extends DefaultTableCellRenderer {

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
    public Component getTableCellRendererComponent(JTable pTable, Object pValue, boolean pSelected, boolean pFocus,
            int prow, int pColumn) throws IllegalArgumentException {
        if ((pValue instanceof Double || pValue instanceof Integer)) {
            throw new IllegalArgumentException("Value cell unexpected.");
        }
        double rating = 0;
        if (pValue instanceof Double) {
            rating = (Double) pValue;
        } else {
            rating += (Integer) pValue;
        }

        JLabel aLabel = new JLabel("");
        aLabel.setHorizontalAlignment(JLabel.CENTER);

        if (rating > 4) {
            aLabel.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella5.gif ")));
        } else if (rating <= 4 && rating > 3) {
            aLabel.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella4.gif ")));
        } else if (rating <= 3 && rating > 2) {
            aLabel.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella3.gif ")));
        } else if (rating <= 2 && rating > 1) {
            aLabel.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella2.gif ")));
        } else {
            aLabel.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella1.gif ")));
        }
        return aLabel;

    }

}