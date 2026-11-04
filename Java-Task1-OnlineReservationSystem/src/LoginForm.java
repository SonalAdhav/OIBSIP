import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        // Window settings
        setTitle("Online Reservation System - Login");
        setSize(380, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // opens in the center of the screen
        setLayout(new BorderLayout(10, 10));

        // Heading at the top
        JLabel heading = new JLabel("Login", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(heading, BorderLayout.NORTH);

        // Middle panel with username and password
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        form.add(new JLabel("Username:"));
        usernameField = new JTextField();
        form.add(usernameField);

        form.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        form.add(passwordField);

        add(form, BorderLayout.CENTER);

        // Login button at the bottom
        JButton loginButton = new JButton("Login");
        JPanel bottom = new JPanel();
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bottom.add(loginButton);
        add(bottom, BorderLayout.SOUTH);

        // When the button is clicked
        loginButton.addActionListener(e -> doLogin());

        // Pressing Enter in the password box also logs in
        passwordField.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validation: no empty fields
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Database.checkLogin(username, password)) {
            new ReservationForm().setVisible(true);
            dispose(); // closes the login window
        } else {
            JOptionPane.showMessageDialog(this,
                    "Access Denied: wrong username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}