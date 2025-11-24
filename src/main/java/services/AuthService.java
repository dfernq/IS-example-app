package services;

import model.Validatable;
import model.dao.CrudDAO;
import model.entities.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private final CrudDAO<User, Long> userDAO;

    public AuthService(CrudDAO<User, Long> userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String password) {
        Credentials credentials = sanitize(username, password);
        User user = findByUsername(credentials.username());
        if (user == null || !passwordMatches(user, credentials.password())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }

    public User signup(String username, String password) throws Validatable.ValidationException {
        Credentials credentials = sanitize(username, password);
        User existing = findByUsername(credentials.username());
        if (existing != null) {
            if (!passwordMatches(existing, credentials.password())) {
                throw new IllegalArgumentException("Username already taken");
            }
            return existing; // idempotent signup: treat as login
        }

        User user = User.builder()
                .username(credentials.username())
                .password(hashPassword(credentials.password()))
                .build();
        try {
            user.validate();
        } catch (Validatable.ValidationException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return userDAO.create(user);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hashed) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private boolean passwordMatches(User user, String plainPassword) {
        return hashPassword(plainPassword).equals(user.getPassword());
    }

    private User findByUsername(String username) {
        return userDAO.list().stream()
                .filter(user -> username.equalsIgnoreCase(user.getUsername()))
                .findFirst()
                .orElse(null);
    }

    private Credentials sanitize(String username, String password) {
        String sanitizedUsername = username == null ? "" : username.trim();
        String sanitizedPassword = password == null ? "" : password;
        if (sanitizedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (sanitizedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return new Credentials(sanitizedUsername, sanitizedPassword);
    }

    private record Credentials(String username, String password) {
    }
}