package com.yandex.app.service;

import com.yandex.app.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int counter = 0;

    //Создание тасков
    public void addTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        int mainTaskId = subtask.getEpicId();

        final Epic epic = epics.get(mainTaskId);
        if (epic == null) {
            return;
        }

        int subtaskId = getNextId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
        epic.putSubtaskId(subtaskId);

        updateEpicStatus(mainTaskId); //Обновляем статус епику
    }

    //Получение списка всех задач.
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //Удаление задач
    public void removeEpics() {
        epics.clear();
        removeSubtasks();
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks() {
        subtasks.clear();

        //Обновляем все эпики
        for (Map.Entry<Integer, Epic> epicData : epics.entrySet()) {
            Epic epic = epicData.getValue();
            epic.cleanSubtasksIds();
            epic.setStatus(Status.NEW);
        }
    }

    //Получение задачи по индентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    //Обновление задач
    public void updateTask(Task newTask) {
        final int id = newTask.getId();
        if (!tasks.containsKey(id)) {
            return;
        }

        tasks.replace(id, newTask);
    }

    public void updateEpic(Epic newEpic) {
        final Epic epic = epics.get(newEpic.getId());
        if (epic == null) {
            return;
        }

        epic.setName(newEpic.getName());
        epic.setDescription(newEpic.getDescription());
    }

    public void updateSubtask(Subtask newSubtask) {
        final int id = newSubtask.getId();
        if (!subtasks.containsKey(id)) {
            return;
        }
        subtasks.replace(id, newSubtask);
        updateEpicStatus(newSubtask.getEpicId());
    }

    //Удаление по идентификатору
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        final Epic epic = epics.remove(id);
        final ArrayList<Integer> subtaskIds = epic.getSubtasksIds();
        for (int subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
    }

    public void removeSubtaskById(int id) {
        final int epicId = subtasks.remove(id).getEpicId();
        epics.get(epicId).removeSubtaskId(id);
        updateEpicStatus(epicId);
    }

    //Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        final Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksIds()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }

        return epicSubtasks;
    }

    //Обновление статуса эпика
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtasksIds();

        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean hasNew = false;
        boolean hasDone = false;

        for (int subtaskId : subtaskIds) {
            Status status = subtasks.get(subtaskId).getStatus();

            if (status == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (status == Status.NEW) {
                hasNew = true;
            } else {
                hasDone = true;
            }
        }

        if (hasNew && !hasDone) {
            epic.setStatus(Status.NEW);
        } else if (!hasNew) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    //Получение идентификатора нового таска (счетчик)
    private int getNextId() {
        return ++counter;
    }
}