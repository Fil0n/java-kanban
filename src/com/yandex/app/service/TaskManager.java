package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskManager {
    //Создание тасков
    Integer addTask(Task task);

    Integer addEpic(Task epic);

    Integer addSubtask(Task subtask);

    //Получение списка всех задач.
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    //Удаление задач
    void removeEpics();

    void removeTasks();

    void removeSubtasks();

    //Получение задачи по индентификатору
    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    //Обновление задач
    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask newSubtask);

    //Удаление по идентификатору
    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    //Получение списка всех подзадач определённого эпика
    List<Subtask> getEpicSubtasks(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    void setPrioritizedTasks(Task task);

    boolean dateValidation(LocalDateTime startDate, LocalDateTime endDate);
}
