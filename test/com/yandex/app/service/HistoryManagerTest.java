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
}
