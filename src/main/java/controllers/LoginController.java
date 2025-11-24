package controllers;

import model.entities.User;
import services.AuthService;
import views.LoginView;

import java.util.function.Consumer;

/**
 * Handles login and sign-up flow, notifies listeners when authentication succeeds.
 */
public class LoginController {

    private final AuthService authService;
    private final LoginView view;
    private final Consumer<User> onAuthenticated;

    public LoginController(AuthService authService, LoginView view, Consumer<User> onAuthenticated) {
        this.authService = authService;
        this.view = view;
        this.onAuthenticated = onAuthenticated;
        wireActions();
    }

    public void show() {
        view.setVisible(true);
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
            view.dispose();
            onAuthenticated.accept(user);
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
            view.dispose();
            onAuthenticated.accept(user);
        } catch (Exception e) {
            view.showError(e.getMessage());
            view.clearPassword();
        }
    }
}