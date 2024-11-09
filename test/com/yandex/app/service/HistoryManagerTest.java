package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    public final TaskManager taskManager = Managers.getDefault();

    @Test
    void checkTaskHistory() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final Integer epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание 1", epicId);
        final Integer subtask1Id = taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание 2", epicId);
        final Integer subtask2Id = taskManager.addSubtask(subtask2);
        Task task = new Task("Таск 1", "Описание 1");
        final int taskId = taskManager.addTask(task);

        for(int i=0; i<10; i++){
            taskManager.getTaskById(taskId);
        }
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtask1Id);
        taskManager.getSubtaskById(subtask2Id);

        List<Task> history = taskManager.getHistory();

        assertEquals(task, history.get(0), "Не соотвтствует таску в элементе 0");
        assertEquals(epic, history.get(1), "Не соотвтствует эпику в элементе 1.");
        assertEquals(subtask1, history.get(2), "Не соотвтствует сабтаску1 в элементе 2.");
        assertEquals(subtask2, history.get(3), "Не соотвтствует сабтаску2 в элементе 3.");
    }

    @Test
    void removeTaskFromHistory() {
        Task task1 = new Task("Таск 1", "Описание 1");
        final int taskId1 = taskManager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        final int taskId2 = taskManager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        final int taskId3 = taskManager.addTask(task3);

        taskManager.getTaskById(taskId1);
        taskManager.getTaskById(taskId2);
        taskManager.getTaskById(taskId3);

        taskManager.removeTaskById(taskId2);
        List<Task> history = taskManager.getHistory();

        assertEquals(task1, history.get(0), "Не соотвтствует таску в элементе 0");
        assertEquals(task3, history.get(1), "Не соотвтствует таску в элементе 1.");
    }

    @Test
    void removeEpicFromHistory() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        final int epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание 1", epic1Id);
        final Integer subtask1Id = taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание 2", epic1Id);
        final Integer subtask2Id = taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание 3", epic1Id);
        final Integer subtask3Id = taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Описание 2");
        final int epic2Id = taskManager.addEpic(epic2);

        Epic epic3 = new Epic("Epic 3", "Описание 3");
        final int epic3Id = taskManager.addEpic(epic3);

        taskManager.getEpicById(epic1Id);
        taskManager.getEpicById(epic2Id);
        taskManager.getEpicById(epic3Id);

        taskManager.getSubtaskById(subtask1Id);
        taskManager.getSubtaskById(subtask2Id);
        taskManager.getSubtaskById(subtask3Id);

        taskManager.removeEpicById(epic1Id);

        List<Task> history = taskManager.getHistory();

        assertEquals(epic2, history.get(0), "Не соотвтствует эпику в элементе 0");
        assertEquals(epic3, history.get(1), "Не соотвтствует эпику в элементе 1.");

        assertEquals(2, history.size(), "Неверное количество задач.");

    }
}
