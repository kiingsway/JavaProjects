package org.example.model;

import org.example.dao.TasksAPI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TasksList {

    private final TasksAPI tasksAPI = new TasksAPI();
    private final List<Task> tasks = new ArrayList<>();

    public TasksList() throws SQLException {
        getAllTasks();
    }

    public void getAllTasks() throws SQLException {
        tasks.clear();
        List<Task> newTasks = tasksAPI.getAllTasks();
        tasks.addAll(newTasks);
    }

    public List<Task> tasks() {return tasks;}

    public void add(Task uncreatedTask) throws SQLException {
        Task task = tasksAPI.addTask(uncreatedTask);
        tasks.add(task);
    }

    public void update(Task updatedTask) throws SQLException {
        tasksAPI.updateTask(updatedTask);

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.id() == updatedTask.id()) {
                tasks.set(i, updatedTask);
                break;
            }
        }
    }


    public void delete(Task task) throws SQLException {
        tasksAPI.deleteTask(task.id());
        tasks.remove(task);
    }
}
