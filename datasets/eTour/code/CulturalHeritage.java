
package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.control.CulturalHeritageManager.ICulturalHeritageAgencyManager;
import unisa.gps.etour.control.CulturalHeritageManager.ICulturalHeritageCommonManager;
import unisa.gps.etour.control.TagManager.ITagCommonManager;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.HelpManager;
import unisa.gps.etour.gui.operatoragency.tables.AverageRatingRenderer;
import unisa.gps.etour.gui.operatoragency.tables.Point3DRenderer;
import unisa.gps.etour.gui.operatoragency.tables.ScrollableTable;
import unisa.gps.etour.gui.operatoragency.tables.SiteTableModel;
import unisa.gps.etour.util.Point3D;

/**
 * Class that implements the interface for the management of cultural side
 * Operator Agency.
 *
 */
public class CulturalHeritage extends JInternalFrame {
    private JDesktopPane JDesktopPane;
    private JPanel jContentPane = null;
    private JToolBar BCToolbar = null;
    private JButton btnNewBC = null;
    private JButton btnCardBC = null;
    private JButton btnDeleteBC = null;
    private JButton btnModifyBC = null;
    private JPanel rightPanel = null;
    private JPanel searchPanel = null;
    private JPanel helpPanel = null;
    private JScrollPane JScrollPane = null;
    private JTable tableBC = null;
    private TagPanel panelTag = null;
    private JTextPane textGuide = null;
    private JTextField nameBC = null;
    private JButton btnSearch = null;
    private JButton btnActive = null;
    private DeskManager desktopManager;
    private ArrayList<CardBC> children;
    private SiteTableModel TableModel;
    private HelpManager bcHelp;
    private ICulturalHeritageAgencyManager managerBC;
    private ITagCommonManager tags;
    protected ICulturalHeritageCommonManager searchBC;

    /**
     * This is the default constructor.
     */
    public CulturalHeritage() {
        super("Cultural Heritage");
        setPreferredSize(Home.CHILD_SIZE);
        frameIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "BC.png"));
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        closable = true;
        resizable = true;
        iconable = true;
        maximizable = true;

