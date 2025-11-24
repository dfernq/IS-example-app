package model.dao.impl;

import model.EntityMapper;
import model.QueryLoader;
import model.Validatable;
import model.dao.CrudDAO;
import model.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements CrudDAO<User, Long> {

    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User user) throws Validatable.ValidationException {
        user.validate();
        String query = QueryLoader.getQuery("insert_user.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    @Override
    public User read(Long id) {
        String query = QueryLoader.getQuery("select_user_by_id.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return EntityMapper.mapResultSetToEntity(rs, User.class);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read user", e);
        }
        return null;
    }

    @Override
    public User update(User user) throws Validatable.ValidationException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User id required");
        }
        user.validate();
        String query = QueryLoader.getQuery("update_user.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setLong(3, user.getId());
            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = QueryLoader.getQuery("delete_user.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public List<User> list() {
        String query = QueryLoader.getQuery("select_all_users.ftl", Collections.emptyMap());
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(EntityMapper.mapResultSetToEntity(rs, User.class));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list users", e);
        }
    }
}