package unisa.gps.etour.gui.operatoragency;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

/**
 * This class realizes the panel for the dialog for entering a New banner or a
 * banner for replacing selected.
 *
 */
public class BannerDialog extends JPanel {
    private final static Dimension bannerSize = new Dimension(140, 40);
    private JLabel preview = null;
    private JButton btnLoad = null;
    private JPanel that;

    /**
     * This is the default constructor.
     */
    public BannerDialog() {
        super(null);
        that = this;
        setPreferredSize(new Dimension(420, 160));
        preview = new JLabel();
        preview.setBounds(new Rectangle(40, 20, 250, 60));
        preview.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Preview Banner ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
        btnLoad = new JButton();
        btnLoad.setBounds(new Rectangle(320, 30, 50, 40));
        btnLoad.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ApriFile.png ")));
        btnLoad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                File f = openFileDialog();

                if (f == null) {
                    return;
                }

                BufferedImage img = null;
                try {
                    img = ImageIO.read(f);
                    if (img.getWidth() > bannerSize.width || img.getHeight() > bannerSize.height) {
                        img = img.getSubimage(0, 0, bannerSize.width, bannerSize.height);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ImageIcon n = new ImageIcon(img);
                preview.setIcon(n);
                preview.repaint();
            }
        });

        JLabel txtCaution = new JLabel("Warning!");
        txtCaution.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png ")));
        txtCaution.setBounds(10, 85, 100, 30);
        JLabel txtTest = new JLabel("<html> <ul> <li> image for the banner can not exceed <b> <font color=\"red\">"
                + bannerSize.width + "X" + bannerSize.height + "</ Font> </ b> pixels."
                + "<li> Images of magnitude larger will be resized. </ Ul> </ html>");
        txtTest.setBounds(0, 90, 420, 80);
        add(txtTest, null);
        add(txtCaution, null);
        add(preview, null);
        add(btnLoad, null);

    }

    /**
     * This method initializes the image contained in the preview.
     *
     * @Param pBanner ImageIcon - an image of a banner.
     */
    public void setSelectedBanner(ImageIcon pBanner) {
        preview.setIcon(pBanner);
    }

    /**
     * This method returns the image contained in the preview.
     *
     * @Return ImageIcon - the image of the banner.
     */
    public ImageIcon getSelectedBanner() {
        return (ImageIcon) preview.getIcon();
    }

    /**
     * This method opens the dialog for selecting a file from disk.
     *
     * @Return
     *         <ul>
     *         <li>Files - the selected file.
     *         <li>Null - if you have not selected any files. </ Ul>
     */
    private File openFileDialog() {
        JFileChooser openFile = new JFileChooser();
        openFile.setDialogTitle("Select a new image");
        openFile.setAcceptAllFileFilterUsed(false);
        openFile.setMultiSelectionEnabled(false);

// File Filter for the window to open the file.
        openFile.setFileFilter(new FileFilter() {
            public boolean accept(File arg0) {
                if (arg0.isDirectory())
                    return true;
                String name = arg0.getName().toLowerCase();
                if (name.endsWith("jpg") || name.endsWith("gif") || name.endsWith("png"))
                    return true;
                else
                    return false;
            }

            public String getDescription() {
                return "Images (*. PNG, *. GIF, *. JPG)";
            }

        });
        int returnVal = openFile.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return openFile.getSelectedFile();
        } else
            return null;

    }

}