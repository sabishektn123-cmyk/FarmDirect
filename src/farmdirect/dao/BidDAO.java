package farmdirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import farmdirect.database.DatabaseConnection;
import farmdirect.model.Bid;
import farmdirect.model.Customer;

public class BidDAO {

    // =====================================================
    // ADD BID
    // =====================================================

    public int addBid(
            int listingId,
            Bid bid) {

        String sql = """
                INSERT INTO bids
                (
                    listing_id,
                    customer_id,
                    bid_price_per_kg
                )
                VALUES (?, ?, ?)
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

            statement.setInt(
                    1,
                    listingId
            );

            statement.setInt(
                    2,
                    bid.getCustomer().getUserId()
            );

            statement.setDouble(
                    3,
                    bid.getBidPricePerKg()
            );


            statement.executeUpdate();


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
                    "Bid insertion error: "
                    + e.getMessage()
            );
        }


        return -1;
    }


    // =====================================================
    // GET BIDS FOR LISTING
    // =====================================================

    public List<Bid> getBidsForListing(
            int listingId) {

        List<Bid> bids =
                new ArrayList<>();


        String sql = """
                SELECT

                    b.bid_id,
                    b.customer_id,
                    b.bid_price_per_kg,

                    u.name,
                    u.email,
                    u.password,
                    u.location

                FROM bids b

                JOIN users u
                ON b.customer_id = u.user_id

                WHERE b.listing_id = ?
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    listingId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Customer customer =
                            new Customer(
                                    resultSet.getInt(
                                            "customer_id"
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


                    Bid bid =
                            new Bid(
                                    resultSet.getInt(
                                            "bid_id"
                                    ),

                                    customer,

                                    resultSet.getDouble(
                                            "bid_price_per_kg"
                                    )
                            );


                    bids.add(
                            bid
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Bid retrieval error: "
                    + e.getMessage()
            );
        }


        return bids;
    }


    // =====================================================
    // GET HIGHEST BID
    // =====================================================

    public Bid getHighestBid(
            int listingId) {

        String sql = """
                SELECT

                    b.bid_id,
                    b.customer_id,
                    b.bid_price_per_kg,

                    u.name,
                    u.email,
                    u.password,
                    u.location

                FROM bids b

                JOIN users u
                ON b.customer_id = u.user_id

                WHERE b.listing_id = ?

                ORDER BY
                    b.bid_price_per_kg DESC

                LIMIT 1
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    listingId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    Customer customer =
                            new Customer(
                                    resultSet.getInt(
                                            "customer_id"
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


                    return new Bid(
                            resultSet.getInt(
                                    "bid_id"
                            ),

                            customer,

                            resultSet.getDouble(
                                    "bid_price_per_kg"
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Highest bid retrieval error: "
                    + e.getMessage()
            );
        }


        return null;
    }
}