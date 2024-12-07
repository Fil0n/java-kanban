package com.yandex.app.model;

import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;
import com.yandex.app.utils.TestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskTest {
    public final TaskManager taskManager = new InMemoryTaskManager();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Test
    void addNewTask() {
        Task task = new Task("Таск 1", "Описание 1", 30, LocalDateTime.parse("01.01.2025 00:00", DATE_TIME_FORMATTER));
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        TestUtils.checkTask(task, savedTask, taskManager.getTasks());
    }

}
