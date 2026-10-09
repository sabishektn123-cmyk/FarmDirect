package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import farmdirect.dao.ProductDAO;
import farmdirect.model.Crop;
import farmdirect.model.Farmer;
import farmdirect.model.FreshnessLevel;
import farmdirect.model.Product;
import farmdirect.service.ProductStore;

public class AddProductFrame extends JFrame {

    private Farmer farmer;

    private JTextField cropNameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField locationField;

    private JComboBox<FreshnessLevel> freshnessBox;

    public AddProductFrame(Farmer farmer) {

        this.farmer = farmer;

        // ==========================================
        // WINDOW SETTINGS
        // ==========================================

        setTitle("FarmDirect - Add Product");

        setSize(550, 550);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        // ==========================================
        // HEADER
        // ==========================================

        JLabel title =
                new JLabel(
                        "UPLOAD FARM PRODUCT",
                        JLabel.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(Color.WHITE);


        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(34, 139, 34)
        );

        header.add(
                title,
                BorderLayout.CENTER
        );


        // ==========================================
        // FORM PANEL
        // ==========================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                15
                        )
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        40,
                        20,
                        40
                )
        );


        // ==========================================
        // INPUT FIELDS
        // ==========================================

        cropNameField =
                new JTextField();

        categoryField =
                new JTextField();

        quantityField =
                new JTextField();

        priceField =
                new JTextField();


        // Farmer location automatically comes
        // from the logged-in farmer

        locationField =
                new JTextField(
                        farmer.getFarmLocation()
                );

        locationField.setEditable(false);


        // ==========================================
        // FRESHNESS
        // ==========================================

        freshnessBox =
                new JComboBox<>(
                        FreshnessLevel.values()
                );


        // ==========================================
        // ADD FORM COMPONENTS
        // ==========================================

        formPanel.add(
                new JLabel("Crop Name:")
        );

        formPanel.add(
                cropNameField
        );


        formPanel.add(
                new JLabel("Category:")
        );

        formPanel.add(
                categoryField
        );


        formPanel.add(
                new JLabel("Quantity (kg):")
        );

        formPanel.add(
                quantityField
        );


        formPanel.add(
                new JLabel("Price (₹/kg):")
        );

        formPanel.add(
                priceField
        );


        formPanel.add(
                new JLabel("Farm Location:")
        );

        formPanel.add(
                locationField
        );


        formPanel.add(
                new JLabel("Freshness:")
        );

        formPanel.add(
                freshnessBox
        );


        // ==========================================
        // UPLOAD BUTTON
        // ==========================================

        JButton uploadButton =
                new JButton(
                        "UPLOAD PRODUCT"
                );

        uploadButton.setBackground(
                new Color(34, 139, 34)
        );

        uploadButton.setForeground(
                Color.WHITE
        );

        uploadButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(
                uploadButton
        );


        // ==========================================
        // ADD TO FRAME
        // ==========================================

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


        // ==========================================
        // BUTTON EVENT
        // ==========================================

        uploadButton.addActionListener(
                e -> saveProduct()
        );
    }


    // =====================================================
    // SAVE PRODUCT
    // =====================================================

    private void saveProduct() {

        // ==========================================
        // READ INPUTS
        // ==========================================

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

        String priceText =
                priceField
                        .getText()
                        .trim();


        // ==========================================
        // VALIDATE EMPTY FIELDS
        // ==========================================

        if (
                cropName.isEmpty()
                || category.isEmpty()
                || quantityText.isEmpty()
                || priceText.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please fill all required fields.",

                    "Product Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // ==========================================
        // CONVERT NUMBERS
        // ==========================================

        double quantity;

        double price;


        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,

                    "Quantity and price must be valid numbers.",

                    "Input Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // ==========================================
        // VALIDATE QUANTITY
        // ==========================================

        if (quantity <= 0) {

            JOptionPane.showMessageDialog(
                    this,

                    "Quantity must be greater than zero.",

                    "Quantity Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // ==========================================
        // VALIDATE PRICE
        // ==========================================

        if (price <= 0) {

            JOptionPane.showMessageDialog(
                    this,

                    "Price must be greater than zero.",

                    "Price Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // ==========================================
        // CREATE CROP
        // ==========================================

        Crop crop =
                new Crop(
                        0,
                        cropName,
                        category
                );


        // ==========================================
        // GET FRESHNESS
        // ==========================================

        FreshnessLevel freshness =
                (FreshnessLevel)
                        freshnessBox
                                .getSelectedItem();


        // ==========================================
        // CREATE PRODUCT
        // ==========================================

        Product product =
                new Product(
                        0,
                        crop,
                        farmer,
                        quantity,
                        price,
                        freshness
                );


        // ==========================================
        // SAVE TO DATABASE
        // ==========================================

        ProductDAO productDAO =
                new ProductDAO();


        int productId =
                productDAO.addProduct(
                        product
                );


        // ==========================================
        // CHECK DATABASE RESULT
        // ==========================================

        if (productId == -1) {

            JOptionPane.showMessageDialog(
                    this,

                    "Product could not be saved to the database.",

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // ==========================================
        // CREATE PRODUCT WITH REAL DATABASE ID
        // ==========================================

        product =
                new Product(
                        productId,
                        crop,
                        farmer,
                        quantity,
                        price,
                        freshness
                );


        // ==========================================
        // ADD TO MEMORY STORE
        // ==========================================

        ProductStore.addProduct(
                product
        );


        // ==========================================
        // CALCULATE TOTAL VALUE
        // ==========================================

        double totalValue =
                product.calculateTotalValue();


        // ==========================================
        // SUCCESS MESSAGE
        // ==========================================

        JOptionPane.showMessageDialog(
                this,

                "Product uploaded successfully!\n\n"

                + "Product ID: "
                + productId

                + "\nFarmer: "
                + farmer.getName()

                + "\nCrop: "
                + cropName

                + "\nCategory: "
                + category

                + "\nQuantity: "
                + quantity
                + " kg"

                + "\nPrice: ₹"
                + String.format(
                        "%.2f",
                        price
                )
                + "/kg"

                + "\nFreshness: "
                + freshness

                + "\nFarm Location: "
                + farmer.getFarmLocation()

                + "\nTotal Value: ₹"
                + String.format(
                        "%.2f",
                        totalValue
                ),

                "FarmDirect",

                JOptionPane.INFORMATION_MESSAGE
        );


        // ==========================================
        // CLEAR FORM
        // ==========================================

        cropNameField.setText("");

        categoryField.setText("");

        quantityField.setText("");

        priceField.setText("");

        freshnessBox.setSelectedIndex(0);
    }
}