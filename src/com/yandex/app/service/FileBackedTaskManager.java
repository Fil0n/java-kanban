package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;
import com.yandex.app.util.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final private String DEFAULT_FILE_NAME = "Tasks.csv";

    public void save() {
        save(DEFAULT_FILE_NAME);
    }

    public void save(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
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

//    public void load(){
//        load(DEFAULT_FILE_NAME);
//    }

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
    public Integer addEpic(Task epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }
}
