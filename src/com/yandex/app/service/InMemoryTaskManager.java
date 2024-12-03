package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private static final Comparator<Task> taskComparator = Comparator.comparing(
            Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId);
    final Map<Integer, Task> tasks = new HashMap<>();
    final Map<Integer, Epic> epics = new HashMap<>();
    final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    private int counter = 0;


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Создание тасков
    @Override
    public Integer addTask(Task task) {
        if (!dateValidation(task.getStartTime(), task.getEndTime())) {
            return null;
        }

        int id = getNextId();
        task.setId(id);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
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

        if (!dateValidation(subtask.getStartTime(), subtask.getEndTime())) {
            return null;
        }

        int mainTaskId = subtask.getEpicId();

        final Epic epic = epics.get(mainTaskId);
        if (epic == null) {
            return null;
        }

        int id = getNextId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.putSubtaskId(id);

        updateEpic(mainTaskId); //Обновляем статус епику
        prioritizedTasks.add(subtaskTask);
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
        if (!tasks.containsKey(id) || !dateValidation(newTask.getStartTime(), newTask.getEndTime())) {
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
        if (!subtasks.containsKey(id) || !dateValidation(newSubtask.getStartTime(), newSubtask.getEndTime())) {
            return;
        }
        subtasks.replace(id, newSubtask);
        updateEpic(newSubtask.getEpicId());
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
        updateEpic(epicId);
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
    void updateEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtasksIds();

        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean hasNew = false;
        boolean hasDone = false;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            Status status = subtask.getStatus();

            if (status == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (status == Status.NEW) {
                hasNew = true;
            } else {
                hasDone = true;
            }

            LocalDateTime subtaskEndDate = subtask.getEndTime();
            LocalDateTime epicEndDate = epic.getEndTime();

            LocalDateTime subtaskStartDate = subtask.getStartTime();
            LocalDateTime epicStartDate = epic.getStartTime();

            if (subtaskEndDate != null) {
                if (epicEndDate == null || subtaskEndDate.isAfter(epicEndDate)) {
                    epic.setEndTime(subtaskEndDate);
                }

                if (epicStartDate == null || subtaskStartDate.isBefore(epicStartDate)) {
                    epic.setStartTime(subtaskStartDate);
                }
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

    void setCounterMaxId(int id) {
        counter = Math.max(counter, id);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void setPrioritizedTasks(Task task) {
        this.prioritizedTasks.add(task);
    }

    public boolean dateValidation(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            return true;
        }

        return (int) prioritizedTasks.stream()
                .filter(task -> (task.getStartTime().isBefore(startDate) && task.getEndTime().isAfter(startDate)) || (startDate.isBefore(task.getStartTime()) && endDate.isAfter(task.getStartTime())))
                .count() == 0;
    }
}