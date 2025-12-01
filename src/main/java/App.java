import application.ApplicationCoordinator;
import application.ControllerFactory;
import events.EventBus;
import model.DBConnection;
import model.dao.impl.TaskDAOImpl;
import model.dao.impl.UserDAOImpl;
import services.AuthService;
import services.TaskService;
import views.ViewFactory;

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

        EventBus eventBus = new EventBus();
        ViewFactory viewFactory = new ViewFactory();
        ControllerFactory controllerFactory = new ControllerFactory(taskService, authService, eventBus, viewFactory);
        ApplicationCoordinator coordinator = new ApplicationCoordinator(controllerFactory, eventBus);

        SwingUtilities.invokeLater(coordinator::start);
    }
}
