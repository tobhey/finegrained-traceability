package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.gui.operatoragency.tables.AverageRatingRenderer;
import unisa.gps.etour.gui.operatoragency.tables.ScrollableTable;
import unisa.gps.etour.gui.operatoragency.tables.SiteTableModel;
import unisa.gps.etour.util.Point3D;

public class RefreshmentPoint extends JInternalFrame {

    private Dimension size;
    private JPanel jContentPane = null;
    private JPanel rightPanel = null;
    private JToolBar toolbarPR = null;
    private JLabel status = null;
    private JPanel CentrePanel = null;
    private JButton btnSkeda = null;
    private JButton btnModify = null;
    private JButton btnDelete = null;
    private JScrollPane jScrollPane = null;
    private JPanel HelpPanel = null;
    private JTextPane jTextPane = null;
    private TagPanel panelTag = null;
    private JButton btnSearch2 = null;
    private JButton btnActive2 = null;
    private JPanel SearchPanel = null;
    private JLabel LabelPr = null;
    private JTextField namePR = null;
    private JLabel LabelTag = null;
    private JTable TablePr = null;
    private JDesktopPane JDesktopPane;
    private RefreshmentPoint internalFrame;
    private JButton btnActive = null;
    private JButton btnHistorical = null;

    /**
     * This is the default constructor xxx
     */
    public RefreshmentPoint() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @Return void
     */
    private void initialize() {
        size = new Dimension();
        setPreferredSize(size);
        this.setSize(new Dimension(700, 480));
        this.setResizable(true);
        this.setClosable(true);
        this.setTitle("Refreshments");
        this.setFrameIcon(new ImageIcon(getClass().getResource("/ interfacceAgency / images / PR.png ")));
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getRightPanel(), BorderLayout.EAST);
            jContentPane.add(getToolbarPR(), BorderLayout.NORTH);
            jContentPane.add(getCentrePanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes RightPanel
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getRightPanel() {
        if (rightPanel == null) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new BorderLayout());
            rightPanel.add(getHelpPanel(), BorderLayout.CENTER);
            rightPanel.add(getSearchPanel(), BorderLayout.CENTER);
        }
        return rightPanel;
    }

    /**
     * This method initializes jJToolBarBar
     *
     * @Return javax.swing.JToolBar
     */
    private JToolBar getToolbarPR() {
        if (toolbarPR == null) {
            toolbarPR = new JToolBar();
            toolbarPR.setLayout(null);
            toolbarPR.setPreferredSize(new Dimension(1, 49));
            toolbarPR.setFloatable(false);
            toolbarPR.add(getBtnActive());
            toolbarPR.addSeparator();
            toolbarPR.add(getBtnHistorical());
            toolbarPR.addSeparator();
            toolbarPR.add(getBtnModify());
            toolbarPR.addSeparator();
            toolbarPR.add(getBtnSkeda());
            toolbarPR.addSeparator();
            toolbarPR.add(getBtnDelete());
        }
        return toolbarPR;
    }

    /**
     * This method initializes CentrePanel
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getCentrePanel() {
        if (CentrePanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.weightx = 1.0;
            CentrePanel = new JPanel();
            CentrePanel.setLayout(new GridBagLayout());
            CentrePanel.setPreferredSize(new Dimension(1, 30));
            CentrePanel.add(getJScrollPane(), gridBagConstraints);

        }
        return CentrePanel;
    }

    /**
     * This method initializes btnSkeda
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnSkeda() {
        if (btnSkeda == null) {
            btnSkeda = new JButton();
            btnSkeda.setText("Point Card <html> <br> Refreshments </ html>");
            btnSkeda.setLocation(new Point(16, 3));
            btnSkeda.setSize(new Dimension(130, 42));
            btnSkeda.setPreferredSize(new Dimension(130, 42));
            btnSkeda.setIcon(new ImageIcon(getClass().getResource("/ interfacceAgency / images / Browse 1.png ")));
            btnSkeda.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {

                }

            });
        }
        return btnSkeda;
    }

    /**
     * This method initializes btnModify
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnModify() {
        if (btnModify == null) {
            btnModify = new JButton();
            btnModify.setText("<html> Edit Point <br> Refreshments </ html>");
            btnModify.setPreferredSize(new Dimension(130, 42));
            btnModify.setMnemonic(KeyEvent.VK_UNDEFINED);
            btnModify.setEnabled(false);
            btnModify.setBounds(new Rectangle(413, 3, 140, 42));
            btnModify.setIcon(new ImageIcon(getClass().getResource("/ interfacceAgency/immagini/edit-32x32.png ")));
        }
        return btnModify;
    }

    /**
     * This method initializes btnDelete
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnDelete() {
        if (btnDelete == null) {
            btnDelete = new JButton();
            btnDelete.setText("Delete item <html> <br> Refreshments </ html>");
            btnDelete.setPreferredSize(new Dimension(130, 42));
            btnDelete.setEnabled(false);
            btnDelete.setMnemonic(KeyEvent.VK_UNDEFINED);
            btnDelete.setBounds(new Rectangle(555, 3, 130, 42));
            btnDelete.setIcon(
                    new ImageIcon(getClass().getResource("/ interfacceAgency/immagini/edit-delete-32x32.png ")));
        }
        return btnDelete;
    }

    /**
     * This method initializes JScrollPane
     *
     * @Return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            if (TablePr == null) {

                BeanRefreshmentPoint[] test = new BeanRefreshmentPoint[30];
                for (int i = 0; i < 30; i++) {
                    test[i] = new BeanRefreshmentPoint(1, 12, 3.5, "Arturo",
                            "Near the sea, great view, romantic and Miao", "089203202", "the mountains", "Amalfi",
                            "Street Principe 35", "84123rd", "Salerno", "1234567898741", new Point3D(34, 34, 34),
                            new Date(2, 23, 3), new Date(3, 3, 4), "Monday");
                }

                SiteTableModel new_ = new SiteTableModel(test);

                TablePr = new ScrollableTable(new_);
// Name
                TablePr.getColumnModel().getColumn(0).setPreferredWidth(140);
// Description
                TablePr.getColumnModel().getColumn(1).setPreferredWidth(80);
// Location
                TablePr.getColumnModel().getColumn(2).setPreferredWidth(140);
// City
                TablePr.getColumnModel().getColumn(3).setPreferredWidth(80);
// State
                TablePr.getColumnModel().getColumn(4).setPreferredWidth(70);
// Number of Votes
                TablePr.getColumnModel().getColumn(5).setPreferredWidth(70);
// Average Ratings
                TablePr.getColumnModel().getColumn(6).setPreferredWidth(60);
// PosGeo
                TablePr.getColumnModel().getColumn(7).setPreferredWidth(30);
                TablePr.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
                TablePr.setRowHeight(32);
                TablePr.setShowVerticalLines(false);
                TablePr.setDefaultRenderer(Double.class, new AverageRatingRenderer());
                TablePr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                TablePr.setSelectionForeground(Color.RED);
                TablePr.setSelectionBackground(Color.white);
                TablePr.setColumnSelectionAllowed(false);
                final javax.swing.ListSelectionModel questo = TablePr.getSelectionModel();
                questo.addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent event) {
                        btnDelete.setEnabled(true);
                        btnModify.setEnabled(true);
                        btnActive.setEnabled(true);
                        btnHistorical.setEnabled(true);
                    }

                });

                TablePr.addKeyListener(new KeyListener() {

                    public void keyPressed(KeyEvent arg0) {
                        System.out.println("keyPressed");

                    }

                    public void keyReleased(KeyEvent arg0) {
                        System.out.println("keyRelased");

                    }

                    public void keyTyped(KeyEvent arg0) {
                        System.out.println("keyTyped");

                    }

                });

// private static final String [] headers = ( "Name", "Phone", "Gone", "Location", "City", "CPC", "Province", "Media Votes", "Number Votes");

            }

            jScrollPane = new JScrollPane(TablePr);
            jScrollPane.setHorizontalScrollBarPolicy(jScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        }
        return jScrollPane;
    }

    /**
     * This method initializes HelpPanel
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getHelpPanel() {
        if (HelpPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;
            gridBagConstraints1.ipadx = 0;
            gridBagConstraints1.gridwidth = 1;
            gridBagConstraints1.ipady = 0;
            gridBagConstraints1.gridx = 0;
            HelpPanel = new JPanel();
            HelpPanel.setLayout(new GridBagLayout());
            HelpPanel.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 153, 255), 3), "Help",
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(51, 102, 255)));
            HelpPanel.setPreferredSize(new Dimension(100, 100));
            HelpPanel.add(getJTextPane(), gridBagConstraints1);
        }
        return HelpPanel;
    }

    /**
     * This method initializes jTextPane
     *
     * @Return javax.swing.JTextPane
     */
    private JTextPane getJTextPane() {
        if (jTextPane == null) {
            jTextPane = new JTextPane();
            jTextPane.setPreferredSize(new Dimension(6, 30));
        }
        return jTextPane;
    }

    /**
     * This method initializes searchPanel1
     *
     * @Return javax.swing.JPanel
     */

    /**
     * This method initializes btnSearch2
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnSearch2() {
        if (btnSearch2 == null) {
            btnSearch2 = new JButton();
            btnSearch2.setPreferredSize(new Dimension(98, 26));
            btnSearch2.setText("Search");
            btnSearch2.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/Search16.png ")));
        }
        return btnSearch2;
    }

    /**
     * This method initializes btnActive2
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnActive2() {
        if (btnActive2 == null) {
            btnActive2 = new JButton();
            btnActive2.setPreferredSize(new Dimension(98, 26));
            btnActive2.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/Active16.png ")));
            btnActive2.setText("Clear");
            btnActive2.setHorizontalTextPosition(SwingConstants.LEADING);
        }
        return btnActive2;
    }

    /**
     * This method initializes SearchPanel
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getSearchPanel() {
        if (SearchPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.BOTH;
            gridBagConstraints5.gridy = 4;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.gridx = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridwidth = 2;
            gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints7.gridy = 6;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.gridwidth = 2;
            gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints6.gridy = 6;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints4.gridy = 3;
            LabelTag = new JLabel();
            LabelTag.setText("Select search tags:");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 2;
            gridBagConstraints3.gridwidth = 2;
            gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints3.weightx = 1.0;
            LabelPr = new JLabel();
            LabelPr.setText("Name Refreshments:");
            SearchPanel = new JPanel();
            SearchPanel.setLayout(new GridBagLayout());
            SearchPanel.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 153, 255), 3),
                            "Searching for refreshments:", TitledBorder.DEFAULT_JUSTIFICATION,
                            TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 102, 255)));
            SearchPanel.add(LabelPr, gridBagConstraints8);
            SearchPanel.add(getNamePR(), gridBagConstraints3);
            SearchPanel.add(LabelTag, gridBagConstraints4);
            BeanTag[] test = new BeanTag[8];
            test[0] = new BeanTag(0, "castle", "really a castle");
            test[1] = new BeanTag(1, "stronghold", "really a hostel");
            test[2] = new BeanTag(3, "Pub", "really a basket");
            test[3] = new BeanTag(4, "Restaurant", "really a basket");
            test[4] = new BeanTag(5, "Pizza", "really a basket");
            test[5] = new BeanTag(6, "Trattoria", "really a basket");
            test[6] = new BeanTag(7, "range", "really a basket");
            test[7] = new BeanTag(8, "Romantic", "really a basket");
            panelTag = new TagPanel(test);
            panelTag.setPreferredSize(new Dimension());
            SearchPanel.add(getBtnActive2(), gridBagConstraints6);
            SearchPanel.add(getBtnSearch2(), gridBagConstraints7);
            SearchPanel.add(panelTag, gridBagConstraints5);
        }
        return SearchPanel;
    }

    /**
     * This method initializes namePR
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getNamePR() {
        if (namePR == null) {
            namePR = new JTextField();
            namePR.setColumns(12);
        }
        return namePR;
    }

    /**
     * This method initializes btnActive
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnActive() {
        if (btnActive == null) {
            btnActive = new JButton();
            btnActive.setText("Enable <html> <br> Convention </ html>");
            btnActive.setPreferredSize(new Dimension(130, 42));
            btnActive.setSize(new Dimension(130, 42));
            btnActive.setLocation(new Point(280, 3));
            btnActive.setMnemonic(KeyEvent.VK_UNDEFINED);
            btnActive.setEnabled(false);
            btnActive.setIcon(new ImageIcon(getClass().getResource("/ interfacceAgency/immagini/wi0054-32x32.png ")));
        }
        return btnActive;
    }

    /**
     * This method initializes btnHistorical
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnHistorical() {
        if (btnHistorical == null) {
            btnHistorical = new JButton();
            btnHistorical.setText("Historical <html> <br> Conventions </ html>");
            btnHistorical.setPreferredSize(new Dimension(130, 42));
            btnHistorical.setBounds(new Rectangle(148, 3, 130, 42));
            btnHistorical.setEnabled(false);
            btnHistorical.setIcon(new ImageIcon(getClass().getResource("/ interfacceAgency / images / Browse 1.png ")));
            btnHistorical.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                }
            });
        }
        return btnHistorical;
    }
}
