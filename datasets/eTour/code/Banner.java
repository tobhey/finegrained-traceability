package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import unisa.gps.etour.bean.BeanBanner;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.control.AdvertisementManager.IAdvertisementAgencyManager;
import unisa.gps.etour.control.RefreshmentPointManager.IRefreshmentPointAgencyManager;
import unisa.gps.etour.control.TagManager.ITagCommonManager;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.HelpManager;
import unisa.gps.etour.gui.operatoragency.tables.BannerNode;
import unisa.gps.etour.gui.operatoragency.tables.BannerRenderer;
import unisa.gps.etour.gui.operatoragency.tables.PRNode;

/**
 * This class implements the interface for the management of advertising banners
 * By the operator of the agency.
 *
 */
public class Banner extends JInternalFrame {

    private JPanel jContentPane = null;
    private JPanel rightPanel = null;
    private JToolBar bannerToolbar = null;
    private JButton btnInsert = null;
    private JButton btnReplace = null;
    private JButton btnDelete = null;
    private JScrollPane JScrollPane = null;
    private JPanel helpPanel = null;
    private JTextPane textGuide = null;
    private TagPanel panelTag = null;
    private JButton btnSearch = null;
    private JButton btnActive = null;
    private JPanel panelSearch = null;
    private JTextField namePR = null;
    private JTree treeBanner = null;
    private JDesktopPane JDesktopPane;
    private HelpManager bannerHelp;
    protected DeskManager desktopManager;
    protected IRefreshmentPointAgencyManager refreshmentPointManager;
    protected IAdvertisementAgencyManager bannerManager;
    protected ITagCommonManager tags;

    /**
     * This grave; the default constructor.
     */
    public Banner() {
        super("Banner");
        resizable = true;
        closable = true;
        iconable = true;
        maximizable = true;
        setPreferredSize(Home.CHILD_SIZE);
        frameIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "banner2.png"));

