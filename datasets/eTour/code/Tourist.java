package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

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
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.operatoragency.tables.ScrollableTable;
import unisa.gps.etour.gui.operatoragency.tables.TouristTableModel;
import unisa.gps.etour.gui.operatoreagenzia.tables.ActivezioneTouristRenderer;
import unisa.gps.etour.util.Data;

/**
 * This class implements the interface for the management of tourists Agency by
 * the Operator.
 *
 */
public class Tourist extends JInternalFrame {

    private JPanel jContentPane = null;
    private JToolBar toolbarTourist = null;
    private JButton btnModifyData = null;
    private JScrollPane scrollTableTourist = null;
    private JTable tableTourist = null;
    private JPanel searchPanel = null;
    private JTextField surnameTourist = null;
    private JPanel panelHelp = null;
    private JTextPane textGuide = null;
    private JPanel rightPanel = null;
    private JButton btnActive;
    private JButton btnViewCard;
    private JButton btnDelete;
    private TouristTableModel TableModel;
    private JToggleButton inactiveTourist;
    private JToggleButton activeTourist;
    private JButton btnActive2;
    private JButton btnSearch;
    protected DeskManager desktopManager;
    protected JDesktopPane jDesktopPane;
    private ArrayList<TouristCard> children;

    /**
     * This is the default constructor.
     *
     */
    public Tourist() {
        super("Tourists");
        setPreferredSize(new Dimension(700, 480));
        frameIcon = new ImageIcon(
                getClass().getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / Tourist.png"));
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        closable = true;
        resizable = true;
        iconable = true;
        maximizable = true;
        setContentPane(getJContentPane());
        children = new ArrayList<TouristCard>();
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameOpened(InternalFrameEvent pEvent) {
                jDesktopPane = pEvent.getInternalFrame().getDesktopPane();
                desktopManager = (DeskManager) jDesktopPane.getDesktopManager();
            }

            public void internalFrameClosing(InternalFrameEvent pEvent) {
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel("Are you sure you want to close the management of tourists?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel("NB will be closed all the windows opened by this administration.",
                        SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(
                        getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/warning16.png ")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = { "Close", "Cancel" };
                int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm closure Tourists",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
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
     * This method initializes the internal frame's content pane.
     *
     * @Return javax.swing.JPanel - the content pane.
     *
     */
    private JPanel getJContentPane() {
        if (null == jContentPane) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getToolbarTourist(), BorderLayout.NORTH);
            jContentPane.add(getRightPanel(), BorderLayout.EAST);
            jContentPane.add(getScrollTableTourist(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes the toolbar on the management of Tourists.
     *
     * @Return javax.swing.JToolBar - the toolbar.
     *
     */
    private JToolBar getToolbarTourist() {
        if (null == toolbarTourist) {
            toolbarTourist = new JToolBar();
            toolbarTourist.setLayout(null);
            toolbarTourist.setPreferredSize(new Dimension(1, 50));
            toolbarTourist.setOrientation(JToolBar.HORIZONTAL);
            toolbarTourist.setFloatable(false);
            toolbarTourist.add(getBtnActive());
            toolbarTourist.add(getBtnModifyData());
            toolbarTourist.add(getBtnViewCard());
            toolbarTourist.add(getBtnDelete());

        }
        return toolbarTourist;
    }

    /**
     * This method initializes the button to activate A tourist.
     *
     * @Return javax.swing.JButton - the button.
     *
     */
    private JButton getBtnActive() {
        if (null == btnActive) {
            btnActive = new JButton();
            btnActive.setText("Enable <html> <br> region </ html>");
            btnActive.setBounds(5, 5, 140, 40);
            btnActive.setEnabled(false);
            btnActive.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/ActiveTourist32.png ")));

            btnActive.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    int selected = tableTourist.getSelectedRow();
                    String[] fields = { "Name", "Name", "Date of Birth", "Place of Birth", "E-Mail", "Phone", "Address",
                            "CPC", "Province", "City" };
                    int[] modelReference = { 1, 2, 5, 6, 3, 4, 7, 9, 10, 8 };
                    JPanel dataTourist = new JPanel(new GridBagLayout());
                    JPanel rootDialog = new JPanel(new GridBagLayout());
                    dataTourist.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(51, 102, 255), 2), "Tourist Information",
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
                    GridBagConstraints g = new GridBagConstraints();
                    g.insets = new Insets(5, 5, 5, 5);
                    g.anchor = GridBagConstraints.WEST;
                    g.gridx = 0;
                    g.gridy = 0;
                    for (int i = 0; i < fields.length; i++) {
                        dataTourist.add(new JLabel(fields[i]), g);
                        g.gridx++;
                        if (i == 2) // Date of Birth
                        {
                            Date dob = (Date) TableModel.getValueAt(selected, 5);
                            dataTourist.add(new JLabel(Data.toCompact(dob)), g);

                        } else {
                            dataTourist.add(new JLabel(TableModel.getValueAt(selected, modelReference[i]).toString()),
                                    g);
                        }
                        g.gridx--;
                        g.gridy++;
                    }
                    g.gridx = 0;
                    g.gridy = 0;
                    rootDialog.add(dataTourist, g);
                    g.gridy++;
                    JLabel txtActive = new JLabel();
                    rootDialog.add(txtActive, g);
                    String[] options = new String[2];
                    ImageIcon iconDialog;
                    options[1] = "Cancel";
                    String title;
                    boolean enabled = (Boolean) TableModel.getValueAt(selected, 0);
                    if (enabled) // The process of decommissioning
                    {
                        title = "Turn off the tourist" + TableModel.getValueAt(selected, 1) + ""
                                + TableModel.getValueAt(selected, 2) + "?";
                        options[0] = "Disable";
                        txtActive.setText("Turn off the tourist selected?");
                        iconDialog = new ImageIcon(getClass()
                                .getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/DisactiveTourist48.png"));
                    } else // The process of activation
                    {
                        title = "Turn on the tourist" + TableModel.getValueAt(selected, 1) + ""
                                + TableModel.getValueAt(selected, 2) + "?";
                        options[0] = "Enable";
                        txtActive.setText("Activate the tourists selected?");
                        iconDialog = new ImageIcon(getClass()
                                .getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/ActiveTourist48.png"));
                    }
                    txtActive.setForeground(Color.red);
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, rootDialog, title,
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconDialog, options,
                            options[1]);
                    if (choice == JOptionPane.OK_OPTION) {
                        TableModel.setValueAt((enabled) ? false : true, selected, 0);
                        TableModel.fireTableDataChanged();
                    }
                }
            });
        }
        return btnActive;
    }

    /**
     * This method initializes the button for changing Data of a tourist.
     *
     * @Return javax.swing.JButton - the button.
     */
    private JButton getBtnModifyData() {
        if (null == btnModifyData) {
            btnModifyData = new JButton();
            btnModifyData.setText("Edit Data <html> <br> region </ html>");
            btnModifyData.setBounds(155, 5, 140, 40);
            btnModifyData.setEnabled(false);
            btnModifyData.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/ModifyTourist32.png ")));
            btnModifyData.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
// TEST
                    Date nuova = new Date();
                    nuova.setMonth(12);
                    nuova.setDate(30);
                    nuova.setYear(82);
                    Date nuova2 = new Date();
                    nuova2.setMonth(4);
                    nuova2.setDate(30);
                    nuova2.setYear(107);
                    BeanTourist test = new BeanTourist(0, "mlmlml", "Ottabio", "of Michil", "Ottawa", "Frattamaggiore",
                            "61616161", "84932nd", "V.le della Mimosa 33", "NA", "ottavio_ottawa@wawa.com", "micacae",
                            nuova, nuova2, false);
                }
            });
        }
        return btnModifyData;
    }

    /**
     * This method initializes the button to display the Board a tourist.
     *
     * @Return javax.swing.JButton - the button.
     */
    private JButton getBtnViewCard() {
        if (null == btnViewCard) {
            btnViewCard = new JButton();
            btnViewCard.setText("Show <html> <br> tourist profile </ html>");
            btnViewCard.setBounds(305, 5, 140, 40);
            btnViewCard.setIcon(new ImageIcon(getClass()
                    .getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / schedaturista.png ")));
            btnViewCard.setEnabled(false);
            btnViewCard.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    Date nuova = new Date();
                    nuova.setMonth(12);
                    nuova.setDate(30);
                    nuova.setYear(82);
                    Date nuova2 = new Date();
                    nuova2.setMonth(4);
                    nuova2.setDate(30);
                    nuova2.setYear(107);
                    BeanTourist test = new BeanTourist(0, "mlmlml", "Ottabio", "of Michil", "Ottawa", "Frattamaggiore",
                            "61616161", "84932nd", "V.le della Mimosa 33", "NA", "ottavio_ottawa@wawa.com", "micacae",
                            nuova, nuova2, false);
                }

            });
        }
        return btnViewCard;
    }

