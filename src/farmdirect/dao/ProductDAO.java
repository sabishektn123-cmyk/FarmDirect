package farmdirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import farmdirect.database.DatabaseConnection;
import farmdirect.model.Crop;
import farmdirect.model.Farmer;
import farmdirect.model.FreshnessLevel;
import farmdirect.model.Product;

public class ProductDAO {

    // =====================================================
    // ADD PRODUCT
    // =====================================================

    public int addProduct(Product product) {

        String sql = """
                INSERT INTO products
                (crop_name, category, farmer_id,
                 quantity, price_per_kg, freshness)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    product.getCrop().getCropName()
            );

            statement.setString(
                    2,
                    product.getCrop().getCategory()
            );

            statement.setInt(
                    3,
                    product.getFarmer().getUserId()
            );

            statement.setDouble(
                    4,
                    product.getQuantity()
            );

            statement.setDouble(
                    5,
                    product.getPricePerKg()
            );

            statement.setString(
                    6,
                    product.getFreshness().name()
            );

            statement.executeUpdate();

            // Get automatically generated product ID
            try (
                    ResultSet keys =
                            statement.getGeneratedKeys()
            ) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Product insertion error: "
                    + e.getMessage()
            );
        }

        return -1;
    }


    // =====================================================
    // GET ALL PRODUCTS
    // =====================================================

    public List<Product> getAllProducts() {

        List<Product> products =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.product_id,
                    p.crop_name,
                    p.category,
                    p.farmer_id,
                    p.quantity,
                    p.price_per_kg,
                    p.freshness,

                    u.name,
                    u.email,
                    u.password,
                    u.location

                FROM products p

                JOIN users u
                ON p.farmer_id = u.user_id
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

                // -----------------------------
                // Product ID
                // -----------------------------

                int productId =
                        resultSet.getInt("product_id");


                // -----------------------------
                // Crop details
                // -----------------------------

                String cropName =
                        resultSet.getString("crop_name");

                String category =
                        resultSet.getString("category");


                // -----------------------------
                // Farmer ID
                // -----------------------------

                int farmerId =
                        resultSet.getInt("farmer_id");


                // -----------------------------
                // Product details
                // -----------------------------

                double quantity =
                        resultSet.getDouble("quantity");

                double price =
                        resultSet.getDouble("price_per_kg");

                String freshnessText =
                        resultSet.getString("freshness");


                // -----------------------------
                // Create Farmer object
                // -----------------------------

                Farmer farmer =
                        new Farmer(
                                farmerId,

                                resultSet.getString(
                                        "name"
                                ),

                                resultSet.getString(
                                        "email"
                                ),

                                resultSet.getString(
                                        "password"
                                ),

                                resultSet.getString(
                                        "location"
                                )
                        );


                // -----------------------------
                // Create Crop object
                // -----------------------------

                Crop crop =
                        new Crop(
                                productId,
                                cropName,
                                category
                        );


                // -----------------------------
                // Create Freshness enum
                // -----------------------------

                FreshnessLevel freshness =
                        FreshnessLevel.valueOf(
                                freshnessText
                        );


                // -----------------------------
                // Create Product object
                // -----------------------------

                Product product =
                        new Product(
                                productId,
                                crop,
                                farmer,
                                quantity,
                                price,
                                freshness
                        );


                products.add(product);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Product retrieval error: "
                    + e.getMessage()
            );
        }

        return products;
    }


    // =====================================================
    // GET PRODUCTS BY FARMER
    // =====================================================

    public List<Product> getProductsByFarmer(
            int farmerId) {

        List<Product> products =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.product_id,
                    p.crop_name,
                    p.category,
                    p.quantity,
                    p.price_per_kg,
                    p.freshness,

                    u.name,
                    u.email,
                    u.password,
                    u.location

                FROM products p

                JOIN users u
                ON p.farmer_id = u.user_id

                WHERE p.farmer_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    farmerId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    // -----------------------------
                    // Product ID
                    // -----------------------------

                    int productId =
                            resultSet.getInt(
                                    "product_id"
                            );


                    // -----------------------------
                    // Create Crop
                    // -----------------------------

                    Crop crop =
                            new Crop(
                                    productId,

                                    resultSet.getString(
                                            "crop_name"
                                    ),

                                    resultSet.getString(
                                            "category"
                                    )
                            );


                    // -----------------------------
                    // Create Farmer
                    // -----------------------------

                    Farmer farmer =
                            new Farmer(
                                    farmerId,

                                    resultSet.getString(
                                            "name"
                                    ),

                                    resultSet.getString(
                                            "email"
                                    ),

                                    resultSet.getString(
                                            "password"
                                    ),

                                    resultSet.getString(
                                            "location"
                                    )
                            );


                    // -----------------------------
                    // Create Product
                    // -----------------------------

                    Product product =
                            new Product(
                                    productId,

                                    crop,

                                    farmer,

                                    resultSet.getDouble(
                                            "quantity"
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


                    products.add(product);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Farmer product retrieval error: "
                    + e.getMessage()
            );
        }

        return products;
    }


    // =====================================================
    // GET SINGLE PRODUCT BY ID
    // =====================================================

    public Product getProductById(int productId) {

        String sql = """
                SELECT
                    p.product_id,
                    p.crop_name,
                    p.category,
                    p.farmer_id,
                    p.quantity,
                    p.price_per_kg,
                    p.freshness,

                    u.name,
                    u.email,
                    u.password,
                    u.location

                FROM products p

                JOIN users u
                ON p.farmer_id = u.user_id

                WHERE p.product_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    productId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    Farmer farmer =
                            new Farmer(
                                    resultSet.getInt(
                                            "farmer_id"
                                    ),

                                    resultSet.getString(
                                            "name"
                                    ),

                                    resultSet.getString(
                                            "email"
                                    ),

                                    resultSet.getString(
                                            "password"
                                    ),

                                    resultSet.getString(
                                            "location"
                                    )
                            );


                    Crop crop =
                            new Crop(
                                    productId,

                                    resultSet.getString(
                                            "crop_name"
                                    ),

                                    resultSet.getString(
                                            "category"
                                    )
                            );


                    FreshnessLevel freshness =
                            FreshnessLevel.valueOf(
                                    resultSet.getString(
                                            "freshness"
                                    )
                            );


                    return new Product(
                            productId,
                            crop,
                            farmer,

                            resultSet.getDouble(
                                    "quantity"
                            ),

                            resultSet.getDouble(
                                    "price_per_kg"
                            ),

                            freshness
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Product search error: "
                    + e.getMessage()
            );
        }

        return null;
    }


    // =====================================================
    // UPDATE PRODUCT QUANTITY
    // =====================================================

    public boolean updateQuantity(
            int productId,
            double newQuantity) {

        String sql = """
                UPDATE products
                SET quantity = ?
                WHERE product_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDouble(
                    1,
                    newQuantity
            );

            statement.setInt(
                    2,
                    productId
            );

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Quantity update error: "
                    + e.getMessage()
            );

            return false;
        }
    }
}