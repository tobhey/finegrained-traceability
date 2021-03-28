package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.control.CulturalHeritageManager.ICulturalHeritageAgencyManager;
import unisa.gps.etour.control.TagManager.ITagCommonManager;
import unisa.gps.etour.gui.operatoragency.documents.LimitedDocument;
import unisa.gps.etour.gui.operatoragency.tables.AverageRatingRenderer;
import unisa.gps.etour.gui.operatoragency.tables.FeedBackTableModel;
import unisa.gps.etour.util.Point3D;

/**
 * Class that models the interface for viewing the card, Modify the data and the
 * insertion of a new cultural object.
 *
 *
 */
public class CardBC extends JInternalFrame implements ICard {

    private static final String[] txts = { "Name", "Address", "City", "Location", "CAP", "Province",
            "Geographical Location", "Phone", "Opening Hours", "Closing Time", "Closing Date", "Ticket price",
            "Description" };
    private static final String[] help = { "Enter the name of cultural property.",
            "Enter the address where is located the cultural property.",
            "Enter the city 'where is located the cultural property.",
            "Enter the location 'of membership of a cultural object.",
            "Enter your zip code, the area where the cultural object is located.",
            "Select the province belonging to the cultural property.",
            "Incorporating three dimensional coordinates for the location of" + "cultural heritage.",
            "Enter the phone for delivery of the cultural management office.",
            "Select the time of public opening of the cultural property.",
            "Select the closing time for the public of cultural property.", "Select the weekly closing day.",
            "Give the ticket price of admission to cultural property.",
            "<html> Enter a full and comprehensive description for the cultural property. <br> Please note that this"
                    + "description will be used as a source of keywords <br> in research by" + "tourists </ html>",
            "<html> Select the search tags for cultural property. <br> search tags allow tourists to seek"
                    + "The sites with the features of interest. </ Html>" };
    private JPanel jContentPane = null;
    private JToolBar toolbarCardBC = null;
    private JToggleButton btnModify = null;
    private JButton btnSave = null;
    private JButton btnCancel = null;
    private JButton btnModifyComment = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel statistics = null;
    private JPanel feedback = null;
    private JTextField address2 = null;
    private JComboBox address1 = null;
    private JTextField cityBC = null;
    private JTextField locationBC = null;
    private JTextField capBC = null;
    private JTextField posGeoX = null;
    private JScrollPane jScrollPane = null;
    private JTextArea descriptionBC = null;
    private JTextField phoneBC = null;
    private JComboBox hoursAP = null;
    private JComboBox minAP = null;
    private TagPanel panelTag;
    private JTextField costBC = null;
    private JComboBox Oreca = null;
    private JComboBox mince = null;
    private JComboBox provBC = null;
    private JPanel dataBC = null;
    private JTextField nameBC = null;
    private JScrollPane scrollPaneFeedback = null;
    private JTable tableFeedback = null;
    private JLabel txtNameBene = null;
    private JLabel averageRatingBC = null;
    private JPanel statMonthCurrent = null;
    private JPanel statTotal = null;
    private JTextField posGeoY = null;
    private JTextField posGeoZ = null;
    private Vector<JLabel> suggestions;
    private BeanCulturalHeritage bc;
    private JComboBox closingDay;
    private JLabel[] statMonthC;
    private JLabel[] statt;
    private CulturalHeritage parent;
    private FeedBackTableModel feedbackModel;
    protected ITagCommonManager tags;
    protected ICulturalHeritageAgencyManager managerBC;
    private ArrayList<Integer> idTag = null;

    /**
     * The default constructor for inclusion of the interface model A new cultural
     * object.
     *
     */
    public CardBC(CulturalHeritage pParent) {
        super("New Cultural Heritage");
        frameIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "nuovoBC.png"));
        closable = true;
        resizable = false;
        iconable = true;
        setSize(600, 560);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        suggestions = new Vector<JLabel>();
        parent = pParent;
        bc = null;

// Initialize the content pane
        jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        jContentPane.add(getToolbarCardBC(), BorderLayout.CENTER);
        jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Data Cultural Heritage",
                new ImageIcon(getClass().getResource(Home.URL_IMAGES + "data.png")), getDataBCForm(), null);
        jContentPane.add(jTabbedPane, BorderLayout.CENTER);
        setContentPane(jContentPane);

