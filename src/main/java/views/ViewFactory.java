package views;

/**
 * Central place to create views. Extend with new view constructors as screens grow.
 */
public class ViewFactory {

    public LoginView createLoginView() {
        return new LoginView();
    }

    public TodoView createTodoView() {
        return new TodoView();
    }
}