// Setting up help manager for cultural.

        textGuide = new JTextPane();

        try {
            bannerHelp = new HelpManager(Home.URL_HELP + "Banner.txt", textGuide);
        } catch (FileNotFoundException e) {
            textGuide.setText("<html> <b> Help not available </ b> </ html>");
        }

        setContentPane(getJContentPane());
        addInternalFrameListener(new InternalFrameAdapter() {
            /*
             * Inclusion of the frame on the desktop retrieves the bread Link to the desktop
             * pane.
             */

            public void internalFrameOpened(InternalFrameEvent pEvent) {
                JInternalFrame frame = pEvent.getInternalFrame();
                JDesktopPane = frame.getDesktopPane();
                desktopManager = (DeskManager) JDesktopPane.getDesktopManager();

// Setting up of remote objects for the management of cultural heritage.
                try {
                    Registry reg = LocateRegistry.getRegistry(Home.HOST);
                    bannerManager = (IAdvertisementAgencyManager) reg.lookup("ManagerCulturalHeritageAgency ");
                    tags = (ITagCommonManager) reg.lookup("ManagerTagCommon ");
                    refreshmentPointManager = (IRefreshmentPointAgencyManager) reg
                            .lookup("ManagerRefreshmentPointAgency ");

// Load data.
                    createTree();
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
        });
    }

    /**
     * This method initializes the content pane.
     *
     * @Return javax.swing.JPanel - the content pane.
     */
    private JPanel getJContentPane() {
        if (null == jContentPane) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getRightPanel(), BorderLayout.EAST);
            jContentPane.add(getBannerToolbar(), BorderLayout.NORTH);
            jContentPane.add(getTreeBanner(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes the toolbar to the functions of management Banner.
     *
     * @Return javax.swing.JToolBar - the toolbar.
     */
    private JToolBar getBannerToolbar() {
        if (null == bannerToolbar) {
            bannerToolbar = new JToolBar();
            bannerToolbar.setLayout(null);
            bannerToolbar.setPreferredSize(new Dimension(1, 50));
            bannerToolbar.setFloatable(false);
            bannerToolbar.add(getBtnInsert());
            bannerToolbar.add(getBtnReplace());
            bannerToolbar.add(getBtnDelete());
        }
        return bannerToolbar;
    }

    /**
     * This method initializes the button to insert a banner.
     *
     * @Return javax.swing.JButton - the button for the insertion.
     */
    private JButton getBtnInsert() {
        if (null == btnInsert) {
            btnInsert = new JButton();
            btnInsert.setBounds(5, 5, 140, 40);
            btnInsert.setText("<html> <br> Show Banner </ html>");
            btnInsert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "NewBanner32.png ")));
            btnInsert.setEnabled(false);
            btnInsert.setName("btnInsert ");
            btnInsert.addMouseListener(bannerHelp);
            btnInsert.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeBanner.getSelectionPath()
                            .getLastPathComponent();
                    OpenDialog(selectedNode);
                }

            });
        }
        return btnInsert;
    }

    /**
     * This method initializes the button for editing a banner.
     *
     * @Return javax.swing.JButton - the button for the change.
     */
    private JButton getBtnReplace() {
        if (null == btnReplace) {
            btnReplace = new JButton();
            btnReplace.setBounds(155, 5, 140, 40);
            btnReplace.setText("Replace <html> <br> Banner </ html>");
            btnReplace.setEnabled(false);
            btnReplace.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ReplaceBanner32.png ")));
            btnReplace.setName("btnReplace ");
            btnReplace.addMouseListener(bannerHelp);
            btnReplace.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeBanner.getSelectionPath()
                            .getLastPathComponent();
                    OpenDialog(selectedNode);
                }

            });
        }
        return btnReplace;
    }

    /**
     * This method initializes the delete button for a banner.
     *
     * @Return javax.swing.JButton - the delete button for.
     */
    private JButton getBtnDelete() {
        if (null == btnDelete) {
            btnDelete = new JButton();
            btnDelete.setBounds(305, 5, 140, 40);
            btnDelete.setText("Delete <html> <br> Banner </ html>");
            btnDelete.setEnabled(false);
            btnDelete.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteBanner32.png ")));
            btnDelete.setName("btnDelete ");
            btnDelete.addMouseListener(bannerHelp);
            btnDelete.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to delete the banner selected?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("The banner can not be more recovered.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = { "Delete", "Cancel" };
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm Delete",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteBanner48.png")), options,
                            options[1]);
                    if (choice == JOptionPane.YES_OPTION) {
                        DefaultTreeModel model = (DefaultTreeModel) treeBanner.getModel();
                        model.removeNodeFromParent(
                                (DefaultMutableTreeNode) treeBanner.getSelectionPath().getLastPathComponent());
                        JLabel confirm = new JLabel("The banner selected" + "Was deleted.");
                        confirm.setFont(new Font("Dialog", Font.BOLD, 14));
                        JOptionPane.showInternalMessageDialog(jContentPane, confirm, "Banner out!",
                                JOptionPane.OK_OPTION,
                                new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ok32.png ")));
                    }

                }

            });
        }
        return btnDelete;
    }

    /**
     * This method creates the tree starting from the information contained in the
     * two ArrayList of bean.
     *
     * @Param ArrayList PPR <BeanRefreshmentPoint> - the array of places to eat.
     * @Param pBanner ArrayList <BeanBanner> - the array of banners associated.
     */
    private void createTree() {
// Create the root
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("eTour");

// For each refreshment dell'arraylist calls the method get
// Banner.
        try {
            ArrayList<BeanRefreshmentPoint> pPR = refreshmentPointManager.getRefreshmentPoint();
            for (int i = 0; i < pPR.size(); i++) {
                BeanRefreshmentPoint current = pPR.get(i);
                int id = current.getId();
                PRNode refreshmentPoint = new PRNode(current.getName(), id);
                HashMap<BeanBanner, ImageIcon> banner = bannerManager.getBannersID(id);
                Iterator<BeanBanner> iteraBanner = banner.keySet().iterator();
                while (iteraBanner.hasNext()) {
                    BeanBanner bannercorrente = iteraBanner.next();
                    BannerNode nuovoBanner = new BannerNode(banner.get(bannercorrente), bannercorrente.getId());
                    refreshmentPoint.add(nuovoBanner);
                }

                root.add(refreshmentPoint);
            }
        } catch (RemoteException ex) {
            JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                    + "<h3> <u> The list of banners was not loaded. </ U> </ h3> " + "<p> Please try again later. </ P>"
                    + "<p> If the error persists, please contact technical support. </ P>"
                    + "<p> We apologize for the inconvenience. </ Html>");
            ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
            JOptionPane.showInternalMessageDialog(this, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
        } finally {
            treeBanner.setModel(new DefaultTreeModel(root));
        }

    }

    /**
     * This method initializes the tree where it displays the banner.
     *
     * @Return javax.swing.JTree
     */
    private JScrollPane getTreeBanner() {

        if (null == treeBanner) {
            treeBanner = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("")));
            treeBanner.setScrollsOnExpand(true);
            treeBanner.setAutoscrolls(true);
            treeBanner.setScrollsOnExpand(true);
            treeBanner.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            treeBanner.setName("treeBanner ");
            treeBanner.addMouseListener(bannerHelp);
            treeBanner.setRootVisible(false);
            treeBanner.setCellRenderer(new BannerRenderer());
            treeBanner.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent s) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeBanner.getLastSelectedPathComponent();

                    if (node instanceof PRNode) {
                        btnInsert.setEnabled(true);
                        btnDelete.setEnabled(false);
                        btnReplace.setEnabled(false);
                    } else if (node instanceof BannerNode) {
                        btnInsert.setEnabled(false);
                        btnReplace.setEnabled(true);
                        btnDelete.setEnabled(true);
                    } else {
                        btnInsert.setEnabled(false);
                        btnReplace.setEnabled(false);
                        btnDelete.setEnabled(false);
                    }

                }
            });

        }
        JScrollPane = new JScrollPane(treeBanner);
        JScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return JScrollPane;
    }

    /**
     * This method initializes the right side panel.
     *
     * @Return javax.swing.JPanel - the right panel.
     */
    private JPanel getRightPanel() {
        if (null == rightPanel) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            g.fill = GridBagConstraints.BOTH;
            g.weighty = 0.7;
            rightPanel.add(getSearchPanel(), g);
            g.gridy++;
            g.weighty = 0.3;
            rightPanel.add(getHelpPanel(), g);
        }
        return rightPanel;
    }

    /**
     * This method initializes the panel that contains the online help.
     *
     * @Return javax.swing.JPanel - the panel for the guide.
     */
    private JPanel getHelpPanel() {
        if (null == helpPanel) {
            helpPanel = new JPanel();
            helpPanel.setLayout(new BorderLayout());
            helpPanel.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3), "Help",
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            helpPanel.setPreferredSize(new Dimension(200, 60));
            textGuide.setPreferredSize(new Dimension(6, 30));
            textGuide.setOpaque(false);
            textGuide.setContentType("text / html");
            textGuide.setText("<html> Move your mouse pointer over a control"
                    + "of interest to display the context-sensitive help. </ html>");
            textGuide.setEditable(false);
            textGuide.setName("textGuide ");
            textGuide.addMouseListener(bannerHelp);
            helpPanel.add(textGuide, BorderLayout.CENTER);
        }
        return helpPanel;
    }

    /**
     * This method initializes the panel for the detection of points Refreshments.
     *
     * @Return javax.swing.JPanel - the panel for research.
     */
    private JPanel getSearchPanel() {
        if (null == panelSearch) {
            panelSearch = new JPanel();
            panelSearch.setLayout(new GridBagLayout());
            panelSearch.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                            "Research Refreshment ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            GridBagConstraints g = new GridBagConstraints();
            g.anchor = GridBagConstraints.CENTER;
            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 2;
            g.insets = new Insets(5, 5, 5, 5);
            panelSearch.add(new JLabel("Name Refreshments:"), g);
            namePR = new JTextField();
            namePR.setName("namePR ");
            namePR.addMouseListener(bannerHelp);
            namePR.setColumns(12);
            g.insets = new Insets(5, 5, 10, 5);
            g.gridy++;
            panelSearch.add(namePR, g);
            g.insets = new Insets(5, 5, 5, 5);
            g.gridy++;
            panelSearch.add(new JLabel("Select search tags:"), g);
            g.weighty = 1.0;
            g.insets = new Insets(5, 5, 10, 5);
            g.gridy++;
            panelTag = new TagPanel();
            panelTag.setName("panelTag ");
            panelTag.addMouseListener(bannerHelp);
            panelTag.setPreferredSize(new Dimension(180, 10));
            g.fill = GridBagConstraints.VERTICAL;
            panelSearch.add(panelTag, g);
            g.fill = GridBagConstraints.NONE;
            g.insets = new Insets(5, 5, 5, 5);
            g.weighty = 0;
            g.gridy++;
            g.gridwidth = 1;
            panelSearch.add(getBtnSearch(), g);
            g.gridx++;
            panelSearch.add(getBtnActive(), g);
        }
        return panelSearch;
    }

    /**
     * This method initializes the button for the submission of the form Search for
     * a refreshment.
     *
     * @Return javax.swing.JButton - the search button.
     */
    private JButton getBtnSearch() {
        if (null == btnSearch) {
            btnSearch = new JButton();
            btnSearch.setPreferredSize(new Dimension(98, 26));
            btnSearch.setText("Search");
            btnSearch.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Search16.png ")));
            btnSearch.setName("btnSearch ");
            btnSearch.addMouseListener(bannerHelp);
        }
        return btnSearch;
    }

    /**
     * This method initializes the button to reset the fields of Search Form for a
     * refreshment.
     *
     * @Return javax.swing.JButton - the Reset button.
     */
    private JButton getBtnActive() {
        if (null == btnActive) {
            btnActive = new JButton();
            btnActive.setPreferredSize(new Dimension(98, 26));
            btnActive.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Active16.png ")));
            btnActive.setText("Clear");
            btnActive.setHorizontalTextPosition(SwingConstants.LEADING);
            btnActive.setName("btnActive ");
            btnActive.addMouseListener(bannerHelp);
            btnActive.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    panelTag.disable();
                    namePR.setText("");
                }

            });
        }
        return btnActive;
    }

    /**
     * This method opens the dialog box for entering a new Banner or editing a
     * banner selected.
     *
     * @Param pSelectedNode DefaultMutableTreeNode - the selected node.
     */
    private void OpenDialog(DefaultMutableTreeNode pSelectedNode) {
// This class intercepts mouse events and then
// Makes the frame below blocked.
        class ModalAdapter extends InternalFrameAdapter {
            Component glass;

            public ModalAdapter(Component pGlassComponent) {
                this.glass = pGlassComponent;

                MouseInputAdapter adapter = new MouseInputAdapter() {
                };
                pGlassComponent.addMouseListener(adapter);
                pGlassComponent.addMouseMotionListener(adapter);
            }

            public void internalFrameClosed(InternalFrameEvent s) {
                glass.setVisible(false);
            }
        }

// Construction of the dialog
        JOptionPane optionPane = new JOptionPane();
        final JInternalFrame modal = optionPane.createInternalFrame(JDesktopPane, "");
        final JPanel glass = new JPanel();
        final BannerDialog dialog = new BannerDialog();
        optionPane.setMessage(dialog);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JButton[] options = new JButton[2];
        options[0] = new JButton();
        options[1] = new JButton("Cancel");
        options[0].setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Save16.png ")));
        options[1].setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Cancel16.png ")));
        optionPane.setOptions(options);
        options[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                modal.setVisible(false);
                glass.setVisible(false);
            }
        });

        glass.setOpaque(false);
        modal.addInternalFrameListener(new ModalAdapter(glass));
        glass.add(modal);
        setGlassPane(glass);
        modal.setLocation(this.getWidth() / 2, this.getHeight() / 2);
        glass.setVisible(true);
        modal.setVisible(true);
        if (pSelectedNode instanceof BannerNode) // Replace
        {
            final BannerNode banner = (BannerNode) pSelectedNode;
            options[0].setText("Replace");
            options[0].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    DefaultTreeModel model = (DefaultTreeModel) treeBanner.getModel();
                    banner.setBanner(dialog.getSelectedBanner());
                    model.nodeChanged(banner);
                    glass.setVisible(false);
                    modal.setVisible(false);
                }

            });

            optionPane.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ReplaceBanner48.png ")));
            modal.setTitle("Replace the banner to the point of relief"
                    + ((PRNode) pSelectedNode.getParent()).getUserObject().toString());
        } else if (pSelectedNode instanceof PRNode) // Inserting
        {
            final PRNode pr = (PRNode) pSelectedNode;
            options[0].setText("Save");
            options[0].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    DefaultTreeModel model = (DefaultTreeModel) treeBanner.getModel();
                    BannerNode new_ = new BannerNode(dialog.getSelectedBanner(), pr.getID());
                    model.insertNodeInto(new_, pr, 0);
                    glass.setVisible(false);
                    modal.setVisible(false);
                }

            });
            optionPane.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "nuovoBanner48.png ")));
            modal.setTitle("Enter banner to the point of relief" + pSelectedNode.getUserObject().toString());
        }

    }

    private void loadTags() {
        ArrayList<BeanTag> beanTags = null;
        try {
            beanTags = tags.getTags();
        }
// If an error displays an error message.
        catch (RemoteException e) {
        } finally {
            for (BeanTag b : beanTags) {
                panelTag.insertTag(b);
            }
            panelTag.repaint();
        }

    }
}