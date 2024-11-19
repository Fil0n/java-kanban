package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskManagerTest {
    private final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void removeEpic() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");

        int epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);

        taskManager.addSubtask(subtask1);

        taskManager.removeEpicById(epic1Id);
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertEquals(0, epics.size(), "Неверное количество задач.");
        assertEquals(0, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void getEpicSubtasks() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");

        int epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        Subtask subtask3 = new Subtask("Subtask 3", "Описание 1", epic1Id);


        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        List<Subtask> subtasks = taskManager.getEpicSubtasks(epic1Id);

        assertEquals(3, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void getEpicStatus() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        int epic1Id = taskManager.addEpic(epic1);
        Epic savedEpic = taskManager.getEpicById(epic1Id);

        assertEquals(Status.NEW, savedEpic.getStatus(), "Неверный статус епика " + savedEpic.getStatus() + " ожидается " + Status.NEW);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        int subtaskId = taskManager.addSubtask(subtask1);
        Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        int subtask2Id = taskManager.addSubtask(subtask2);
        Subtask savedSubtask2 = taskManager.getSubtaskById(subtask2Id);
        savedSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(savedSubtask);

        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Неверный статус епика " + savedEpic.getStatus() + " ожидается " + Status.IN_PROGRESS);

        savedSubtask.setStatus(Status.DONE);
        savedSubtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(savedSubtask);
        taskManager.updateSubtask(savedSubtask2);

        assertEquals(Status.DONE, savedEpic.getStatus(), "Неверный статус епика " + savedEpic.getStatus() + " ожидается " + Status.DONE);
    }
}
