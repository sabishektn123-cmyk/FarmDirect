package farmdirect.model;

import java.util.ArrayList;
import java.util.List;

public class BulkListing {

    private int listingId;
    private Farmer farmer;
    private Crop crop;
    private double quantity;
    private double minimumPrice;

    private List<Bid> bids;

    public BulkListing(int listingId,
                       Farmer farmer,
                       Crop crop,
                       double quantity,
                       double minimumPrice) {

        this.listingId = listingId;
        this.farmer = farmer;
        this.crop = crop;
        this.quantity = quantity;
        this.minimumPrice = minimumPrice;

        this.bids = new ArrayList<>();
    }

    public int getListingId() {
        return listingId;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public Crop getCrop() {
        return crop;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getMinimumPrice() {
        return minimumPrice;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public Bid getHighestBid() {

        if (bids.isEmpty()) {
            return null;
        }

        Bid highestBid = bids.get(0);

        for (Bid bid : bids) {

            if (bid.getBidPricePerKg()
                    > highestBid.getBidPricePerKg()) {

                highestBid = bid;
            }
        }

        return highestBid;
    }

    @Override
    public String toString() {

        return "Listing ID: " + listingId
                + " | Crop: " + crop.getCropName()
                + " | Quantity: " + quantity + " kg"
                + " | Minimum Price: ₹"
                + minimumPrice + "/kg";
    }
}