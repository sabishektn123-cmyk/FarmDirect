
package farmdirect.model;

public class BulkSaleRecord {

    private int saleId;
    private int listingId;
    private String cropName;
    private String farmerName;
    private String customerName;
    private double quantity;
    private double pricePerKg;
    private double totalAmount;
    private String saleDate;

    public BulkSaleRecord(
            int saleId,
            int listingId,
            String cropName,
            String farmerName,
            String customerName,
            double quantity,
            double pricePerKg,
            double totalAmount,
            String saleDate) {

        this.saleId = saleId;
        this.listingId = listingId;
        this.cropName = cropName;
        this.farmerName = farmerName;
        this.customerName = customerName;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
    }

    public int getSaleId() {
        return saleId;
    }

    public int getListingId() {
        return listingId;
    }

    public String getCropName() {
        return cropName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getSaleDate() {
        return saleDate;
    }

    @Override
    public String toString() {
        return "BULK SALE #" + saleId
                + " | Product: " + cropName
                + " | Farmer: " + farmerName
                + " | Quantity: " + quantity + " kg"
                + " | Price: ₹" + String.format("%.2f", pricePerKg)
                + "/kg | Total: ₹"
                + String.format("%.2f", totalAmount)
                + " | Status: CONFIRMED";
    }
}
