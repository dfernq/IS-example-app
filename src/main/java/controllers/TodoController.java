package controllers;

import model.entities.Task;
import model.entities.User;
import services.TaskService;
import views.TodoView;

import java.util.List;

/**
 * Connects the Swing view with the service layer.
 */
public class TodoController {

    private final TaskService taskService;
    private final TodoView view;
    private final User currentUser;

    public TodoController(TaskService taskService, TodoView view, User currentUser) {
        this.taskService = taskService;
        this.view = view;
        this.currentUser = currentUser;
        view.setCurrentUser(currentUser.getUsername());
        wireActions();
        refreshTasks();
    }

    public void show() {
        view.setVisible(true);
    }

    private void wireActions() {
        view.onAddTask(this::handleAddTask);
        view.onToggleTask(this::handleToggleTask);
        view.onDeleteTask(this::handleDeleteTask);
    }

    private void handleAddTask() {
        try {
            taskService.createTask(currentUser.getId(), view.getTitleInput(), view.getDescriptionInput());
            view.clearInputs();
            refreshTasks();
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    private void handleToggleTask() {
        Task selected = view.getSelectedTask();
        if (selected == null) {
            view.showInfo("Select a task to toggle its completion state.");
            return;
        }
        try {
            taskService.toggleCompletion(currentUser.getId(), selected.getId());
            refreshTasks();
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

    private void handleDeleteTask() {
        Task selected = view.getSelectedTask();
        if (selected == null) {
            view.showInfo("Select a task to delete.");
            return;
        }
        try {
            taskService.deleteTask(currentUser.getId(), selected.getId());
            refreshTasks();
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    private void refreshTasks() {
        List<Task> tasks = taskService.getTasksForUser(currentUser.getId());
        view.renderTasks(tasks);
    }
}