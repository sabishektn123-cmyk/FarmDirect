package farmdirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import farmdirect.database.DatabaseConnection;
import farmdirect.model.Customer;
import farmdirect.model.Farmer;
import farmdirect.model.User;

public class UserDAO {

    // ==========================================
    // REGISTER USER
    // ==========================================

    public boolean registerUser(User user, String location) {

        String sql = """
                INSERT INTO users
                (name, email, password, role, location)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, location);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Registration error: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // ==========================================
    // LOGIN USER
    // ==========================================

    public User loginUser(
            String email,
            String password,
            String role) {

        String sql = """
                SELECT user_id,
                       name,
                       email,
                       password,
                       role,
                       location
                FROM users
                WHERE email = ?
                AND password = ?
                AND role = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, role);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                int userId =
                        resultSet.getInt("user_id");

                String name =
                        resultSet.getString("name");

                String userEmail =
                        resultSet.getString("email");

                String userPassword =
                        resultSet.getString("password");

                String userRole =
                        resultSet.getString("role");

                String location =
                        resultSet.getString("location");

                // =========================
                // FARMER
                // =========================

                if (userRole.equalsIgnoreCase("FARMER")) {

                    return new Farmer(
                            userId,
                            name,
                            userEmail,
                            userPassword,
                            location
                    );
                }

                // =========================
                // CUSTOMER
                // =========================

                if (userRole.equalsIgnoreCase("CUSTOMER")) {

                    return new Customer(
                            userId,
                            name,
                            userEmail,
                            userPassword,
                            location
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Login error: "
                    + e.getMessage()
            );
        }

        return null;
    }
}