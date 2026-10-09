package farmdirect.model;

public class Customer extends User {

    private String deliveryAddress;

    public Customer(int userId,
                    String name,
                    String email,
                    String password,
                    String deliveryAddress) {

        super(userId, name, email, password);

        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }
}