// Setting up dell'help manager for cultural.
        textGuide = new JTextPane();
        try {
            bcHelp = new HelpManager(Home.URL_HELP + "CulturalHeritage.txt", textGuide);
        } catch (FileNotFoundException e) {
            textGuide.setText("<html> <b> Help not available </ b> </ html>");
        }
        setContentPane(getJContentPane());
        children = new ArrayList<CardBC>();
        addInternalFrameListener(new InternalFrameAdapter() {
            /*
             * Inclusion of the frame on the desktop desktop retrieves bread bread And
             * desktop manager and initializes the remote objects Management of cultural
             * heritage.
             */

            public void internalFrameOpened(InternalFrameEvent pEvent) {
                JInternalFrame frame = pEvent.getInternalFrame();
                JDesktopPane = frame.getDesktopPane();
                desktopManager = (DeskManager) JDesktopPane.getDesktopManager();

// Setting up objects for remote asset management
// Cultural.
                try {
                    Registry reg = LocateRegistry.getRegistry(Home.HOST);
                    managerBC = (ICulturalHeritageAgencyManager) reg.lookup("ManagerCulturalHeritageAgency");
                    tags = (ITagCommonManager) reg.lookup("ManagerTagCommon ");
                    searchBC = (ICulturalHeritageCommonManager) reg.lookup("ManagerCulturalHeritageCommon");
// Load data.
                    loadTable(false);
                    loadTags();
                }
                /*
                 * Two exceptions: RemoteException and NotBoundException. The Result is the
                 * same. The management is not operable and After the error message window
                 * closes.
                 */
                catch (Exception ex) {
                    JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                            + "<h3> <u> The dialog management request is closed. </ U> </ h3>"
                            + "<p> <b> Possible Causes: </ b>" + "<ul> <li> No connection to the network. </ Li>"
                            + "Server <li> inactive. </ Li>" + "Server <li> clogged. </ Li> </ ul>"
                            + "<p> Please try again later. </ P>"
                            + "<p> If the error persists, please contact technical support. </ P>"
                            + "<p> We apologize for the inconvenience. </ Html>");
                    ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(JDesktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
                    frame.dispose();
                }
            }

            /*
             * At the end of the frame displays the dialog Confirmation.
             */
            public void internalFrameClosing(InternalFrameEvent pEvent) {
// Create the confirmation dialog.
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel("Are you sure you want to close the management of cultural heritage?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel("NB will be closed all the windows opened by this administration.",
                        SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = { "Close", "Cancel" };
                int choice = JOptionPane.showInternalOptionDialog(jContentPane, root,
                        "Confirm closing of Cultural Heritage", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                /*
                 * If it is confirmed the closing of management, all Classes "daughters" are
                 * closed.
                 */
                if (choice == JOptionPane.OK_OPTION) {
                    for (int i = 0; i < children.size(); i++) {
                        children.get(i).dispose();
                    }
                    pEvent.getInternalFrame().dispose();
                }
            }

        });
    }

    /**
     * Update the current model of the table of cultural property with the bean Of
     * the cultural supplied input.
     *
     * @Param pbc BeanCulturalHeritage - the bean with which to update the Model.
     */
    protected void updateTableModel(BeanCulturalHeritage PBC) {
        TableModel.updateBC(PBC);
    }

    /**
     * Closes the tab cultural selected.
     *
     * @Param pCard CardBC - the cultural card to close.
     */
    protected void closeCard(CardBC pCard) {
        children.remove(pCard);
        pCard.dispose();
    }

    /**
     * This method initializes the content pane of the frame.
     *
     * @Return javax.swing.JPanel - the content pane.
     */
    private JPanel getJContentPane() {
        if (null == jContentPane) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getBCToolbar(), BorderLayout.NORTH);
            jContentPane.add(getRightPanel(), BorderLayout.EAST);
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes the toolbar for features on Management of cultural
     * heritage.
     *
     * @Return javax.swing.JToolBar - the toolbar for managing assets Cultural.
     */
    private JToolBar getBCToolbar() {
        if (null == BCToolbar) {
            BCToolbar = new JToolBar();
            BCToolbar.setPreferredSize(new Dimension(1, 50));
            BCToolbar.setFloatable(false);
            BCToolbar.setOrientation(JToolBar.HORIZONTAL);
            BCToolbar.setLayout(null);
            BCToolbar.add(getBtnNewBC());
            BCToolbar.addSeparator();
            BCToolbar.add(getBtnModifyBC());
            BCToolbar.addSeparator();
            BCToolbar.add(getBtnCardBC());
            BCToolbar.addSeparator();
            BCToolbar.add(getBtnDeleteBC());
        }
        return BCToolbar;
    }

    /**
     * This method initializes the button to insert a new good Cultural.
     *
     * @Return javax.swing.JButton - the button for the insertion.
     */
    private JButton getBtnNewBC() {
        if (null == btnNewBC) {
            btnNewBC = new JButton();
            btnNewBC.setText("<html> New <br> Cultural Heritage </ html>");
            btnNewBC.setBounds(5, 5, 140, 40);
            btnNewBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "nuovoBC.png ")));
            btnNewBC.setName("btnNewBC ");
            btnNewBC.addMouseListener(bcHelp);
            btnNewBC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnNewBC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
