package model.dao.impl;

import model.EntityMapper;
import model.QueryLoader;
import model.Validatable;
import model.dao.CrudDAO;
import model.entities.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskDAOImpl implements CrudDAO<Task, Long> {

    private final Connection connection;

    public TaskDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Task create(Task task) {
        validate(task);
        String query = QueryLoader.getQuery("insert_task.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, Boolean.TRUE.equals(task.getCompleted()));
            stmt.setLong(4, task.getUserId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create task", e);
        }

        return task;
    }

    @Override
    public Task read(Long id) {
        String query = QueryLoader.getQuery("select_task_by_id.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return EntityMapper.mapResultSetToEntity(rs, Task.class);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read task", e);
        }
    }

    @Override
    public Task update(Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task id is required to update");
        }
        validate(task);

        String query = QueryLoader.getQuery("update_task.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, Boolean.TRUE.equals(task.getCompleted()));
            stmt.setLong(4, task.getId());
            stmt.setLong(5, task.getUserId());
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Task not found or does not belong to user");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update task", e);
        }

        return task;
    }

    @Override
    public void delete(Long id) {
        String query = QueryLoader.getQuery("delete_task.ftl", Collections.emptyMap());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    @Override
    public List<Task> list() {
        String query = QueryLoader.getQuery("select_all_tasks.ftl", Collections.emptyMap());
        List<Task> tasks = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tasks.add(EntityMapper.mapResultSetToEntity(rs, Task.class));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list tasks", e);
        }

        return tasks;
    }

    private void validate(Task task) {
        try {
            task.validate();
        } catch (Validatable.ValidationException e) {
            throw new IllegalArgumentException("Task payload is invalid", e);
        }
    }
}
