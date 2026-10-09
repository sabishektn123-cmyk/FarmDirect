
package farmdirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import farmdirect.database.DatabaseConnection;
import farmdirect.model.BulkListing;
import farmdirect.model.Crop;
import farmdirect.model.Farmer;

public class BulkListingDAO {

    // Create a new bulk listing.
    public int addListing(BulkListing listing) {

        String sql = """
                INSERT INTO bulk_listings
                (farmer_id, crop_name, category, quantity, minimum_price)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setInt(1, listing.getFarmer().getUserId());
            statement.setString(2, listing.getCrop().getCropName());
            statement.setString(3, listing.getCrop().getCategory());
            statement.setDouble(4, listing.getQuantity());
            statement.setDouble(5, listing.getMinimumPrice());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Bulk listing insertion error: "
                    + e.getMessage());
        }

        return -1;
    }


    // Return all listings, including sold listings.
    public List<BulkListing> getAllListings() {

        String sql = """
                SELECT b.listing_id, b.farmer_id,
                       b.crop_name, b.category,
                       b.quantity, b.minimum_price,
                       u.name, u.email, u.password, u.location
                FROM bulk_listings b
                JOIN users u ON b.farmer_id = u.user_id
                ORDER BY b.listing_id DESC
                """;

        return fetchListings(sql, -1);
    }


    // Return only unsold listings for customers.
    public List<BulkListing> getAvailableListings() {

        String sql = """
                SELECT b.listing_id, b.farmer_id,
                       b.crop_name, b.category,
                       b.quantity, b.minimum_price,
                       u.name, u.email, u.password, u.location
                FROM bulk_listings b
                JOIN users u ON b.farmer_id = u.user_id
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM bulk_sales bs
                    WHERE bs.listing_id = b.listing_id
                )
                ORDER BY b.listing_id DESC
                """;

        return fetchListings(sql, -1);
    }


    // Return only this farmer's unsold listings.
    public List<BulkListing> getAvailableListingsByFarmer(int farmerId) {

        String sql = """
                SELECT b.listing_id, b.farmer_id,
                       b.crop_name, b.category,
                       b.quantity, b.minimum_price,
                       u.name, u.email, u.password, u.location
                FROM bulk_listings b
                JOIN users u ON b.farmer_id = u.user_id
                WHERE b.farmer_id = ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM bulk_sales bs
                      WHERE bs.listing_id = b.listing_id
                  )
                ORDER BY b.listing_id DESC
                """;

        return fetchListings(sql, farmerId);
    }


    // Return all listings belonging to a farmer,
    // including sold listings for history purposes.
    public List<BulkListing> getListingsByFarmer(int farmerId) {

        String sql = """
                SELECT b.listing_id, b.farmer_id,
                       b.crop_name, b.category,
                       b.quantity, b.minimum_price,
                       u.name, u.email, u.password, u.location
                FROM bulk_listings b
                JOIN users u ON b.farmer_id = u.user_id
                WHERE b.farmer_id = ?
                ORDER BY b.listing_id DESC
                """;

        return fetchListings(sql, farmerId);
    }


    // Shared database retrieval method.
    private List<BulkListing> fetchListings(
            String sql, int farmerId) {

        List<BulkListing> listings = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            if (farmerId >= 0) {
                statement.setInt(1, farmerId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    listings.add(mapListing(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Bulk listing retrieval error: "
                    + e.getMessage());
        }

        return listings;
    }


    // Convert a database row into a BulkListing object.
    private BulkListing mapListing(ResultSet resultSet)
            throws SQLException {

        int listingId = resultSet.getInt("listing_id");

        Farmer farmer = new Farmer(
                resultSet.getInt("farmer_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("location")
        );

        Crop crop = new Crop(
                listingId,
                resultSet.getString("crop_name"),
                resultSet.getString("category")
        );

        return new BulkListing(
                listingId,
                farmer,
                crop,
                resultSet.getDouble("quantity"),
                resultSet.getDouble("minimum_price")
        );
    }
}