// Opens a form for entering a new good
// Cultural.
                    openTab(null, false);
                }
            });
        }
        return btnNewBC;
    }

    /**
     * This method initializes the button to access to details of property Cultural
     * selected.
     *
     * @Return javax.swing.JButton - the button for the card.
     */
    private JButton getBtnCardBC() {
        if (null == btnCardBC) {
            btnCardBC = new JButton();
            btnCardBC.setBounds(305, 5, 140, 40);
            btnCardBC.setText("<html> Card <br> Cultural Heritage </ html>");
            btnCardBC.setVerticalTextPosition(SwingConstants.TOP);
            btnCardBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "scheda.png ")));
            btnCardBC.setEnabled(false);
            btnCardBC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnCardBC.setName("btnCardBC ");
            btnCardBC.addMouseListener(bcHelp);
            btnCardBC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    int selectedRow = tableBC.getSelectedRow();
                    BeanCulturalHeritage todo = null;
                    try {
                        todo = managerBC.getCulturalHeritage(TableModel.getID(selectedRow));
// Open the card with the change of the cultural
// Disabled.
                        openTab(todo, false);
                    } catch (Exception ex) {
                        JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                                + "The card <h3> <u> request can not be loaded. </ U> </ h3>"
                                + "<p> Please try again later. </ P>"
                                + "<p> If the error persists, please contact technical support. </ P>"
                                + "<p> We apologize for the inconvenience. </ Html>");
                        ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                        JOptionPane.showMessageDialog(JDesktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
                    }
                }
            });
        }
        return btnCardBC;
    }

    /**
     * This method initializes the button to access the modifytion of a Cultural.
     *
     * @Return javax.swing.JButton - the button for the card.
     */
    private JButton getBtnModifyBC() {
        if (null == btnModifyBC) {
            btnModifyBC = new JButton();
            btnModifyBC.setBounds(155, 5, 140, 40);
            btnModifyBC.setText("Edit Data <html> <br> Cultural Heritage </ html>");
            btnModifyBC.setEnabled(false);
            btnModifyBC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnModifyBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ModifyBC32.png ")));
            btnModifyBC.setName("btnModifyBC ");
            btnModifyBC.addMouseListener(bcHelp);
            btnModifyBC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    int selectedRow = tableBC.getSelectedRow();
                    BeanCulturalHeritage todo = null;
                    try {
                        todo = managerBC.getCulturalHeritage(TableModel.getID(selectedRow));
// Open the card with the change of the cultural
// Enabled.
                        openTab(todo, true);
                    } catch (Exception ex) {
                        JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                                + "The card <h3> <u> request can not be loaded. </ U> </ h3>"
                                + "<p> Please try again later. </ P>"
                                + "<p> If the error persists, please contact technical support. </ P>"
                                + "<p> We apologize for the inconvenience. </ Html>");
                        ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                        JOptionPane.showMessageDialog(JDesktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
                    }

                }
            });
        }
        return btnModifyBC;
    }

    /**
     * This method initializes the button for the elimination of property Cultural
     * selected.
     *
     * @Return javax.swing.JButton - the delete button for.
     */
    private JButton getBtnDeleteBC() {
        if (null == btnDeleteBC) {
            btnDeleteBC = new JButton();
            btnDeleteBC.setBounds(455, 5, 140, 40);
            btnDeleteBC.setText("Delete <html> <br> Cultural Heritage </ html>");
            btnDeleteBC.setVerticalTextPosition(SwingConstants.TOP);
            btnDeleteBC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnDeleteBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteBC32.png ")));
            btnDeleteBC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnDeleteBC.setEnabled(false);
            btnDeleteBC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    int selectedRow = tableBC.getSelectedRow();
                    String name = (String) TableModel.getValueAt(selectedRow, 0);

// Create the delete confirmation dialog.
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to delete the cultural heritage" + name + "?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("The deleted data can not be filled again.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = { "Delete", "Cancel" };
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm Delete",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteBC48.png")), options,
                            options[1]);
