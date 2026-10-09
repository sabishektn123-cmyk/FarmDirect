package farmdirect.model;

public class Feedback {

    private int feedbackId;
    private Customer customer;
    private Farmer farmer;
    private Product product;
    private int rating;
    private String comment;

    public Feedback(
            int feedbackId,
            Customer customer,
            Farmer farmer,
            Product product,
            int rating,
            String comment) {

        this.feedbackId = feedbackId;
        this.customer = customer;
        this.farmer = farmer;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public Product getProduct() {
        return product;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {

        return "Customer: "
                + customer.getName()
                + " | Farmer: "
                + farmer.getName()
                + " | Rating: "
                + rating
                + "/5"
                + " | Comment: "
                + comment;
    }
}