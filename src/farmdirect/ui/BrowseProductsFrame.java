
package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import farmdirect.dao.OrderDAO;
import farmdirect.dao.ProductDAO;
import farmdirect.model.Customer;
import farmdirect.model.Order;
import farmdirect.model.Product;
import farmdirect.service.FairPriceService;
import farmdirect.service.OrderStore;
import farmdirect.service.ProductStore;

public class BrowseProductsFrame extends JFrame {

    private final Customer customer;

    private JList<Product> productList;
    private DefaultListModel<Product> listModel;
    private JTextField quantityField;
    private JLabel fairPriceLabel;

    private List<Product> products;

    public BrowseProductsFrame(Customer customer) {

        this.customer = customer;

        setTitle("FarmDirect - Browse Products");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // =================================================
        // HEADER
        // =================================================

        JLabel title = new JLabel(
                "FRESH FARM PRODUCTS",
                JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 139, 34));
        header.add(title, BorderLayout.CENTER);

        // =================================================
        // PRODUCT LIST
        // =================================================

        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);

        productList.setFont(new Font("Arial", Font.PLAIN, 15));
        productList.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        // Display the crop's fair price instead of the farmer's
        // individual quotation.
        productList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                JLabel label = (JLabel)
                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                if (value instanceof Product) {

                    Product product = (Product) value;

                    double fairPrice =
                            FairPriceService.calculateFairPrice(
                                    product.getCrop().getCropName()
                            );

                    if (fairPrice <= 0) {
                        fairPrice = product.getPricePerKg();
                    }

                    label.setText(
                            product.getCrop().getCropName()
                            + " | Farmer: "
                            + product.getFarmer().getName()
                            + " | Quantity: "
                            + String.format(
                                    "%.2f",
                                    product.getQuantity()
                            )
                            + " kg | Fixed Fair Price: ₹"
                            + String.format("%.2f", fairPrice)
                            + "/kg | Freshness: "
                            + product.getFreshness()
                    );
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(productList);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Available Products"
                )
        );

        // =================================================
        // PURCHASE PANEL
        // =================================================

        JPanel purchasePanel = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        purchasePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        quantityField = new JTextField();

        fairPriceLabel = new JLabel(
                "Fixed Fair Price: Select a product"
        );

        fairPriceLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        JButton priceButton =
                new JButton("CHECK FAIR PRICE");

        JButton purchaseButton =
                new JButton("PURCHASE");

        priceButton.setToolTipText(
                "Calculate the average quotation for the selected crop"
        );

        purchaseButton.setBackground(
                new Color(34, 139, 34)
        );

        purchaseButton.setForeground(Color.WHITE);

        purchasePanel.add(new JLabel("Quantity (kg):"));
        purchasePanel.add(quantityField);

        purchasePanel.add(new JLabel("Pricing:"));
        purchasePanel.add(fairPriceLabel);

        purchasePanel.add(priceButton);
        purchasePanel.add(purchaseButton);

        // =================================================
        // ADD COMPONENTS
        // =================================================

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(purchasePanel, BorderLayout.SOUTH);

        // =================================================
        // LOAD PRODUCTS
        // =================================================

        loadProducts();

        // =================================================
        // PRODUCT SELECTION
        // =================================================

        productList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                Product selected = productList.getSelectedValue();

                if (selected != null) {
                    updateFairPrice(selected);
                }
            }
        });

        // =================================================
        // CHECK FAIR PRICE
        // =================================================

        priceButton.addActionListener(e -> {

            Product selected = productList.getSelectedValue();

            if (selected == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a product first.",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            updateFairPrice(selected);
        });

        // =================================================
        // PURCHASE
        // =================================================

        purchaseButton.addActionListener(
                e -> purchaseProduct()
        );
    }

    // =====================================================
    // LOAD PRODUCTS
    // =====================================================

    private void loadProducts() {

        listModel.clear();

        ProductStore.loadFromDatabase();

        products = ProductStore.getProducts();

        for (Product product : products) {
            listModel.addElement(product);
        }

        productList.repaint();

        if (products.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No products are currently available.",
                    "FarmDirect",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // =====================================================
    // CALCULATE AND DISPLAY FIXED FAIR PRICE
    // =====================================================

    private double getFairPrice(Product product) {

        double fairPrice =
                FairPriceService.calculateFairPrice(
                        product.getCrop().getCropName()
                );

        if (fairPrice <= 0) {
            fairPrice = product.getPricePerKg();
        }

        return fairPrice;
    }

    private void updateFairPrice(Product product) {

        double fairPrice = getFairPrice(product);

        fairPriceLabel.setText(
                "Fixed Fair Price: ₹"
                + String.format("%.2f", fairPrice)
                + "/kg"
        );
    }

    // =====================================================
    // PURCHASE PRODUCT
    // =====================================================

    private void purchaseProduct() {

        Product selectedProduct =
                productList.getSelectedValue();

        if (selectedProduct == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a product.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Read quantity
        String quantityText =
                quantityField.getText().trim();

        if (quantityText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the quantity.",
                    "Quantity Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric quantity.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!Double.isFinite(quantity) || quantity <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Quantity must be a positive number.",
                    "Quantity Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Validate stock
        if (quantity > selectedProduct.getQuantity()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only "
                    + selectedProduct.getQuantity()
                    + " kg is available.",
                    "Insufficient Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Calculate the same fair price shown in the UI.
        double fairPrice = getFairPrice(selectedProduct);

        if (!Double.isFinite(fairPrice) || fairPrice <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "A valid fair price could not be calculated.",
                    "Pricing Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        double totalAmount = quantity * fairPrice;

        // Confirm purchase before saving.
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Confirm your purchase?\n\n"
                + "Crop: "
                + selectedProduct.getCrop().getCropName()
                + "\nFarmer: "
                + selectedProduct.getFarmer().getName()
                + "\nQuantity: "
                + quantity + " kg"
                + "\nFixed Fair Price: ₹"
                + String.format("%.2f", fairPrice)
                + "/kg\nTotal: ₹"
                + String.format("%.2f", totalAmount),
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // Create order using the fair price, not the farmer's
        // individual quoted price.
        Order order = new Order(
                0,
                customer,
                selectedProduct,
                quantity,
                fairPrice
        );

        OrderDAO orderDAO = new OrderDAO();

        boolean databaseSuccess = orderDAO.addOrder(order);

        if (!databaseSuccess) {

            JOptionPane.showMessageDialog(
                    this,
                    "Order could not be saved to the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Reduce stock in SQLite.
        double remainingQuantity =
                selectedProduct.getQuantity() - quantity;

        ProductDAO productDAO = new ProductDAO();

        boolean stockUpdated = productDAO.updateQuantity(
                selectedProduct.getProductId(),
                remainingQuantity
        );

        if (!stockUpdated) {

            // The order has already been saved. Do not claim
            // that the entire transaction succeeded.
            JOptionPane.showMessageDialog(
                    this,
                    "The order was saved, but stock could not be "
                    + "updated. Please check the inventory before "
                    + "accepting another purchase.",
                    "Stock Update Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Update in-memory data only after database stock succeeds.
        selectedProduct.setQuantity(remainingQuantity);

        OrderStore.addOrder(order);

        JOptionPane.showMessageDialog(
                this,
                "Purchase successful!\n\n"
                + "Customer: " + customer.getName()
                + "\nProduct: "
                + selectedProduct.getCrop().getCropName()
                + "\nFarmer: "
                + selectedProduct.getFarmer().getName()
                + "\nQuantity: " + quantity + " kg"
                + "\nFixed Fair Price: ₹"
                + String.format("%.2f", fairPrice)
                + "/kg\nTotal Amount: ₹"
                + String.format("%.2f", totalAmount)
                + "\nStatus: PENDING",
                "FarmDirect",
                JOptionPane.INFORMATION_MESSAGE
        );

        quantityField.setText("");

        // Reload current inventory from SQLite.
        loadProducts();
    }
}
