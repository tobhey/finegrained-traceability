
package unisa.gps.etour.gui.operatoragency.tables;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import javax.swing.table.AbstractTableModel;
import unisa.gps.etour.util.Point3D;

/**
 * <b> SitoTableModel </ b> Serves as a data container
 * 
 * of cultural or refreshment areas that need Be displayed in a JTable. </ P> @
 */

public class SiteTableModel extends AbstractTableModel {
    String[] columnNames = { "Name", "City", "Distance" };
    Object[][] cells;
    Point3D sitePosition;
    Point3D myLocation;

    public SiteTableModel(BeanRefreshmentPoint[] pr, Point3D myLocation) {
        super();
        cells = new Object[pr.length][3]; // First value = second rows = columns
        for (int i = 0; i < pr.length; i++) {
            cells[i][0] = pr[i].getName();
            cells[i][1] = pr[i].getCity();

        }
    }

    public SiteTableModel(BeanCulturalHeritage[] bc, Point3D myLocation) {
        super();
        cells = new Object[bc.length][3]; // First value = second rows = columns
        for (int i = 0; i < bc.length; i++) {
            cells[i][0] = bc[i].getName();
            cells[i][1] = bc[i].getCity();

        }
    }

    public int getRowCount() {
        return cells.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int r, int c) {
        if (c < columnNames.length - 1)
            return cells[r][c];
        else {
            double value = myLocation.distance(sitePosition);
            return new Double(value);
        }

    }

    public String getColumnName(int c) {
        return columnNames[c];
    }
}
