package unisa.gps.etour.gui.operatoragency.tables;

import java.awt.*;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

/**
 * Create a custom JTable that can be displayed through Components that enable
 * scrolling.
 * 
 */
public class ScrollableTable extends JTable implements Scrollable {

    private static final int maxUnitIncrement = 20;

    public ScrollableTable() {
        super();
    }

    public ScrollableTable(TableModel tm) {
        super(tm);
        setGridColor(Color.LIGHT_GRAY);
        setIntercellSpacing(new Dimension(5, 0));
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {

        int posCurrent = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            posCurrent = visibleRect.x;
        } else {
            posCurrent = visibleRect.y;
        }

        if (direction < 0) {
            int newPos = posCurrent - (posCurrent / maxUnitIncrement) * maxUnitIncrement;
            return (newPos == 0) ? maxUnitIncrement : newPos;
        } else {
            return ((posCurrent / maxUnitIncrement) + 1) * maxUnitIncrement - posCurrent;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}