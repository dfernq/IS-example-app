package model.dao.impl;

import model.EntityMapper;
import model.QueryLoader;
import model.dao.UserDAO;
import model.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {

    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User user){
        String query = QueryLoader.getQuery("insert_user.ftl", Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "password", user.getPassword()
        ));
        try (PreparedStatement stmt = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }

        return user;
    }

    @Override
    public User read(Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public User update(Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<User> list() {
        List<User> users = new ArrayList<>();
        String query = QueryLoader.getQuery("select_all_users.ftl",
                Collections.emptyMap());

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                users.add(EntityMapper.mapResultSetToEntity(rs, User.class));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list users", e);
        }

        return users;
    }

}
