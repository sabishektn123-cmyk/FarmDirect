
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
import javax.swing.JTextField;

import farmdirect.dao.BidDAO;
import farmdirect.dao.BulkListingDAO;
import farmdirect.exception.InvalidBidException;
import farmdirect.model.Bid;
import farmdirect.model.BulkListing;
import farmdirect.model.Customer;
import farmdirect.service.BiddingService;

public class BulkBiddingFrame extends JFrame {

    private final Customer customer;

    private DefaultListModel<BulkListing> listingModel;
    private JList<BulkListing> listingList;
    private JTextField bidPriceField;
    private JLabel highestBidLabel;
    private JLabel listingStatusLabel;

    private final BulkListingDAO listingDAO =
            new BulkListingDAO();

    private final BidDAO bidDAO =
            new BidDAO();

    private final BiddingService biddingService =
            new BiddingService();


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BulkBiddingFrame(Customer customer) {

        this.customer = customer;

        setTitle("FarmDirect - Bulk Bidding");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));


        // =================================================
        // HEADER
        // =================================================

        JLabel title = new JLabel(
                "BULK SALES & BIDDING",
                JLabel.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 27)
        );

        title.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(
                new Color(34, 139, 34)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 10, 15, 10
                )
        );

        header.add(title, BorderLayout.CENTER);


        // =================================================
        // AVAILABLE LISTINGS
        // =================================================

        listingModel = new DefaultListModel<>();

        listingList = new JList<>(listingModel);

        listingList.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );

        listingList.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane listingScroll = new JScrollPane(listingList);

        listingScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Available Bulk Listings"
                )
        );


        // =================================================
        // BID INPUT
        // =================================================

        bidPriceField = new JTextField();

        highestBidLabel = new JLabel(
                "Highest bid: Select a listing"
        );

        highestBidLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        listingStatusLabel = new JLabel(
                "Select a listing to see its bidding details."
        );

        JPanel inputPanel = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 30, 10, 30
                )
        );

        inputPanel.add(
                new JLabel("Your Bid (₹/kg):")
        );

        inputPanel.add(bidPriceField);

        inputPanel.add(
                new JLabel("Highest Offer:")
        );

        inputPanel.add(highestBidLabel);


        // =================================================
        // BUTTONS
        // =================================================

        JButton placeBidButton =
                new JButton("PLACE BID");

        JButton highestBidButton =
                new JButton("VIEW HIGHEST BID");

        JButton refreshButton =
                new JButton("REFRESH LISTINGS");

        JButton closeButton =
                new JButton("CLOSE");

        placeBidButton.setBackground(
                new Color(34, 139, 34)
        );

        placeBidButton.setForeground(Color.WHITE);

        highestBidButton.setBackground(
                new Color(30, 136, 229)
        );

        highestBidButton.setForeground(Color.WHITE);


        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 4, 10, 10)
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 30, 15, 30
                )
        );

        buttonPanel.add(placeBidButton);
        buttonPanel.add(highestBidButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);


        // =================================================
        // BOTTOM PANEL
        // =================================================

        JPanel bottomPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        bottomPanel.add(
                listingStatusLabel,
                BorderLayout.NORTH
        );

        bottomPanel.add(
                inputPanel,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // ADD COMPONENTS
        // =================================================

        add(header, BorderLayout.NORTH);
        add(listingScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


        // =================================================
        // EVENT HANDLERS
        // =================================================

        listingList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                bidPriceField.setText("");

                updateListingDetails();
            }
        });


        placeBidButton.addActionListener(
                e -> placeBid()
        );


        highestBidButton.addActionListener(
                e -> showHighestBid()
        );


        refreshButton.addActionListener(
                e -> loadAvailableListings()
        );


        closeButton.addActionListener(
                e -> dispose()
        );


        // Initial load
        loadAvailableListings();
    }


    // =====================================================
    // LOAD ONLY UNSOLD LISTINGS
    // =====================================================

    private void loadAvailableListings() {

        BulkListing previous =
                listingList.getSelectedValue();

        listingModel.clear();

        List<BulkListing> listings =
                listingDAO.getAvailableListings();

        for (BulkListing listing : listings) {
            listingModel.addElement(listing);
        }

        if (listings.isEmpty()) {

            highestBidLabel.setText(
                    "No active bids"
            );

            listingStatusLabel.setText(
                    "No unsold bulk listings are currently available."
            );

            bidPriceField.setText("");

            return;
        }

        int selectedIndex = 0;

        if (previous != null) {

            for (int i = 0; i < listingModel.size(); i++) {

                if (listingModel.get(i).getListingId()
                        == previous.getListingId()) {

                    selectedIndex = i;
                    break;
                }
            }
        }

        listingList.setSelectedIndex(selectedIndex);

        updateListingDetails();
    }


    // =====================================================
    // UPDATE SELECTED LISTING DETAILS
    // =====================================================

    private void updateListingDetails() {

        BulkListing selected =
                listingList.getSelectedValue();

        if (selected == null) {

            highestBidLabel.setText(
                    "Highest bid: Select a listing"
            );

            return;
        }

        Bid highest =
                bidDAO.getHighestBid(
                        selected.getListingId()
                );

        if (highest == null) {

            highestBidLabel.setText(
                    "No bids received yet"
            );

        } else {

            highestBidLabel.setText(
                    "₹"
                    + String.format(
                            "%.2f",
                            highest.getBidPricePerKg()
                    )
                    + "/kg"
            );
        }

        listingStatusLabel.setText(
                selected.getCrop().getCropName()
                + " | Quantity: "
                + selected.getQuantity()
                + " kg | Minimum: ₹"
                + String.format(
                        "%.2f",
                        selected.getMinimumPrice()
                )
                + "/kg"
        );
    }


    // =====================================================
    // PLACE BID
    // =====================================================

    private void placeBid() {

        BulkListing selected =
                listingList.getSelectedValue();

        if (selected == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a bulk listing first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String bidText =
                bidPriceField.getText().trim();

        if (bidText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your bid price.",
                    "Bid Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double bidPrice;

        try {

            bidPrice = Double.parseDouble(bidText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid numeric bid.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!Double.isFinite(bidPrice) || bidPrice <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bid price must be a positive number.",
                    "Invalid Bid",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Prevent the customer from bidding below the minimum.
        if (bidPrice < selected.getMinimumPrice()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your bid must be at least ₹"
                    + String.format(
                            "%.2f",
                            selected.getMinimumPrice()
                    )
                    + "/kg.",
                    "Bid Too Low",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Place your bid?\n\n"
                + "Crop: "
                + selected.getCrop().getCropName()
                + "\nQuantity: "
                + selected.getQuantity()
                + " kg\nYour bid: ₹"
                + String.format("%.2f", bidPrice)
                + "/kg\nPotential total: ₹"
                + String.format(
                        "%.2f",
                        selected.getQuantity() * bidPrice
                ),
                "Confirm Bid",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // Recheck that the listing has not already been sold.
        boolean stillAvailable = false;

        for (BulkListing listing :
                listingDAO.getAvailableListings()) {

            if (listing.getListingId()
                    == selected.getListingId()) {

                stillAvailable = true;
                break;
            }
        }

        if (!stillAvailable) {

            JOptionPane.showMessageDialog(
                    this,
                    "This listing has already been sold.",
                    "Listing Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );

            loadAvailableListings();

            return;
        }

        Bid bid = new Bid(
                0,
                customer,
                bidPrice
        );

        try {

            // Validate the bid and add it to the listing's
            // in-memory bidding collection.
            biddingService.placeBid(selected, bid);

            // Persist the bid in SQLite.
            int bidId = bidDAO.addBid(
                    selected.getListingId(),
                    bid
            );

            if (bidId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bid could not be saved to the database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Your bid has been placed successfully!\n\n"
                    + "Crop: "
                    + selected.getCrop().getCropName()
                    + "\nBid: ₹"
                    + String.format("%.2f", bidPrice)
                    + "/kg",
                    "Bid Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            bidPriceField.setText("");

            updateListingDetails();

        } catch (InvalidBidException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Bid",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // =====================================================
    // VIEW HIGHEST BID
    // =====================================================

    private void showHighestBid() {

        BulkListing selected =
                listingList.getSelectedValue();

        if (selected == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a listing first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Bid highest =
                bidDAO.getHighestBid(
                        selected.getListingId()
                );

        if (highest == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No bids have been placed for this listing yet.",
                    "Highest Bid",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "HIGHEST BID\n\n"
                + "Customer: "
                + highest.getCustomer().getName()
                + "\nBid: ₹"
                + String.format(
                        "%.2f",
                        highest.getBidPricePerKg()
                )
                + "/kg",
                "Highest Bid",
                JOptionPane.INFORMATION_MESSAGE
        );

        updateListingDetails();
    }
}
