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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.Point3D;

/**
 * Class that models the interface for viewing the card, Modify the data and the
 * insertion of a new resting spot.
 *
 *
 */
public class CardPR extends JInternalFrame {

    private JPanel jContentPane = null;
    private JToolBar toolbarCardBC = null;
    private JToggleButton btnModify = null;
    private JButton btnSave = null;
    private JButton btnCancel = null;
    private JButton btnModifyComment = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel statistics = null;
    private JPanel feedback = null;
    private JLabel txtName = null;
    private JLabel txtAddress = null;
    private JLabel txtCAP = null;
    private JLabel txtCity = null;
    private JLabel txtLocation = null;
    private JLabel txtProvince = null;
    private JLabel txtPos = null;
    private JLabel txtPhone = null;
    private JLabel txtOraAp = null;
    private JLabel txtOraCh = null;

    private JLabel jLabel = null;
    private JTextField addressPR = null;
    private JComboBox addressPR1 = null;
    private JTextField cityPR = null;
    private JComboBox locationPR = null;
    private JTextField capPR = null;
    private JScrollPane jScrollPane = null;
    private JTextArea descriptionPR = null;
    private JTextField phonePR = null;
    private JComboBox scheduleAPOrePR = null;
    private JLabel jLabel1 = null;
    private JComboBox scheduleApMinPR = null;
    private TagTableModel panelTag;
    private JTextField costBC = null;

    private JLabel jLabel3 = null;
    private JComboBox scheduleCHMinPR = null;
    private JComboBox provPR = null;
    private JPanel dataPR = null;
    private JTextField namePR = null;
    private JPanel jPanel = null;
    private JScrollPane jScrollPane2 = null;
    private JTable feedbackTable = null;
    private JLabel txtNameBene = null;
    private JLabel averageRatingPR = null;
    private JPanel statisticMonthCurrent = null;
    private JPanel statisticTotal = null;
    private JLabel jLabel4 = null;
    private JLabel jLabel41 = null;
    private ActionListener campoCompilato;
    private FocusListener validating;
    private JToolBar ToolbarCardPR = null;
    private JTextField posGeoX = null;
    private JTextField posGeoY = null;
    private JTextField posGeoZ = null;
    private JLabel jLabel2 = null;
    private JComboBox scheduleCHOrePR = null;

