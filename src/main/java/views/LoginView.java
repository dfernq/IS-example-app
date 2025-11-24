package views;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Login");
    private final JButton signupButton = new JButton("Sign Up");

    public LoginView() {
        super("TODO App - Login");
        initLayout();
    }

    private void initLayout() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(360, 220);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 4, 4));
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        formPanel.add(new JLabel("Username"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password"));
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(signupButton);
        buttonPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void onLogin(Runnable action) {
        loginButton.addActionListener(e -> action.run());
        passwordField.addActionListener(e -> action.run());
    }

    public void onSignup(Runnable action) {
        signupButton.addActionListener(e -> action.run());
    }

    public String getUsernameInput() {
        return usernameField.getText().trim();
    }

    public String getPasswordInput() {
        return new String(passwordField.getPassword());
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
