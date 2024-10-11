package com.yandex.app.Tests;

import com.yandex.app.model.Task;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;

class Tests {
    public final TaskManager taskManager = Managers.getDefault();
    public final HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void addNewTask() {
        Task task1 = new Task("Таск 1", "Описание 1");

        int taskId = taskManager.addTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
    }
}