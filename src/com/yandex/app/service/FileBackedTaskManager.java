package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.TaskType;
import com.yandex.app.util.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    final File dafaultFile = new File("tasks.csv");

    public FileBackedTaskManager() {
        loadFromFile();
    }

    public FileBackedTaskManager(File file) {
        loadFromFile(file);
    }

    public void save() {

        try {
            save(dafaultFile);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    public void save(File file) throws ManagerSaveException {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new ManagerSaveException(e);
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
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public void loadFromFile() {
        loadFromFile(dafaultFile);
    }

    public void loadFromFile(File file) {
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (true) {
                String line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }

                final TaskType type = Task.typeFromString(line);

                switch (type) {
                    case TASK -> putTask(line);
                    case EPIC -> putEpic(line);
                    default -> putSubTask(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }
    }

    private void putTask(String data) {
        Task task = Task.fromString(data);
        tasks.put(task.getId(), task);
        setCounterMaxId(task.getId());
    }

    private void putEpic(String data) {
        Epic epic = (Epic) Epic.fromString(data);
        epics.put(epic.getId(), epic);
        setCounterMaxId(epic.getId());
    }

    private void putSubTask(String data) {
        Subtask subtask = (Subtask) Subtask.fromString(data);
        subtasks.put(subtask.getId(), subtask);
        setCounterMaxId(subtask.getId());

        int epicId = subtask.getEpicId();
        final Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        epic.putSubtaskId(subtask.getId());

        updateEpic(epicId);
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
