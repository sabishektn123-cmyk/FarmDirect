package farmdirect.model;

public class Bid {

    private int bidId;
    private Customer customer;
    private double bidPricePerKg;

    public Bid(int bidId,
               Customer customer,
               double bidPricePerKg) {

        this.bidId = bidId;
        this.customer = customer;
        this.bidPricePerKg = bidPricePerKg;
    }

    public int getBidId() {
        return bidId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBidPricePerKg() {
        return bidPricePerKg;
    }

    @Override
    public String toString() {

        return "Bid ID: " + bidId
                + " | Customer: " + customer.getName()
                + " | Bid: ₹" + bidPricePerKg + "/kg";
    }
}