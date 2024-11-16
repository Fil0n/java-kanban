package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    private final FileBackedTaskManager taskManager = new FileBackedTaskManager();

    @Test
    void addTasksOnFile() {
        Task task1 = new Task("Таск 1", "Описание 1");
        int task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        int task2Id = taskManager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        int task3Id = taskManager.addTask(task3);

        FileBackedTaskManager fTaskMenager = new FileBackedTaskManager();
        List<Task> tasks = fTaskMenager.getTasks();

        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1.getName(), fTaskMenager.getTaskById(task1Id).getName(), "Таски с идентификатором 1 не соответствуют");
        assertEquals(task2.getName(), fTaskMenager.getTaskById(task2Id).getName(), "Таски с идентификатором 2 не соответствуют");
        assertEquals(task3.getName(), fTaskMenager.getTaskById(task3Id).getName(), "Таски с идентификатором 3 не соответствуют");
    }

    @Test
    void removeTasksOnFile() {
        Task task1 = new Task("Таск 1", "Описание 1");
        int task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        int task2Id = taskManager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        int task3Id = taskManager.addTask(task3);

        taskManager.removeTaskById(task3Id);

        FileBackedTaskManager fTaskMenager = new FileBackedTaskManager();
        List<Task> tasks = fTaskMenager.getTasks();

        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task1.getName(), fTaskMenager.getTaskById(task1Id).getName(), "Таски с идентификатором 1 не соответствуют");
        assertEquals(task2.getName(), fTaskMenager.getTaskById(task2Id).getName(), "Таски с идентификатором 2 не соответствуют");
    }

    @Test
    void addEpiscOnFile() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        int epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        int subtaskId = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        int subtask2Id = taskManager.addSubtask(subtask2);

        FileBackedTaskManager fTaskMenager = new FileBackedTaskManager();

        assertEquals(1, fTaskMenager.epics.size(), "Неверное количество эпиков.");
        assertEquals(2, fTaskMenager.getEpicById(epic1Id).getSubtasksIds().size(), "Неверное количество сабтасков в епике");
        assertEquals(2, fTaskMenager.subtasks.size(), "Неверное количество сабтасков.");
        assertEquals(subtask1.getName(), fTaskMenager.getSubtaskById(subtaskId).getName(), "Сабтаски с идентификатором 2 не соответствуют");
        assertEquals(subtask2.getName(), fTaskMenager.getSubtaskById(subtask2Id).getName(), "Сабтаски с идентификатором 3 не соответствуют");
    }

    @Test
    void removeSubtaskOnFile() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        int epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        int subtaskId = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        int subtask2Id = taskManager.addSubtask(subtask2);

        taskManager.removeSubtaskById(subtask2Id);

        FileBackedTaskManager fTaskMenager = new FileBackedTaskManager();

        assertEquals(1, fTaskMenager.epics.size(), "Неверное количество эпиков.");
        assertEquals(1, fTaskMenager.getEpicById(epic1Id).getSubtasksIds().size(), "Неверное количество сабтасков в епике");
        assertEquals(1, fTaskMenager.subtasks.size(), "Неверное количество сабтасков.");
        assertEquals(subtask1.getName(), fTaskMenager.getSubtaskById(subtaskId).getName(), "Сабтаски с идентификатором 2 не соответствуют");
    }
}
