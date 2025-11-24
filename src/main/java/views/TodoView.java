package views;

import model.entities.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodoView extends JFrame {

    private final DefaultListModel<Task> listModel = new DefaultListModel<>();
    private final JList<Task> taskList = new JList<>(listModel);
    private final JTextField titleField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(3, 20);
    private final JButton addButton = new JButton("Add Task");
    private final JButton toggleButton = new JButton("Toggle Complete");
    private final JButton deleteButton = new JButton("Delete Task");
    private final JLabel userLabel = new JLabel();

    public TodoView() {
        super("TODO List");
        initLayout();
    }

    private void initLayout() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JPanel inputPanel = new JPanel(new BorderLayout(4, 4));
        inputPanel.setBorder(BorderFactory.createTitledBorder("New task"));
        inputPanel.add(new JLabel("Title"), BorderLayout.NORTH);
        inputPanel.add(titleField, BorderLayout.CENTER);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(new JLabel("Description"), BorderLayout.NORTH);
        descriptionPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        inputPanel.add(descriptionPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(toggleButton);
        buttonPanel.add(deleteButton);

        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        headerPanel.add(userLabel, BorderLayout.EAST);

        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setBorder(BorderFactory.createTitledBorder("Tasks"));

        add(headerPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
    }

    public void onAddTask(Runnable action) {
        addButton.addActionListener(e -> action.run());
    }

    public void onToggleTask(Runnable action) {
        toggleButton.addActionListener(e -> action.run());
    }

    public void onDeleteTask(Runnable action) {
        deleteButton.addActionListener(e -> action.run());
    }

    public void renderTasks(List<Task> tasks) {
        listModel.clear();
        tasks.forEach(listModel::addElement);
    }

    public String getTitleInput() {
        return titleField.getText();
    }

    public String getDescriptionInput() {
        return descriptionArea.getText();
    }

    public Task getSelectedTask() {
        return taskList.getSelectedValue();
    }

    public void clearInputs() {
        titleField.setText("");
        descriptionArea.setText("");
    }

    public void setCurrentUser(String username) {
        userLabel.setText("Logged in as: " + username);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
