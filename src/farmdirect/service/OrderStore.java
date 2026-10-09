package farmdirect.service;

import java.util.ArrayList;
import java.util.List;

import farmdirect.model.Order;

public class OrderStore {

    private static final List<Order> orders = new ArrayList<>();

    private OrderStore() {
        // Prevent object creation
    }

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static List<Order> getOrders() {
        return orders;
    }
}