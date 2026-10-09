
package farmdirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import farmdirect.database.DatabaseConnection;
import farmdirect.model.BulkSaleRecord;

public class BulkSaleDAO {

    public static class SaleResult {

        private final boolean success;
        private final String message;
        private final String customerName;
        private final double pricePerKg;
        private final double quantity;
        private final double totalAmount;

        public SaleResult(
                boolean success,
                String message,
                String customerName,
                double pricePerKg,
                double quantity,
                double totalAmount) {

            this.success = success;
            this.message = message;
            this.customerName = customerName;
            this.pricePerKg = pricePerKg;
            this.quantity = quantity;
            this.totalAmount = totalAmount;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getCustomerName() {
            return customerName;
        }

        public double getPricePerKg() {
            return pricePerKg;
        }

        public double getQuantity() {
            return quantity;
        }

        public double getTotalAmount() {
            return totalAmount;
        }
    }

    public BulkSaleDAO() {
        createTable();
    }

    private void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS bulk_sales (
                    sale_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    listing_id INTEGER NOT NULL UNIQUE,
                    bid_id INTEGER NOT NULL,
                    farmer_id INTEGER NOT NULL,
                    customer_id INTEGER NOT NULL,
                    crop_name TEXT NOT NULL,
                    quantity REAL NOT NULL,
                    price_per_kg REAL NOT NULL,
                    total_amount REAL NOT NULL,
                    sale_date TEXT DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(
                    "Bulk sale table error: " + e.getMessage()
            );
        }
    }

    // Retrieve accepted bulk sales for one customer.
    public List<BulkSaleRecord> getSalesByCustomer(int customerId) {

        List<BulkSaleRecord> sales = new ArrayList<>();

        String sql = """
                SELECT
                    bs.sale_id,
                    bs.listing_id,
                    bs.crop_name,
                    bs.quantity,
                    bs.price_per_kg,
                    bs.total_amount,
                    bs.sale_date,
                    c.name AS customer_name,
                    f.name AS farmer_name
                FROM bulk_sales bs
                JOIN users c
                    ON c.user_id = bs.customer_id
                JOIN users f
                    ON f.user_id = bs.farmer_id
                WHERE bs.customer_id = ?
                ORDER BY bs.sale_id DESC
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    BulkSaleRecord sale = new BulkSaleRecord(
                            rs.getInt("sale_id"),
                            rs.getInt("listing_id"),
                            rs.getString("crop_name"),
                            rs.getString("farmer_name"),
                            rs.getString("customer_name"),
                            rs.getDouble("quantity"),
                            rs.getDouble("price_per_kg"),
                            rs.getDouble("total_amount"),
                            rs.getString("sale_date")
                    );

                    sales.add(sale);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Customer bulk sale retrieval error: "
                    + e.getMessage()
            );
        }

        return sales;
    }

    // Accept the highest valid bid and record the sale.
    public SaleResult acceptHighestBid(
            int listingId,
            int farmerId) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                String listingSql = """
                        SELECT crop_name, quantity, minimum_price
                        FROM bulk_listings
                        WHERE listing_id = ? AND farmer_id = ?
                        """;

                String cropName;
                double quantity;
                double minimumPrice;

                try (PreparedStatement statement =
                             connection.prepareStatement(listingSql)) {

                    statement.setInt(1, listingId);
                    statement.setInt(2, farmerId);

                    try (ResultSet rs = statement.executeQuery()) {

                        if (!rs.next()) {
                            connection.rollback();
                            return result(
                                    false,
                                    "Listing not found or does not belong to you."
                            );
                        }

                        cropName = rs.getString("crop_name");
                        quantity = rs.getDouble("quantity");
                        minimumPrice = rs.getDouble("minimum_price");
                    }
                }

                String soldSql = """
                        SELECT sale_id
                        FROM bulk_sales
                        WHERE listing_id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(soldSql)) {

                    statement.setInt(1, listingId);

                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            connection.rollback();
                            return result(
                                    false,
                                    "This listing has already been sold."
                            );
                        }
                    }
                }

                String bidSql = """
                        SELECT b.bid_id,
                               b.customer_id,
                               b.bid_price_per_kg,
                               u.name
                        FROM bids b
                        JOIN users u ON u.user_id = b.customer_id
                        WHERE b.listing_id = ?
                          AND b.bid_price_per_kg >= ?
                        ORDER BY b.bid_price_per_kg DESC, b.bid_id ASC
                        LIMIT 1
                        """;

                int bidId;
                int customerId;
                double pricePerKg;
                String customerName;

                try (PreparedStatement statement =
                             connection.prepareStatement(bidSql)) {

                    statement.setInt(1, listingId);
                    statement.setDouble(2, minimumPrice);

                    try (ResultSet rs = statement.executeQuery()) {

                        if (!rs.next()) {
                            connection.rollback();
                            return result(
                                    false,
                                    "No valid bids have been received."
                            );
                        }

                        bidId = rs.getInt("bid_id");
                        customerId = rs.getInt("customer_id");
                        pricePerKg = rs.getDouble("bid_price_per_kg");
                        customerName = rs.getString("name");
                    }
                }

                double totalAmount = quantity * pricePerKg;

                String insertSql = """
                        INSERT INTO bulk_sales
                        (listing_id, bid_id, farmer_id, customer_id,
                         crop_name, quantity, price_per_kg, total_amount)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(insertSql)) {

                    statement.setInt(1, listingId);
                    statement.setInt(2, bidId);
                    statement.setInt(3, farmerId);
                    statement.setInt(4, customerId);
                    statement.setString(5, cropName);
                    statement.setDouble(6, quantity);
                    statement.setDouble(7, pricePerKg);
                    statement.setDouble(8, totalAmount);

                    statement.executeUpdate();
                }

                connection.commit();

                return new SaleResult(
                        true,
                        "Bulk sale accepted successfully.",
                        customerName,
                        pricePerKg,
                        quantity,
                        totalAmount
                );

            } catch (SQLException e) {

                connection.rollback();

                if (e.getMessage() != null
                        && e.getMessage().toLowerCase()
                                .contains("unique")) {
                    return result(
                            false,
                            "This listing has already been sold."
                    );
                }

                return result(
                        false,
                        "Could not complete sale: " + e.getMessage()
                );

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            return result(
                    false,
                    "Database error: " + e.getMessage()
            );
        }
    }

    private SaleResult result(boolean success, String message) {
        return new SaleResult(
                success, message, "", 0, 0, 0
        );
    }
}
