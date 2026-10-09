
package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.ListSelectionModel;

import farmdirect.dao.BulkSaleDAO;
import farmdirect.dao.OrderDAO;
import farmdirect.model.BulkSaleRecord;
import farmdirect.model.Customer;
import farmdirect.model.Order;

public class MyOrdersFrame extends JFrame {

    private final Customer customer;

    private final DefaultListModel<String> orderModel =
            new DefaultListModel<>();

    private JList<String> orderList;

    private JLabel statusLabel;
    private JLabel trackingLabel;

    private final OrderDAO orderDAO = new OrderDAO();
    private final BulkSaleDAO bulkSaleDAO = new BulkSaleDAO();

    private List<Order> regularOrders;
    private List<BulkSaleRecord> bulkSales;

    private static final String REGULAR = "REGULAR";
    private static final String BULK = "BULK";

    private String selectedType;
    private int selectedId;

    public MyOrdersFrame(Customer customer) {

        this.customer = customer;

        setTitle("FarmDirect - My Orders");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        JLabel title = new JLabel(
                "MY ORDERS & TRACKING",
                JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 139, 34));
        header.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );
        header.add(title, BorderLayout.CENTER);

        orderList = new JList<>(orderModel);
        orderList.setFont(new Font("Arial", Font.PLAIN, 14));
        orderList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(orderList);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Your Orders and Accepted Bulk Purchases"
                )
        );

        statusLabel = new JLabel("Status: Select an order");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        trackingLabel = new JLabel("Tracking: -");
        trackingLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel trackingPanel = new JPanel(
                new java.awt.GridLayout(2, 1, 5, 5)
        );

        trackingPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );

        trackingPanel.add(statusLabel);
        trackingPanel.add(trackingLabel);

        JButton refreshButton = new JButton("REFRESH ORDERS");
        JButton trackingButton = new JButton("VIEW TRACKING");
        JButton closeButton = new JButton("CLOSE");

        refreshButton.setBackground(new Color(46, 125, 50));
        refreshButton.setForeground(Color.WHITE);

        trackingButton.setBackground(new Color(30, 136, 229));
        trackingButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(trackingButton);
        buttonPanel.add(closeButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(trackingPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        orderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedOrder();
            }
        });

        refreshButton.addActionListener(e -> loadOrders());
        trackingButton.addActionListener(e -> showTracking());
        closeButton.addActionListener(e -> dispose());

        loadOrders();
    }

    private void loadOrders() {

        int previousId = selectedId;
        String previousType = selectedType;

        orderModel.clear();

        regularOrders =
                orderDAO.getOrdersByCustomer(customer.getUserId());

        bulkSales =
                bulkSaleDAO.getSalesByCustomer(customer.getUserId());

        // Add regular purchases.
        for (Order order : regularOrders) {
            orderModel.addElement(
                    "[REGULAR] " + order.toString()
            );
        }

        // Add accepted bulk purchases.
        for (BulkSaleRecord sale : bulkSales) {
            orderModel.addElement(
                    "[BULK SALE] " + sale.toString()
            );
        }

        if (orderModel.isEmpty()) {

            selectedType = null;
            selectedId = -1;

            statusLabel.setText("Status: No orders found");
            trackingLabel.setText(
                    "Tracking: Your purchases will appear here."
            );

            return;
        }

        // Restore the previous selection if it still exists.
        int selectedIndex = 0;

        if (previousType != null) {

            if (REGULAR.equals(previousType)) {

                for (int i = 0; i < regularOrders.size(); i++) {
                    if (regularOrders.get(i).getOrderId()
                            == previousId) {
                        selectedIndex = i;
                        break;
                    }
                }

            } else if (BULK.equals(previousType)) {

                for (int i = 0; i < bulkSales.size(); i++) {
                    if (bulkSales.get(i).getSaleId()
                            == previousId) {
                        selectedIndex = regularOrders.size() + i;
                        break;
                    }
                }
            }
        }

        orderList.setSelectedIndex(selectedIndex);
        displaySelectedOrder();
    }

    private void displaySelectedOrder() {

        int index = orderList.getSelectedIndex();

        if (index < 0) {
            return;
        }

        if (index < regularOrders.size()) {

            Order order = regularOrders.get(index);

            selectedType = REGULAR;
            selectedId = order.getOrderId();

            statusLabel.setText(
                    "Order ID: " + order.getOrderId()
                    + " | Current Status: " + order.getStatus()
            );

            trackingLabel.setText(
                    "Tracking: "
                    + trackingMessage(order.getStatus().name())
            );

        } else {

            int bulkIndex = index - regularOrders.size();

            if (bulkIndex < 0 || bulkIndex >= bulkSales.size()) {
                return;
            }

            BulkSaleRecord sale = bulkSales.get(bulkIndex);

            selectedType = BULK;
            selectedId = sale.getSaleId();

            statusLabel.setText(
                    "Bulk Sale ID: " + sale.getSaleId()
                    + " | Status: CONFIRMED"
            );

            trackingLabel.setText(
                    "Tracking: Farmer accepted your winning bid."
            );
        }
    }

    private void showTracking() {

        int index = orderList.getSelectedIndex();

        if (index < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an order first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Refresh the data from SQLite before showing details.
        loadOrders();

        index = orderList.getSelectedIndex();

        if (index < 0) {
            return;
        }

        if (index < regularOrders.size()) {

            Order order = regularOrders.get(index);

            JOptionPane.showMessageDialog(
                    this,
                    "ORDER TRACKING\n\n"
                    + "Order ID: " + order.getOrderId()
                    + "\nProduct: "
                    + order.getProduct().getCrop().getCropName()
                    + "\nFarmer: "
                    + order.getProduct().getFarmer().getName()
                    + "\nQuantity: " + order.getQuantity() + " kg"
                    + "\nPrice: ₹"
                    + String.format("%.2f", order.getSalePricePerKg())
                    + "/kg\nTotal: ₹"
                    + String.format("%.2f", order.getTotalAmount())
                    + "\nStatus: " + order.getStatus()
                    + "\n\n"
                    + trackingMessage(order.getStatus().name()),
                    "Order Tracking",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            int bulkIndex = index - regularOrders.size();

            if (bulkIndex < 0 || bulkIndex >= bulkSales.size()) {
                return;
            }

            BulkSaleRecord sale = bulkSales.get(bulkIndex);

            JOptionPane.showMessageDialog(
                    this,
                    "BULK PURCHASE DETAILS\n\n"
                    + "Sale ID: " + sale.getSaleId()
                    + "\nListing ID: " + sale.getListingId()
                    + "\nProduct: " + sale.getCropName()
                    + "\nFarmer: " + sale.getFarmerName()
                    + "\nQuantity: " + sale.getQuantity() + " kg"
                    + "\nAccepted price: ₹"
                    + String.format("%.2f", sale.getPricePerKg())
                    + "/kg\nTotal amount: ₹"
                    + String.format("%.2f", sale.getTotalAmount())
                    + "\nSale date: " + sale.getSaleDate()
                    + "\nStatus: CONFIRMED"
                    + "\n\nThe farmer has accepted your winning bid.",
                    "Bulk Purchase Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private String trackingMessage(String status) {

        switch (status) {
            case "PENDING":
                return "Waiting for the farmer to confirm your order.";
            case "CONFIRMED":
                return "The farmer has confirmed your order.";
            case "PACKED":
                return "Your products have been packed.";
            case "SHIPPED":
                return "Your order has been dispatched.";
            case "PICKUP":
                return "Your order is ready for pickup.";
            case "DELIVERED":
                return "Your order has been delivered.";
            case "CANCELLED":
                return "This order has been cancelled.";
            default:
                return "Please refresh to check the latest status.";
        }
    }
}
