package unisa.gps.etour.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import unisa.gps.etour.gui.operatoragency.ICard;

/**
 * Class for handling custom internal frame inserted in a JDesktopPane.
 *
 */
public class DeskManager extends DefaultDesktopManager {
    public static final String URL_IMAGES = "/ unisa / gps / eTour / gui / images /";
    private JPopupMenu deskMenu;
    private JMenuItem reduceAll;
    private JMenuItem restoreAll;
    private JMenuItem closeAll;
    private Vector<JInternalFrame> iconifiedFrames;
    private int locationX;
    private int locationY;

    /**
     * Default Constructor.
     */
    public DeskManager() {
        super();
        iconifiedFrames = new Vector<JInternalFrame>();
        initializeDeskMenu();
        locationX = 0;
        locationY = -1;
    }

    /**
     * Manages the movement of JInternalFrame inside the area of JDesktopPane,
     * preventing the frames are brought out of the visible area.
     *
     * @Param javax.swing.JComponent AComponent - the component of which Manage the
     *        move.
     * @Param int x - x cordinate the point where it was moved Component.
     * @Param int y - y cordinate the point where it was moved Component.
     */
    public void dragFrame(JComponent aComponent, int x, int y) {
        if (aComponent instanceof JInternalFrame) {
            JInternalFrame frame = (JInternalFrame) aComponent;
            if (frame.isIcon()) {
                x = frame.getLocation().x;
                y = frame.getLocation().y;
            } else {
                JDesktopPane desk = frame.getDesktopPane();
                Dimension d = desk.getSize();
                if (x < 0) {
                    x = 0;
                }

                else {
                    if (x + frame.getWidth() > d.width) {
                        x = d.width - frame.getWidth();
                    }
                }

                if (y < 0) {
                    y = 0;
                } else {
                    if (y + frame.getHeight() > d.height) {
                        y = d.height - frame.getHeight();
                    }
                }
            }
        }

        super.dragFrame(aComponent, x, y);
    }

