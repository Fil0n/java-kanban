package com.yandex.app.service;

import com.yandex.app.model.*;
import com.yandex.app.util.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final private String DEFAULT_FILE_NAME = "resources\\\\tasks.csv";
    final private File DEFAULT_FILE = new File(DEFAULT_FILE_NAME);

    public void save() {
        save(DEFAULT_FILE);
    }

    public void save(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            List<Task> tasks = getTasks();
            if (tasks.isEmpty()) {
                for (Task task : tasks) {
                    writer.append(task.toString());
                }
            }

            List<Epic> epics = getEpics();
            if (epics.isEmpty()) {
                for (Epic epic : epics) {
                    writer.append(epic.toString());
                }
            }

            List<Subtask> subtasks = getSubtasks();
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks) {
                    writer.append(subtask.toString());
                }
            }
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(){
        load(DEFAULT_FILE);
    }

    public void load(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }

                final TaskType type = Task.typeFromString(line);

                switch (type) {
                    case TASK -> addTask(Task.fromString(line));
                    case EPIC -> addEpic(Epic.fromString(line));
                    default -> addSubtask(Subtask.fromString(line));
                }
            }
        } catch (NullPointerException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public Integer addSubtask(Task subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Integer addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }
    @Override
    public Integer reMoveEpic(Task epic) {
        int id = super.addEpic(epic);
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
}
