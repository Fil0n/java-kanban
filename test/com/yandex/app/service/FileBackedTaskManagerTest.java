package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    @BeforeEach
    void clearTasks() {
        taskManager.removeTasks();
        taskManager.removeEpics();
    }

    private final FileBackedTaskManager taskManager = new FileBackedTaskManager();

    @Test
    void addTasks() {
        Task task1 = new Task("Таск 1", "Описание 1");
        Integer task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        Integer task2Id = taskManager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        Integer task3Id = taskManager.addTask(task3);

        taskManager.tasks.clear();
        taskManager.loadFromFile();
        List<Task> tasks = taskManager.getTasks();

        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1.getName(), taskManager.getTaskById(task1Id).getName(), "Таски с идентификатором 1 не соответствуют");
        assertEquals(task2.getName(), taskManager.getTaskById(task2Id).getName(), "Таски с идентификатором 2 не соответствуют");
        assertEquals(task3.getName(), taskManager.getTaskById(task3Id).getName(), "Таски с идентификатором 3 не соответствуют");
    }

    @Test
    void removeTask() {
        Task task1 = new Task("Таск 1", "Описание 1");
        Integer task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        Integer task2Id = taskManager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        Integer task3Id = taskManager.addTask(task3);

        taskManager.removeTaskById(task3Id);

        taskManager.tasks.clear();
        taskManager.loadFromFile();
        List<Task> tasks = taskManager.getTasks();

        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task1.getName(), taskManager.getTaskById(task1Id).getName(), "Таски с идентификатором 1 не соответствуют");
        assertEquals(task2.getName(), taskManager.getTaskById(task2Id).getName(), "Таски с идентификатором 2 не соответствуют");
    }

    @Test
    void addEpic() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        Integer epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        Integer subtaskId = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        Integer subtask2Id = taskManager.addSubtask(subtask2);

        taskManager.epics.clear();
        taskManager.subtasks.clear();
        taskManager.loadFromFile();

        assertEquals(1, taskManager.epics.size(), "Неверное количество эпиков.");
        assertEquals(2, taskManager.getEpicById(epic1Id).getSubtasksIds().size(), "Неверное количество сабтасков в епике");
        assertEquals(2, taskManager.subtasks.size(), "Неверное количество сабтасков.");
        assertEquals(subtask1.getName(), taskManager.getSubtaskById(subtaskId).getName(), "Сабтаски с идентификатором 2 не соответствуют");
        assertEquals(subtask2.getName(), taskManager.getSubtaskById(subtask2Id).getName(), "Сабтаски с идентификатором 3 не соответствуют");
    }

    @Test
    void removeSubtask() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        Integer epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        Integer subtaskId = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        Integer subtask2Id = taskManager.addSubtask(subtask2);

        taskManager.removeSubtaskById(subtask2Id);

        taskManager.epics.clear();
        taskManager.subtasks.clear();
        taskManager.loadFromFile();

        assertEquals(1, taskManager.epics.size(), "Неверное количество эпиков.");
        assertEquals(1, taskManager.getEpicById(epic1Id).getSubtasksIds().size(), "Неверное количество сабтасков в епике");
        assertEquals(1, taskManager.subtasks.size(), "Неверное количество сабтасков.");
        assertEquals(subtask1.getName(), taskManager.getSubtaskById(subtaskId).getName(), "Сабтаски с идентификатором 2 не соответствуют");
    }

    @Test
    void removeEpic() {
        Epic epic1 = new Epic("Epic 1", "Описание 1");
        Integer epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", epic1Id);
        Integer subtaskId = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Описание 1", epic1Id);
        Integer subtask2Id = taskManager.addSubtask(subtask2);

        taskManager.removeEpicById(epic1Id);

        taskManager.epics.clear();
        taskManager.subtasks.clear();
        taskManager.loadFromFile();

        assertEquals(0, taskManager.epics.size(), "Неверное количество эпиков.");
        assertEquals(0, taskManager.subtasks.size(), "Неверное количество сабтасков.");
    }
}
