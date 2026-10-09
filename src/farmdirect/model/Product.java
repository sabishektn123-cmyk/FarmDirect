package farmdirect.model;

public class Product {

    private int productId;
    private Crop crop;
    private Farmer farmer;
    private double quantity;
    private double pricePerKg;
    private FreshnessLevel freshness;

    public Product(int productId,
                   Crop crop,
                   Farmer farmer,
                   double quantity,
                   double pricePerKg,
                   FreshnessLevel freshness) {

        this.productId = productId;
        this.crop = crop;
        this.farmer = farmer;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.freshness = freshness;
    }

    public int getProductId() {
        return productId;
    }

    public Crop getCrop() {
        return crop;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public FreshnessLevel getFreshness() {
        return freshness;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public void setFreshness(FreshnessLevel freshness) {
        this.freshness = freshness;
    }

    public double calculateTotalValue() {
        return quantity * pricePerKg;
    }

    @Override
    public String toString() {

        return crop.getCropName()
                + " | Farmer: " + farmer.getName()
                + " | Quantity: " + quantity + " kg"
                + " | Price: ₹" + pricePerKg + "/kg"
                + " | Freshness: " + freshness;
    }
}