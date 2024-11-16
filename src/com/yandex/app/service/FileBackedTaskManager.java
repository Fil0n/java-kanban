package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public void save() {

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
