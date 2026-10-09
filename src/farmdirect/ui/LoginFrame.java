
package farmdirect.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import farmdirect.dao.UserDAO;
import farmdirect.model.Customer;
import farmdirect.model.Farmer;
import farmdirect.model.User;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    private final Color green = new Color(34, 139, 34);
    private final Color darkGreen = new Color(27, 94, 32);
    private final Color background = new Color(242, 247, 240);

    public LoginFrame() {

        setTitle("FarmDirect - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // A reasonable initial size instead of stretching every
        // form component across the entire screen.
        setSize(760, 520);
        setMinimumSize(new Dimension(600, 450));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(background);

        // ================================================
        // HEADER
        // ================================================

        JLabel title = new JLabel(
                "WELCOME TO FARMDIRECT",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkGreen);
        header.setBorder(
                BorderFactory.createEmptyBorder(18, 15, 18, 15)
        );

        header.add(title, BorderLayout.CENTER);

        // ================================================
        // FORM PANEL
        // ================================================

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 220)
                        ),
                        BorderFactory.createEmptyBorder(
                                30, 35, 30, 35
                        )
                )
        );

        emailField = new JTextField(22);
        passwordField = new JPasswordField(22);

        roleBox = new JComboBox<>(
                new String[]{"FARMER", "CUSTOMER"}
        );

        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        roleBox.setFont(new Font("Arial", Font.PLAIN, 15));

        emailField.setPreferredSize(new Dimension(280, 36));
        passwordField.setPreferredSize(new Dimension(280, 36));
        roleBox.setPreferredSize(new Dimension(280, 36));

        addFormRow(formPanel, "Email:", emailField, 0);
        addFormRow(formPanel, "Password:", passwordField, 1);
        addFormRow(formPanel, "Login As:", roleBox, 2);

        // ================================================
        // BUTTONS
        // ================================================

        JButton loginButton = new JButton("LOGIN");
        JButton registerButton = new JButton("REGISTER");

        loginButton.setPreferredSize(new Dimension(130, 40));
        registerButton.setPreferredSize(new Dimension(130, 40));

        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        loginButton.setBackground(green);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(darkGreen);
        registerButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0)
        );

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // ================================================
        // CENTER CONTENT
        // ================================================

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(background);

        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 230, 220)
                )
        );

        card.add(formPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints contentConstraints =
                new GridBagConstraints();

        contentConstraints.gridx = 0;
        contentConstraints.gridy = 0;
        contentConstraints.weightx = 1;
        contentConstraints.weighty = 1;
        contentConstraints.anchor = GridBagConstraints.CENTER;
        contentConstraints.fill = GridBagConstraints.NONE;
        contentPanel.add(card, contentConstraints);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(background);
        outerPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        );
        outerPanel.add(contentPanel, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(outerPanel, BorderLayout.CENTER);

        // ================================================
        // BUTTON ACTIONS
        // ================================================

        loginButton.addActionListener(e -> loginUser());

        registerButton.addActionListener(e -> {
            dispose();
            new RegistrationFrame().setVisible(true);
        });

        // Press Enter in the password field to log in.
        passwordField.addActionListener(e -> loginUser());
    }

    // ================================================
    // FORM ROW HELPER
    // ================================================

    private void addFormRow(
            JPanel panel,
            String labelText,
            java.awt.Component field,
            int row) {

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.insets = new Insets(12, 10, 12, 10);
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.LINE_START;

        constraints.gridx = 0;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        panel.add(field, constraints);
    }

    // ================================================
    // LOGIN
    // ================================================

    private void loginUser() {

        String email = emailField.getText().trim();
        String password =
                new String(passwordField.getPassword());

        String role = (String) roleBox.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your email and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        UserDAO userDAO = new UserDAO();

        User user = userDAO.loginUser(
                email,
                password,
                role
        );

        if (user == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email, password, or account type.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Login successful!\nWelcome, "
                + user.getName() + "!",
                "FarmDirect",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        if (user instanceof Farmer) {

            new FarmerDashboard((Farmer) user)
                    .setVisible(true);

        } else if (user instanceof Customer) {

            new CustomerDashboard((Customer) user)
                    .setVisible(true);
        }
    }

    // ================================================
    // MAIN METHOD FOR DIRECT TESTING
    // ================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.out.println(
                        "Could not apply system appearance."
                );
            }

            new LoginFrame().setVisible(true);
        });
    }
}
