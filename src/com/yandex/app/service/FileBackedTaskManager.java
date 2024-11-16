package com.yandex.app.service;

import com.yandex.app.model.*;
import com.yandex.app.util.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final private String DEFAULT_FILE_NAME = "tasks.csv";
    final private File DEFAULT_FILE = new File(DEFAULT_FILE_NAME);

    public FileBackedTaskManager() {
        loadFromFile();
    }

    public FileBackedTaskManager(File file) {
        loadFromFile(file);
    }

    public void save() {
        save(DEFAULT_FILE);
    }

    public void save(File file) {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            List<Task> tasks = getTasks();
            if (!tasks.isEmpty()) {
                for (Task task : tasks) {
                    writer.write(task.toString());
                    writer.newLine();
                }
            }

            List<Epic> epics = getEpics();
            if (!epics.isEmpty()) {
                for (Epic epic : epics) {
                    writer.write(epic.toString());
                    writer.newLine();
                }
            }

            List<Subtask> subtasks = getSubtasks();
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks) {
                    writer.write(subtask.toString());
                    writer.newLine();
                }
            }
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFromFile() {
        loadFromFile(DEFAULT_FILE);
    }

    public void loadFromFile(File file) {
        final TaskManager taskManager = new InMemoryTaskManager();
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }

                final TaskType type = Task.typeFromString(line);

                switch (type) {
                    case TASK -> putTask(line);
                    case EPIC -> putEpic(line);
                    default -> putSubTask(line);
                }
            }
        } catch (NullPointerException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }
    }

    private void putTask(String data) {
        Task task = Task.fromString(data);
        tasks.put(task.getId(), task);
    }

    private void putEpic(String data) {
        Epic epic = (Epic) Epic.fromString(data);
        epics.put(epic.getId(), epic);
    }

    private void putSubTask(String data) {
        Subtask subtask = (Subtask) Subtask.fromString(data);
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        final Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        epic.putSubtaskId(subtask.getId());

        updateEpicStatus(epicId);
    }

    @Override
    public Integer addSubtask(Task subtask) {
        Integer id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Integer addTask(Task task) {
        Integer id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public Integer addEpic(Task epicTask) {
        Integer id = super.addEpic(epicTask);
        save();
        return id;
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }
}
