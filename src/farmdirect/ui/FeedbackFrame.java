package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import farmdirect.dao.FeedbackDAO;
import farmdirect.model.Customer;
import farmdirect.model.Feedback;
import farmdirect.model.Order;
import farmdirect.model.Product;
import farmdirect.service.OrderStore;

public class FeedbackFrame extends JFrame {

    private Customer customer;

    private JComboBox<Product> productBox;

    private JComboBox<Integer> ratingBox;

    private JTextArea commentArea;

    private JLabel ratingLabel;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public FeedbackFrame(Customer customer) {

        this.customer = customer;

        setTitle(
                "FarmDirect - Feedback & Rating"
        );

        setSize(
                650,
                550
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
                        "FARMER FEEDBACK & RATING",
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
                                3,
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


        // =================================================
        // PRODUCT BOX
        // =================================================

        productBox =
                new JComboBox<>();


        loadPurchasedProducts();


        formPanel.add(
                new JLabel(
                        "Purchased Product:"
                )
        );

        formPanel.add(
                productBox
        );


        // =================================================
        // RATING
        // =================================================

        ratingBox =
                new JComboBox<>(
                        new Integer[]{
                                1,
                                2,
                                3,
                                4,
                                5
                        }
                );


        ratingLabel =
                new JLabel(
                        "Rating: 1 / 5"
                );


        formPanel.add(
                new JLabel(
                        "Rating:"
                )
        );

        formPanel.add(
                ratingBox
        );


        // =================================================
        // RATING CHANGE EVENT
        // =================================================

        ratingBox.addActionListener(
                e -> {

                    Integer rating =
                            (Integer)
                                    ratingBox
                                            .getSelectedItem();

                    if (rating != null) {

                        ratingLabel.setText(
                                "Rating: "
                                + rating
                                + " / 5"
                        );
                    }
                }
        );


        // =================================================
        // COMMENT
        // =================================================

        commentArea =
                new JTextArea(
                        6,
                        30
                );

        commentArea.setLineWrap(
                true
        );

        commentArea.setWrapStyleWord(
                true
        );


        JScrollPane commentScroll =
                new JScrollPane(
                        commentArea
                );

        commentScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Your Comment"
                )
        );


        // =================================================
        // SUBMIT BUTTON
        // =================================================

        JButton submitButton =
                new JButton(
                        "SUBMIT FEEDBACK"
                );

        submitButton.setBackground(
                new Color(
                        34,
                        139,
                        34
                )
        );

        submitButton.setForeground(
                Color.WHITE
        );


        // =================================================
        // VIEW RATINGS BUTTON
        // =================================================

        JButton viewButton =
                new JButton(
                        "VIEW FARMER RATINGS"
                );


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(
                submitButton
        );

        buttonPanel.add(
                viewButton
        );


