package com.yandex.app.model;

import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;
import com.yandex.app.utils.TestUtils;

public class TaskTest {
    public final TaskManager taskManager = Managers.getDefault();

    @Test
    void addNewTask() {
        Task task = new Task("Таск 1", "Описание 1");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        TestUtils.checkTask(task, savedTask, taskManager.getTasks());
    }
}
