package unisa.gps.etour.gui.operatoragency;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.util.Data;

/**
 * Class that models the interface for displaying the card and Modify the data
 * of an account tourist.
 *
 */
public class TouristCard extends JInternalFrame implements ICard {

    private static final String[] help = { "" };
    private JPanel jContentPane = null;
    private JToolBar toolbarscheda = null;
    private JToggleButton btnModify = null;
    private JButton btnSave = null;
    private JButton btnReset = null;
    private JTabbedPane jTabbedPane = null;
    private JTextField address2 = null;
    private JComboBox address1 = null;
    private JTextField city = null;
    private JTextField cap = null;
    private JTextField phone = null;
    private JComboBox province = null;
    private JPanel dataTourist = null;
    private JTextField name = null;
    private Vector<JLabel> suggestions;
    private BeanTourist tourist;
    private JTextField surname;
    private JComboBox day;
    private JComboBox month;
    private JComboBox years;
    private JTextField placeOfBirth;
    private JTextField email;
    private JTextField username;
    private JPasswordField password;
    private JLabel dataRegistration;
    private Tourist parent;

    /**
     *
     * The only card manufacturer model of a tourist or modifytion of data From
     * the bean.
     *
     * @Param pParent unisa.gps.etour.gui.operatoreagenzia.Tourist - the window
     *        "father."
     * @Param pTourist unisa.gps.etour.bean.BeanTourist - the bean contentente data
     *        Of the tourist.
     * @Param boolean pModify
     *        <ul>
     *        <li>true - if amendments are made to the Data.
     *        <li>False - if you are viewing the card.
     *
     */
    public TouristCard(Tourist pParent, BeanTourist pTourist, boolean pModify) {
        super();
        this.parent = pParent;
        setIconifiable(true);
        setSize(560, 520);
        suggestions = new Vector<JLabel>();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setClosable(true);
        tourist = pTourist;
        if (tourist.isActive()) {
            frameIcon = new ImageIcon(getClass()
                    .getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / tab turista.png"));
        } else {
            frameIcon = new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/DisactiveTourist32.png"));
        }
        initialize();
        if (pModify) {
            btnModify.setSelected(true);
            btnSave.setVisible(true);
            btnReset.setVisible(true);
        } else {
            showHideSuggestions();
            activeDisactiveEdit();
        }
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent pEvent) {
                if (btnModify.isSelected()) {
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to close the tab of this tourist?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("Warning! Unsaved data will be lost.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(
                            getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/warning16.png ")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = { "Close", "Cancel" };
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root,
                            "Confirm closing Tourist Card" + tourist.getName() + "" + tourist.getSurname(),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                    if (choice == JOptionPane.OK_OPTION) {
                        parent.closeCard(pEvent.getInternalFrame());
                    }
                } else {
                    parent.closeCard(pEvent.getInternalFrame());
                }
            }
        });
    }

    /**
     *
     * This method initializes the interface card for tourists.
     *
     * @Return void
     *
     */
    private void initialize() {
        jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        jContentPane.add(this.getToolbarCard(), BorderLayout.CENTER);
        jTabbedPane = new JTabbedPane();
        jTabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jTabbedPane.addTab("Tourist Information", frameIcon, getDataTouristForm(), null);
        jContentPane.add(jTabbedPane, BorderLayout.CENTER);
        setContentPane(jContentPane);
        loadDataForm();
    }

    /**
     *
     * This method loads the bean data provided tourist camps Of the form.
     *
     * @Return void
     *
     */
    private void loadDataForm() {
        setTitle("Profile Tourist -" + tourist.getName() + "" + tourist.getSurname());
        name.setText(tourist.getName());
        surname.setText(tourist.getSurname());
        Date dob = tourist.getDateOfBirth();
        day.setSelectedIndex(dob.getDate());
        month.setSelectedIndex(dob.getMonth());
        years.setSelectedIndex(dob.getYear());
        StringTokenizer tokenizer = new StringTokenizer(tourist.getStreet());
        String string = tokenizer.nextToken();
        address1.setSelectedItem(string);
        address2.setText(tourist.getStreet().substring(string.length()));
        placeOfBirth.setText(tourist.getCityNascita());
        phone.setText(tourist.getPhone());
        city.setText(tourist.getCityResidenza());
        password.setText(tourist.getPassword());
        province.setSelectedItem(tourist.getProvince());
        username.setText(tourist.getUserName());
        cap.setText(tourist.getCap());
        email.setText(tourist.getEmail());
        dataRegistration.setText(Data.toEstesa(tourist.getDataRegistration()));
    }

    /**
     *
     * This method shows or hides the suggestions relating to the form fields.
     *
     * @Return void
     *
     */
    private void showHideSuggestions() {
        Iterator<JLabel> s =

                suggestions.iterator();
        while (s.hasNext())

        {
            JLabel current = s.next();
            current.setVisible(current.isVisible() ? false : true);

        }
    }

    /**
     *
     * This method makes it or not editable form fields.
     *
     * @Return void
     *
     */
    private void activeDisactiveEdit() {
        Component[] components = dataTourist.getComponents();
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
    }

    /**
     * This method initializes the toolbar for the functionality of the card
     * Tourist.
     *
     * @Return javax.swing.JToolBar
     *
     */
    private JToolBar getToolbarCard() {
        if (toolbarscheda == null) {
            toolbarscheda = new JToolBar();
            toolbarscheda.setFloatable(false);
            toolbarscheda.add(getBtnModify());
            toolbarscheda.addSeparator();
            toolbarscheda.add(getBtnSave());
            toolbarscheda.addSeparator();
            toolbarscheda.add(getBtnReset());
            toolbarscheda.addSeparator();
        }
        return toolbarscheda;
    }

    /**
     * This method initializes the button for editing data.
     *
     * @Return javax.swing.JToggleButton
     *
     */
    private JToggleButton getBtnModify() {
        if (null == btnModify) {
            btnModify = new JToggleButton();
            btnModify.setText("Change Data");
            btnModify.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/ModifyTourist32.png ")));
            btnModify.setToolTipText("Enable or disable data modifytion tourists selected.");
            btnModify.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    showHideSuggestions();
                    activeDisactiveEdit();
                    btnSave.setVisible((btnModify.isSelected() ? true : false));
                    btnReset.setVisible((btnModify.isSelected() ? true : false));

                }

            });
        }
        return btnModify;
    }

    /**
     * This method initializes the button to save the changes Made to the data of
     * the tourist.
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnSave() {
        if (null == btnSave) {
            btnSave = new JButton();
            btnSave.setText("Save");
            btnSave.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa / gps / eTour / gui / operatoreagenzia / images / salva.png ")));
            btnSave.setToolTipText("Save changes to the tourist profile selected.");
            btnSave.setVisible(false);
            btnSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
// Construction of the dialog for confirmation of the change
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Updatang the tourist profile of" + tourist.getName() + ""
                            + tourist.getSurname() + "with" + "Data form?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("The previous data can not be more recovered.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(
                            getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/warning16.png ")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = { "Edit", "Cancel" };
// The dialog screen appears
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root,
                            "Commit Changes tourist figures", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(getClass().getResource(
                                    "/ unisa/gps/etour/gui/operatoreagenzia/images/ModifyTourist48.png")),
                            options, options[1]);
// If you chose to confirm the change
                    if (choice == JOptionPane.YES_OPTION) {
                        tourist.setName(name.getText());
                        tourist.setSurname(surname.getText());
                        tourist.setCap(cap.getText());
                        tourist.setCityNascita(placeOfBirth.getText());
                        tourist.setDateOfBirth(
                                new Date(years.getSelectedIndex(), month.getSelectedIndex(), day.getSelectedIndex()));
                        tourist.setCityResidenza(city.getText());
                        tourist.setUsername(username.getText());
                        tourist.setEmail(email.getText());
                        tourist.setPhone(phone.getText());
                        tourist.setStreet(address1.getSelectedItem().toString() + "" + address2.getText());
                        tourist.setProvince(province.getSelectedItem().toString());
                        String pass = "";
                        char[] password_ = password.getPassword();
                        for (int i = 0; i < password_.length; i++) {
                            pass += password_[i];
                        }
                        tourist.setPassword(pass);
                        loadDataForm();
                        activeDisactiveEdit();
                        btnSave.setVisible(false);
                        btnReset.setVisible(false);
                        btnModify.setSelected(false);
                        showHideSuggestions();
                        parent.updateTableModel(tourist);
                        JOptionPane.showInternalMessageDialog(jContentPane,
                                "The data of tourists have been updated successfully. ", "Modified Profile Tourist!",
                                JOptionPane.OK_OPTION, new ImageIcon(getClass()
                                        .getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/ok48.png ")));
                    }
                }
            });
        }
        return btnSave;
    }

    /**
     * This method initializes the button to reset the data of the tourist In the
     * form.
     *
     * @Return javax.swing.JButton
     */
    private JButton getBtnReset() {
        if (null == btnReset) {
            btnReset = new JButton();
            btnReset.setText("Reset");
            btnReset.setIcon(new ImageIcon(
                    getClass().getResource("/ unisa/gps/etour/gui/operatoreagenzia/images/Cancel32.png ")));
            btnReset.setToolTipText("Reload the selected tourist information.");
            btnReset.setVisible(false);
            btnReset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    loadDataForm();
                }
            });
        }
        return btnReset;
    }

    /**
     * This method initializes the form contentente data of the tourist.
     *
     * @Return javax.swing.JPanel
     *
     */
    private JPanel getDataTouristForm() {
        if (null == dataTourist) {
            dataTourist = new JPanel(null);
            dataTourist.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
// Creation Tips
            String[] txts = { "Name", "Name", "Date of Birth", "Place of Birth", "Phone", "Address", "City", "CPC",
                    "Province", "E-Mail", "Username", "Password", "Save" };

            for (int i = 0; i < help.length; i++) {
                JLabel nuova = new JLabel();
                nuova.setIcon(new ImageIcon(getClass().getResource("/ unisa/gps/etour/gui/images/Info16.png ")));
                nuova.setBounds(145, 8 + 30 * i, 24, 24);
                nuova.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                nuova.setToolTipText(help[i]);
                suggestions.add(nuova);
                dataTourist.add(nuova);

            }

            for (int i = 0; i < txts.length; i++) {
                JLabel nuova = new JLabel(txts[i], SwingConstants.RIGHT);
                nuova.setBounds(25, 10 + 30 * i, 120, 20);
                nuova.repaint();
                dataTourist.add(nuova, null);
            }
// Name
            name = new JTextField(12);
            name.setBounds(185, 10, 136, 20);
            name.setName("Name");
            dataTourist.add(name, null);

// Surname
            surname = new JTextField(12);
            surname.setBounds(185, 40, 136, 20);
            surname.setName("Name");
            dataTourist.add(name, null);

// Date of Birth
            day = new JComboBox();
            day.setBounds(185, 70, 40, 20);
            for (int i = 1; i <= 31; i++) {
                day.addItem(i);
            }
            month = new JComboBox();
            month.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent pEvent) {
                    int number = day.getItemCount();
                    switch (month.getSelectedIndex()) {
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        for (int i = number + 1; i <= 31; i++) {
                            day.addItem(i);
                        }
                        break;

                    case 1:
                        int year = (Integer) years.getSelectedItem();
                        boolean leap = ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
                        if (number != 28) {
                            for (int i = number - 1; i > 27; i--) {
                                day.removeItemAt(i);
                            }
                        }
                        if (leap && number != 29) {
                            day.addItem("29");
                        }
                        break;

                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        if (number == 31) {
                            day.removeItemAt(30);
                        } else {
                            for (int i = number + 1; i <= 30; i++) {
                                day.addItem(i);
                            }
                        }
                        break;
                    }
                }

            });
            month.setBounds(245, 70, 40, 20);
            for (int i = 1; i <= 12; i++) {
                month.addItem(i);
            }
            years = new JComboBox();
            years.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    if (month.getSelectedIndex() == 1) {
                        int year = (Integer) years.getSelectedItem();
                        boolean leap = ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
                        int number = day.getItemCount();
                        if (leap && number != 29) {
                            day.addItem("29");
                        } else if (leap && number == 29) {
                            day.removeItemAt(28);
                        }
                    }

                }

            });
            years.setBounds(305, 70, 80, 20);
            Date today = new Date();
            for (int i = 0; i <= today.getYear() - 14; i++) {
                years.addItem(1900 + i);
            }
            dataTourist.add(day, null);
            dataTourist.add(month, null);
            dataTourist.add(years, null);

