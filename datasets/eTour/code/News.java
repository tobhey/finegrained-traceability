package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.control.AdvertisementManager.IAdvertisementAgencyManager;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.HelpManager;
import unisa.gps.etour.gui.operatoragency.documents.LimitedDocument;
import unisa.gps.etour.gui.operatoragency.tables.NewsTableModel;
import unisa.gps.etour.gui.operatoragency.tables.PriorityRenderer;
import unisa.gps.etour.gui.operatoragency.tables.ScrollableTable;
import unisa.gps.etour.gui.operatoragency.tables.TextNewsRenderer;
import unisa.gps.etour.util.Data;

/**
 * This class implements the interface for collecting news for the actor
 * Operator Agency.
 *
 */
public class News extends JInternalFrame {
    private JPanel jContentPane = null;
    private JToolBar NewsToolbar = null;
    private JButton btnDeleteN = null;
    private JPanel rightPanel = null;
    private JPanel formNews = null;
    private JSlider prSlider = null;
    private JButton btnInsertModify = null;
    private JButton btnReset = null;
    private JPanel panelHelp = null;
    private JTextPane textGuide = null;
    private JScrollPane scrollTableNews = null;
    private JTable tableNews = null;
    private JTextArea textNews = null;
    private JComboBox durationNews = null;
    private JButton btnModifyN = null;
    private JLabel labelCharaters;
    private int idNews = -1;
    private NewsTableModel TableModel;
    private HelpManager newsHelp;
    protected DeskManager desktopManager;
    protected JDesktopPane JDesktopPane;
    private IAdvertisementAgencyManager managerNews;

    /**
     * This is the default constructor.
     */
    public News() {
        super("News");
        setPreferredSize(Home.CHILD_SIZE);
        setMinimumSize(new Dimension(600, 480));
        setResizable(true);
        setFrameIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "News32.png ")));
        setIconifiable(true);
        setMaximizable(true);
        setClosable(true);

// Setting up dell'help manager to manage the news.
        textGuide = new JTextPane();
        try {
            newsHelp = new HelpManager(Home.URL_HELP + "news.txt", textGuide);
        } catch (FileNotFoundException e) {
            textGuide.setText("<html> <b> Help not available </ b> </ html>");
        }

        setContentPane(getJContentPane());

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
                    managerNews = (IAdvertisementAgencyManager) reg.lookup("ManagerAdvertisementAgency");