// If the deletion is confirmed, remove the well
// Cultural selected.
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            managerBC.clearCulturalHeritage(TableModel.getID(selectedRow));
                            TableModel.removeSite(selectedRow);
                            JLabel confirm = new JLabel("The cultural heritage" + name + "was deleted.");
                            confirm.setFont(new Font("Dialog", Font.BOLD, 14));
                            JOptionPane.showInternalMessageDialog(jContentPane, confirm, "Cultural Heritage",
                                    JOptionPane.OK_OPTION,
                                    new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Ok32.png ")));
                        } catch (Exception ex) {
                            JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                                    + "<h3> <u> Delete operation request can not be completed. </ U> </ h3>"
                                    + "<p> Please try again later. </ P>"
                                    + "<p> If the error persists, please contact technical support. </ P>"
                                    + "<p> We apologize for the inconvenience. </ Html>");
                            ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                            JOptionPane.showMessageDialog(JDesktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE,
                                    err);
                        }
                    }
                }
            });
            btnDeleteBC.setName("btnDeleteBC ");
            btnDeleteBC.addMouseListener(bcHelp);

        }
        return btnDeleteBC;

    }

    /**
     * This method initializes the right side of the interface.
     *
     * @Return javax.swing.JPanel - the right pane of the interface.
     */
    private JPanel getRightPanel() {
        if (null == rightPanel) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new BorderLayout());
            rightPanel.add(getHelpPanel(), BorderLayout.CENTER);
            rightPanel.add(getSearchPanel(), BorderLayout.CENTER);
        }
        return rightPanel;
    }

    /**
     * This method initializes the panel for finding property Cultural.
     *
     * @Return javax.swing.JPanel - the search panel.
     */
    private JPanel getSearchPanel() {
        if (null == searchPanel) {
            GridBagConstraints g = new GridBagConstraints();
            searchPanel = new JPanel(new GridBagLayout());
            searchPanel.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                            "Search for Cultural Heritage", TitledBorder.DEFAULT_JUSTIFICATION,
                            TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            g.anchor = GridBagConstraints.CENTER;
// Top - Left - Bottom - Right
            g.insets = new Insets(5, 5, 5, 5);
            g.gridwidth = 2;
            g.gridx = 0;
            g.gridy = 0;
            searchPanel.add(new JLabel("Name of Cultural Heritage"), g);
            g.gridy = 1;
            nameBC = new JTextField();
            nameBC.setColumns(12);
            nameBC.setName("nameBC ");
            nameBC.addMouseListener(bcHelp);
            searchPanel.add(nameBC, g);
            g.gridy = 2;
            searchPanel.add(new JLabel("Select search tags:"), g);
            g.fill = GridBagConstraints.VERTICAL;
            g.gridy = 3;
            g.weightx = 1.0;
            g.weighty = 1.0;
            g.insets = new Insets(5, 5, 10, 5);
            panelTag = new TagPanel();
            panelTag.setPreferredSize(new Dimension(180, 40));
            panelTag.setName("panelTag ");
            panelTag.addMouseListener(bcHelp);
            searchPanel.add(panelTag, g);
            g.insets = new Insets(5, 5, 5, 5);
            g.weightx = 0;
            g.weighty = 0;
            g.gridwidth = 1;
            g.gridy = 4;
            g.fill = GridBagConstraints.NONE;
            searchPanel.add(getBtnSearch(), g);
            g.gridx = 1;
            searchPanel.add(getBtnActive(), g);

        }
        return searchPanel;
    }

    /**
     * This method initializes the panel containing the online help.
     *
     * @Return javax.swing.JPanel - the panel of the guide.
     */
    private JPanel getHelpPanel() {
        if (null == helpPanel) {
            helpPanel = new JPanel();
            helpPanel.setLayout(new BorderLayout());
            helpPanel.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3), "Help",
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            textGuide.setPreferredSize(new Dimension(100, 80));
            textGuide.setContentType("text / html");
            textGuide.setText("<html> Move your mouse pointer over a control"
                    + "Of interest to display the context-sensitive help. </ Html>");
            textGuide.setEditable(false);
            textGuide.setOpaque(false);
            helpPanel.add(textGuide, BorderLayout.CENTER);
        }
        return helpPanel;
    }

    /**
     * This method initializes the bread and table scroll of cultural heritage.
     *
     * @Return javax.swing.JScrollPane - the scrollPane.
     */
    private JScrollPane getJScrollPane() {
        if (JScrollPane == null) {
            if (null == tableBC) {
                TableModel = new SiteTableModel();
                tableBC = new ScrollableTable(TableModel);
                tableBC.setRowHeight(32);
                tableBC.setDefaultRenderer(Double.class, new AverageRatingRenderer());
                tableBC.setDefaultRenderer(Point3D.class, new Point3DRenderer());
                tableBC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tableBC.setSelectionForeground(Color.RED);
                tableBC.setSelectionBackground(Color.white);
                tableBC.setShowVerticalLines(false);
                tableBC.setColumnSelectionAllowed(false);
                tableBC.addMouseListener(bcHelp);
                tableBC.setName("tableBC ");
                /*
                 * SelectionListener - if a selected row, the buttons Tab, edit and delete are
                 * active. Otherwise, are Disabled.
                 */
                ListSelectionModel selectionModel = tableBC.getSelectionModel();
                selectionModel.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent pEvent) {
                        if (tableBC.getSelectedRow() != -1) {
                            btnDeleteBC.setEnabled(true);
                            btnCardBC.setEnabled(true);
                            btnModifyBC.setEnabled(true);
                        } else {
                            btnDeleteBC.setEnabled(false);
                            btnCardBC.setEnabled(false);
                            btnModifyBC.setEnabled(false);
                        }
                    }
                });
                /*
                 * KeyListener <ENTER> - Details of the cultural selected. <Backspace> - Delete
                 * the selected cultural. <space> -- Modify the cultural selected.
                 */
                tableBC.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent pEvent) {
                        int keyCode = pEvent.getKeyCode();
                        if (keyCode == KeyEvent.VK_ENTER) {
                            btnCardBC.doClick();
                        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                            btnDeleteBC.doClick();
                        } else if (keyCode == KeyEvent.VK_SPACE) {
                            btnModifyBC.doClick();
                        }
                    }
                });
            }

            JScrollPane = new JScrollPane(tableBC);
            JScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            JScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return JScrollPane;
    }

    /**
     * This method initializes the button to search for a good Cultural.
     *
     * @Return javax.swing.JButton - the search button.
     */
    private JButton getBtnSearch() {
        if (null == btnSearch) {
            btnSearch = new JButton();
            btnSearch.setText("Search");
            btnSearch.setPreferredSize(new Dimension(98, 26));
            btnSearch.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Search16.png ")));
            btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnSearch.setName("btnSearch ");
            btnSearch.addMouseListener(bcHelp);
        }
        return btnSearch;
    }

    /**
     * This method initializes the button to clear the form Cultural research.
     *
     * @Return javax.swing.JButton - the button to reset the form.
     */
    private JButton getBtnActive() {
        if (null == btnActive) {
            btnActive = new JButton();
            btnActive.setText("Clear");
            btnActive.setHorizontalTextPosition(SwingConstants.LEADING);
            btnActive.setPreferredSize(new Dimension(98, 26));
            btnActive.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Active16.png ")));
            btnActive.setName("btnActive ");
            btnActive.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnActive.addMouseListener(bcHelp);
            btnActive.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    nameBC.setText("");
                    panelTag.disable();
                }

            });
        }
        return btnActive;
    }

    /**
     * This method opens a tab of the selected cultural or opens the Window for
     * entering a new cultural object.
     *
     * @Param pbc BeanCulturalHeritage - the bean of the cultural property of which
     *        Want to see the card.
     * @Param boolean pModify --
     *        <ul>
     *        <li><i> True </ i> - indicates that you are making a change Data
     *        cultural.<li> <i> False </ i> indicates that you are viewing the card
     *        The cultural property.
     */
    private void openTab(BeanCulturalHeritage pBc, boolean pModify) {
        CardBC new_;
        if (pBc == null) {
            new_ = new CardBC(this);

        } else {
            for (int i = 0; i < children.size(); i++) {
                CardBC current = children.get(i);
                if (pBc.getId() == current.getId()) {
                    desktopManager.activateFrame(current);
                    return;
                }
            }
            new_ = new CardBC(this, pBc, pModify);
        }
        JDesktopPane.add(new_, Integer.MAX_VALUE);
        desktopManager.centerFrame(new_);
        new_.setVisible(true);
        children.add(new_);
    }

    /**
     * This method imports the cultural downloaded from the server in Table.
     *
     * @Param boolean pSearch
     *        <ul>
     *        <li><i> True </ i> - include the search parameters.<li> <i> False </
     *        i> otherwise. </ Ul>
     */
    private void loadTable(boolean pSearch) {
        ArrayList<BeanCulturalHeritage> CulturalHeritage = null;
        try {
            if (pSearch) {

            } else {
                CulturalHeritage = managerBC.getCulturalHeritage();
            }
        }
// If an error displays an error message.
        catch (RemoteException e) {
            JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                    + "<h3> <u> The list of cultural goods has been loaded. </ U> </ h3>"
                    + "<p> Please try again later. </ P>"
                    + "<p> If the error persists, please contact technical support. </ P>"
                    + "<p> We apologize for the inconvenience. </ Html>");
            ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
            JOptionPane.showInternalMessageDialog(this, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
        } finally {
            TableModel = new SiteTableModel(CulturalHeritage);
            tableBC.setModel(TableModel);
            organizeColums();
        }
    }

    /**
     * This method loads the tags available in the system and import them into Panel
     * tag.
     */
    private void loadTags() {
        ArrayList<BeanTag> beanTags = null;
        try {
            beanTags = tags.getTags();
            for (BeanTag b : beanTags) {
                panelTag.insertTag(b);
            }
            panelTag.repaint();
        }
// If an error panel tag remains blank.
        catch (RemoteException e) {
        }
    }

    /**
     * This method sets the size of columns for the data assets Cultural.
     */
    private void organizeColums() {
// Name
        tableBC.getColumnModel().getColumn(0).setPreferredWidth(120);
// Address
        tableBC.getColumnModel().getColumn(1).setPreferredWidth(120);
// Phone
        tableBC.getColumnModel().getColumn(2).setPreferredWidth(80);
// Location
        tableBC.getColumnModel().getColumn(3).setPreferredWidth(80);
// City
        tableBC.getColumnModel().getColumn(4).setPreferredWidth(80);
// CAP
        tableBC.getColumnModel().getColumn(5).setPreferredWidth(50);
// Test
        tableBC.getColumnModel().getColumn(6).setPreferredWidth(30);
// RATINGS
        tableBC.getColumnModel().getColumn(7).setPreferredWidth(80);
// POSGEO
        tableBC.getColumnModel().getColumn(8).setPreferredWidth(120);
    }
}