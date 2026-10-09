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

import farmdirect.dao.FeedbackDAO;
import farmdirect.model.Farmer;

public class FarmerDashboard extends JFrame {

    private Farmer farmer;

    public FarmerDashboard(Farmer farmer) {

        this.farmer = farmer;

        setTitle("FarmDirect - Farmer Dashboard");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(
                new Color(27, 94, 32)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        JLabel title =
                new JLabel(
                        "Farmer Dashboard - Welcome, "
                        + farmer.getName()
                );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        JLabel location =
                new JLabel(
                        "Farm Location: "
                        + farmer.getFarmLocation()
                );

        location.setForeground(Color.WHITE);

        location.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        JPanel headerText =
                new JPanel(
                        new GridLayout(2, 1)
                );

        headerText.setOpaque(false);

        headerText.add(title);
        headerText.add(location);

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

        JButton uploadButton =
                new JButton(
                        "ADD / UPLOAD CROP"
                );

        JButton productsButton =
                new JButton(
                        "MY PRODUCTS"
                );

        JButton bulkButton =
                new JButton(
                        "BULK SALES"
                );

        JButton ordersButton =
                new JButton(
                        "CUSTOMER ORDERS"
                );

        JButton ratingButton =
                new JButton(
                        "MY RATINGS"
                );

        JButton priceButton =
                new JButton(
                        "PRICE HISTORY"
                );

        JButton logoutButton =
                new JButton(
                        "LOGOUT"
                );

        // =====================================================
        // BUTTON FONT
        // =====================================================

        Font buttonFont =
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                );

        uploadButton.setFont(buttonFont);
        productsButton.setFont(buttonFont);
        bulkButton.setFont(buttonFont);
        ordersButton.setFont(buttonFont);
        ratingButton.setFont(buttonFont);
        priceButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);

        // =====================================================
        // BUTTON COLORS
        // =====================================================

        uploadButton.setBackground(
                new Color(46, 125, 50)
        );

        productsButton.setBackground(
                new Color(56, 142, 60)
        );

        bulkButton.setBackground(
                new Color(67, 160, 71)
        );

        ordersButton.setBackground(
                new Color(76, 175, 80)
        );

        ratingButton.setBackground(
                new Color(102, 187, 106)
        );

        priceButton.setBackground(
                new Color(129, 199, 132)
        );

        logoutButton.setBackground(
                new Color(198, 40, 40)
        );

        uploadButton.setForeground(Color.WHITE);
        productsButton.setForeground(Color.WHITE);
        bulkButton.setForeground(Color.WHITE);
        ordersButton.setForeground(Color.WHITE);
        ratingButton.setForeground(Color.WHITE);
        priceButton.setForeground(Color.BLACK);
        logoutButton.setForeground(Color.WHITE);

        // =====================================================
        // UPLOAD PRODUCT
        // =====================================================

        uploadButton.addActionListener(e -> {

            new AddProductFrame(
                    farmer
            ).setVisible(true);

        });

        // =====================================================
        // MY PRODUCTS
        // =====================================================

        productsButton.addActionListener(e -> {

            new MyProductsFrame(
                    farmer
            ).setVisible(true);

        });

        // =====================================================
        // BULK SALES
        // =====================================================

        bulkButton.addActionListener(e -> {
            new FarmerBiddingFrame(farmer).setVisible(true);
        });

        // =====================================================
        // CUSTOMER ORDERS
        // =====================================================

        ordersButton.addActionListener(e -> {

            new FarmerOrdersFrame(
                    farmer
            ).setVisible(true);

        });

        // =====================================================
        // RATINGS
        // =====================================================

        ratingButton.addActionListener(e -> {

            showFarmerRatings();

        });

        // =====================================================
        // PRICE HISTORY
        // =====================================================

        priceButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,

                    "PRICE INFORMATION\n\n"
                    + "FarmDirect uses farmer quotations "
                    + "to calculate the fair price.\n\n"
                    + "The fair price is calculated using "
                    + "the average price of available "
                    + "farmer products.",

                    "Price History",

                    JOptionPane.INFORMATION_MESSAGE
            );

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

        buttonPanel.add(uploadButton);
        buttonPanel.add(productsButton);

        buttonPanel.add(bulkButton);
        buttonPanel.add(ordersButton);

        buttonPanel.add(ratingButton);
        buttonPanel.add(priceButton);

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


    // =====================================================
    // SHOW FARMER RATINGS
    // =====================================================

    private void showFarmerRatings() {

        FeedbackDAO feedbackDAO =
                new FeedbackDAO();

        double average =
                feedbackDAO.getAverageRating(
                        farmer.getUserId()
                );

        int count =
                feedbackDAO.getRatingCount(
                        farmer.getUserId()
                );

        String message =
                "FARMER TRUST & RATING\n\n"

                + "Farmer: "
                + farmer.getName()

                + "\n\nAverage Rating: "
                + String.format(
                        "%.2f",
                        average
                )
                + " / 5"

                + "\nTotal Ratings: "
                + count;

        JOptionPane.showMessageDialog(
                this,

                message,

                "FarmDirect - My Ratings",

                JOptionPane.INFORMATION_MESSAGE
        );
    }
}