        // =================================================
        // ADD COMPONENTS
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
                commentScroll,
                BorderLayout.EAST
        );

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // SUBMIT
        // =================================================

        submitButton.addActionListener(
                e -> submitFeedback()
        );


        // =================================================
        // VIEW RATINGS
        // =================================================

        viewButton.addActionListener(
                e -> viewFarmerRating()
        );
    }


    // =====================================================
    // LOAD PURCHASED PRODUCTS
    // =====================================================

    private void loadPurchasedProducts() {

        DefaultComboBoxModel<Product>
                model =
                new DefaultComboBoxModel<>();


        // Get orders belonging to customer

        List<Order> orders =
                new farmdirect.dao.OrderDAO()
                        .getOrdersByCustomer(
                                customer.getUserId()
                        );


        // Add purchased products

        for (
                Order order :
                orders
        ) {

            Product product =
                    order.getProduct();


            boolean alreadyAdded =
                    false;


            for (
                    int i = 0;
                    i < model.getSize();
                    i++
            ) {

                Product existing =
                        model.getElementAt(i);


                if (
                        existing
                                .getProductId()
                        ==
                        product
                                .getProductId()
                ) {

                    alreadyAdded =
                            true;

                    break;
                }
            }


            if (!alreadyAdded) {

                model.addElement(
                        product
                );
            }
        }


        productBox.setModel(
                model
        );
    }


    // =====================================================
    // SUBMIT FEEDBACK
    // =====================================================

    private void submitFeedback() {

        Product product =
                (Product)
                        productBox
                                .getSelectedItem();


        // =================================================
        // CHECK PRODUCT
        // =================================================

        if (product == null) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please purchase a product before giving feedback.",

                    "Feedback",

                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =================================================
        // GET RATING
        // =================================================

        Integer rating =
                (Integer)
                        ratingBox
                                .getSelectedItem();


        if (
                rating == null
                || rating < 1
                || rating > 5
        ) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please select a rating between 1 and 5.",

                    "Rating Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =================================================
        // GET COMMENT
        // =================================================

        String comment =
                commentArea
                        .getText()
                        .trim();


        if (comment.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please enter a comment.",

                    "Feedback Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =================================================
        // CREATE FEEDBACK
        // =================================================

        Feedback feedback =
                new Feedback(
                        0,

                        customer,

                        product.getFarmer(),

                        product,

                        rating,

                        comment
                );


        // =================================================
        // SAVE DATABASE
        // =================================================

        FeedbackDAO feedbackDAO =
                new FeedbackDAO();


        boolean success =
                feedbackDAO.addFeedback(
                        feedback
                );


        if (!success) {

            JOptionPane.showMessageDialog(
                    this,

                    "Feedback could not be saved.",

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =================================================
        // SUCCESS
        // =================================================

        double averageRating =
                feedbackDAO.getAverageRating(
                        product
                                .getFarmer()
                                .getUserId()
                );


        int ratingCount =
                feedbackDAO.getRatingCount(
                        product
                                .getFarmer()
                                .getUserId()
                );


        JOptionPane.showMessageDialog(
                this,

                "Feedback submitted successfully!\n\n"

                + "Farmer: "
                + product
                        .getFarmer()
                        .getName()

                + "\nYour Rating: "
                + rating
                + "/5"

                + "\n\nFarmer Average Rating: "
                + String.format(
                        "%.2f",
                        averageRating
                )
                + "/5"

                + "\nTotal Ratings: "
                + ratingCount,

                "FarmDirect",

                JOptionPane.INFORMATION_MESSAGE
        );


        // =================================================
        // CLEAR COMMENT
        // =================================================

        commentArea.setText("");

        ratingBox.setSelectedItem(
                5
        );
    }


    // =====================================================
    // VIEW FARMER RATING
    // =====================================================

    private void viewFarmerRating() {

        Product product =
                (Product)
                        productBox
                                .getSelectedItem();


        if (product == null) {

            JOptionPane.showMessageDialog(
                    this,

                    "Please select a product.",

                    "Rating",

                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int farmerId =
                product
                        .getFarmer()
                        .getUserId();


        FeedbackDAO feedbackDAO =
                new FeedbackDAO();


        double average =
                feedbackDAO.getAverageRating(
                        farmerId
                );


        int count =
                feedbackDAO.getRatingCount(
                        farmerId
                );


        List<Feedback> feedbackList =
                feedbackDAO.getFeedbackByFarmer(
                        farmerId
                );


        StringBuilder message =
                new StringBuilder();


        message.append(
                "FARMER RATING\n\n"
        );


        message.append(
                "Farmer: "
                + product
                        .getFarmer()
                        .getName()
                + "\n"
        );


        message.append(
                "Average Rating: "
                + String.format(
                        "%.2f",
                        average
                )
                + "/5\n"
        );


        message.append(
                "Total Ratings: "
                + count
                + "\n\n"
        );


        message.append(
                "REVIEWS\n"
        );


        message.append(
                "------------------------------\n"
        );


        if (feedbackList.isEmpty()) {

            message.append(
                    "No reviews yet."
            );

        } else {

            for (
                    Feedback feedback :
                    feedbackList
            ) {

                message.append(
                        "\n"
                );

                message.append(
                        feedback
                                .getCustomer()
                                .getName()
                );

                message.append(
                        " - "
                );

                message.append(
                        feedback
                                .getRating()
                );

                message.append(
                        "/5\n"
                );

                message.append(
                        feedback
                                .getComment()
                );

                message.append(
                        "\n"
                );
            }
        }


        JTextArea textArea =
                new JTextArea(
                        message.toString()
                );

        textArea.setEditable(
                false
        );

        textArea.setLineWrap(
                true
        );

        textArea.setWrapStyleWord(
                true
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        textArea
                );

        scrollPane.setPreferredSize(
                new java.awt.Dimension(
                        550,
                        400
                )
        );


        JOptionPane.showMessageDialog(
                this,

                scrollPane,

                "Farmer Ratings",

                JOptionPane.INFORMATION_MESSAGE
        );
    }
}