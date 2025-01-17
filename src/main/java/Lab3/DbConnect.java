package Lab3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnect {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/ooplabs";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "database1";
    private Connection connection = null;

    public DbConnect() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskItem> fetchAllTasks() {
        List<TaskItem> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("taskId");
                String description = rs.getString("taskDescription");
                Status status = Status.valueOf(rs.getString("taskStatus"));
                tasks.add(new TaskItem(id, description, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public TaskItem fetchTaskById(int taskId) {
        TaskItem task = null;
        String query = "SELECT * FROM tasks WHERE taskId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, taskId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String description = rs.getString("taskDescription");
                    Status status = Status.valueOf(rs.getString("taskStatus"));
                    task = new TaskItem(taskId, description, status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public void createTask(TaskItem task) {
        String query = "INSERT INTO tasks (taskId, taskDescription, taskStatus) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, task.getTaskId());
            pstmt.setString(2, task.getTaskDescription());
            pstmt.setString(3, task.getTaskStatus().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(int taskId, String newDescription) {
        String query = "UPDATE tasks SET taskDescription = ? WHERE taskId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newDescription);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

