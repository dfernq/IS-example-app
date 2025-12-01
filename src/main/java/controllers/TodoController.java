package controllers;

import events.AppEventType;
import events.EventBus;
import model.entities.Task;
import model.entities.User;
import services.TaskService;
import views.TodoView;

import java.util.List;

/**
 * Connects the Swing view with the service layer.
 */
public class TodoController implements Controller {

    private final TaskService taskService;
    private final TodoView view;
    private final User currentUser;
    private final EventBus eventBus;

    public TodoController(TaskService taskService, TodoView view, User currentUser, EventBus eventBus) {
        this.taskService = taskService;
        this.view = view;
        this.currentUser = currentUser;
        this.eventBus = eventBus;
        view.setCurrentUser(currentUser.getUsername());
        wireActions();
        refreshTasks();
    }

    @Override
    public void show() {
        view.setVisible(true);
    }

    @Override
    public void close() {
        view.dispose();
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
            eventBus.publish(AppEventType.TASKS_CHANGED, currentUser);
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
            eventBus.publish(AppEventType.TASKS_CHANGED, currentUser);
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
            eventBus.publish(AppEventType.TASKS_CHANGED, currentUser);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
        }
    }

    private void refreshTasks() {
        List<Task> tasks = taskService.getTasksForUser(currentUser.getId());
        view.renderTasks(tasks);
    }
}
