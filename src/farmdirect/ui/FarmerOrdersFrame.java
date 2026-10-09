package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import farmdirect.dao.OrderDAO;
import farmdirect.model.Farmer;
import farmdirect.model.Order;
import farmdirect.model.OrderStatus;

public class FarmerOrdersFrame extends JFrame {

    private Farmer farmer;

    private DefaultListModel<Order> orderModel;

    private JList<Order> orderList;

    private JComboBox<OrderStatus> statusBox;

    private JLabel summaryLabel;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public FarmerOrdersFrame(Farmer farmer) {

        this.farmer = farmer;

        setTitle(
                "FarmDirect - Farmer Orders"
        );

        setSize(
                900,
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
                        "CUSTOMER ORDERS",
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
        // ORDER LIST
        // =================================================

        orderModel =
                new DefaultListModel<>();

        orderList =
                new JList<>(
                        orderModel
                );

        orderList.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        orderList
                );

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Orders For Your Products"
                )
        );


        // =================================================
        // SUMMARY
        // =================================================

        summaryLabel =
                new JLabel(
                        "Orders: 0"
                );

        summaryLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );


        // =================================================
        // STATUS PANEL
        // =================================================

        JPanel statusPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                10
                        )
                );

        statusPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        30,
                        10,
                        30
                )
        );


        statusBox =
                new JComboBox<>(
                        OrderStatus.values()
                );


        JButton updateButton =
                new JButton(
                        "UPDATE STATUS"
                );

        JButton refreshButton =
                new JButton(
                        "REFRESH"
                );


        updateButton.setBackground(
                new Color(
                        46,
                        125,
                        50
                )
        );

        updateButton.setForeground(
                Color.WHITE
        );


        statusPanel.add(
                new JLabel(
                        "New Status:"
                )
        );

        statusPanel.add(
                statusBox
        );

        statusPanel.add(
                updateButton
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
                statusPanel,
                BorderLayout.CENTER
        );


        JPanel refreshPanel =
                new JPanel();

        refreshPanel.add(
                refreshButton
        );

        bottomPanel.add(
                refreshPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // ADD COMPONENTS
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
        // LOAD ORDERS
        // =================================================

        loadOrders();


        // =================================================
        // UPDATE STATUS
        // =================================================

        updateButton.addActionListener(
                e -> updateStatus()
        );


        // =================================================
        // REFRESH
        // =================================================

        refreshButton.addActionListener(
                e -> loadOrders()
        );


        // =================================================
        // ORDER SELECTION
        // =================================================

        orderList.addListSelectionListener(
                e -> {

                    if (
                            !e.getValueIsAdjusting()
                            && orderList.getSelectedValue()
                                    != null
                    ) {

                        Order order =
                                orderList
                                        .getSelectedValue();

                        statusBox.setSelectedItem(
                                order.getStatus()
                        );
                    }
                }
        );
    }


    // =====================================================
    // LOAD ORDERS
    // =====================================================

    private void loadOrders() {

        orderModel.clear();


        OrderDAO orderDAO =
                new OrderDAO();


        List<Order> orders =
                orderDAO.getOrdersByFarmer(
                        farmer.getUserId()
                );


        for (
                Order order :
                orders
        ) {

            orderModel.addElement(
                    order
            );
        }


        summaryLabel.setText(
                "Orders: "
                + orders.size()
        );
    }


    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private void updateStatus() {

        Order selectedOrder =
                orderList.getSelectedValue();


        if (selectedOrder == null) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please select an order first.",

                    "Order Required",

                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        OrderStatus newStatus =
                (OrderStatus)
                        statusBox
                                .getSelectedItem();


        if (newStatus == null) {
            return;
        }


        int choice =
                JOptionPane.showConfirmDialog(
                        this,

                        "Change order status from\n"
                        + selectedOrder.getStatus()
                        + "\nto\n"
                        + newStatus
                        + "?",

                        "Confirm Status Update",

                        JOptionPane.YES_NO_OPTION
                );


        if (
                choice
                != JOptionPane.YES_OPTION
        ) {

            return;
        }


        OrderDAO orderDAO =
                new OrderDAO();


        boolean success =
                orderDAO.updateOrderStatus(
                        selectedOrder.getOrderId(),
                        newStatus
                );


        if (!success) {

            JOptionPane.showMessageDialog(
                    this,

                    "Order status could not be updated.",

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        selectedOrder.setStatus(
                newStatus
        );


        orderList.repaint();


        JOptionPane.showMessageDialog(
                this,

                "Order status updated successfully!\n\n"
                + "Order ID: "
                + selectedOrder.getOrderId()
                + "\nNew Status: "
                + newStatus,

                "FarmDirect",

                JOptionPane.INFORMATION_MESSAGE
        );
    }
}