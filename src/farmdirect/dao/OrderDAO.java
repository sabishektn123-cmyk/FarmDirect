package farmdirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import farmdirect.database.DatabaseConnection;
import farmdirect.model.Crop;
import farmdirect.model.Customer;
import farmdirect.model.Farmer;
import farmdirect.model.FreshnessLevel;
import farmdirect.model.Order;
import farmdirect.model.OrderStatus;
import farmdirect.model.Product;

public class OrderDAO {

    // =====================================================
    // ADD ORDER
    // =====================================================

    public boolean addOrder(Order order) {

        String sql = """
                INSERT INTO orders
                (
                    customer_id,
                    product_id,
                    quantity,
                    sale_price_per_kg,
                    total_amount,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    order.getCustomer().getUserId()
            );

            statement.setInt(
                    2,
                    order.getProduct().getProductId()
            );

            statement.setDouble(
                    3,
                    order.getQuantity()
            );

            statement.setDouble(
                    4,
                    order.getSalePricePerKg()
            );

            statement.setDouble(
                    5,
                    order.getTotalAmount()
            );

            statement.setString(
                    6,
                    order.getStatus().name()
            );

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Order insertion error: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =====================================================
    // GET ALL ORDERS
    // =====================================================

    public List<Order> getAllOrders() {

        List<Order> orders =
                new ArrayList<>();

        String sql = """
                SELECT
                    o.order_id,
                    o.customer_id,
                    o.product_id,
                    o.quantity,
                    o.sale_price_per_kg,
                    o.total_amount,
                    o.status,

                    c.name AS customer_name,
                    c.email AS customer_email,
                    c.password AS customer_password,
                    c.location AS customer_location,

                    p.crop_name,
                    p.category,
                    p.farmer_id,
                    p.quantity AS product_quantity,
                    p.price_per_kg,
                    p.freshness,

                    f.name AS farmer_name,
                    f.email AS farmer_email,
                    f.password AS farmer_password,
                    f.location AS farmer_location

                FROM orders o

                JOIN users c
                ON o.customer_id = c.user_id

                JOIN products p
                ON o.product_id = p.product_id

                JOIN users f
                ON p.farmer_id = f.user_id
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                // ==========================================
                // CUSTOMER
                // ==========================================

                Customer customer =
                        new Customer(
                                resultSet.getInt(
                                        "customer_id"
                                ),

                                resultSet.getString(
                                        "customer_name"
                                ),

                                resultSet.getString(
                                        "customer_email"
                                ),

                                resultSet.getString(
                                        "customer_password"
                                ),

                                resultSet.getString(
                                        "customer_location"
                                )
                        );


                // ==========================================
                // FARMER
                // ==========================================

                Farmer farmer =
                        new Farmer(
                                resultSet.getInt(
                                        "farmer_id"
                                ),

                                resultSet.getString(
                                        "farmer_name"
                                ),

                                resultSet.getString(
                                        "farmer_email"
                                ),

                                resultSet.getString(
                                        "farmer_password"
                                ),

                                resultSet.getString(
                                        "farmer_location"
                                )
                        );


                // ==========================================
                // CROP
                // ==========================================

                Crop crop =
                        new Crop(
                                resultSet.getInt(
                                        "product_id"
                                ),

                                resultSet.getString(
                                        "crop_name"
                                ),

                                resultSet.getString(
                                        "category"
                                )
                        );


                // ==========================================
                // PRODUCT
                // ==========================================

                Product product =
                        new Product(
                                resultSet.getInt(
                                        "product_id"
                                ),

                                crop,

                                farmer,

                                resultSet.getDouble(
                                        "product_quantity"
                                ),

                                resultSet.getDouble(
                                        "price_per_kg"
                                ),

                                FreshnessLevel.valueOf(
                                        resultSet.getString(
                                                "freshness"
                                        )
                                )
                        );


                // ==========================================
                // ORDER
                // ==========================================

                Order order =
                        new Order(
                                resultSet.getInt(
                                        "order_id"
                                ),

                                customer,

                                product,

                                resultSet.getDouble(
                                        "quantity"
                                ),

                                resultSet.getDouble(
                                        "sale_price_per_kg"
                                )
                        );


                // ==========================================
                // ORDER STATUS
                // ==========================================

                order.setStatus(
                        OrderStatus.valueOf(
                                resultSet.getString(
                                        "status"
                                )
                        )
                );


                orders.add(order);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Order retrieval error: "
                    + e.getMessage()
            );
        }

        return orders;
    }


    // =====================================================
    // GET ORDERS BY CUSTOMER
    // =====================================================

    public List<Order> getOrdersByCustomer(
            int customerId) {

        List<Order> orders =
                new ArrayList<>();

        for (Order order : getAllOrders()) {

            if (
                    order.getCustomer()
                            .getUserId()
                    == customerId
            ) {

                orders.add(order);
            }
        }

        return orders;
    }


    // =====================================================
    // GET ORDERS BY FARMER
    // =====================================================

    public List<Order> getOrdersByFarmer(
            int farmerId) {

        List<Order> orders =
                new ArrayList<>();

        for (Order order : getAllOrders()) {

            if (
                    order.getProduct()
                            .getFarmer()
                            .getUserId()
                    == farmerId
            ) {

                orders.add(order);
            }
        }

        return orders;
    }


    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    public boolean updateOrderStatus(
            int orderId,
            OrderStatus status) {

        String sql = """
                UPDATE orders
                SET status = ?
                WHERE order_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status.name()
            );

            statement.setInt(
                    2,
                    orderId
            );

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Order status update error: "
                    + e.getMessage()
            );

            return false;
        }
    }
}