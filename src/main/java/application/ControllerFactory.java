package application;

import controllers.LoginController;
import controllers.TodoController;
import events.EventBus;
import model.entities.User;
import services.AuthService;
import services.TaskService;
import views.ViewFactory;

/**
 * Builds controllers and injects their required dependencies.
 */
public class ControllerFactory {

    private final TaskService taskService;
    private final AuthService authService;
    private final EventBus eventBus;
    private final ViewFactory viewFactory;

    public ControllerFactory(TaskService taskService, AuthService authService, EventBus eventBus, ViewFactory viewFactory) {
        this.taskService = taskService;
        this.authService = authService;
        this.eventBus = eventBus;
        this.viewFactory = viewFactory;
    }

    public LoginController createLoginController() {
        return new LoginController(authService, viewFactory.createLoginView(), eventBus);
    }

    public TodoController createTodoController(User user) {
        return new TodoController(taskService, viewFactory.createTodoView(), user, eventBus);
    }
}