    /**
     * Customize the action of reducing the JInternalFrame an icon, creating
     * Clickable bars on the bottom of JDesktopPane.
     *
     * @Param JInternalFrame frame - a frame inside a JDesktopPane.
     */
    public void iconifyFrame(JInternalFrame frame) {
        try {
            JDesktopPane desk = frame.getDesktopPane();
            Dimension d = desk.getSize();
            frame.setClosable(false);
            frame.setMaximizable(true);
            frame.setIconifiable(false);
            Rectangle features;
            if (frame.isMaximum()) {
                features = frame.getNormalBounds();
            } else
                features = frame.getBounds();
            frame.setSize(200, 30);
            setPreviousBounds(frame, features);
            if (iconifiedFrames.isEmpty()) {
                locationX = 0;
            } else {
                locationX += 200;
            }
            if (locationY == -1) {
                locationY = d.height - 30;
            }
            if (locationX + 200 > d.width) {
                locationX = 0;
                locationY -= 30;
            }
            frame.setLocation(locationX, locationY);
            frame.setResizable(false);
            iconifiedFrames.add(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Restore the frame from the effect of minimizing, resetting the Position and
     * size it had before.
     *
     * @Param javax.swing.JInternalFrame frame - a frame inside a JDesktopPane.
     */
    public void deiconifyFrame(JInternalFrame frame) {
        try {
            JDesktopPane desk = frame.getDesktopPane();
            Dimension deskSize = desk.getSize();
            iconifiedFrames.remove(frame);
            Rectangle features = getPreviousBounds(frame);
            if (features.width > deskSize.width) {
                features.width = deskSize.width;
                features.x = 0;
            }
            if (features.width + features.x > deskSize.width) {
                features.x = (deskSize.width - features.width) / 2;
            }
            if (features.height > deskSize.height) {
                features.height = deskSize.height;
                features.y = 0;
            }
            if (features.height + features.y > deskSize.height) {
                features.y = (deskSize.height - features.height) / 2;
            }
            frame.setSize(features.width, features.height);
            frame.setLocation(features.x, features.y);
            frame.setIconifiable(true);
            frame.setClosable(true);
            if (frame instanceof ICard) {
                frame.setMaximizable(false);
                frame.setResizable(false);
            } else {
                frame.setMaximizable(true);
                frame.setResizable(true);
            }
            locationX -= 200;
            if (locationX < 0) {
                locationX = deskSize.width / 200 - 200;
                if (locationY != deskSize.height - 30) {
                    locationY -= 30;
                }
            }
            repaintIconifiedFrames(desk);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Return the focus to a selected frame, and, if the frame Is iconificato, the
     * deiconifica.
     *
     * @Param JInternalFrame frame - a frame within a JDesktopPane
     */
    public void activateFrame(JInternalFrame frame) {
        try {
            if (frame.isIcon())
                frame.setIcon(false);
            frame.setSelected(true);
            super.activateFrame(frame);
        } catch (PropertyVetoException s) {
            s.printStackTrace();
        }

    }

    /**
     * Center the frame supplied as a parameter in JDesktopPane.
     *
     * @Param javax.swing.JInternalFrame frame - a frame inside a JDesktopPane.
     * @Return void
     */
    public void centerFrame(JInternalFrame frame) {
        JDesktopPane desk = frame.getDesktopPane();
        Dimension d = desk.getSize();
        Dimension f = frame.getSize();
        frame.setLocation(d.width / 2 - f.width / 2, d.height / 2 - f.height / 2);
    }

    /**
     * Redraw the frames in the desktop iconificati bread provided.
     *
     * @Param javax.swing.JDesktopPane Desk - a desktop bread associated with a Desk
     *        manager.
     * @Throws IllegalArgumentException - was supplied as a parameter JDesktopPane
     *         which is not associated with a Desk Manager.
     */
    public void repaintIconifiedFrames(JDesktopPane desk) throws IllegalArgumentException {
        if (desk.getDesktopManager() != this)
            throw new IllegalArgumentException("I found no object" + "Type DeskManager associated");
        Iterator<JInternalFrame> iconificati = iconifiedFrames.iterator();
        int i = 0;
        int xLocation;
        int yLocation = desk.getHeight() - 30;
        while (iconificati.hasNext()) {
            JInternalFrame current = iconificati.next();
            xLocation = 200 * i;
            if (xLocation + 200 >= desk.getWidth()) {
                xLocation = 0;
                yLocation -= 30;
                i = 0;
            }
            current.setLocation(xLocation, yLocation);
            i++;
        }
    }

    /**
     * Redraw (and resize if necessary) all the frames contained in a Since
     * JDesktopPane.
     *
     * @Param javax.swing.JDesktopPane Desk - a desktop pane.
     * @Throws IllegalArgumentException - if the desktop bread supply is not
     *         Associated with a desktop manager like DeskManager.
     */
    public void repaintAllFrames(JDesktopPane desk) throws IllegalArgumentException {
        if (desk.getDesktopManager() != this)
            throw new IllegalArgumentException("I found no object" + "Type DeskManager associated");
        JInternalFrame[] frames = desk.getAllFrames();
        Dimension deskSize = desk.getSize();
        for (int i = 0; i < frames.length; i++) {
            JInternalFrame current = frames[i];
            if (!current.isIcon()) {
                Rectangle frameBounds = current.getBounds();
                if (frameBounds.width > deskSize.width)
                    frameBounds.width = deskSize.width;
                if (frameBounds.height > deskSize.height)
                    frameBounds.height = deskSize.height;
                if (frameBounds.x + frameBounds.width > deskSize.width)
                    frameBounds.x = deskSize.width - frameBounds.width;
                if (frameBounds.y + frameBounds.height > deskSize.height)
                    frameBounds.y = deskSize.height - frameBounds.height;
                current.setBounds(frameBounds);
            }

        }
        repaintIconifiedFrames(desk);
    }

    /**
     * Open a frame of the class specified using the display Waterfall. If you
     * already have a frame of classes given, the frame is Activated.
     *
     * @Param class class - a class type that extends JInternalFrame.
     * @Param javax.swing.JDesktopPane Desk - a desktop pane.
     * @Throws IllegalArgumentException - The class provided is not a
     *         JInternalFrame.
     */
    public void openFrame(Class classe, JDesktopPane desk) throws IllegalArgumentException {
        if (classe.getSuperclass() != JInternalFrame.class)
            throw new IllegalArgumentException(
                    "The class provided input has" + "As a superclass javax.swing.JInternalFrame.");
        try {
            JInternalFrame[] frames = desk.getAllFrames();
            int i;
            for (i = 0; i < frames.length; i++)
                if (frames[i].getClass().equals(classe))
                    break;
            if (i == frames.length) {
                JInternalFrame new_ = (JInternalFrame) classe.newInstance();
                desk.add(new_, Integer.MAX_VALUE);
                Dimension frameSize = new_.getPreferredSize();
                new_.setSize(frameSize);
                Dimension deskSize = desk.getSize();
                Point posNew = new Point(10, 10);
                for (i = frames.length - 1; i >= 0; i--) {
                    if (frames[i].getLocation().equals(posNew)) {
                        posNew.x = frames[i].getLocation().x + 30;
                        posNew.y = frames[i].getLocation().y + 30;
                    }
                }
                if ((posNew.x + frameSize.width > deskSize.width)
                        || (posNew.y + frameSize.height > deskSize.height))
                    centerFrame(new_);
                else
                    new_.setLocation(posNew);
                new_.setVisible(true);
            } else {
                activateFrame(frames[i]);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Displays a popup menu with options for frames of a desktop bread The selected
     * location.
     *
     * @Param java.awt.Point Pointe - the point where to place the menu.
     * @Param javax.swing.JDesktopPane desk - a JDesktopPane which &grave; an
     *        associated Instance of DeskManager.
     * @Throws IllegalArgumentException - &grave; was provided as a parameter
     *         JDesktopPane that &grave; not associated with a Desk Manager.
     */
    public void showPopupMenu(Point Pointe, JDesktopPane desk) {
        if (desk.getDesktopManager() != this)
            throw new IllegalArgumentException("I found no object" + "Type DeskManager associated");
        restoreAll.setEnabled(true);
        closeAll.setEnabled(true);
        reduceAll.setEnabled(true);
        JInternalFrame[] frames = desk.getAllFrames();
        if (frames.length == 0) {
            restoreAll.setEnabled(false);
            closeAll.setEnabled(false);
            reduceAll.setEnabled(false);
        }
        if (iconifiedFrames.size() == 0) {
            restoreAll.setEnabled(false);

        }
        if (iconifiedFrames.size() == frames.length) {
            reduceAll.setEnabled(false);
        }
        deskMenu.show(desk, Pointe.x, Pointe.y);
    }

    /**
     * Deiconifica all frames previously iconificati.
     *
     */
    public void deiconifyAll() {
        if (iconifiedFrames.size() != 0) {
            Vector<JInternalFrame> copy = (Vector<JInternalFrame>) iconifiedFrames.clone();
            Iterator<JInternalFrame> frames = copy.iterator();
            while (frames.hasNext()) {
                try {
                    frames.next().setIcon(false);
                } catch (PropertyVetoException s) {
                    s.printStackTrace();
                }

            }
            copy = null;
            iconifiedFrames.removeAllElements();

        }
    }

    /**
     * Minimize all frames of a JDesktopPane provided in &grave; an associated
     * DeskManager.
     *
     * @Param JDesktopPane Desk - a desktop pane.
     * @Throws IllegalArgumentException - &grave; was provided as a parameter
     *         JDesktopPane that &grave; not associated with a Desk Manager.
     */
    public void iconifyAll(JDesktopPane desk) {
        if (desk.getDesktopManager() != this)
            throw new IllegalArgumentException("I found no object" + "Type DeskManager associated");
        JInternalFrame[] frames = desk.getAllFrames();
        for (int i = 0; i < frames.length; i++)
            try {
                frames[i].setIcon(true);
            } catch (PropertyVetoException s) {
                s.printStackTrace();
            }
    }

    /**
     * Close all frames in a given JDesktopPane.
     *
     * @Param javax.swing.JDesktopPane Desk - a desktop &grave; bread in an
     *        associated DeskManager.
     * @Throws IllegalArgumentException - &grave; was provided as a parameter
     *         JDesktopPane that &grave; not associated with a Desk Manager.
     */
    public void closeAll(JDesktopPane desk) {
        if (desk.getDesktopManager() != this)
            throw new IllegalArgumentException("I found no object" + "Type DeskManager associated");
        JInternalFrame[] frames = desk.getAllFrames();
        if (frames.length != 0) {
            for (int i = 0; i < frames.length; i++)
                frames[i].dispose();
            iconifiedFrames.removeAllElements();
        }
    }

    /**
     * Initialize the DeskMenu.
     *
     */
    public void initializeDeskMenu() {
        deskMenu = new JPopupMenu();
        reduceAll = new JMenuItem("Collapse All");
        reduceAll.setIcon(new ImageIcon(getClass().getResource(URL_IMAGES + "reduceAll.png ")));
        restoreAll = new JMenuItem("Reset All");
        restoreAll.setIcon(new ImageIcon(getClass().getResource(URL_IMAGES + "activateall.png ")));
        closeAll = new JMenuItem("Close All");
        closeAll.setIcon(new ImageIcon(getClass().getResource(URL_IMAGES + "closeall.png ")));
        deskMenu.add(reduceAll);
        deskMenu.addSeparator();
        deskMenu.add(restoreAll);
        deskMenu.addSeparator();
        deskMenu.add(closeAll);
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent aEvent) {
                if (aEvent.getSource() == restoreAll)
                    deiconifyAll();
                if (aEvent.getSource() == closeAll)
                    closeAll((JDesktopPane) deskMenu.getInvoker());
                if (aEvent.getSource() == reduceAll)
                    iconifyAll((JDesktopPane) deskMenu.getInvoker());
            }

        };
        reduceAll.addActionListener(menuListener);
        restoreAll.addActionListener(menuListener);
        closeAll.addActionListener(menuListener);
    }
}
