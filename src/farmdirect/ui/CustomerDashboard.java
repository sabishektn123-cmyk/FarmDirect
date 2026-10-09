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

import farmdirect.model.Customer;

public class CustomerDashboard extends JFrame {

    private Customer customer;

    public CustomerDashboard(Customer customer) {

        this.customer = customer;

        setTitle(
                "FarmDirect - Customer Dashboard"
        );

        setSize(
                900,
                650
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        // =====================================================
        // HEADER
        // =====================================================

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

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );


        JLabel title =
                new JLabel(
                        "Customer Dashboard - Welcome, "
                        + customer.getName()
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );


        JLabel address =
                new JLabel(
                        "Delivery Address: "
                        + customer.getDeliveryAddress()
                );

        address.setForeground(
                Color.WHITE
        );

        address.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );


        JPanel headerText =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        headerText.setOpaque(false);

        headerText.add(title);
        headerText.add(address);

        header.add(
                headerText,
                BorderLayout.CENTER
        );


        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                20,
                                20
                        )
                );

        buttonPanel.setBackground(
                new Color(
                        232,
                        245,
                        233
                )
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        35,
                        60,
                        35,
                        60
                )
        );


        JButton browseButton =
                new JButton(
                        "BROWSE PRODUCTS"
                );

        JButton purchaseButton =
                new JButton(
                        "PURCHASE PRODUCTS"
                );

        JButton bulkButton =
                new JButton(
                        "BULK BIDDING"
                );

        JButton ordersButton =
                new JButton(
                        "MY ORDERS"
                );

        JButton feedbackButton =
                new JButton(
                        "FEEDBACK & RATING"
                );

        JButton freshButton =
                new JButton(
                        "FRESH PRODUCTS"
                );

        JButton logoutButton =
                new JButton(
                        "LOGOUT"
                );


        Font buttonFont =
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                );

        browseButton.setFont(buttonFont);
        purchaseButton.setFont(buttonFont);
        bulkButton.setFont(buttonFont);
        ordersButton.setFont(buttonFont);
        feedbackButton.setFont(buttonFont);
        freshButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);


        // =====================================================
        // BUTTON COLORS
        // =====================================================

        browseButton.setBackground(
                new Color(46, 125, 50)
        );

        purchaseButton.setBackground(
                new Color(56, 142, 60)
        );

        bulkButton.setBackground(
                new Color(67, 160, 71)
        );

        ordersButton.setBackground(
                new Color(76, 175, 80)
        );

        feedbackButton.setBackground(
                new Color(102, 187, 106)
        );

        freshButton.setBackground(
                new Color(129, 199, 132)
        );

        logoutButton.setBackground(
                new Color(198, 40, 40)
        );


        browseButton.setForeground(Color.WHITE);
        purchaseButton.setForeground(Color.WHITE);
        bulkButton.setForeground(Color.WHITE);
        ordersButton.setForeground(Color.WHITE);
        feedbackButton.setForeground(Color.WHITE);
        freshButton.setForeground(Color.BLACK);
        logoutButton.setForeground(Color.WHITE);


        // =====================================================
        // BROWSE
        // =====================================================

        browseButton.addActionListener(e -> {

            new BrowseProductsFrame(
                    customer
            ).setVisible(true);

        });


        // =====================================================
        // PURCHASE
        // =====================================================

        purchaseButton.addActionListener(e -> {

            new BrowseProductsFrame(
                    customer
            ).setVisible(true);

        });


        // =====================================================
        // BULK BIDDING
        // =====================================================

        bulkButton.addActionListener(e -> {

            new BulkBiddingFrame(
                    customer
            ).setVisible(true);

        });


        // =====================================================
        // MY ORDERS
        // =====================================================

        ordersButton.addActionListener(e -> {

            new MyOrdersFrame(
                    customer
            ).setVisible(true);

        });


        // =====================================================
        // FEEDBACK
        // =====================================================

        feedbackButton.addActionListener(e -> {

            new FeedbackFrame(
                    customer
            ).setVisible(true);

        });


        // =====================================================
        // FRESH PRODUCTS
        // =====================================================

        freshButton.addActionListener(e -> {

            new BrowseProductsFrame(
                    customer
            ).setVisible(true);

        });


        // =====================================================
        // LOGOUT
        // =====================================================

        logoutButton.addActionListener(e -> {

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,

                            "Are you sure you want to logout?",

                            "Logout",

                            JOptionPane.YES_NO_OPTION
                    );

            if (
                    choice
                    == JOptionPane.YES_OPTION
            ) {

                dispose();

                new LoginFrame()
                        .setVisible(true);
            }

        });


        // =====================================================
        // ADD BUTTONS
        // =====================================================

        buttonPanel.add(browseButton);
        buttonPanel.add(purchaseButton);

        buttonPanel.add(bulkButton);
        buttonPanel.add(ordersButton);

        buttonPanel.add(feedbackButton);
        buttonPanel.add(freshButton);

        buttonPanel.add(logoutButton);


        // =====================================================
        // FRAME
        // =====================================================

        add(
                header,
                BorderLayout.NORTH
        );

        add(
                buttonPanel,
                BorderLayout.CENTER
        );
    }
}