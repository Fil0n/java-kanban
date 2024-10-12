package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;

public interface TaskManager {
    //Создание тасков
    Integer addTask(Task task);
    Integer addEpic(Epic epic);
    Integer addSubtask(Subtask subtask);

    //Получение списка всех задач.
    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<Subtask> getSubtasks();

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
    ArrayList<Subtask> getEpicSubtasks(int id);
    Task[] getHistory();
}
