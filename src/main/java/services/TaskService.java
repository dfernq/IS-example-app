package services;

import model.Validatable;
import model.dao.CrudDAO;
import model.entities.Task;

import java.util.List;

/**
 * Coordinates Task DAO operations and provides business-oriented helpers for controllers.
 */
public class TaskService {

    private final CrudDAO<Task, Long> taskDAO;

    public TaskService(CrudDAO<Task, Long> taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getTasksForUser(Long userId) {
        return taskDAO.list().stream()
                .filter(task -> userId.equals(task.getUserId()))
                .toList();
    }

    public Task createTask(Long userId, String title, String description) throws Validatable.ValidationException {
        Task task = Task.builder()
                .title(title)
                .description(description)
                .completed(false)
                .userId(userId)
                .build();
        return taskDAO.create(task);
    }

    public Task toggleCompletion(Long userId, Long taskId) throws Validatable.ValidationException {
        Task existing = requireTask(userId, taskId);
        existing.setCompleted(!Boolean.TRUE.equals(existing.getCompleted()));
        return taskDAO.update(existing);
    }

    public Task updateTask(Long userId, Long taskId, String title, String description, boolean completed) throws Validatable.ValidationException {
        Task existing = requireTask(userId, taskId);
        existing.setTitle(title);
        existing.setDescription(description);
        existing.setCompleted(completed);
        return taskDAO.update(existing);
    }

    public void deleteTask(Long userId, Long taskId) {
        requireTask(userId, taskId);
        taskDAO.delete(taskId);
    }

    private Task requireTask(Long userId, Long taskId) {
        Task task = taskDAO.read(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        if (!task.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Task does not belong to the current user");
        }
        try {
            task.validate();
        } catch (Validatable.ValidationException e) {
            throw new IllegalStateException("Stored task is invalid", e);
        }
        return task;
    }
}