// Dialog closure to close the entry window.
        addInternalFrameListener(new InternalFrameAdapter() {
            /*
             * Inclusion of the frame on the desktop desktop retrieves bread bread And
             * desktop manager and initializes the remote objects for managing Cultural
             * heritage.
             */

            public void internalFrameOpened(InternalFrameEvent pEvent) {
                JInternalFrame frame = pEvent.getInternalFrame();

// Setting up of remote objects for the management of cultural heritage.
                try {
                    Registry reg = LocateRegistry.getRegistry(Home.HOST);
                    tags = (ITagCommonManager) reg.lookup("ManagerTagCommon ");
                    managerBC = (ICulturalHeritageAgencyManager) reg.lookup("ManagerCulturalHeritageAgency ");
// Load data.
                    loadTags();
                }
                /*
                 * Two exceptions: RemoteException and NotBoundException. The Result is the
                 * same. The management is not operable and After the error message window
                 * closes.
                 */
                catch (Exception ex) {
                    JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                            + "<h3> <u> Card for entering a new cultural asset will be closed. </ U> </ h3>"
                            + "<p> <b> Possible Causes: </ b>" + "<ul> <li> No connection to the network. </ Li>"
                            + "Server <li> inactive. </ Li>" + "Server <li> clogged. </ Li> </ ul>"
                            + "<p> Please try again later. </ P>"
                            + "<p> If the error persists, please contact technical support. </ P>"
                            + "<p> We apologize for the inconvenience. </ Html>");
                    ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(frame, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
                    frame.dispose();
                }
            }

            public void internalFrameClosing(InternalFrameEvent pEvent) {
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel("Are you sure you want to cancel the creation of a new cultural asset?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel("Warning! Unsaved data will be lost.", SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = { "Close", "Cancel" };
                int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm closure",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                if (choice == JOptionPane.OK_OPTION) {
                    parent.closeCard((CardBC) pEvent.getInternalFrame());
                }
            }
        });

// Initialize button
        btnModify.setVisible(false);
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        btnCancel.setText("Clear");

    }

    /**
     * This interface models the manufacturer regarding modifytion of data and
     * Display of the tab of a cultural object.
     *
     * @Param BeanCulturalHeritage pbc - the bean contains the data of Selected
     *        cultural.
     * @Param boolean pModify
     *        <ul>
     *        <li>true - the fields will be editable, and then you are To amend the
     *        data of a cultural object.
     *        <li>False - the fields will not be Edit, and then you are viewing the
     *        tab of a cultural object. </ Ul>
     *
     */
    public CardBC(CulturalHeritage pParent, BeanCulturalHeritage pBc, boolean pModify) {
        super();
        frameIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "scheda.png"));
        closable = true;
        resizable = false;
        iconable = true;
        setSize(600, 560);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

