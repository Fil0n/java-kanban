package com.yandex.app.service;

import com.yandex.app.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    final Map<Integer, Task> tasks = new HashMap<>();
    final Map<Integer, Epic> epics = new HashMap<>();
    final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int counter = 0;

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Создание тасков
    @Override
    public Integer addTask(Task task) {
        int id = getNextId();
        task.setId(id);
        tasks.put(task.getId(), task);
        return id;
    }

    @Override
    public Integer addEpic(Task epicTask) {
        Epic epic = (Epic) epicTask;
        int id = getNextId();
        epic.setId(id);
        epics.put(epic.getId(), epic);
        return id;
    }

    @Override
    public Integer addSubtask(Task subtaskTask) {
        Subtask subtask = (Subtask) subtaskTask;
        int mainTaskId = subtask.getEpicId();

        final Epic epic = epics.get(mainTaskId);
        if (epic == null) {
            return null;
        }

        int id = getNextId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.putSubtaskId(id);

        updateEpicStatus(mainTaskId); //Обновляем статус епику
        return id;
    }

    //Получение списка всех задач.
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //Удаление задач
    @Override
    public void removeEpics() {
        for (Epic epic : getEpics()) {
            historyManager.remove(epic.getId());
        }

        epics.clear();
        removeSubtasks();
    }

    @Override
    public void removeTasks() {
        for (Task task : getTasks()) {
            historyManager.remove(task.getId());
        }

        tasks.clear();
    }

    @Override
    public void removeSubtasks() {
        for (Task subtask : getSubtasks()) {
            historyManager.remove(subtask.getId());
        }

        subtasks.clear();

        //Обновляем все эпики
        for (Map.Entry<Integer, Epic> epicData : epics.entrySet()) {
            Epic epic = epicData.getValue();
            epic.cleanSubtasksIds();
            epic.setStatus(Status.NEW);
        }
    }

    //Получение задачи по индентификатору
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    //Обновление задач
    @Override
    public void updateTask(Task newTask) {
        final int id = newTask.getId();
        if (!tasks.containsKey(id)) {
            return;
        }

        tasks.replace(id, newTask);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        final Epic epic = epics.get(newEpic.getId());
        if (epic == null) {
            return;
        }

        epic.setName(newEpic.getName());
        epic.setDescription(newEpic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        final int id = newSubtask.getId();
        if (!subtasks.containsKey(id)) {
            return;
        }
        subtasks.replace(id, newSubtask);
        updateEpicStatus(newSubtask.getEpicId());
    }

    //Удаление по идентификатору
    @Override
    public void removeTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        historyManager.remove(id);
        final Epic epic = epics.remove(id);
        final List<Integer> subtaskIds = epic.getSubtasksIds();
        for (int subtaskId : subtaskIds) {
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        historyManager.remove(id);
        final int epicId = subtasks.remove(id).getEpicId();
        epics.get(epicId).removeSubtaskId(id);
        updateEpicStatus(epicId);
    }

    //Получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        final Epic epic = epics.get(id);
        List<Subtask> epicSubtasks = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksIds()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }

        return epicSubtasks;
    }

    //Обновление статуса эпика
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtasksIds();

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