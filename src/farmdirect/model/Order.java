package farmdirect.model;

public class Order {

    private int orderId;
    private Customer customer;
    private Product product;
    private double quantity;
    private double totalAmount;
    private OrderStatus status;
    private double salePricePerKg;

    public Order(int orderId,
            Customer customer,
            Product product,
            double quantity,
            double salePricePerKg) {

   this.orderId = orderId;
   this.customer = customer;
   this.product = product;
   this.quantity = quantity;
   this.salePricePerKg = salePricePerKg;

   this.totalAmount =
           quantity * salePricePerKg;

   this.status = OrderStatus.PENDING;
}

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void confirmOrder() {
        status = OrderStatus.CONFIRMED;
    }

    public void packOrder() {
        status = OrderStatus.PACKED;
    }

    public void shipOrder() {
        status = OrderStatus.SHIPPED;
    }

    public void markDelivered() {
        status = OrderStatus.DELIVERED;
    }
    public double getSalePricePerKg() {
        return salePricePerKg;
    }

    @Override
    public String toString() {

        return "Order ID: " + orderId
                + " | Customer: " + customer.getName()
                + " | Product: " + product.getCrop().getCropName()
                + " | Quantity: " + quantity + " kg"
                + " | Total: ₹" + totalAmount
                + " | Status: " + status;
    }
}