// Initialize instance variables
        bc = pBc;
        this.parent = pParent;
        suggestions = new Vector<JLabel>();
        initializeCardBC();

        if (pModify) // Are we change the cultural property.
        {
            btnModify.setSelected(true);
            btnSave.setVisible(true);
            btnCancel.setVisible(true);
            btnCancel.setText("Reset");
        } else // We're viewing the tab of a cultural object.
        {
            showHideSuggestions();
            activeDisactiveEdit();
        }

    }

    /**
     * This method returns the id of the cultural property for which you are viewing
     * the Contact or changing data.
     *
     * @Return int - the id of the cultural property.
     *
     */
    public int getId() {
        if (bc == null) {
            return -1;
        }
        return bc.getId();
    }

    /**
     *
     * This method initializes the interface for display board A cultural object.
     *
     * @Return void
     */
    private void initializeCardBC() {
        setTitle(bc.getName());

// Dialog closed frame
        addInternalFrameListener(new InternalFrameAdapter() {
            /*
             * Inclusion of the frame on the desktop desktop retrieves bread bread And
             * desktop manager and initializes the remote objects for managing Cultural
             * heritage.
             */

            public void internalFrameOpened(InternalFrameEvent pEvent) {
                JInternalFrame frame = pEvent.getInternalFrame();

// Setting up of remote objects for the management of cultural heritage.
                try {
                    Registry reg = LocateRegistry.getRegistry(Home.HOST);
                    tags = (ITagCommonManager) reg.lookup("ManagerTagCommon ");
                    managerBC = (ICulturalHeritageAgencyManager) reg.lookup("ManagerCulturalHeritageAgency ");
// Load data.
                    loadTags();
                    loadStatistic();
                }
                /*
                 * Two exceptions: RemoteException and NotBoundException. The Result is the
                 * same. The management is not operable and After the error message window
                 * closes.
                 */
                catch (Exception ex) {
                    JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                            + "<h3> <u> The board of the cultural inquiry will be closed. </ U> </ h3>"
                            + "<p> <b> Possible Causes: </ b>" + "<ul> <li> No connection to the network. </ Li>"
                            + "Server <li> inactive. </ Li>" + "Server <li> clogged. </ Li> </ ul>"
                            + "<p> Please try again later. </ P>"
                            + "<p> If the error persists, please contact technical support. </ P>"
                            + "<p> We apologize for the inconvenience. </ Html>");
                    ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(frame, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
                    frame.dispose();
                }
            }

            public void internalFrameClosing(InternalFrameEvent pEvent) {
// If you are an amendment asks for confirmation.
                if (btnModify.isSelected()) {
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to close the tab of this cultural asset?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("Warning! Unsaved data will be lost.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = { "Close", "Cancel" };
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root,
                            "Confirm closing tab Cultural Heritage" + bc.getName(), JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                    if (choice == JOptionPane.OK_OPTION) {
                        parent.closeCard((CardBC) pEvent.getInternalFrame());
                    }
                }
// Otherwise directly closes the window.
                else {
                    parent.closeCard((CardBC) pEvent.getInternalFrame());
                }
            }
        });

// Initialize the content pane.
        jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        jContentPane.add(getToolbarCardBC(), BorderLayout.CENTER);
        jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Data Cultural Heritage",
                new ImageIcon(getClass().getResource(Home.URL_IMAGES + "data.png")), getDataBCForm(), null);
        JScrollPane new_ = new JScrollPane(getStatistic());
        new_.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jTabbedPane.addTab("Statistics", new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stat24.png")), new_,
                null);
        jTabbedPane.addTab("Feedback received", new ImageIcon(getClass().getResource(Home.URL_IMAGES + "feedback.png")),
                getFeedback(), null);
        jContentPane.add(jTabbedPane, BorderLayout.CENTER);
        setContentPane(jContentPane);
        jTabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent pChange) {
// Data cultural
                if (jTabbedPane.getSelectedIndex() == 0) {
                    toolbarCardBC.setVisible(true);
                    btnModify.setVisible(true);
                    if (btnModify.isSelected()) {
                        btnSave.setVisible(true);
                        btnCancel.setVisible(true);
                    }
                    btnModifyComment.setVisible(false);
                }
// Statistics
                else if (jTabbedPane.getSelectedIndex() == 1) {
                    toolbarCardBC.setVisible(false);
                }
// Feedback received
                else {
                    if (btnModify.isSelected()) {
                        btnSave.setVisible(false);
                        btnCancel.setVisible(false);
                    }
                    toolbarCardBC.setVisible(true);
                    btnModifyComment.setVisible(true);
                    btnModify.setVisible(false);

                }

            }

        });

