package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.util.ManagerSaveException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public void save() {
        try (FileWriter fileWriter = new FileWriter("Tasks.csv")) {
            List<Task> tasks = getTasks();
            if (tasks.isEmpty()) {
                for (Task task : tasks) {
                    fileWriter.append(task.toString());
                }
            }

            List<Epic> epics = getEpics();
            if (epics.isEmpty()) {
                for (Epic epic : epics) {
                    fileWriter.append(epic.toString());
                }
            }

            List<Subtask> subtasks = getSubtasks();
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks) {
                    fileWriter.append(subtask.toString());
                }
            }
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
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

    public Integer addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }
}
