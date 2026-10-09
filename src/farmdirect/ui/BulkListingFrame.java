package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import farmdirect.dao.BulkListingDAO;
import farmdirect.model.BulkListing;
import farmdirect.model.Crop;
import farmdirect.model.Farmer;
import farmdirect.service.BulkListingStore;

public class BulkListingFrame extends JFrame {

    private Farmer farmer;

    private JTextField cropNameField;

    private JTextField categoryField;

    private JTextField quantityField;

    private JTextField minimumPriceField;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BulkListingFrame(
            Farmer farmer) {

        this.farmer = farmer;


        setTitle(
                "FarmDirect - Bulk Sale"
        );

        setSize(
                550,
                500
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        // =================================================
        // HEADER
        // =================================================

        JLabel title =
                new JLabel(
                        "CREATE BULK SALE",
                        JLabel.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(
                Color.WHITE
        );


        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(
                        34,
                        139,
                        34
                )
        );

        header.add(
                title,
                BorderLayout.CENTER
        );


        // =================================================
        // FORM
        // =================================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                15
                        )
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        40,
                        30,
                        40
                )
        );


        cropNameField =
                new JTextField();

        categoryField =
                new JTextField();

        quantityField =
                new JTextField();

        minimumPriceField =
                new JTextField();


        formPanel.add(
                new JLabel(
                        "Crop Name:"
                )
        );

        formPanel.add(
                cropNameField
        );


        formPanel.add(
                new JLabel(
                        "Category:"
                )
        );

        formPanel.add(
                categoryField
        );


        formPanel.add(
                new JLabel(
                        "Quantity (kg):"
                )
        );

        formPanel.add(
                quantityField
        );


        formPanel.add(
                new JLabel(
                        "Minimum Price (₹/kg):"
                )
        );

        formPanel.add(
                minimumPriceField
        );


        // =================================================
        // BUTTON
        // =================================================

        JButton createButton =
                new JButton(
                        "CREATE BULK LISTING"
                );

        createButton.setBackground(
                new Color(
                        34,
                        139,
                        34
                )
        );

        createButton.setForeground(
                Color.WHITE
        );


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(
                createButton
        );


        // =================================================
        // ADD
        // =================================================

        add(
                header,
                BorderLayout.NORTH
        );

        add(
                formPanel,
                BorderLayout.CENTER
        );

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        createButton.addActionListener(
                e -> createListing()
        );
    }


    // =====================================================
    // CREATE LISTING
    // =====================================================

    private void createListing() {

        String cropName =
                cropNameField
                        .getText()
                        .trim();

        String category =
                categoryField
                        .getText()
                        .trim();

        String quantityText =
                quantityField
                        .getText()
                        .trim();

        String minimumPriceText =
                minimumPriceField
                        .getText()
                        .trim();


        // =================================================
        // VALIDATION
        // =================================================

        if (
                cropName.isEmpty()
                || category.isEmpty()
                || quantityText.isEmpty()
                || minimumPriceText.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please fill all fields.",

                    "Bulk Sale",

                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        double quantity;

        double minimumPrice;


        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

            minimumPrice =
                    Double.parseDouble(
                            minimumPriceText
                    );

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,

                    "Quantity and price must be numbers.",

                    "Input Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (
                quantity <= 0
                || minimumPrice <= 0
        ) {

            JOptionPane.showMessageDialog(
                    this,

                    "Quantity and minimum price must be greater than zero.",

                    "Input Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =================================================
        // CREATE CROP
        // =================================================

        Crop crop =
                new Crop(
                        0,
                        cropName,
                        category
                );


        // =================================================
        // CREATE LISTING
        // =================================================

        BulkListing listing =
                new BulkListing(
                        0,
                        farmer,
                        crop,
                        quantity,
                        minimumPrice
                );


        // =================================================
        // SAVE DATABASE
        // =================================================

        BulkListingDAO dao =
                new BulkListingDAO();


        int listingId =
                dao.addListing(
                        listing
                );


        if (listingId == -1) {

            JOptionPane.showMessageDialog(
                    this,

                    "Bulk listing could not be saved.",

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =================================================
        // CREATE WITH REAL ID
        // =================================================

        listing =
                new BulkListing(
                        listingId,
                        farmer,
                        crop,
                        quantity,
                        minimumPrice
                );


        // =================================================
        // STORE IN MEMORY
        // =================================================

        BulkListingStore.addListing(
                listing
        );


        // =================================================
        // SUCCESS
        // =================================================

        JOptionPane.showMessageDialog(
                this,

                "Bulk listing created successfully!\n\n"

                + "Listing ID: "
                + listingId

                + "\nCrop: "
                + cropName

                + "\nQuantity: "
                + quantity
                + " kg"

                + "\nMinimum Price: ₹"
                + String.format(
                        "%.2f",
                        minimumPrice
                )
                + "/kg",

                "FarmDirect",

                JOptionPane.INFORMATION_MESSAGE
        );


        // =================================================
        // CLEAR
        // =================================================

        cropNameField.setText("");

        categoryField.setText("");

        quantityField.setText("");

        minimumPriceField.setText("");
    }
}