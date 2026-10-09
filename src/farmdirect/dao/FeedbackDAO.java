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
import farmdirect.model.Feedback;
import farmdirect.model.FreshnessLevel;
import farmdirect.model.Product;

public class FeedbackDAO {

    // =====================================================
    // ADD FEEDBACK
    // =====================================================

    public boolean addFeedback(Feedback feedback) {

        String sql = """
                INSERT INTO feedback
                (
                    customer_id,
                    farmer_id,
                    product_id,
                    rating,
                    comment
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    feedback.getCustomer().getUserId()
            );

            statement.setInt(
                    2,
                    feedback.getFarmer().getUserId()
            );

            statement.setInt(
                    3,
                    feedback.getProduct().getProductId()
            );

            statement.setInt(
                    4,
                    feedback.getRating()
            );

            statement.setString(
                    5,
                    feedback.getComment()
            );

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Feedback insertion error: "
                    + e.getMessage()
            );

            return false;
        }
    }


    // =====================================================
    // GET ALL FEEDBACK
    // =====================================================

    public List<Feedback> getAllFeedback() {

        List<Feedback> feedbackList =
                new ArrayList<>();

        String sql = """
                SELECT

                    fb.feedback_id,
                    fb.customer_id,
                    fb.farmer_id,
                    fb.product_id,
                    fb.rating,
                    fb.comment,

                    c.name AS customer_name,
                    c.email AS customer_email,
                    c.password AS customer_password,
                    c.location AS customer_location,

                    f.name AS farmer_name,
                    f.email AS farmer_email,
                    f.password AS farmer_password,
                    f.location AS farmer_location,

                    p.crop_name,
                    p.category,
                    p.quantity,
                    p.price_per_kg,
                    p.freshness

                FROM feedback fb

                JOIN users c
                ON fb.customer_id = c.user_id

                JOIN users f
                ON fb.farmer_id = f.user_id

                JOIN products p
                ON fb.product_id = p.product_id
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


                // ==========================================
                // FEEDBACK
                // ==========================================

                Feedback feedback =
                        new Feedback(
                                resultSet.getInt(
                                        "feedback_id"
                                ),

                                customer,

                                farmer,

                                product,

                                resultSet.getInt(
                                        "rating"
                                ),

                                resultSet.getString(
                                        "comment"
                                )
                        );


                feedbackList.add(
                        feedback
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Feedback retrieval error: "
                    + e.getMessage()
            );
        }


        return feedbackList;
    }


    // =====================================================
    // GET FEEDBACK BY FARMER
    // =====================================================

    public List<Feedback> getFeedbackByFarmer(
            int farmerId) {

        List<Feedback> feedbackList =
                new ArrayList<>();

        for (
                Feedback feedback :
                getAllFeedback()
        ) {

            if (
                    feedback.getFarmer()
                            .getUserId()
                    == farmerId
            ) {

                feedbackList.add(
                        feedback
                );
            }
        }

        return feedbackList;
    }


    // =====================================================
    // GET AVERAGE FARMER RATING
    // =====================================================

    public double getAverageRating(
            int farmerId) {

        String sql = """
                SELECT AVG(rating)
                FROM feedback
                WHERE farmer_id = ?
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

                if (resultSet.next()) {

                    double average =
                            resultSet.getDouble(1);

                    return average;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Rating calculation error: "
                    + e.getMessage()
            );
        }


        return 0.0;
    }


    // =====================================================
    // GET NUMBER OF RATINGS
    // =====================================================

    public int getRatingCount(
            int farmerId) {

        String sql = """
                SELECT COUNT(*)
                FROM feedback
                WHERE farmer_id = ?
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

                if (resultSet.next()) {

                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Rating count error: "
                    + e.getMessage()
            );
        }


        return 0;
    }
}