// Place of Birth
            placeOfBirth = new JTextField(12);
            placeOfBirth.setBounds(185, 100, 136, 20);
            placeOfBirth.setName("Birth Place");
            dataTourist.add(placeOfBirth, null);

// Phone
            phone = new JTextField(12);
            phone.setBounds(185, 130, 136, 20);
            phone.setName("Phone");
            dataTourist.add(phone, null);

// Address
            address2 = new JTextField(12);
            address2.setBounds(270, 160, 136, 20);
            address1 = new JComboBox();
            address1.setSelectedIndex(-1);
            address1.setBounds(185, 160, 60, 20);
            dataTourist.add(address2, null);
            dataTourist.add(address1, null);

// City
            city = new JTextField(12);
            city.setBounds(185, 190, 136, 20);
            city.setName("City");
            dataTourist.add(city, null);

// CAP
            cap = new JTextField(8);
            cap.setBounds(185, 220, 92, 20);
            dataTourist.add(cap, null);

// State
            province = new JComboBox();
            province.setSelectedIndex(-1);
            province.setBounds(185, 250, 50, 20);
            dataTourist.add(province, null);

// E-Mail
            email = new JTextField();
            email.setBounds(185, 280, 200, 20);
            email.setName("E-Mail");
            dataTourist.add(email, null);

// Username
            username = new JTextField();
            username.setBounds(185, 310, 136, 20);
            username.setName("Username");
            dataTourist.add(username, null);

// Password
            password = new JPasswordField(12);
            password.setBounds(185, 340, 136, 20);
            password.setName("Password");
            dataTourist.add(password, null);

// Data entry
            dataRegistration = new JLabel();
            dataRegistration.setBounds(185, 370, 140, 20);
            dataTourist.add(dataRegistration, null);
        }
        return dataTourist;
    }

    /**
     * This method returns the id of the tourist who is viewing / Edit.
     *
     * @Return int - the id of the tourist.
     *
     */
    public int getId() {
        return tourist.getId();
    }
}