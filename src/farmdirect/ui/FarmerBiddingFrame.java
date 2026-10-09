
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
import javax.swing.JSplitPane;

import farmdirect.dao.BidDAO;
import farmdirect.dao.BulkListingDAO;
import farmdirect.dao.BulkSaleDAO;
import farmdirect.model.Bid;
import farmdirect.model.BulkListing;
import farmdirect.model.Farmer;

public class FarmerBiddingFrame extends JFrame {

    private final Farmer farmer;

    private final DefaultListModel<BulkListing> listingModel =
            new DefaultListModel<>();

    private final DefaultListModel<Bid> bidModel =
            new DefaultListModel<>();

    private JList<BulkListing> listingList;
    private JList<Bid> bidList;

    private JLabel highestBidLabel;
    private JLabel bidCountLabel;

    private final BulkListingDAO listingDAO = new BulkListingDAO();
    private final BidDAO bidDAO = new BidDAO();
    private final BulkSaleDAO bulkSaleDAO = new BulkSaleDAO();


    public FarmerBiddingFrame(Farmer farmer) {

        this.farmer = farmer;

        setTitle("FarmDirect - Customer Bids");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel title = new JLabel(
                "CUSTOMER BIDS - " + farmer.getName(),
                JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(27, 94, 32));
        header.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );
        header.add(title, BorderLayout.CENTER);

        // Listings
        listingList = new JList<>(listingModel);
        listingList.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane listingScroll = new JScrollPane(listingList);
        listingScroll.setBorder(
                BorderFactory.createTitledBorder("Active Bulk Listings")
        );

        // Customer bids
        bidList = new JList<>(bidModel);
        bidList.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane bidScroll = new JScrollPane(bidList);
        bidScroll.setBorder(
                BorderFactory.createTitledBorder("Customer Bids")
        );

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                listingScroll,
                bidScroll
        );
        splitPane.setResizeWeight(0.45);

        // Summary
        highestBidLabel = new JLabel(
                "Highest bid: Select a listing"
        );
        highestBidLabel.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        bidCountLabel = new JLabel("Total bids: 0");

        JPanel summaryPanel = new JPanel(new BorderLayout(5, 5));
        summaryPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );
        summaryPanel.add(highestBidLabel, BorderLayout.NORTH);
        summaryPanel.add(bidCountLabel, BorderLayout.SOUTH);

        // Buttons
        JButton refreshButton = new JButton("REFRESH BIDS");
        JButton createButton = new JButton("CREATE BULK LISTING");
        JButton acceptButton = new JButton("ACCEPT HIGHEST BID & SELL");
        JButton closeButton = new JButton("CLOSE");

        refreshButton.setBackground(new Color(46, 125, 50));
        refreshButton.setForeground(Color.WHITE);

        createButton.setBackground(new Color(27, 94, 32));
        createButton.setForeground(Color.WHITE);

        acceptButton.setBackground(new Color(21, 101, 60));
        acceptButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(createButton);
        buttonPanel.add(acceptButton);
        buttonPanel.add(closeButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        listingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadBids();
            }
        });

        refreshButton.addActionListener(e -> refreshData());

        createButton.addActionListener(e -> {
            new BulkListingFrame(farmer).setVisible(true);
        });

        acceptButton.addActionListener(e -> acceptHighestBid());

        closeButton.addActionListener(e -> dispose());

        refreshData();
    }


    // Load only this farmer's active, unsold listings.
    private void refreshData() {

        BulkListing previous = listingList.getSelectedValue();

        listingModel.clear();
        bidModel.clear();

        List<BulkListing> listings =
                listingDAO.getAvailableListingsByFarmer(
                        farmer.getUserId()
                );

        for (BulkListing listing : listings) {
            listingModel.addElement(listing);
        }

        if (listings.isEmpty()) {
            highestBidLabel.setText("No active bulk listings.");
            bidCountLabel.setText("Total bids: 0");
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
        loadBids();
    }


    // Load bids for the selected active listing.
    private void loadBids() {

        BulkListing selected = listingList.getSelectedValue();
        bidModel.clear();

        if (selected == null) {
            highestBidLabel.setText("Highest bid: Select a listing");
            bidCountLabel.setText("Total bids: 0");
            return;
        }

        List<Bid> bids =
                bidDAO.getBidsForListing(selected.getListingId());

        for (Bid bid : bids) {
            bidModel.addElement(bid);
        }

        bidCountLabel.setText("Total bids: " + bids.size());

        Bid highest = bidDAO.getHighestBid(selected.getListingId());

        if (highest == null) {
            highestBidLabel.setText("Highest bid: No bids received");
        } else {
            highestBidLabel.setText(
                    "Highest bid: ₹"
                    + String.format("%.2f", highest.getBidPricePerKg())
                    + "/kg | Customer: "
                    + highest.getCustomer().getName()
            );
        }
    }


    // Accept the highest bid and record the sale.
    private void acceptHighestBid() {

        BulkListing selected = listingList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an active listing first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Bid highest = bidDAO.getHighestBid(selected.getListingId());

        if (highest == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No bids are available for this listing.",
                    "No Bids",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double total = selected.getQuantity()
                * highest.getBidPricePerKg();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Accept the highest bid and sell this listing?\n\n"
                + "Crop: " + selected.getCrop().getCropName()
                + "\nCustomer: " + highest.getCustomer().getName()
                + "\nQuantity: " + selected.getQuantity() + " kg"
                + "\nPrice: ₹"
                + String.format("%.2f", highest.getBidPricePerKg())
                + "/kg\nTotal: ₹" + String.format("%.2f", total),
                "Confirm Bulk Sale",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        BulkSaleDAO.SaleResult result =
                bulkSaleDAO.acceptHighestBid(
                        selected.getListingId(),
                        farmer.getUserId()
                );

        if (result.isSuccess()) {

            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage()
                    + "\n\nWinning customer: " + result.getCustomerName()
                    + "\nAccepted price: ₹"
                    + String.format("%.2f", result.getPricePerKg())
                    + "/kg\nQuantity: " + result.getQuantity()
                    + " kg\nTotal: ₹"
                    + String.format("%.2f", result.getTotalAmount()),
                    "Bulk Sale Confirmed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // The sold listing is now excluded from this dashboard.
            refreshData();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage(),
                    "Bulk Sale Error",
                    JOptionPane.ERROR_MESSAGE
            );

            // Refresh in case another operation already sold it.
            refreshData();
        }
    }
}
