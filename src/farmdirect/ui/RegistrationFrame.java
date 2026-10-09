package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import farmdirect.dao.UserDAO;
import farmdirect.model.Customer;
import farmdirect.model.Farmer;
import farmdirect.model.User;

public class RegistrationFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField locationField;
    private JComboBox<String> roleBox;

    public RegistrationFrame() {

        setTitle("FarmDirect - Registration");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =========================
        // HEADER
        // =========================

        JLabel title = new JLabel(
                "Create FarmDirect Account",
                JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 139, 34));
        header.add(title, BorderLayout.CENTER);

        // =========================
        // FORM
        // =========================

        JPanel formPanel = new JPanel(
                new GridLayout(5, 2, 10, 15)
        );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 40, 20, 40
                )
        );

        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        locationField = new JTextField();

        roleBox = new JComboBox<>(
                new String[] {
                        "FARMER",
                        "CUSTOMER"
                }
        );

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleBox);

        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);

        // =========================
        // BUTTONS
        // =========================

        JButton registerButton =
                new JButton("REGISTER");

        JButton backButton =
                new JButton("BACK TO LOGIN");

        registerButton.setBackground(
                new Color(34, 139, 34)
        );

        registerButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // =========================
        // ADD COMPONENTS
        // =========================

        add(header, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // =========================
        // REGISTER BUTTON
        // =========================

        registerButton.addActionListener(e -> registerUser());

        // =========================
        // BACK BUTTON
        // =========================

        backButton.addActionListener(e -> {

            dispose();

            new LoginFrame().setVisible(true);
        });
    }

    // =========================
    // REGISTER USER
    // =========================

    private void registerUser() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        String role =
                (String) roleBox.getSelectedItem();

        String location =
                locationField.getText().trim();

        // =========================
        // VALIDATION
        // =========================

        if (name.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || location.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =========================
        // CREATE USER OBJECT
        // =========================

        User user;

        if (role.equals("FARMER")) {

            user = new Farmer(
                    0,
                    name,
                    email,
                    password,
                    location
            );

        } else {

            user = new Customer(
                    0,
                    name,
                    email,
                    password,
                    location
            );
        }

        // =========================
        // SAVE USER TO DATABASE
        // =========================

        UserDAO userDAO =
                new UserDAO();

        boolean success =
                userDAO.registerUser(
                        user,
                        location
                );

        // =========================
        // REGISTRATION RESULT
        // =========================

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful!\n"
                    + "You can now login to FarmDirect.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            new LoginFrame().setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed.\n"
                    + "Email may already exist.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // TEST MAIN METHOD
    // =========================

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            RegistrationFrame frame =
                    new RegistrationFrame();

            frame.setVisible(true);
        });
    }
}