// Load the data of the cultural and statistics.
        loadDataForm();
    }

    /**
     *
     * This method loads the data supplied to the constructor of the cultural In the
     * form.
     *
     */
    private void loadDataForm() {
        nameBC.setText(bc.getName());
        capBC.setText(bc.getCap());
        cityBC.setText(bc.getCity());
        costBC.setText("" + bc.getTicketCost());
        descriptionBC.setText(bc.getDescription());
        StringTokenizer tokenizer = new StringTokenizer(bc.getStreet());
        String string = tokenizer.nextToken();
        address1.setSelectedItem(string);
        address2.setText(bc.getStreet().substring(string.length()));
        provBC.setSelectedItem(bc.getProvince());
        Point3D pos = bc.getPosition();
        posGeoX.setText("" + pos.getLatitude());
        posGeoY.setText("" + pos.getLongitude());
        posGeoZ.setText("" + pos.getAltitude());
        phoneBC.setText(bc.getPhone());
        int minutes = bc.getOpeningTime().getMinutes();
        if (minutes == 0) {
            minAP.setSelectedIndex(0);
        } else {
            minAP.setSelectedItem(minutes);
        }
        int hours = bc.getOpeningTime().getHours();
        if (hours < 10) {
            hoursAP.setSelectedItem("0" + hours);
        } else {
            hoursAP.setSelectedItem(hours);
        }
        minutes = bc.getClosingTime().getMinutes();
        if (minutes == 0) {
            minAP.setSelectedIndex(0);
        } else {
            minAP.setSelectedItem(minutes);
        }
        hours = bc.getClosingTime().getHours();
        if (hours < 10) {
            hoursAP.setSelectedItem("0" + hours);
        } else {
            hoursAP.setSelectedItem(hours);
        }
    }

    /**
     *
     * This method loads the statistics provided cultural Input to the constructor
     * of the class.
     *
     */
    private void loadStatistic() {
        txtNameBene.setText(bc.getName());
        double rating = bc.getAverageRating();
        if (rating >= 4) {
            averageRatingBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella5.gif ")));
        } else if (rating < 4 && rating >= 3) {
            averageRatingBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella4.gif ")));
        } else if (rating < 3 && rating >= 2) {
            averageRatingBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella3.gif ")));
        } else if (rating < 2 && rating >= 1) {
            averageRatingBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella2.gif ")));
        } else {
            averageRatingBC.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stella1.gif ")));
        }

        ArrayList<Integer> stats = null;
        try {
            stats = managerBC.getStatisticCulturalHeritage(bc.getId());

        } catch (RemoteException e) {
        }
        statMonthC[0].setText("" + stats.get(0));
        statMonthC[1].setText("" + stats.get(1));
        statMonthC[2].setText("" + stats.get(2));
        statMonthC[3].setText("" + stats.get(3));
        statMonthC[4].setText("" + stats.get(4));
        statMonthC[5].setText("" + stats.get(5));
        statt[0].setText("142");
        statt[1].setText("112");
        statt[2].setText("132");
        statt[3].setText("212");
        statt[4].setText("152");
        statt[5].setText("748");
// END TEST
    }

    /**
     *
     * This method shows or hides the label next to the suggestions Of the form.
     *
     */
    private void showHideSuggestions() {
        Iterator<JLabel> s = suggestions.iterator();
        while (s.hasNext()) {
            JLabel current = s.next();
            current.setVisible(current.isVisible() ? false : true);
        }
    }

    /**
     *
     * This method makes the form editable or not.
     *
     */
    private void activeDisactiveEdit() {
        Component[] components = dataBC.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component current = components[i];
            if (current instanceof JTextField) {
                JTextField textbox = (JTextField) current;
                textbox.setEditable(textbox.isEditable() ? false : true);
                textbox.setBackground(Color.white);

            } else if (current instanceof JComboBox) {
                JComboBox combobox = (JComboBox) current;
                combobox.setEnabled(combobox.isEnabled() ? false : true);

            }
        }
        descriptionBC.setEditable(descriptionBC.isEditable() ? false : true);
        panelTag.activeDisactive();
    }

    /**
     * This method initializes the toolbar tab of a cultural object.
     *
     * @Return javax.swing.JToolBar - the toolbar.
     */
    private JToolBar getToolbarCardBC() {
        if (null == toolbarCardBC) {
            toolbarCardBC = new JToolBar();
            toolbarCardBC.setFloatable(false);
            toolbarCardBC.add(getBtnModify());
            toolbarCardBC.addSeparator();
            toolbarCardBC.add(getBtnSave());
            toolbarCardBC.addSeparator();
            toolbarCardBC.add(getBtnCancel());
            toolbarCardBC.addSeparator();
            if (bc != null) {
                toolbarCardBC.add(getBtnModifyComment());
                toolbarCardBC.addSeparator();
            }
        }
        return toolbarCardBC;
    }

    /**
     * This method initializes the button to modify data of good Cultural.
     *
     * @Return javax.swing.JToggleButton - the button for the change.
     *
     */
    private JToggleButton getBtnModify() {
        if (null == btnModify) {
            btnModify = new JToggleButton();
            btnModify.setText("Change Data");
            btnModify.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ModifyBC32.png ")));
            btnModify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnModify.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    showHideSuggestions();
                    activeDisactiveEdit();
                    btnSave.setVisible((btnModify.isSelected() ? true : false));
                    btnCancel.setVisible((btnModify.isSelected() ? true : false));

                }

            });
        }
        return btnModify;
    }

    /**
     * This method initializes btnSave
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnSave() {
        if (null == btnSave) {
            btnSave = new JButton();
            btnSave.setText("Save");
            btnSave.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "salva.png ")));
            btnSave.setVisible(false);
            btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    if (bc == null) {
                        bc = putDataBean();
                        try {
                            managerBC.insertCulturalHeritage(bc);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    } else {
// Construction of the dialog for confirmation of the change
                        JPanel root = new JPanel(new BorderLayout());
                        JLabel message = new JLabel(
                                "Updatang the data of the cultural " + bc.getName() + "with" + "Data form?");
                        message.setFont(new Font("Dialog", Font.BOLD, 14));
                        JLabel alert = new JLabel("The previous data can not be more recovered.",
                                SwingConstants.CENTER);
                        alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                        root.add(message, BorderLayout.NORTH);
                        root.add(alert, BorderLayout.CENTER);
                        String[] options = { "Edit", "Cancel" };
// The dialog screen appears
                        int choice = JOptionPane.showInternalOptionDialog(jContentPane, root,
                                "Edit Data Confirm Cultural Heritage", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ModifyBC48.png")), options,
                                options[1]);
// If you chose to confirm the change
                        if (choice == JOptionPane.YES_OPTION) {
                            bc = putDataBean();
                            loadDataForm();
                            activeDisactiveEdit();
                            btnSave.setVisible(false);
                            btnCancel.setVisible(false);
                            btnModify.setSelected(false);
                            showHideSuggestions();
                            parent.updateTableModel(bc);
                            JOptionPane.showInternalMessageDialog(jContentPane,
                                    "The data of the cultural object has been updated successfully. ",
                                    "Data cultural change!", JOptionPane.OK_OPTION,
                                    new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ok32.png ")));
                        }
                    }
                }
            });
        }
        return btnSave;
    }

    /**
     * This method initializes the button to clear the form (well again Culture) or
     * reload the data of the cultural (change data).
     *
     * @Return javax.swing.JButton - the button above.
     *
     */
    private JButton getBtnCancel() {
        if (null == btnCancel) {
            btnCancel = new JButton();
            btnCancel.setText("Cancel");
            btnCancel.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Cancel32.png ")));
            btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnCancel.setVisible(false);
            btnCancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    if (bc == null) {
                        Component[] components = dataBC.getComponents();
                        for (int i = 0; i < components.length; i++) {
                            Component current = components[i];
                            if (current instanceof JTextComponent) {
                                ((JTextComponent) current).setText("");
                            } else if (current instanceof JComboBox) {
                                JComboBox combobox = (JComboBox) current;
                                combobox.setSelectedIndex(-1);
                            }
                        }
                        panelTag.clear();
                        descriptionBC.setText("");
                    } else {
                        loadDataForm();
                    }

                }

            });
        }
        return btnCancel;
    }

    /**
     * This method initializes the button to edit a comment.
     *
     * @Return javax.swing.JButton - the button to edit a comment.
     */
    private JButton getBtnModifyComment() {
        if (null == btnModifyComment) {
            btnModifyComment = new JButton();
            btnModifyComment.setText("Edit Comment");
            btnModifyComment
                    .setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "modifyComment.png ")));
            btnModifyComment.setVisible(false);
            btnModifyComment.setEnabled(false);
            btnModifyComment.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent pEvent) {
                    int selectedRow = tableFeedback.getSelectedRow();
                    String nuovoComment = (String) JOptionPane.showInternalInputDialog(jContentPane,
                            "Changing the selected comment:", "Edit Comment", JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(getClass().getResource(Home.URL_IMAGES + "modifyComment.png")), null,
                            feedbackModel.getValueAt(selectedRow, 1));
                    if (nuovoComment != null) {
                        feedbackModel.modifyComment(nuovoComment, selectedRow);
                    }
                }

            });
        }
        return btnModifyComment;
    }

    /**
     * This method initializes the format for the data of a cultural object.
     *
     * @Return javax.swing.JPanel - the form for the data.
     *
     */
    private JPanel getDataBCForm() {
        if (null == dataBC) {
            dataBC = new JPanel(null);
            dataBC.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

// Creation Tips
            for (int i = 0; i < help.length; i++) {
                JLabel nuova = new JLabel();
                nuova.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Info16.png ")));
                nuova.setBounds(145, 8 + 30 * i, 24, 24);
                nuova.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                nuova.setToolTipText(help[i]);
                suggestions.add(nuova);
                dataBC.add(nuova);
                if (i == help.length - 1) {
                    nuova.setBounds(400, 155, 24, 24);
                }

            }

            for (int i = 0; i < txts.length; i++) {
                JLabel nuova = new JLabel(txts[i], SwingConstants.RIGHT);
                nuova.setBounds(25, 10 + 30 * i, 120, 16);
                nuova.repaint();
                dataBC.add(nuova, null);
            }
// Name of Cultural Heritage
            nameBC = new JTextField();
            nameBC.setColumns(12);
            nameBC.setDocument(new LimitedDocument(30));
            nameBC.setBounds(185, 10, 136, 20);
            nameBC.setName("Name Cultural Heritage");
            dataBC.add(nameBC, null);

// Address
            address2 = new JTextField();
            address2.setBounds(270, 40, 136, 20);
            address2.setDocument(new LimitedDocument(30));
            address1 = new JComboBox();
            address1.setSelectedIndex(-1);
            address1.setBounds(185, 40, 60, 20);
            address1.setName("Address");
            dataBC.add(address2, null);
            dataBC.add(address1, null);

// City
            cityBC = new JTextField();
            cityBC.setColumns(12);
            cityBC.setBounds(185, 70, 136, 20);
            cityBC.setName("City");
            cityBC.setDocument(new LimitedDocument(25));
            dataBC.add(cityBC);

// Location
            locationBC = new JTextField();
            locationBC.setBounds(185, 100, 136, 20);
            locationBC.setName("Localit ");
            locationBC.setDocument(new LimitedDocument(25));
            dataBC.add(locationBC, null);

// CAP
            capBC = new JTextField();
            capBC.setColumns(8);
            capBC.setBounds(185, 130, 92, 20);
            capBC.setDocument(new LimitedDocument(5));
            dataBC.add(capBC, null);

// Geographical Location
            JLabel txtX = new JLabel("X");
            JLabel txtY = new JLabel("Y");
            JLabel txtZ = new JLabel("Z");
            Font new_ = new Font("Dialog", Font.BOLD, 14);
            txtX.setFont(new_);
            txtY.setFont(new_);
            txtZ.setFont(new_);
            txtZ.setBounds(365, 190, 10, 22);
            txtY.setBounds(295, 190, 10, 22);
            txtX.setBounds(227, 190, 14, 20);
            posGeoX = new JTextField(12);
            posGeoX.setBounds(185, 190, 40, 20);
            posGeoY = new JTextField(12);
            posGeoY.setBounds(255, 190, 40, 20);
            posGeoZ = new JTextField(12);
            posGeoZ.setBounds(325, 190, 40, 20);
            posGeoX.setName("X-coordinate");
            posGeoY.setName("Y coordinate");
            posGeoZ.setName("z coordinate");
            dataBC.add(txtX, null);
            dataBC.add(txtY, null);
            dataBC.add(txtZ, null);
            dataBC.add(posGeoX, null);
            dataBC.add(posGeoY, null);
            dataBC.add(posGeoZ, null);

// State
            provBC = new JComboBox();
            provBC.setSelectedIndex(-1);
            provBC.setBounds(185, 160, 50, 20);
            dataBC.add(provBC, null);

// Description
            descriptionBC = new JTextArea();
            descriptionBC.setCursor(new Cursor(Cursor.TEXT_CURSOR));
            descriptionBC.setWrapStyleWord(true);
            descriptionBC.setLineWrap(true);
            jScrollPane = new JScrollPane(descriptionBC);
            jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setBounds(new Rectangle(185, 370, 395, 69));
            descriptionBC.setName("Description");
            dataBC.add(jScrollPane, null);

// Phone
            phoneBC = new JTextField(12);
            phoneBC.setBounds(185, 220, 136, 20);
            phoneBC.setDocument(new LimitedDocument(10));
            phoneBC.setName("Phone");
            dataBC.add(phoneBC, null);

// Opening
            hoursAP = new JComboBox();
            hoursAP.setBounds(185, 250, 40, 20);
            Oreca = new JComboBox();
            hoursAP.setBounds(185, 280, 40, 20);
            for (int i = 0; i < 24; i++) {
                if (i < 10) {
                    hoursAP.addItem("0" + i);
                    hoursAP.addItem("0" + i);
                } else {
                    hoursAP.addItem(i);
                    hoursAP.addItem(i);
                }
            }
            minAP = new JComboBox();
            minAP.setBounds(241, 250, 40, 20);
            minAP.addItem("00");
            minAP.addItem("15");
            minAP.addItem("30");
            minAP.addItem("45");
            mince = new JComboBox();
            mince.setBounds(241, 280, 40, 20);
            mince.addItem("00");
            mince.addItem("15");
            mince.addItem("30");
            mince.addItem("45");
            minAP.setSelectedIndex(0);
            hoursAP.setSelectedIndex(-1);
            mince.setSelectedIndex(0);
            hoursAP.setSelectedIndex(-1);
            new_ = new Font("Dialog", Font.BOLD, 18);
            JLabel punto1 = new JLabel(":");
            punto1.setBounds(230, 245, 10, 24);
            punto1.setFont(new_);
            JLabel punto2 = new JLabel(":");
            punto2.setBounds(230, 275, 10, 24);
            punto2.setFont(new_);
            dataBC.add(hoursAP, null);
            dataBC.add(minAP, null);
            dataBC.add(mince, null);
            dataBC.add(Oreca, null);
            dataBC.add(punto1, null);
            dataBC.add(punto2, null);

// Closed
            String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
            closingDay = new JComboBox(days);
            closingDay.setBounds(185, 310, 96, 20);
            closingDay.setSelectedIndex(-1);
            dataBC.add(closingDay, null);

// Cost
            costBC = new JTextField();
            costBC.setColumns(8);
            costBC.setBounds(185, 340, 40, 20);
            JLabel euro = new JLabel("Euro");
            euro.setBounds(230, 340, 30, 16);
            dataBC.add(costBC, null);
            dataBC.add(euro, null);

// panelTag
            panelTag = new TagPanel();
            panelTag.setBounds(405, 180, 180, 170);
            JLabel txtTag = new JLabel("Search Tags");
            txtTag.setBounds(420, 150, 140, 30);
            dataBC.add(txtTag, null);
            dataBC.add(panelTag, null);

        }
        return dataBC;
    }

    /**
     * This method initializes the statistics of a container panel Cultural.
     *
     * @Return javax.swing.JPanel - the panel statistics.
     *
     */
    private JPanel getStatistic() {
        if (statistics == null) {
            statistics = new JPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            g.insets = new Insets(5, 5, 5, 5);
            g.anchor = GridBagConstraints.WEST;
            JLabel stat = new JLabel("Statistics");
            Font new_ = new Font("Dialog", Font.BOLD, 18);
            stat.setFont(new_);
            statistics.add(stat, g);
            g.gridx = 1;
            txtNameBene = new JLabel();
            txtNameBene.setFont(new_);
            statistics.add(txtNameBene, g);
            g.gridx = 2;
            averageRatingBC = new JLabel();
            statistics.add(averageRatingBC, g);
            g.gridwidth = 3;
            g.gridx = 0;
            g.anchor = GridBagConstraints.CENTER;
            g.gridy = 1;
            statistics.add(getStatMonthCurrent(), g);
            g.gridy = 2;
            statistics.add(getStatTotal(), g);
        }
        return statistics;
    }

    /**
     * This method initializes the panel to display feedback Received from a
     * cultural object.
     *
     * @Return javax.swing.JPanel - the panel of feedback.
     *
     */
    private JPanel getFeedback() {
        if (feedback == null) {
            feedback = new JPanel();
            feedback.setLayout(new BorderLayout());
            feedbackModel = new FeedBackTableModel();
            tableFeedback = new JTable(feedbackModel);
            TableColumn aColumn = tableFeedback.getColumnModel().getColumn(0);
// Rating
            aColumn.setPreferredWidth(80);
            aColumn.setCellRenderer(new AverageRatingRenderer());
// Comment
            aColumn = tableFeedback.getColumnModel().getColumn(1);
            aColumn.setPreferredWidth(260);
// Release Date
            aColumn = tableFeedback.getColumnModel().getColumn(2);
            aColumn.setPreferredWidth(80);
// Username
            aColumn = tableFeedback.getColumnModel().getColumn(3);
            aColumn.setPreferredWidth(80);
            tableFeedback.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableFeedback.setColumnSelectionAllowed(false);
            ListSelectionModel selectionModel = tableFeedback.getSelectionModel();
            selectionModel.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    btnModifyComment.setEnabled((tableFeedback.getSelectedRow() != -1) ? true : false);
                }
            });
            scrollPaneFeedback = new JScrollPane(tableFeedback);
            scrollPaneFeedback.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            feedback.add(scrollPaneFeedback, BorderLayout.CENTER);

        }
        return feedback;
    }

    /**
     * This method initializes the panel of statistics for the current month.
     *
     * @Return javax.swing.JPanel - the panel of statistics for the current month.
     *
     */
    private JPanel getStatMonthCurrent() {
        if (null == statMonthCurrent) {
            statMonthCurrent = new JPanel();
            statMonthCurrent.setLayout(new GridBagLayout());
            statMonthCurrent.setPreferredSize(new Dimension(500, 280));
            statMonthCurrent.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                            "Statistics Current Month", TitledBorder.DEFAULT_JUSTIFICATION,
                            TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            statMonthCurrent.setBackground(Color.white);
            statMonthC = new JLabel[6];
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 3;
            g.insets = new Insets(5, 5, 5, 8);
            g.anchor = GridBagConstraints.WEST;
            statMonthCurrent.add(new JLabel("Details votes received this month:"), g);
            g.anchor = GridBagConstraints.CENTER;
            g.gridwidth = 1;
            Font new_ = new Font("Dialog", Font.BOLD, 16);
            for (int i = 5; i >= 1; i--) {

                int gridX = g.gridx;
                g.gridy++;
                JLabel aLabel = new JLabel(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "star" + i + "Gif")),
                        JLabel.CENTER);
                statMonthCurrent.add(aLabel, g);
                g.gridx++;
                statMonthCurrent.add(new JLabel("=="), g);
                g.gridx++;
                g.anchor = GridBagConstraints.EAST;
                statMonthC[i - 1] = new JLabel();
                statMonthC[i - 1].setFont(new_);
                statMonthCurrent.add(statMonthC[i - 1], g);
                g.gridx = gridX;
                g.anchor = GridBagConstraints.CENTER;
            }
            g.gridy = 6;
            g.anchor = GridBagConstraints.WEST;
            g.gridwidth = 2;
            g.gridx = 0;
            statMonthCurrent.add(new JLabel("Number of ratings released this month:"), g);
            statMonthC[5] = new JLabel();
            statMonthC[5].setFont(new Font("Dialog", Font.BOLD, 18));
            g.gridx = 2;
            g.gridwidth = 1;
            statMonthCurrent.add(statMonthC[5], g);

        }
        return statMonthCurrent;
    }

    /**
     * This method initializes the panel on the total statistics The cultural
     * property.
     *
     * @Return javax.swing.JPanel - the panel statistics totals.
     *
     */
    private JPanel getStatTotal() {
        if (null == statTotal) {
            statTotal = new JPanel();
            statTotal.setLayout(new GridBagLayout());
            statTotal.setPreferredSize(new Dimension(500, 280));
            statTotal.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                            "Statistics Total", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            statTotal.setBackground(Color.white);
            statt = new JLabel[6];
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 3;
            g.insets = new Insets(5, 5, 5, 8);
            g.anchor = GridBagConstraints.WEST;
            statTotal.add(new JLabel("Details votes received this month:"), g);
            g.anchor = GridBagConstraints.CENTER;
            g.gridwidth = 1;
            Font new_ = new Font("Dialog", Font.BOLD, 16);
            for (int i = 5; i >= 1; i--) {
                int gridX = g.gridx;
                g.gridy++;
                JLabel aLabel = new JLabel(
                        new ImageIcon(
                                getClass().getResource("/ unisa / gps / eTour / gui / images / star" + i + "Gif")),
                        JLabel.CENTER);
                statTotal.add(aLabel, g);
                g.gridx++;
                statTotal.add(new JLabel("=="), g);
                g.gridx++;
                g.anchor = GridBagConstraints.EAST;
                statt[i - 1] = new JLabel();
                statt[i - 1].setFont(new_);
                statTotal.add(statt[i - 1], g);
                g.gridx = gridX;
                g.anchor = GridBagConstraints.CENTER;
            }
            g.gridy = 6;
            g.anchor = GridBagConstraints.WEST;
            g.gridwidth = 2;
            g.gridx = 0;
            statTotal.add(new JLabel("Number of ratings released this month:"), g);
            statt[5] = new JLabel();
            statt[5].setFont(new Font("Dialog", Font.BOLD, 18));
            g.gridx = 2;
            g.gridwidth = 1;
            statTotal.add(statt[5], g);
        }
        return statTotal;
    }

    private BeanCulturalHeritage putDataBean() {
        BeanCulturalHeritage new_ = new BeanCulturalHeritage();
        new_.setName(nameBC.getText());
        new_.setDescription(descriptionBC.getText());
        new_.setCap(capBC.getText());
        new_.setTicketCost(Double.parseDouble(costBC.getText()));
        new_.setClosingDay((String) closingDay.getSelectedItem());
        new_.setPhone(phoneBC.getText());
        new_.setCity(cityBC.getText());
        new_.setLocation(locationBC.getText());
// Date (int year, int month, int date, int hrs, int min)
        Date scheduleAP = new Date(0, 0, 0, hoursAP.getSelectedIndex(), minAP.getSelectedIndex());
        Date scheduleCH = new Date(0, 0, 0, Oreca.getSelectedIndex(), mince.getSelectedIndex());
        new_.setOpeningTime(scheduleAP);
        new_.setClosingTime(scheduleCH);
        new_.setProvince((String) provBC.getSelectedItem());
        new_.setStreet(((String) address1.getSelectedItem()) + "" + address2.getText());
        Point3D position = new Point3D(Double.parseDouble(posGeoX.getText()), Double.parseDouble(posGeoY.getText()),
                Double.parseDouble(posGeoZ.getText()));
        new_.setPosition(position);
        return new_;
    }

    private void loadTags() {
        ArrayList<BeanTag> beanTags = null;
        try {
            beanTags = tags.getTags();
            if (bc != null) {
                idTag = new ArrayList<Integer>();
                ArrayList<BeanTag> tagDaSelezionare = managerBC.getTagCulturalHeritage(bc.getId());
                for (BeanTag b : tagDaSelezionare) {
                    idTag.add(b.getId());
                }
            }
        }
// If an error panel tag remains blank.
        catch (RemoteException e) {
        } finally {
            for (BeanTag b : beanTags) {
                panelTag.insertTag(b);
            }
            panelTag.setSelectedTags(idTag);
            panelTag.repaint();
        }
    }
}
