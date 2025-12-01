package controllers;

import events.AppEventType;
import events.EventBus;
import model.entities.User;
import services.AuthService;
import views.LoginView;

/**
 * Handles login and sign-up flow, notifies listeners when authentication succeeds.
 */
public class LoginController implements Controller {

    private final AuthService authService;
    private final LoginView view;
    private final EventBus eventBus;

    public LoginController(AuthService authService, LoginView view, EventBus eventBus) {
        this.authService = authService;
        this.view = view;
        this.eventBus = eventBus;
        wireActions();
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
        view.onLogin(this::handleLogin);
        view.onSignup(this::handleSignup);
    }

    private void handleLogin() {
        try {
            User user = authService.login(view.getUsernameInput(), view.getPasswordInput());
            view.clearPassword();
            view.setVisible(false);
            eventBus.publish(AppEventType.LOGIN_SUCCESS, user);
        } catch (RuntimeException e) {
            view.showError(e.getMessage());
            view.clearPassword();
        }
    }

    private void handleSignup() {
        try {
            User user = authService.signup(view.getUsernameInput(), view.getPasswordInput());
            view.clearPassword();
            view.setVisible(false);
            eventBus.publish(AppEventType.LOGIN_SUCCESS, user);
        } catch (Exception e) {
            view.showError(e.getMessage());
            view.clearPassword();
        }
    }
}
