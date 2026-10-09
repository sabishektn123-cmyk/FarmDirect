package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import farmdirect.dao.ProductDAO;
import farmdirect.model.Farmer;
import farmdirect.model.Product;

public class MyProductsFrame extends JFrame {

    private Farmer farmer;

    private DefaultListModel<Product> productModel;

    private JList<Product> productList;

    private JLabel summaryLabel;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public MyProductsFrame(Farmer farmer) {

        this.farmer = farmer;

        setTitle(
                "FarmDirect - My Products"
        );

        setSize(
                850,
                600
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
                        "MY FARM PRODUCTS",
                        JLabel.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
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
                        27,
                        94,
                        32
                )
        );

        header.add(
                title,
                BorderLayout.CENTER
        );


        // =================================================
        // PRODUCT LIST
        // =================================================

        productModel =
                new DefaultListModel<>();

        productList =
                new JList<>(
                        productModel
                );

        productList.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        productList
                );

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Products Uploaded By You"
                )
        );


        // =================================================
        // SUMMARY
        // =================================================

        summaryLabel =
                new JLabel(
                        "Products: 0"
                );

        summaryLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );


        // =================================================
        // BUTTONS
        // =================================================

        JButton refreshButton =
                new JButton(
                        "REFRESH"
                );

        JButton detailsButton =
                new JButton(
                        "VIEW DETAILS"
                );

        JButton addProductButton =
                new JButton(
                        "ADD PRODUCT"
                );


        refreshButton.setBackground(
                new Color(
                        46,
                        125,
                        50
                )
        );

        refreshButton.setForeground(
                Color.WHITE
        );


        addProductButton.setBackground(
                new Color(
                        27,
                        94,
                        32
                )
        );

        addProductButton.setForeground(
                Color.WHITE
        );


        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                10
                        )
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        30,
                        10,
                        30
                )
        );

        buttonPanel.add(
                refreshButton
        );

        buttonPanel.add(
                detailsButton
        );

        buttonPanel.add(
                addProductButton
        );


        // =================================================
        // BOTTOM PANEL
        // =================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.add(
                summaryLabel,
                BorderLayout.NORTH
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // ADD TO FRAME
        // =================================================

        add(
                header,
                BorderLayout.NORTH
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // LOAD PRODUCTS
        // =================================================

        loadProducts();


        // =================================================
        // REFRESH
        // =================================================

        refreshButton.addActionListener(
                e -> loadProducts()
        );


        // =================================================
        // DETAILS
        // =================================================

        detailsButton.addActionListener(
                e -> showProductDetails()
        );


        // =================================================
        // ADD PRODUCT
        // =================================================

        addProductButton.addActionListener(
                e -> {

                    new AddProductFrame(
                            farmer
                    ).setVisible(true);
                }
        );
    }


    // =====================================================
    // LOAD PRODUCTS
    // =====================================================

    private void loadProducts() {

        productModel.clear();


        ProductDAO productDAO =
                new ProductDAO();


        List<Product> products =
                productDAO.getProductsByFarmer(
                        farmer.getUserId()
                );


        for (
                Product product :
                products
        ) {

            productModel.addElement(
                    product
            );
        }


        summaryLabel.setText(
                "Products: "
                + products.size()
        );


        if (products.isEmpty()) {

            summaryLabel.setText(
                    "Products: 0 | No products uploaded yet"
            );
        }
    }


    // =====================================================
    // SHOW PRODUCT DETAILS
    // =====================================================

    private void showProductDetails() {

        Product selectedProduct =
                productList.getSelectedValue();


        if (selectedProduct == null) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please select a product first.",

                    "Product Required",

                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        double totalValue =
                selectedProduct
                        .calculateTotalValue();


        String message =
                "PRODUCT DETAILS\n\n"

                + "Product ID: "
                + selectedProduct.getProductId()

                + "\nCrop: "
                + selectedProduct
                        .getCrop()
                        .getCropName()

                + "\nCategory: "
                + selectedProduct
                        .getCrop()
                        .getCategory()

                + "\nQuantity: "
                + selectedProduct
                        .getQuantity()
                + " kg"

                + "\nPrice: ₹"
                + String.format(
                        "%.2f",
                        selectedProduct
                                .getPricePerKg()
                )
                + "/kg"

                + "\nFreshness: "
                + selectedProduct
                        .getFreshness()

                + "\nFarm Location: "
                + farmer.getFarmLocation()

                + "\nTotal Value: ₹"
                + String.format(
                        "%.2f",
                        totalValue
                );


        JOptionPane.showMessageDialog(
                this,

                message,

                "FarmDirect - Product Details",

                JOptionPane.INFORMATION_MESSAGE
        );
    }
}