    /**
     * The default constructor for inclusion of the interface model A new
     * refreshment.
     *
     */
    public CardPR() {
        super("New Refreshment");
        campoCompilato = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            }
        };
        validating = new FocusListener() {

            private final Color ERROR_BACKGROUND = new Color(255, 215, 215);
            private final Color WARNING_BACKGROUND = new Color(255, 235, 205);
            private String text;

            public void focusGained(FocusEvent fe) {
                if (fe.getSource() instanceof JTextField) {
                    JTextField textbox = (JTextField) fe.getSource();
                    text = textbox.getText();
                }

            }

            public void focusLost(FocusEvent fe) {
                if (fe.getSource() instanceof JTextField) {
                    JTextField textbox = (JTextField) fe.getSource();
                    if (!text.equals(textbox.getText())) {
                        text = textbox.getText();
                        if (text.equals("")) {
                            textbox.setBackground(ERROR_BACKGROUND);
                            Rectangle bounds = textbox.getBounds();
                            JLabel nuova = new JLabel();
                            nuova.setIcon(new ImageIcon(
                                    getClass().getResource("/ unisa / gps / eTour / gui / images / error.png ")));
                            nuova.setBounds(bounds);
                            nuova.setToolTipText("Field" + textbox.getName() + "can not be empty!");
                            dataPR.add(nuova, null);
                            dataPR.repaint();
                        }
                    }

                }
                initialize();
            }
        };
    }

    /**
     * This interface models the manufacturer regarding modifytion of data and
     * Display board a refreshment.
     * 
     * @Param unisa.gps.etour.bean.BeanRefreshmentPoint PR - the bean contains the
     *        data of RefreshmentPoint selected.
     * @Param boolean change - indicates whether the fields should be editable, so
     *        if You are viewing a card or change the cultural property.
     *
     */
    public CardPR(BeanRefreshmentPoint pr, boolean edit) {
        this();

        namePR.setText(pr.getName());
        setTitle(pr.getName());
        capPR.setText(pr.getCap());
        cityPR.setText(pr.getCity());

        descriptionPR.setText(pr.getDescription());
        StringTokenizer tokenizer = new StringTokenizer(pr.getStreet());
        /*
         * AddressPR1.addItem ( "Street"); addressPR1.addItem ("P.zza ");
         * addressPR1.addItem ( "V.le"); addressPR1.addItem ("V.co ");
         * addressPR1.addItem ( "Largo"); addressPR1.addItem ( "Course");
         */
        String[] via = { "Street", "P.zza", "V.le", "V.co", "Largo", "Course" };
        String string = tokenizer.nextToken();
        int i;
        for (i = 0; i < via.length; i++) {
            if (string.equalsIgnoreCase(via[i]))
                break;
            this.addressPR1.setSelectedIndex(i);
            while (tokenizer.hasMoreTokens())
                this.addressPR.setText(addressPR.getText() + "" + tokenizer.nextToken());
            this.provPR.setSelectedItem(pr.getProvince());
            Point3D pos = pr.getPosition();
            this.posGeoX.setText("" + pos.getAltitude());
            this.posGeoY.setText("" + pos.getLatitude());
            this.posGeoZ.setText("" + pos.getLongitude());
            this.phonePR.setText(pr.getPhone());
            int minutes = pr.getOpeningTime().getMinutes();
            if (minutes == 0)
                this.scheduleApMinPR.setSelectedIndex(0);
            else
                this.scheduleApMinPR.setSelectedItem(minutes);
            int hours = pr.getOpeningTime().getHours();
            if (hours < 10)
                this.scheduleAPOrePR.setSelectedItem("0" + hours);
            else
                this.scheduleAPOrePR.setSelectedItem(hours);
            this.scheduleCHMinPR.setSelectedItem(pr.getClosingTime().getMinutes());
            this.scheduleAPOrePR.setSelectedItem(pr.getOpeningTime().getHours());
            this.scheduleCHOrePR.setSelectedItem(pr.getClosingTime().getHours());
            if (edit) {
                btnModify.setSelected(true);
            } else {
                makeEditabled();
            }

        }
    }

    /**
     * Method called by the constructor
     *
     * @Return void
     */
    private void initialize() {
        this.setIconifiable(true);
        this.setBounds(new Rectangle(0, 0, 600, 540));
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setFrameIcon(new ImageIcon(
                getClass().getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / scheda.png ")));
        this.setClosable(true);
        this.setContentPane(getJContentPane());
    }

    private void makeEditabled() {
        Component[] components = dataPR.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component current = components[i];
            if (current instanceof JTextField) {
                JTextField textbox = (JTextField) current;
                textbox.setEditable(textbox.isEditable() ? false : true);
                textbox.setBackground(Color.white);

            } else if (current instanceof JComboBox) {
                JComboBox combo = (JComboBox) current;
                combo.setEnabled(combo.isEnabled() ? false : true);

            }
        }
        descriptionPR.setEditable(descriptionPR.isEditable() ? false : true);
    }

    /**
     * Method which initializes a jContentPane
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());

            jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
            jContentPane.add(getToolbarCardPR(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes the button (ToggleButton) the alteration Data for
     * refreshmentPoint
     *
     * @Return javax.swing.JToggleButton
     */
    private JToggleButton getBtnModify() {
        if (btnModify == null) {
            btnModify = new JToggleButton();
            btnModify.setText("Change Data");
            btnModify.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / modify.png ")));
            btnModify.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    makeEditabled();
                    btnSave.setVisible((btnModify.isSelected() ? true : false));
                    btnCancel.setVisible((btnModify.isSelected() ? true : false));

                }

            });
        }
        return btnModify;
    }

    /**
     * Method to initialize the Save button (btnSave)
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton();
            btnSave.setText("Save");
            btnSave.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / salva.png ")));
            btnSave.setVisible(false);
        }
        return btnSave;
    }

    /**
     * Method to initialize the Cancel button (btnCancel)
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton();
            btnCancel.setText("Cancel");
            btnCancel.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / annulla.png ")));
            btnCancel.setVisible(false);
        }
        return btnCancel;
    }

    /**
     * Method to initialize the button for Changing a comment (btnModifyComment)
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnModifyComment() {
        if (btnModifyComment == null) {
            btnModifyComment = new JButton();
            btnModifyComment.setText("Edit Comment");
            btnModifyComment.setIcon(new ImageIcon(getClass()
                    .getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / modifyComment.png ")));
            btnModifyComment.setVisible(false);
        }
        return btnModifyComment;
    }

    /**
     * Create and initialize a JTabbedPane
     *
     * @Return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            jTabbedPane.addTab("Data Refreshment",
                    new ImageIcon(getClass()
                            .getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / data.png")),
                    getDataPR(), null);
            jTabbedPane.addTab("MenuTouristco",
                    new ImageIcon(getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/stat24.png")),
                    null, null);
            jTabbedPane.addTab("Statistics",
                    new ImageIcon(getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/stat24.png")),
                    getStatistic(), null);
            jTabbedPane.addTab("Feedback received",
                    new ImageIcon(getClass()
                            .getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / feedback.png")),
                    getFeedback(), null);

        }
        return jTabbedPane;
    }

    /**
     * Method to initialize a panel (dataPR)
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getDataPR() {
        if (dataPR == null) {
            GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
            gridBagConstraints27.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints27.gridy = 9;
            gridBagConstraints27.weightx = 1.0;
            gridBagConstraints27.anchor = GridBagConstraints.WEST;
            gridBagConstraints27.insets = new Insets(5, 5, 36, 0);
            gridBagConstraints27.ipadx = 18;
            gridBagConstraints27.gridx = 1;
            GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
            gridBagConstraints34.gridx = 7;
            gridBagConstraints34.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints34.gridy = 6;
            jLabel2 = new JLabel();
            jLabel2.setText("z");
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            gridBagConstraints33.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints33.gridy = 6;
            gridBagConstraints33.weightx = 1.0;
            gridBagConstraints33.ipadx = 50;
            gridBagConstraints33.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints33.anchor = GridBagConstraints.WEST;
            gridBagConstraints33.gridx = 6;
            GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
            gridBagConstraints38.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints38.gridy = 6;
            gridBagConstraints38.weightx = 1.0;
            gridBagConstraints38.ipadx = 50;
            gridBagConstraints38.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints38.anchor = GridBagConstraints.WEST;
            gridBagConstraints38.gridx = 4;
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints22.gridy = 6;
            gridBagConstraints22.weightx = 0.0;
            gridBagConstraints22.ipadx = 50;
            gridBagConstraints22.anchor = GridBagConstraints.WEST;
            gridBagConstraints22.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints22.gridx = 1;
            GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
            gridBagConstraints36.insets = new Insets(0, 5, 0, 5);
            gridBagConstraints36.gridy = 6;
            gridBagConstraints36.ipadx = 0;
            gridBagConstraints36.ipady = 0;
            gridBagConstraints36.gridwidth = 1;
            gridBagConstraints36.gridx = 5;
            GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
            gridBagConstraints35.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints35.gridy = 6;
            gridBagConstraints35.ipadx = 0;
            gridBagConstraints35.ipady = 0;
            gridBagConstraints35.gridwidth = 1;
            gridBagConstraints35.anchor = GridBagConstraints.WEST;
            gridBagConstraints35.gridx = 3;
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.insets = new Insets(15, 20, 5, 0);
            gridBagConstraints32.gridx = 16;
            gridBagConstraints32.gridy = 4;
            gridBagConstraints32.ipadx = 172;
            gridBagConstraints32.ipady = 125;
            gridBagConstraints32.gridwidth = 0;
            gridBagConstraints32.gridheight = 6;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints31.gridwidth = 9;
            gridBagConstraints31.gridx = 1;
            gridBagConstraints31.gridy = 0;
            gridBagConstraints31.weightx = 0.0;
            gridBagConstraints31.ipadx = 240;
            gridBagConstraints31.anchor = GridBagConstraints.WEST;
            gridBagConstraints31.insets = new Insets(20, 5, 5, 0);
            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.fill = GridBagConstraints.BOTH;
            gridBagConstraints30.gridwidth = 17;
            gridBagConstraints30.gridx = 1;
            gridBagConstraints30.gridy = 10;
            gridBagConstraints30.ipadx = 265;
            gridBagConstraints30.ipady = 70;
            gridBagConstraints30.weightx = 1.0;
            gridBagConstraints30.weighty = 1.0;
            gridBagConstraints30.gridheight = 4;
            gridBagConstraints30.anchor = GridBagConstraints.WEST;
            gridBagConstraints30.insets = new Insets(5, 5, 2, 5);
            GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
            gridBagConstraints29.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints29.gridwidth = 3;
            gridBagConstraints29.gridx = 4;
            gridBagConstraints29.gridy = 9;
            gridBagConstraints29.weightx = 1.0;
            gridBagConstraints29.ipadx = 18;
            gridBagConstraints29.anchor = GridBagConstraints.WEST;
            gridBagConstraints29.insets = new Insets(5, 5, 36, 2);
            GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
            gridBagConstraints28.insets = new Insets(3, 5, 34, 4);
            gridBagConstraints28.gridy = 9;
            gridBagConstraints28.gridx = 3;
            GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
            gridBagConstraints26.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints26.gridwidth = 3;
            gridBagConstraints26.gridx = 4;
            gridBagConstraints26.gridy = 8;
            gridBagConstraints26.weightx = 1.0;
            gridBagConstraints26.anchor = GridBagConstraints.WEST;
            gridBagConstraints26.ipadx = 18;
            gridBagConstraints26.insets = new Insets(6, 5, 4, 2);
            GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
            gridBagConstraints25.insets = new Insets(4, 5, 2, 4);
            gridBagConstraints25.gridy = 8;
            gridBagConstraints25.anchor = GridBagConstraints.WEST;
            gridBagConstraints25.gridx = 3;
            GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
            gridBagConstraints24.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints24.gridx = 1;
            gridBagConstraints24.gridy = 8;
            gridBagConstraints24.weightx = 1.0;
            gridBagConstraints24.ipadx = 18;
            gridBagConstraints24.gridwidth = 3;
            gridBagConstraints24.anchor = GridBagConstraints.WEST;
            gridBagConstraints24.insets = new Insets(6, 5, 4, 1);
            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
            gridBagConstraints23.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints23.gridwidth = 9;
            gridBagConstraints23.gridx = 1;
            gridBagConstraints23.gridy = 7;
            gridBagConstraints23.weightx = 1.0;
            gridBagConstraints23.ipadx = 120;
            gridBagConstraints23.anchor = GridBagConstraints.WEST;
            gridBagConstraints23.insets = new Insets(4, 5, 4, 17);
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints21.gridwidth = 7;
            gridBagConstraints21.gridx = 1;
            gridBagConstraints21.gridy = 5;
            gridBagConstraints21.ipadx = 70;
            gridBagConstraints21.ipady = 0;
            gridBagConstraints21.weightx = 1.0;
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.insets = new Insets(5, 5, 5, 6);
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints20.gridwidth = 7;
            gridBagConstraints20.gridx = 1;
            gridBagConstraints20.gridy = 4;
            gridBagConstraints20.weightx = 1.0;
            gridBagConstraints20.ipadx = 60;
            gridBagConstraints20.anchor = GridBagConstraints.WEST;
            gridBagConstraints20.insets = new Insets(0, 5, 0, 0);
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints19.gridwidth = 4;
            gridBagConstraints19.gridx = 1;
            gridBagConstraints19.gridy = 3;
            gridBagConstraints19.weightx = 1.0;
            gridBagConstraints19.ipadx = 20;
            gridBagConstraints19.anchor = GridBagConstraints.WEST;
            gridBagConstraints19.insets = new Insets(6, 5, 5, 18);
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints18.gridwidth = 6;
            gridBagConstraints18.gridx = 1;
            gridBagConstraints18.gridy = 2;
            gridBagConstraints18.weightx = 1.0;
            gridBagConstraints18.ipadx = 100;
            gridBagConstraints18.anchor = GridBagConstraints.WEST;
            gridBagConstraints18.insets = new Insets(0, 5, 0, 0);
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints17.gridwidth = 9;
            gridBagConstraints17.gridx = 2;
            gridBagConstraints17.gridy = 1;
            gridBagConstraints17.weightx = 1.0;
            gridBagConstraints17.ipadx = 200;
            gridBagConstraints17.anchor = GridBagConstraints.WEST;
            gridBagConstraints17.insets = new Insets(5, 5, 5, 0);
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints16.gridwidth = 3;
            gridBagConstraints16.gridx = 1;
            gridBagConstraints16.gridy = 1;
            gridBagConstraints16.weightx = 1.0;
            gridBagConstraints16.anchor = GridBagConstraints.WEST;
            gridBagConstraints16.ipadx = 0;
            gridBagConstraints16.insets = new Insets(5, 5, 5, 0);
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints15.gridy = 10;
            gridBagConstraints15.gridwidth = 1;
            gridBagConstraints15.gridheight = 0;
            gridBagConstraints15.gridx = 0;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.insets = new Insets(5, 15, 36, 0);
            gridBagConstraints14.gridy = 9;
            gridBagConstraints14.gridx = 0;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints13.gridy = 8;
            gridBagConstraints13.gridx = 0;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints12.gridy = 7;
            gridBagConstraints12.gridx = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints11.gridy = 6;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints10.gridy = 5;
            gridBagConstraints10.gridx = 0;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints9.gridy = 4;
            gridBagConstraints9.gridx = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints8.gridy = 3;
            gridBagConstraints8.gridx = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints7.gridy = 2;
            gridBagConstraints7.gridx = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new Insets(5, 15, 5, 0);
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.gridx = 0;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.insets = new Insets(20, 15, 5, 0);
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.gridwidth = 1;
            gridBagConstraints5.gridx = 0;
            jLabel41 = new JLabel();
            jLabel41.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabel41.setText("y");
            jLabel4 = new JLabel();
            jLabel4.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabel4.setText("x");
            jLabel3 = new JLabel();
            jLabel3.setFont(new Font("Dialog", Font.BOLD, 18));
            jLabel3.setText(":");

            jLabel1 = new JLabel();
            jLabel1.setFont(new Font("Dialog", Font.BOLD, 18));
            jLabel1.setText(":");
            jLabel = new JLabel();
            jLabel.setText("Description");
            txtOraCh = new JLabel();
            txtOraCh.setText("Closing Time");
            txtOraAp = new JLabel();
            txtOraAp.setText("Opening Hours");
            txtPhone = new JLabel();
            txtPhone.setText("Phone");
            txtPos = new JLabel();
            txtPos.setText("Geographic Position");
            txtProvince = new JLabel();
            txtProvince.setText("Province");
            txtLocation = new JLabel();
            txtLocation.setText("Location");
            txtCity = new JLabel();
            txtCity.setText("City");
            txtCAP = new JLabel();
            txtCAP.setText("CAP");
            txtAddress = new JLabel();
            txtAddress.setText("Address");
            txtName = new JLabel();
            txtName.setText("Name Refreshment");
            dataPR = new JPanel();
            dataPR.setLayout(new GridBagLayout());
            dataPR.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            dataPR.add(txtName, gridBagConstraints5);
            dataPR.add(txtAddress, gridBagConstraints6);
            dataPR.add(txtCity, gridBagConstraints7);
            dataPR.add(txtLocation, gridBagConstraints8);
            dataPR.add(txtCAP, gridBagConstraints9);
            dataPR.add(txtProvince, gridBagConstraints10);
            dataPR.add(txtPos, gridBagConstraints11);
            dataPR.add(txtPhone, gridBagConstraints12);
            dataPR.add(txtOraAp, gridBagConstraints13);
            dataPR.add(txtOraCh, gridBagConstraints14);
            dataPR.add(jLabel, gridBagConstraints15);
            dataPR.add(getAddressPR1(), gridBagConstraints16);
            dataPR.add(getAddressPR(), gridBagConstraints17);
            dataPR.add(getCityPR(), gridBagConstraints18);
            dataPR.add(getLocationPR(), gridBagConstraints19);
            dataPR.add(getCapPR(), gridBagConstraints20);
            dataPR.add(getProvPR(), gridBagConstraints21);
            dataPR.add(getPhonePR(), gridBagConstraints23);
            dataPR.add(getScheduleAPOrePR(), gridBagConstraints24);
            dataPR.add(jLabel1, gridBagConstraints25);
            dataPR.add(getScheduleApMinPR(), gridBagConstraints26);
            dataPR.add(jLabel3, gridBagConstraints28);
            dataPR.add(getScheduleCHMinPR(), gridBagConstraints29);
            dataPR.add(getJScrollPane(), gridBagConstraints30);
            dataPR.add(getNamePR(), gridBagConstraints31);
            dataPR.add(getJPanel(), gridBagConstraints32);
            dataPR.add(jLabel4, gridBagConstraints35);
            dataPR.add(jLabel41, gridBagConstraints36);
            dataPR.add(getPosGeoX(), gridBagConstraints22);
            dataPR.add(getPosGeoY(), gridBagConstraints38);
            dataPR.add(getPosGeoZ(), gridBagConstraints33);
            dataPR.add(jLabel2, gridBagConstraints34);
            dataPR.add(getScheduleCHOrePR(), gridBagConstraints27);
        }
        return dataPR;
    }

    /**
     * Method for iniziailizzare a panel (statistics)
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getStatistic() {
        if (statistics == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridwidth = 0;
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.insets = new Insets(20, 0, 0, 0);
            gridBagConstraints4.gridy = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridwidth = 2;
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.insets = new Insets(0, 0, 20, 0);
            gridBagConstraints3.gridy = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.insets = new Insets(0, 30, 30, 0);
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridy = 0;
            averageRatingPR = new JLabel();
            averageRatingPR.setText("JLabel");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new Insets(0, 0, 30, 0);
            gridBagConstraints1.gridy = 0;
            txtNameBene = new JLabel();
            txtNameBene.setText("Well name> Culturale>");
            txtNameBene.setFont(new Font("Dialog", Font.BOLD, 18));
            statistics = new JPanel();
            statistics.setLayout(new GridBagLayout());
            statistics.add(txtNameBene, gridBagConstraints1);
            statistics.add(averageRatingPR, gridBagConstraints2);
            statistics.add(getStatisticMonthCurrent(), gridBagConstraints3);
            statistics.add(getStatisticTotal(), gridBagConstraints4);
        }
        return statistics;
    }

    /**
     * Method to initialize a panel (feedback)
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getFeedback() {
        if (feedback == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.gridx = 0;
            feedback = new JPanel();
            feedback.setLayout(new GridBagLayout());
            feedback.add(getJScrollPane2(), gridBagConstraints);
        }
        return feedback;
    }

    /**
     * Initialize a JexField (addressPR)
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getAddressPR() {
        if (addressPR == null) {
            addressPR = new JTextField();
            addressPR.setColumns(12);
            addressPR.addActionListener(campoCompilato);
        }
        return addressPR;
    }

    /**
     * Method to initialize the type field address (addressPR) Or via, piazza ....
     *
     * @Return javax.swing.JComboBox
     */
    private JComboBox getAddressPR1() {
        if (addressPR1 == null) {
            addressPR1 = new JComboBox();
            addressPR1.setPreferredSize(new Dimension(60, 20));
            addressPR1.setMinimumSize(new Dimension(60, 25));
            addressPR1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            addressPR1.addItem("Street");
            addressPR1.addItem("P.zza ");
            addressPR1.addItem("V.le");
            addressPR1.addItem("V.co ");
            addressPR1.addItem("Largo");
            addressPR1.addItem("Course");

        }
        return addressPR1;
    }

    /**
     * Initialize a JTextField for entering Uan city CityPR
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getCityPR() {
        if (cityPR == null) {
            cityPR = new JTextField();
            cityPR.setColumns(12);
            cityPR.addActionListener(campoCompilato);
        }
        return cityPR;
    }

    /**
     * This method initializes locationPR
     *
     * @Return javax.swing.JComboBox
     */
    private JComboBox getLocationPR() {
        if (locationPR == null) {
            locationPR = new JComboBox();
            locationPR.setMinimumSize(new Dimension(80, 25));
            locationPR.setPreferredSize(new Dimension(80, 20));
            locationPR.addActionListener(campoCompilato);
        }
        return locationPR;
    }

    /**
     * Code of refreshment. Definition capPR JTextField
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getCapPR() {
        if (capPR == null) {
            capPR = new JTextField();
            capPR.setColumns(8);
            capPR.addActionListener(campoCompilato);
        }
        return capPR;
    }

    /**
     * Creation JScrollPane
     *
     * @Return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setViewportView(getDescriptionPR());
        }
        return jScrollPane;
    }

    /**
     * Method to create JTextArea's whole descriptionPR
     *
     * @Return javax.swing.JTextArea
     */
    private JTextArea getDescriptionPR() {
        if (descriptionPR == null) {
            descriptionPR = new JTextArea();
            descriptionPR.setColumns(12);
            descriptionPR.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        }
        return descriptionPR;
    }

    /**
     * Method to create the JTextField phonePR
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getPhonePR() {
        if (phonePR == null) {
            phonePR = new JTextField();
            phonePR.setColumns(12);
            phonePR.addActionListener(campoCompilato);
        }
        return phonePR;
    }

    /**
     * method to initialize a JComboBox with the hours (scheduleAPOrePR)
     *
     * @Return javax.swing.JComboBox
     */
    private JComboBox getScheduleAPOrePR() {
        if (scheduleAPOrePR == null) {
            scheduleAPOrePR = new JComboBox();
            scheduleAPOrePR.setPreferredSize(new Dimension(40, 20));
            for (int i = 0; i < 24; i++) {
                if (i < 10)
                    scheduleAPOrePR.addItem("0" + i);
                else
                    scheduleAPOrePR.addItem(i);

            }
            scheduleAPOrePR.addActionListener(campoCompilato);
        }
        return scheduleAPOrePR;
    }

    /**
     * Method to initialize a JComboBox with the minutes (scheduleApMinPR)
     *
     * @Return javax.swing.JComboBox
     */
    private JComboBox getScheduleApMinPR() {
        if (scheduleApMinPR == null) {
            scheduleApMinPR = new JComboBox();
            scheduleApMinPR.setLightWeightPopupEnabled(true);
            scheduleApMinPR.setPreferredSize(new Dimension(40, 20));
            scheduleApMinPR.addItem("00");
            scheduleApMinPR.addItem("15");
            scheduleApMinPR.addItem("30");
            scheduleApMinPR.addItem("45");
            scheduleApMinPR.addActionListener(campoCompilato);
        }
        return scheduleApMinPR;
    }

    /**
     * Method to initialize a JComboBox with the minutes (scheduleCHMinPR)
     *
     * @Return javax.swing.JComboBox
     */
    private JComboBox getScheduleCHMinPR() {
        if (scheduleCHMinPR == null) {
            scheduleCHMinPR = new JComboBox();
            scheduleCHMinPR.setPreferredSize(new Dimension(40, 20));
            scheduleCHMinPR.addItem("00");
            scheduleCHMinPR.addItem("15");
            scheduleCHMinPR.addItem("30");
            scheduleCHMinPR.addItem("45");
            scheduleCHMinPR.addActionListener(campoCompilato);
        }
        return scheduleCHMinPR;

    }

    /**
     * Create and initialize a jCombo Box with all the provinces (provPR)
     *
     * @Return javax.swing.JTextField
     */
    private JComboBox getProvPR() {
        if (provPR == null) {
            final String[] province = { "AG", "AL", "an", "AO", "AQ", "AR", "AP", "AT", "AV", "BA", "BL", "BN", "BG",
                    "BI", "BO", "BR", "BS", "BZ", "CA", "CB", "CE", "CH", "CI", "CL", "CN", "CO", "CR", "CS", "KR",
                    "en", "FC ", " FE ", " FI ", " FG ", " FR ", " GE ", " GO ", " GR ", " IM ", " IS ", " LC ", "LE",
                    "LI", "LO", "LT", "LU", "MC", "ME", "MF", "MN", "MO", "MS", "MT", "NA ", " NO ", " NU ", " OG ",
                    " OR ", " OT ", " PA ", " PC ", " PD ", " PE ", " PG ", " PO ", " PR ", "PU", "R", "RA", "RC", "RE",
                    "RG", "RI", "RM", "RN", "RO", "SA", "YES", "SO", "SP", "SS", "SV", "TA", "TE", "TN ", " TP ",
                    " TR ", " TS ", " TV ", " UD ", " VA ", " VB ", " VC ", " VE ", " VI ", "VR", "VS", "VT", "VV" };
            provPR = new JComboBox();
            for (int i = 0; i < province.length; i++) {
                provPR.addItem(province[i]);
            }
            provPR.addActionListener(campoCompilato);
        }
        return provPR;
    }

    class DocumentoNumerico extends PlainDocument {

        private int limit;

        public DocumentoNumerico(int limit) {

            this.limit = limit;

        }

        /**
         * Initialization and management position
         *
         * @Param integer pOffset
         * @Param String pString
         * @Param Attribute Pattra
         *
         */
        public void insertString(int pOffset, String pStr, AttributeSet Pattra) throws BadLocationException {
            if (pStr == null)
                return;

            if ((getLength() + pStr.length()) <= limit) {
                super.insertString(pOffset, pStr, Pattra);
            }
        }
    }

    /**
     * Initialization of a data point of the snack (namePR)
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getNamePR() {
        if (namePR == null) {
            namePR = new JTextField();
            namePR.setColumns(12);
            namePR.setPreferredSize(new Dimension(180, 20));
            namePR.addActionListener(campoCompilato);
            namePR.addFocusListener(validating);
            namePR.setDocument(new DocumentoNumerico(20));

        }
        return namePR;
    }

    /**
     * Initialize and create a panel (JPanel)
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Tag the 'Search",
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font("Dialog", Font.BOLD, 12), Color.black));
            BeanTag[] test = new BeanTag[8];
            test[0] = new BeanTag(0, "castle", "really a castle");
            test[1] = new BeanTag(1, "stronghold", "really a hostel");
            test[2] = new BeanTag(3, "statue", "really a basket");
            test[3] = new BeanTag(4, "Column", "really a basket");
            test[4] = new BeanTag(5, "internal", "really a basket");
            test[5] = new BeanTag(6, "external", "really a basket");
            test[6] = new BeanTag(7, "eight hundred", "really a basket");
            test[7] = new BeanTag(8, "Novecento", "really a basket");
            panelTag = new TagTableModel(test);
            jPanel.add(BorderLayout.CENTER, null);
        }
        return jPanel;
    }

    /**
     * Creating a JScrollPane (jScrollPane2)
     *
     * @Return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane2.setViewportView(getFeedbackTable());
        }
        return jScrollPane2;
    }

    /**
     * Create a JTable (feedbackTable)
     *
     * @Return javax.swing.JTable
     */
    private JTable getFeedbackTable() {
        if (feedbackTable == null) {
            feedbackTable = new JTable();
        }
        return feedbackTable;
    }

    /**
     * Creation of a panel (statisticMonthCurrent)
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getStatisticMonthCurrent() {
        if (statisticMonthCurrent == null) {
            statisticMonthCurrent = new JPanel();
            statisticMonthCurrent.setLayout(new GridBagLayout());
            statisticMonthCurrent.setPreferredSize(new Dimension(500, 120));
        }
        return statisticMonthCurrent;
    }

    /**
     * Creation of a panel (statisticTotal)
     *
     * @Return javax.swing.JPanel
     */
    private JPanel getStatisticTotal() {
        if (statisticTotal == null) {
            statisticTotal = new JPanel();
            statisticTotal.setLayout(new GridBagLayout());
            statisticTotal.setPreferredSize(new Dimension(500, 120));
        }
        return statisticTotal;
    }

    /**
     * Method for creating a toolbar (ToolbarCardPR)
     *
     * @Return javax.swing.JToolBar
     */
    private JToolBar getToolbarCardPR() {
        if (ToolbarCardPR == null) {

            ToolbarCardPR = new JToolBar();
            ToolbarCardPR.setFloatable(false);
            ToolbarCardPR.add(getBtnModify());
            ToolbarCardPR.addSeparator();
            ToolbarCardPR.add(getBtnSave());
            ToolbarCardPR.addSeparator();
            ToolbarCardPR.add(getBtnCancel());
            ToolbarCardPR.addSeparator();
            ToolbarCardPR.add(getBtnModifyComment());
            ToolbarCardPR.addSeparator();
        }
        return ToolbarCardPR;
    }

    /**
     * Method to initialize posGeoX The X position of the GPS
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getPosGeoX() {
        if (posGeoX == null) {
            posGeoX = new JTextField();
        }
        return posGeoX;
    }

    /**
     * Method to initialize posGeoY The Y position of the GPS
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getPosGeoY() {
        if (posGeoY == null) {
            posGeoY = new JTextField();
        }
        return posGeoY;
    }

    /**
     * Method to initialize posGeoZ The Z position of the GPS
     *
     * @Return javax.swing.JTextField
     */
    private JTextField getPosGeoZ() {
        if (posGeoZ == null) {
            posGeoZ = new JTextField();
        }
        return posGeoZ;
    }

    /**
     * Method to initialize a JComboBox with the hours (scheduleCHOrePR)
     *
     * @Return javax.swing.JComboBox
     */
    private JComboBox getScheduleCHOrePR() {
        if (scheduleCHOrePR == null) {
            scheduleCHOrePR = new JComboBox();
            scheduleCHOrePR.setPreferredSize(new Dimension(40, 20));
        }
        return scheduleCHOrePR;
    }

}
