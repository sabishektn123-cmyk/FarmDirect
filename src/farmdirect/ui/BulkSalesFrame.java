package farmdirect.ui;

import java.awt.BorderLayout;
import farmdirect.service.BulkListingStore;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import farmdirect.model.BulkListing;
import farmdirect.model.Crop;
import farmdirect.model.Farmer;

public class BulkSalesFrame extends JFrame {

    private JTextField cropNameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField minimumPriceField;

    private Farmer farmer;

    public BulkSalesFrame(Farmer farmer) {

        this.farmer = farmer;

        setTitle("FarmDirect - Bulk Sales");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ==========================
        // MAIN PANEL
        // ==========================

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(232, 245, 233)
        );

        // ==========================
        // HEADER
        // ==========================

        JPanel header = new JPanel();

        header.setBackground(
                new Color(27, 94, 32)
        );

        JLabel title =
                new JLabel("📦 Bulk Sales Listing");

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        header.add(title);

        // ==========================
        // FORM
        // ==========================

        JPanel formPanel =
                new JPanel(new GridBagLayout());

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 40, 30, 40
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Crop Name
        JLabel cropLabel =
                new JLabel("Crop Name:");

        cropNameField =
                new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                cropLabel,
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                cropNameField,
                gbc
        );

        // Category
        JLabel categoryLabel =
                new JLabel("Category:");

        categoryField =
                new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                categoryLabel,
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                categoryField,
                gbc
        );

        // Quantity
        JLabel quantityLabel =
                new JLabel("Bulk Quantity (kg):");

        quantityField =
                new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                quantityLabel,
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                quantityField,
                gbc
        );

        // Minimum Price
        JLabel minimumPriceLabel =
                new JLabel("Minimum Price/kg:");

        minimumPriceField =
                new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
                minimumPriceLabel,
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                minimumPriceField,
                gbc
        );

        // ==========================
        // CREATE LISTING BUTTON
        // ==========================

        JButton createButton =
                new JButton("CREATE BULK LISTING");

        createButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        createButton.setForeground(
                Color.WHITE
        );

        createButton.setBackground(
                new Color(46, 125, 50)
        );

        gbc.gridx = 0;
        gbc.gridy = 4;

        gbc.gridwidth = 2;

        formPanel.add(
                createButton,
                gbc
        );

        // ==========================
        // EVENT
        // ==========================

        createButton.addActionListener(
                e -> createListing()
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);
    }

    // ==========================
    // CREATE LISTING
    // ==========================

    private void createListing() {

        try {

            String cropName =
                    cropNameField
                            .getText()
                            .trim();

            String category =
                    categoryField
                            .getText()
                            .trim();

            double quantity =
                    Double.parseDouble(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            double minimumPrice =
                    Double.parseDouble(
                            minimumPriceField
                                    .getText()
                                    .trim()
                    );

            // Validation

            if (cropName.isEmpty()
                    || category.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (quantity <= 0
                    || minimumPrice <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity and minimum price "
                        + "must be greater than 0.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Create Crop
            Crop crop =
                    new Crop(
                            1,
                            cropName,
                            category
                    );

            // Create Bulk Listing
            BulkListing listing =
                    new BulkListing(
                            1,
                            farmer,
                            crop,
                            quantity,
                            minimumPrice
                    );
            BulkListingStore.addListing(listing);

            JOptionPane.showMessageDialog(
                    this,
                    "Bulk Listing Created!\n\n"
                    + "Crop: "
                    + cropName
                    + "\nQuantity: "
                    + quantity
                    + " kg"
                    + "\nMinimum Price: ₹"
                    + minimumPrice
                    + "/kg"
                    + "\nFarmer: "
                    + farmer.getName(),
                    "FarmDirect",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}