    /**
     *
     * This method initializes the delete button for a tourist.
     *
     * @Return javax.swing.JButton - the button.
     */
    private JButton getBtnDelete() {
        if (null == btnDelete) {
            btnDelete = new JButton();
            btnDelete.setText("Delete <html> <br> region </ html>");
            btnDelete.setBounds(455, 5, 140, 40);
            btnDelete.setEnabled(false);
            btnDelete.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/DeleteTourist32.png ")));
            btnDelete.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    int selectedRow = tableTourist.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = (String) TableModel.getValueAt(selectedRow, 1) + ""
                                + (String) TableModel.getValueAt(selectedRow, 2);
                        JPanel root = new JPanel(new BorderLayout());
                        JLabel message = new JLabel(
                                "Are you sure you want to delete the account of the tourist" + name + "?");
                        message.setFont(new Font("Dialog", Font.BOLD, 14));
                        JLabel alert = new JLabel(
                                "The data account and all personal settings " + "Can not be filled again.",
                                SwingConstants.CENTER);
                        alert.setIcon(new ImageIcon(getClass()
                                .getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/warning16.png ")));
                        root.add(message, BorderLayout.NORTH);
                        root.add(alert, BorderLayout.CENTER);
                        String[] options = { "Delete", "Cancel" };
                        int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm Delete",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                new ImageIcon(getClass().getResource(
                                        "/ unisa/gps/etour/gui/operatoreagenzia/images/DeleteTourist48.png")),
                                options, options[1]);
                        if (choice == JOptionPane.YES_OPTION) {
                            TableModel.removeTourist(selectedRow);
                            TableModel.fireTableDataChanged();
                            JLabel confirm = new JLabel("Account Tourists" + name + "Was deleted.");
                            confirm.setFont(new Font("Dialog", Font.BOLD, 14));
                            JOptionPane.showInternalMessageDialog(jContentPane, confirm, "Accounts Tourist out! ",
                                    JOptionPane.OK_OPTION, new ImageIcon(getClass()
                                            .getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/ok32.png ")));
                        }
                    }

                }

            });
        }
        return btnDelete;
    }

    /**
     * This method initializes the scroll with the table bread tourists.
     *
     * @Return javax.swing.JScrollPane - the scroll pane.
     */
    private JScrollPane getScrollTableTourist() {
        if (null == scrollTableTourist) {
            TableModel = new TouristTableModel();
            Date new_v = new Date();
            new_v.setMonth(12);
            new_v.setDate(30);
            new_v.setYear(82);
            Date nuova2 = new Date();
            nuova2.setMonth(4);
            nuova2.setDate(30);
            nuova2.setYear(107);
            BeanTourist new_ = new BeanTourist(0, "mlmlml", "Ottabio", "of Michil", "Ottawa", "Frattamaggiore",
                    "61616161", "84932nd", "V.le della Mimosa 33", "NA", "ottavio_ottawa@wawa.com", "micacae", new_v,
                    nuova2, false);
            BeanTourist nuovo2 = new BeanTourist(1, "mlmlml", "Ottabiolino", "of Michil", "Ottawa", "Frattamaggiore",
                    "61616161", "84932nd", "V.le della Mimosa 33", "NA", "ottavio_ottawa@wawa.com", "micacae", new_v,
                    nuova2, true);
            BeanTourist nuovo3 = new BeanTourist(2, "mlmlml", "Ottavio", "Michil", "Ottawa", "Frattamaggiore",
                    "61616161", "84932nd", "V.le Mimose 33", "NA", "ottavio_ottawa@wawa.com", "micacae", new_v, nuova2,
                    false);
            TableModel.insertTourist(new_);
            TableModel.insertTourist(nuovo2);
            TableModel.insertTourist(nuovo3);
            for (int i = 0; i < 12; i++) {
                TableModel.insertTourist(new_);
            }
// END TEST
            tableTourist = new ScrollableTable(TableModel);
            tableTourist.setAutoCreateColumnsFromModel(true);
            tableTourist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableTourist.setSelectionForeground(Color.RED);
            tableTourist.setSelectionBackground(Color.white);
            tableTourist.setColumnSelectionAllowed(false);
            tableTourist.setShowVerticalLines(false);
            tableTourist.setRowHeight(32);
// Status
            tableTourist.getColumnModel().getColumn(0).setPreferredWidth(40);
            tableTourist.getColumnModel().getColumn(0).setCellRenderer(new ActiveTouristRenderer());
// Name
            tableTourist.getColumnModel().getColumn(1).setPreferredWidth(100);
// Surname
            tableTourist.getColumnModel().getColumn(2).setPreferredWidth(100);
// Email
            tableTourist.getColumnModel().getColumn(3).setPreferredWidth(140);
// Phone
            tableTourist.getColumnModel().getColumn(4).setPreferredWidth(80);
// Date of Birth
            tableTourist.getColumnModel().getColumn(5).setPreferredWidth(100);
// City of Birth
            tableTourist.getColumnModel().getColumn(6).setPreferredWidth(100);
// Address
            tableTourist.getColumnModel().getColumn(7).setPreferredWidth(100);
// Residence
            tableTourist.getColumnModel().getColumn(8).setPreferredWidth(80);
// CAP
            tableTourist.getColumnModel().getColumn(9).setPreferredWidth(60);
// State
            tableTourist.getColumnModel().getColumn(10).setPreferredWidth(30);
// Data entry
            tableTourist.getColumnModel().getColumn(11).setPreferredWidth(90);

            ListSelectionModel selectionModel = tableTourist.getSelectionModel();
            selectionModel.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    int selectedRow = tableTourist.getSelectedRow();
                    if (selectedRow != -1) {
                        btnActive.setEnabled(true);
                        btnModifyData.setEnabled(true);
                        btnViewCard.setEnabled(true);
                        btnDelete.setEnabled(true);
                        if ((Boolean) TableModel.getValueAt(selectedRow, 0)) {
                            btnActive.setText("Enable <html> <br> region </ html>");
                            btnActive.setIcon(new ImageIcon(getClass().getResource(
                                    "/ unisa/gps/etour/gui/operatoreagenzia/images/ActiveTourist32.png ")));
                        } else {
                            btnActive.setText("Disable <html> <br> region </ html>");
                            btnActive.setIcon(new ImageIcon(getClass().getResource(
                                    "/ unisa/gps/etour/gui/operatoreagenzia/images/DisactiveTourist32.png ")));
                        }
                    } else {
                        btnDelete.setEnabled(false);
                        btnModifyData.setEnabled(false);
                        btnActive.setEnabled(false);
                        btnViewCard.setEnabled(false);
                    }

                }
            });
            scrollTableTourist = new JScrollPane();
            scrollTableTourist.setViewportView(tableTourist);
            scrollTableTourist.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollTableTourist.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return scrollTableTourist;
    }

    /**
     * This method initializes the form to the search for tourists.
     *
     * @Return javax.swing.JPanel
     *
     */
    private JPanel getSearchPanel() {
        if (null == searchPanel) {
            searchPanel = new JPanel();
            searchPanel.setLayout(new GridBagLayout());
            searchPanel.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                            "Research Tourists ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            GridBagConstraints g = new GridBagConstraints();
// Create Panel, choose tourists
            JPanel turisti = new JPanel(new GridBagLayout());
            turisti.setPreferredSize(new Dimension(200, 120));
            turisti.setBorder(BorderFactory.createLoweredBevelBorder());
            JToggleButton turistiActiveti = new JToggleButton("active tourists");
            turistiActiveti.setPreferredSize(new Dimension(165, 30));
            JToggleButton turistiDisactiveti = new JToggleButton("Tourists Off");
            turistiDisactiveti.setPreferredSize(new Dimension(165, 30));
            turistiActiveti.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/schedaturista24.png ")));
            ActionListener almenoUno = new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    if (pEvent.getSource() == turistiActiveti) {
                        if (!turistiActiveti.isSelected() && !turistiDisactiveti.isSelected()) {
                            turistiDisactiveti.setSelected(true);
                        }
                    } else {
                        if (!turistiActiveti.isSelected() && !turistiDisactiveti.isSelected()) {
                            turistiActiveti.setSelected(true);
                        }
                    }

                }
            };
            turistiActiveti.addActionListener(almenoUno);
            turistiDisactiveti.addActionListener(almenoUno);
            turistiActiveti.setSelected(true);
            turistiDisactiveti.setSelected(true);
            turistiDisactiveti.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/DisactiveTourist24.png ")));
            g.insets = new Insets(8, 8, 8, 8);
            g.gridx = 0;
            g.gridy = 0;
            g.weighty = 0.2;
            g.fill = GridBagConstraints.HORIZONTAL;
            turisti.add(turistiActiveti, g);
            g.gridy++;
            turisti.add(turistiDisactiveti, g);

