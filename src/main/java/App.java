import controllers.LoginController;
import controllers.TodoController;
import model.DBConnection;
import model.dao.impl.TaskDAOImpl;
import model.dao.impl.UserDAOImpl;
import model.entities.User;
import services.AuthService;
import services.TaskService;
import views.LoginView;
import views.TodoView;

import javax.swing.SwingUtilities;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        TaskDAOImpl taskDAO = new TaskDAOImpl(connection);
        UserDAOImpl userDAO = new UserDAOImpl(connection);
        TaskService taskService = new TaskService(taskDAO);
        AuthService authService = new AuthService(userDAO);

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(authService, loginView, user -> openTodo(taskService, user));
            loginController.show();
        });
    }

    private static void openTodo(TaskService taskService, User user) {
        TodoView todoView = new TodoView();
        TodoController todoController = new TodoController(taskService, todoView, user);
        todoController.show();
    }
}