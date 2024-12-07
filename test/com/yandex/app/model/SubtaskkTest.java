package com.yandex.app.model;

import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.TaskManager;
import com.yandex.app.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskkTest {
    public final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewEpicAndSubtasks() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        TestUtils.checkTask(epic, savedEpic, taskManager.getEpics());

        Subtask subtask = new Subtask("Сабтаск 1", "Описание Сабтаск 1", 30, "01.01.2025 00:00", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        TestUtils.checkTask(subtask, savedSubtask, taskManager.getSubtasks());

        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание Сабтаск 2", 30, "01.01.2025 00:31", epicId);
        final int subtask2Id = taskManager.addSubtask(subtask2);
        final Subtask savedSubtask2 = taskManager.getSubtaskById(subtask2Id);

        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание Сабтаск 3", 30, "01.01.2025 01:02", epicId);
        final int subtask3Id = taskManager.addSubtask(subtask3);
        final Subtask savedSubtask3 = taskManager.getSubtaskById(subtask3Id);

        assertEquals(epic.getEndTime(), savedSubtask3.getEndTime(), "Не верная дата завершения Эпика");
        assertEquals(epic.getStartTime(), savedSubtask.getStartTime(), "Не верная дата начала Эпика");
        assertEquals(3, epic.getSubtasksIds().size(), "Неверное количество задач.");
    }

}
