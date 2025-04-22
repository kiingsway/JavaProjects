# ⏳ Timed Tasks

A simple application to manage tasks with specified start and end times. The user can input a list of tasks, each with a duration, and the application will calculate the start and end times based on the input.

## ✨ Features

- **Add Tasks**: Easily add tasks to the list with their estimated time of arrival (ETA), start time, and end time.
- **Manage Tasks**:
  - **Undo/Redo**: Quickly undo or redo changes made to tasks.
  - **Edit**: Modify task details such as title, ETA, start time, and end time.
  - **Move Tasks**: Reorder tasks in the list (implementation pending).
  - **Delete Tasks**: Remove tasks from the list.
- **Real-time Updates**: Changes made to task details are reflected immediately in the interface.

## 🛠️ Technologies

- **Java 17+**
- **Swing** (for the graphical user interface)
- **SQL** (for storing task data)
- **Custom Components**: Includes custom time picker and debounced text fields.

## 📜 License

This project is licensed under the [MIT License](../LICENSE).