// Load data.
                    loadTable();
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
     * Initialize the content pane of the frame inside.
     *
     * @Return javax.swing.JPanel - the content pane.
     */
    private JPanel getJContentPane() {
        if (null == jContentPane) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getNewsToolbar(), BorderLayout.NORTH);
            jContentPane.add(getRightPanel(), BorderLayout.EAST);
            jContentPane.add(getTableNews(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes the toolbar to manage the news.
     *
     * @Return javax.swing.JToolBar - the toolbar management news.
     */
    private JToolBar getNewsToolbar() {
        if (null == NewsToolbar) {
            NewsToolbar = new JToolBar();
            NewsToolbar.setFloatable(false);
            NewsToolbar.add(getBtnModifyN());
            NewsToolbar.addSeparator();
            NewsToolbar.add(getBtnDeleteN());
        }
        return NewsToolbar;
    }

    /**
     * This method initializes the button to edit a news.
     *
     * @Return javax.swing.JButton - button to change.
     */
    private JButton getBtnModifyN() {
        if (null == btnModifyN) {
            btnModifyN = new JButton();
            btnModifyN.setText("Edit News");
            btnModifyN.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ModifyNews32.png ")));
            btnModifyN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnModifyN.setName("btnModify ");
            btnModifyN.addMouseListener(newsHelp);
            btnModifyN.setEnabled(false);
            btnModifyN.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pActionEvent) {
                    int selectedRow = tableNews.getSelectedRow();
                    if (idNews == -1) // In this way I know if she was
// Edit
                    {
                        btnInsertModify.setText("Change");
                        btnInsertModify.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Save16.png ")));
                        btnReset.setText("Cancel");
                        btnReset.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Cancel16.png ")));
                        formNews.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(new Color(51, 102, 255), 3), "Edit News",
                                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                                new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
                    }
                    String text = (String) TableModel.getValueAt(selectedRow, 0);
                    int numCharaters = 200 - text.length() + 1;
                    labelCharaters.setText("# Characters:" + numCharaters);
                    Document docTest = textNews.getDocument();
                    try {
                        docTest.remove(0, docTest.getLength());
                        docTest.insertString(0, text, null);
                    } catch (BadLocationException s) {
                        s.printStackTrace();
                    }
                    Date expires = (Date) TableModel.getValueAt(selectedRow, 3);
                    durationNews.setSelectedIndex(Data.getNumDays(expires));
                    idNews = TableModel.getID(selectedRow);
                    prSlider.setValue((Integer) TableModel.getValueAt(selectedRow, 1));
                }

            });
        }
        return btnModifyN;
    }

    /**
     * This method initializes the button to delete a news.
     *
     * @Return javax.swing.JButton - the button for deletion.
     */
    private JButton getBtnDeleteN() {
        if (null == btnDeleteN) {
            btnDeleteN = new JButton();
            btnDeleteN.setText("Delete News");
            btnDeleteN.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteNews32.png ")));
            btnDeleteN.setEnabled(false);
            btnDeleteN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnDeleteN.setName("btnDelete ");
            btnDeleteN.addMouseListener(newsHelp);
            btnDeleteN.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    int selectedRow = tableNews.getSelectedRow();

// Construction of the dialog for confirmation
// Deletetion
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to delete the selected news?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("The deleted data can not be filled again.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = { "Delete", "Cancel" };

// The dialog screen appears
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm Delete News",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteNews48.png")), options,
                            options[1]);

