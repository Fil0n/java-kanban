package com.yandex.app.Tests;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Tests {
    public final TaskManager taskManager = Managers.getDefault();

    @Test
    void addNewTask() {
        Task task = new Task("Таск 1", "Описание 1");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        checkTask(task, savedTask, taskManager.getTasks());
    }

    @Test
    void addNewEpicAndSubtask() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        checkTask(epic, savedEpic, taskManager.getEpics());

        Subtask subtask = new Subtask("Сабтаск 1", "Описание Сабтаск 1", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        checkTask(subtask, savedSubtask, taskManager.getSubtasks());
    }

    @Test
    void addSubtaskAsEpic() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final Integer epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание 1", epicId);
        final Integer subtask1Id = taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание 2", subtask1Id);
        final Integer subtask2Id = taskManager.addSubtask(subtask1);
        assertNotNull(subtask2Id, "Cабтаск может быть своим эпиком.");
    }

    @Test
    void checkManadgers() {
        assertNotNull(Managers.getDefault(), "Менеджер тасков не загружен");
        assertNotNull(Managers.getDefaultHistory(), "Менеджер истории не загружен");
    }

    <T> void checkTask(T task, T savedTask, List<T> tasks) {
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

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
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);
        taskManager.getSubtaskById(subtask1Id);
        taskManager.getSubtaskById(subtask2Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(task, history.get(0), "Не соотвтствует таску в элементе 0.");
        assertEquals(task, history.get(1), "Не соотвтствует таску в элементе 2.");
        assertEquals(task, history.get(2), "Не соотвтствует таску в элементе 1.");
        assertEquals(task, history.get(3), "Не соотвтствует таску в элементе 3.");
        assertEquals(task, history.get(4), "Не соотвтствует таску в элементе 4.");
        assertEquals(task, history.get(5), "Не соотвтствует таску в элементе 5.");
        assertEquals(task, history.get(6), "Не соотвтствует таску в элементе 6.");
        assertEquals(epic, history.get(7), "Не соотвтствует эпику в элементе 7.");
        assertEquals(subtask1, history.get(8), "Не соотвтствует сабтаску1 в элементе 8.");
        assertEquals(subtask2, history.get(9), "Не соотвтствует сабтаску2 в элементе 9.");
    }
}