// Create search panel
            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 2;
            g.anchor = GridBagConstraints.CENTER;
            g.fill = GridBagConstraints.NONE;
            g.insets = new Insets(5, 5, 5, 5);
            searchPanel.add(new JLabel("Name Tourist"), g);
            surnameTourist = new JTextField(12);
            g.gridy++;
            searchPanel.add(surnameTourist, g);
            g.gridy++;
            searchPanel.add(new JLabel("View"), g);
            g.gridy++;
            g.weighty = 0.2;
            g.insets = new Insets(5, 5, 20, 5);
            searchPanel.add(turisti, g);
            g.insets = new Insets(5, 5, 5, 5);
            g.gridwidth = 1;
            g.gridy++;
            searchPanel.add(getBtnSearch(), g);
            g.gridx = 1;
            searchPanel.add(getBtnActive(), g);
        }
        return searchPanel;
    }

    /**
     * This method initializes the panel's online help.
     *
     * @Return javax.swing.JPanel
     *
     */
    private JPanel getPanelHelp() {
        if (null == panelHelp) {
            panelHelp = new JPanel();
            panelHelp.setLayout(new BorderLayout());
            panelHelp.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3), "Help",
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            panelHelp.setPreferredSize(new Dimension(200, 100));
            textGuide = new JTextPane();
            textGuide.setOpaque(false);
            textGuide.setContentType("text / html");
            textGuide.setEditable(false);
            textGuide.setOpaque(false);
            panelHelp.add(textGuide, BorderLayout.CENTER);

        }
        return panelHelp;
    }

    /**
     * This method initializes the side panel of the interface.
     *
     * @Return javax.swing.JPanel
     *
     */
    private JPanel getRightPanel() {
        if (null == rightPanel) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 1;
            g.fill = GridBagConstraints.BOTH;
            g.weighty = 0.3;
            rightPanel.add(getPanelHelp(), g);
            g.weighty = 0.7;
            g.gridx = 0;
            g.gridy = 0;
            rightPanel.add(getSearchPanel(), g);
        }
        return rightPanel;
    }

    /**
     * This method initializes the search button tourists.
     *
     * @Return javax.swing.JButton
     *
     */
    private JButton getBtnSearch() {
        if (null == btnSearch) {
            btnSearch = new JButton();
            btnSearch.setText("Search");
            btnSearch.setPreferredSize(new Dimension(98, 26));
            btnSearch.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/Search16.png ")));
        }
        return btnSearch;
    }

    /**
     * This method initializes the button to reset form Search.
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnActive2() {
        if (null == btnActive) {
            btnActive = new JButton();
            btnActive.setText("Clear");
            btnActive.setHorizontalTextPosition(SwingConstants.LEADING);
            btnActive.setPreferredSize(new Dimension(98, 26));
            btnActive.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/Active16.png ")));
            btnActive.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pAcEvent) {
                    surnameTourist.setText("");
                    activeTourist.setSelected(true);
                    inactiveTourist.setSelected(true);
                }
            });
        }
        return btnActive;
    }

    /**
     *
     * This method opens a tab for the bean tourists supplied input.
     *
     * @Param pTourist BeanTourist - the bean tourists
     * @Param boolean pModify
     *        <ul>
     *        <li>true - if you change the data.
     *        <li>* False - if you receive the card. </ Ul>
     * @Return void
     */
    private void openTab(BeanTourist pTourist, boolean pModify) {
        for (int i = 0; i < children.size(); i++) {
            TouristCard current = children.get(i);
            if (pTourist.getId() == current.getId()) {
                desktopManager.activateFrame(null);
                return;
            }
        }
        TouristCard nuova = new TouristCard(this, pTourist, pModify);
        jDesktopPane.add(null, Integer.MAX_VALUE);
        jDesktopPane.setVisible(true);
        children.add(nuova);
    }

    /**
     *
     * Closes the selected tab.
     *
     * @Param pCard profiling - the tab to close.
     * @Return void
     */
    protected void closeCard(JInternalFrame pCard) {
        children.remove(pCard);
        pCard.dispose();
    }

    /**
     *
     * Update the table model of the tourists with the bean supplied Input.
     *
     * @Param pTourist BeanTourist - the bean of the tourist.
     * @Return void
     */
    protected void updateTableModel(BeanTourist pTourist) {
        TableModel.updateTourist(pTourist);
    }
}