// If you chose to confirm the deletion
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            managerNews.clearNews(TableModel.getID(selectedRow));
                            TableModel.removeNews(selectedRow);
                            JOptionPane.showInternalMessageDialog(jContentPane,
                                    "The news has been selected successfully deleted", "News out!",
                                    JOptionPane.OK_OPTION,
                                    new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Ok48.png ")));
                            clearForm();
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
        }
        return btnDeleteN;
    }

    /**
     * This method initializes the panel that realizes the interface side Right of
     * news management.
     *
     * @Return javax.swing.JPanel - the right panel.
     */
    private JPanel getRightPanel() {
        if (null == rightPanel) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.fill = GridBagConstraints.BOTH;
            g.gridx = 0;
            g.gridy = 0;
            g.weighty = 0.7;
            rightPanel.add(getFormNews(), g);
            g.weighty = 0.3;
            g.gridy = 1;
            rightPanel.add(getPanelHelp(), g);

        }
        return rightPanel;
    }

    /**
     * This method initializes the form for entering and editing a News.
     *
     * @Return javax.swing.JPanel - the format
     */
    private JPanel getFormNews() {
        if (null == formNews) {
            formNews = new JPanel(new GridBagLayout());
            formNews.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                            "New News", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            GridBagConstraints g = new GridBagConstraints();
            g.anchor = GridBagConstraints.CENTER;
            g.gridx = 0;
            g.gridy = 0;
            g.weighty = 0.1;
            g.gridwidth = 1;
            g.gridheight = 1;
            g.insets = new Insets(5, 5, 5, 5);
            formNews.add(new JLabel("Text of the news:"), g);
            labelCharaters = new JLabel("# Characters: 200");
            g.gridx = 1;
            formNews.add(labelCharaters, g);
            g.gridx = 0;
            g.gridwidth = 2;
            g.gridy = 1;
            g.weighty = 0.3;
            g.fill = GridBagConstraints.VERTICAL;
            JScrollPane scrollText = new JScrollPane(getTextNews());
            scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            formNews.add(scrollText, g);
            g.weighty = 0.1;
            g.gridy = 2;
            g.fill = GridBagConstraints.NONE;
            formNews.add(new JLabel("Priority:"), g);
            g.gridy = 3;
            formNews.add(getPrSlider(), g);
            JPanel new_ = new JPanel(new FlowLayout());
            new_.add(new JLabel("Length of news :"));
            durationNews = new JComboBox();
            for (int i = 0; i <= 60; i++) {
                durationNews.addItem("" + i);
            }
            durationNews.setSelectedIndex(0);
            durationNews.setEditable(false);
            durationNews.setName("duration");
            durationNews.addMouseListener(newsHelp);
            new_.add(durationNews);
            new_.add(new JLabel("days"));
            g.gridy = 4;
            g.fill = GridBagConstraints.HORIZONTAL;
            formNews.add(new_, g);
            g.gridwidth = 1;
            g.gridy = 5;
            formNews.add(getBtnInsertModify(), g);
            g.gridx = 1;
            formNews.add(getBtnReset(), g);
        }
        return formNews;
    }

    /**
     * This method initializes the slider to set the priority of a News.
     *
     * @Return javax.swing.JSlider - the slider with ticks from 1 to 5.
     */
    private JSlider getPrSlider() {
        if (null == prSlider) {
            prSlider = new JSlider(JSlider.HORIZONTAL, 5, 1);
            prSlider.setMinimum(1);
            prSlider.setMaximum(5);
            prSlider.setMajorTickSpacing(1);
            prSlider.setLabelTable(prSlider.createStandardLabels(1));
            prSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            prSlider.setName("priority");
            prSlider.addMouseListener(newsHelp);
            prSlider.setPaintLabels(true);
            prSlider.setPaintTicks(true);
            prSlider.setPaintTicks(true);
            prSlider.setSnapToTicks(true);
        }
        return prSlider;
    }

    /**
     * This method initializes the radio button for submission of the form Insert /
     * edit.
     *
     * @Return javax.swing.JButton - the button of submission of the form.
     */
    private JButton getBtnInsertModify() {
        if (null == btnInsertModify) {
            btnInsertModify = new JButton();
            btnInsertModify.setText("Insert");
            btnInsertModify.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "nuovaNews16.png ")));
            btnInsertModify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnInsertModify.setName("btnInsert ");
            btnInsertModify.addMouseListener(newsHelp);
            btnInsertModify.setFont(new Font("Dialog", Font.BOLD, 12));
            btnInsertModify.setHorizontalTextPosition(SwingConstants.TRAILING);
            btnInsertModify.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent pEvent) {
                    if (textNews.getText().length() == 0) {
                        JOptionPane.showInternalMessageDialog(jContentPane, "The text of a news can not be empty!",
                                "Error New News", JOptionPane.ERROR_MESSAGE,
                                new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error32.png ")));
                        return;
                    }
                    if (idNews != -1) // We're making a change
                    {
// Construction of the dialog for confirmation of
// Edit
                        JPanel root = new JPanel(new BorderLayout());
                        JLabel message = new JLabel("Changing the selected news with" + "New data?");
                        message.setFont(new Font("Dialog", Font.BOLD, 14));
                        JLabel alert = new JLabel("The previous data can not be more recovered.",
                                SwingConstants.CENTER);
                        alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
                        root.add(message, BorderLayout.NORTH);
                        root.add(alert, BorderLayout.CENTER);
                        String[] options = { "Edit", "Cancel" };
// The dialog screen appears
                        int choice = JOptionPane.showInternalOptionDialog(jContentPane, root,
                                "Confirmation Change News", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ModifyNews48.png")), options,
                                options[1]);
// If you chose to confirm the change
                        if (choice == JOptionPane.YES_OPTION) {
                            try {
                                Date expires = new Date();
                                expires.setDate(expires.getDate() + durationNews.getSelectedIndex());
                                BeanNews new_ = new BeanNews(textNews.getText(), new Date(), expires,
                                        prSlider.getValue(), idNews);
                                managerNews.modifyNews(new_);
                                TableModel.updateNews(new_);
                                JOptionPane.showInternalMessageDialog(jContentPane,
                                        "The news has been changed successfully selected.", "News changed!",
                                        JOptionPane.OK_OPTION,
                                        new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Ok48.png ")));
                            } catch (Exception ex) {
                                JLabel error = new JLabel(
                                        "<html> <h2> Unable to communicate with the server eTour. </ h2>"
                                                + "<h3> <u> Change operation request can not be completed. </ U> </ h3>"
                                                + "<p> Please try again later. </ P>"
                                                + "<p> If the error persists, please contact technical support. </ P>"
                                                + "<p> We apologize for the inconvenience. </ Html>");
                                ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Error48.png"));
                                JOptionPane.showMessageDialog(JDesktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE,
                                        err);
                            }
                        }
                    } else
// We are posting
                    {
                        Date expires = new Date();
                        expires.setDate(expires.getDate() + durationNews.getSelectedIndex());
                        BeanNews new_ = new BeanNews(textNews.getText(), new Date(), expires, prSlider.getValue(), 33);
                        try {
                            boolean ok = managerNews.insertNews(new_);
                            if (ok) {
                                loadTable();
                                TableModel.insertNews(new_);
                                JOptionPane.showInternalMessageDialog(jContentPane,
                                        "The news is selected correctly inserted into the system.", "New news!",
                                        JOptionPane.OK_OPTION,
                                        new ImageIcon(getClass().getResource(Home.URL_IMAGES + "NuovaNews48.png ")));
                            }
                        } catch (RemoteException e) {
                            JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                                    + "<h3> <u> Insertion operation request can not be completed. </ U> </ h3>"
                                    + "<p> Please try again later. </ P>"
                                    + "<p> If the error persists, please contact technical support. </ P>"
                                    + "<p> We apologize for the inconvenience. </ Html>");
                            ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                            JOptionPane.showMessageDialog(JDesktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE,
                                    err);
                        }
                    }
                    TableModel.fireTableDataChanged();
                    clearForm();
                }
            });

        }
        return btnInsertModify;
    }

    /**
     * This method initializes the button to clear the form or Cancel editing
     * actions on a selected news.
     *
     * @Return javax.swing.JButton - the button above.
     */
    private JButton getBtnReset() {
        if (null == btnReset) {
            btnReset = new JButton();
            btnReset.setText("Clear");
            btnReset.setHorizontalTextPosition(SwingConstants.LEADING);
            btnReset.setPreferredSize(new Dimension(103, 26));
            btnReset.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "clear16.png ")));
            btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnReset.setName("btnActive ");
            btnReset.addMouseListener(newsHelp);
            btnReset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    clearForm();
                }
            });
        }
        return btnReset;
    }

    /**
     * This method initializes the panel's online help.
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getPanelHelp() {
        if (null == panelHelp) {
            panelHelp = new JPanel();
            panelHelp.setLayout(new BorderLayout());
            panelHelp.setBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3), "Help",
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
            textGuide.setEditable(false);
            textGuide.setContentType("text / html");
            textGuide.setOpaque(false);
            textGuide.setText("<html> Move your mouse pointer over a control"
                    + "Of interest to display the context-sensitive help. </ Html>");
            textGuide.setName("textGuide ");
            textGuide.addMouseListener(newsHelp);
            panelHelp.setPreferredSize(new Dimension(200, 100));
            panelHelp.add(textGuide, BorderLayout.CENTER);
        }
        return panelHelp;
    }

    /**
     * This method initializes the table with all the news these In the system.
     *
     * @Return javax.swing.JTable
     */
    private JScrollPane getTableNews() {
        if (null == tableNews) {
            TableModel = new NewsTableModel();
            tableNews = new ScrollableTable(TableModel);
            tableNews.setRowHeight(64);
            tableNews.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableNews.setSelectionBackground(new Color(0xe6, 0xe6, 0xFA));
            tableNews.setColumnSelectionAllowed(false);
            ListSelectionModel selectionModel = tableNews.getSelectionModel();
            selectionModel.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    int selectedRow = tableNews.getSelectedRow();
                    btnModifyN.setEnabled((selectedRow != -1) ? true : false);
                    btnDeleteN.setEnabled((selectedRow != -1) ? true : false);
                }
            });
            tableNews.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent pKey) {
                    int keyCode = pKey.getKeyCode();
                    if (keyCode == KeyEvent.VK_ENTER) {
                        btnModifyN.doClick();
                    } else if ((keyCode == KeyEvent.VK_CANCEL) || (keyCode == KeyEvent.VK_BACK_SPACE)) {
                        btnDeleteN.doClick();
                    }
                }
            });

            scrollTableNews = new JScrollPane();
            scrollTableNews.setViewportView(tableNews);
            scrollTableNews.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        return scrollTableNews;
    }

    /**
     * This method initializes the text area that contains the text of a news.
     *
     * @Return javax.swing.JTextArea - the text area.
     */
    private JTextArea getTextNews() {
        if (null == textNews) {
            textNews = new JTextArea();
            textNews.setWrapStyleWord(true);
            textNews.setLineWrap(true);
            textNews.setBorder(BorderFactory.createLoweredBevelBorder());
            textNews.setColumns(18);
            textNews.setLineWrap(true);
            textNews.setRows(4);
            textNews.setDocument(new LimitedDocument(200));
            textNews.setName("text");
            textNews.addMouseListener(newsHelp);
            textNews.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent pKeyEvent) {
                    char keyChar = pKeyEvent.getKeyChar();
                    if (Character.isDigit(keyChar) || Character.isLetter(keyChar) || Character.isWhitespace(keyChar)) {
                        int len = textNews.getText().length();
                        if (len != 200) {
                            labelCharaters.setText("# Characters" + (200 - (textNews.getText().length() + 1)));
                        }
                    }
                }

                public void keyPressed(KeyEvent pKeyEvent) {
                    int keyCode = pKeyEvent.getKeyCode();
                    if (keyCode == KeyEvent.VK_CANCEL || keyCode == KeyEvent.VK_BACK_SPACE) {
                        int len = textNews.getText().length();
                        if (len != 0) {
                            labelCharaters.setText("# Characters" + (200 - len + 1));
                        }
                    }
                }
            });

        }
        return textNews;
    }

    /**
     * This method resets the form fields.
     */
    private void clearForm() {
        btnInsertModify.setText("Insert");
        btnReset.setText("Clear");
        btnReset.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "clear16.png ")));
        formNews.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "New News", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
        durationNews.setSelectedIndex(0);
        textNews.setText("");
        tableNews.clearSelection();
        prSlider.setValue(1);
        labelCharaters.setText("# Characters: 200");
        idNews = -1;
    }

    /**
     * This method imports the news downloaded from the server in the table.
     */
    private void loadTable() {
        ArrayList<BeanNews> news = null;
        try {
            news = managerNews.getAllNews();
        }
// If an error displays an error message.
        catch (RemoteException e) {
            JLabel error = new JLabel("<html> <h2> Unable to communicate with the server eTour. </ h2>"
                    + "The list of <h3> <u> News is not loaded. </ U> </ h3>" + "<p> Please try again later. </ P>"
                    + "<p> If the error persists, please contact technical support. </ P>"
                    + "<p> We apologize for the inconvenience. </ Html>");
            ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
            JOptionPane.showInternalMessageDialog(this, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
        } finally {
            TableModel = new NewsTableModel(news);
            tableNews.setModel(TableModel);
// Text of news
            tableNews.getColumnModel().getColumn(0).setPreferredWidth(320);
            tableNews.getColumnModel().getColumn(0).setCellRenderer(new TextNewsRenderer());
// Priority
            tableNews.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableNews.getColumnModel().getColumn(1).setCellRenderer(new PriorityRenderer());
// Date of entry
            tableNews.getColumnModel().getColumn(2).setPreferredWidth(80);
// End Date
            tableNews.getColumnModel().getColumn(3).setPreferredWidth(80);
        }

    }
}