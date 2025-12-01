package application;

import controllers.Controller;
import events.AppEventType;
import events.Command;
import events.CommandFactory;
import events.EventBus;
import model.entities.User;

/**
 * Central navigation orchestrator: listens to events and swaps controllers.
 */
public class ApplicationCoordinator {

    private final ControllerFactory controllerFactory;
    private final EventBus eventBus;
    private Controller activeController;

    public ApplicationCoordinator(ControllerFactory controllerFactory, EventBus eventBus) {
        this.controllerFactory = controllerFactory;
        this.eventBus = eventBus;
        wireEvents();
    }

    public void start() {
        switchTo(controllerFactory.createLoginController());
    }

    private void wireEvents() {
        eventBus.subscribe(AppEventType.LOGIN_SUCCESS, loginSuccessCommand());
        // Future: eventBus.subscribe(AppEventType.SHOW_DONE_TASKS, doneTasksCommand());
    }

    private CommandFactory<User> loginSuccessCommand() {
        return user -> (Command) () -> switchTo(controllerFactory.createTodoController(user));
    }

    private void switchTo(Controller controller) {
        if (activeController != null) {
            activeController.close();
        }
        activeController = controller;
        activeController.show();
    }
}
