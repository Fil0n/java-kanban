package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.utils.TestUtils;
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

    @Test
    void checkTaskSorting() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        TestUtils.checkTask(epic, savedEpic, taskManager.getEpics());

        Subtask subtask = new Subtask("Сабтаск 1", "Описание Сабтаск 1", 30, "01.01.2025 00:00", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание Сабтаск 2", 30, "01.01.2025 00:31", epicId);
        final int subtask2Id = taskManager.addSubtask(subtask2);
        final Subtask savedSubtask2 = taskManager.getSubtaskById(subtask2Id);

        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание Сабтаск 3", 30, "01.01.2025 01:02", epicId);
        final int subtask3Id = taskManager.addSubtask(subtask3);
        final Subtask savedSubtask3 = taskManager.getSubtaskById(subtask3Id);

        Task task = new Task("Таск 1", "Описание 1", 60, "31.12.2024 00:00");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);

        Task task2 = new Task("Таск 2", "Описание 2", 60, "31.12.2024 03:00");
        final int task2Id = taskManager.addTask(task2);
        final Task savedTask2 = taskManager.getTaskById(task2Id);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(prioritizedTasks.get(0), savedTask, "Первый таск не соответствует таску 1");
        assertEquals(prioritizedTasks.get(1), savedTask2, "Второй таск не соответствует таску 2");
        assertEquals(prioritizedTasks.get(2), savedSubtask, "Третий таск не соответствует сабтаску 1");
        assertEquals(prioritizedTasks.get(3), savedSubtask2, "Четвертый таск не соответствует сабтаску 2");
        assertEquals(prioritizedTasks.get(4), savedSubtask3, "Пятый таск не соответствует сабтаску 3");
    }
}
