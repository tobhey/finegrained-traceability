package unisa.gps.etour.gui.operatoragency.tables;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class BannerRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree pTree, Object pValue, boolean pSelected, boolean pExpanded,
            boolean pLeaf, int prow, boolean pHasFocus) {
        Object obj = ((DefaultMutableTreeNode) pValue).getUserObject();
        if ((obj instanceof ImageIcon)) {
            throw new IllegalArgumentException("Value cell unexpected.");
        }
        ImageIcon image = (ImageIcon) obj;
        JLabel aLabel = new JLabel();
        aLabel.setIcon(image);
        aLabel.setSize(image.getIconWidth() + 10, image.getIconHeight() + 10);
        if (pSelected) {
            aLabel.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        }
        return aLabel;

    }

}