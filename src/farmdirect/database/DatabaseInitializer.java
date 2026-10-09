package farmdirect.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {

        // =================================================
        // USERS TABLE
        // =================================================

        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,
                    location TEXT
                )
                """;


        // =================================================
        // PRODUCTS TABLE
        // =================================================

        String productsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    product_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    crop_name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    farmer_id INTEGER NOT NULL,
                    quantity REAL NOT NULL,
                    price_per_kg REAL NOT NULL,
                    freshness TEXT NOT NULL
                )
                """;


        // =================================================
        // ORDERS TABLE
        // =================================================

        String ordersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    quantity REAL NOT NULL,
                    sale_price_per_kg REAL NOT NULL,
                    total_amount REAL NOT NULL,
                    status TEXT NOT NULL
                )
                """;


        // =================================================
        // FEEDBACK TABLE
        // =================================================

        String feedbackTable = """
                CREATE TABLE IF NOT EXISTS feedback (
                    feedback_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    farmer_id INTEGER NOT NULL,
                    product_id INTEGER NOT NULL,
                    rating INTEGER NOT NULL,
                    comment TEXT
                )
                """;


        // =================================================
        // BULK LISTINGS TABLE
        // =================================================

        String bulkListingsTable = """
                CREATE TABLE IF NOT EXISTS bulk_listings (
                    listing_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    farmer_id INTEGER NOT NULL,
                    crop_name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    quantity REAL NOT NULL,
                    minimum_price REAL NOT NULL
                )
                """;


        // =================================================
        // BIDS TABLE
        // =================================================

        String bidsTable = """
                CREATE TABLE IF NOT EXISTS bids (
                    bid_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    listing_id INTEGER NOT NULL,
                    customer_id INTEGER NOT NULL,
                    bid_price_per_kg REAL NOT NULL
                )
                """;


        // =================================================
        // CREATE TABLES
        // =================================================

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                Statement statement =
                        connection.createStatement()
        ) {

            statement.executeUpdate(
                    usersTable
            );

            statement.executeUpdate(
                    productsTable
            );

            statement.executeUpdate(
                    ordersTable
            );

            statement.executeUpdate(
                    feedbackTable
            );

            statement.executeUpdate(
                    bulkListingsTable
            );

            statement.executeUpdate(
                    bidsTable
            );


            System.out.println(
                    "Database tables created successfully."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database initialization error: "
                    + e.getMessage()
            );
        }
    }
}