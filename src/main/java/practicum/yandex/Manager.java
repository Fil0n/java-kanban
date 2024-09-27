package main.java.practicum.yandex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static int counter = 0;

    //Создание тасков
    public void addTask(Task task) {
        if (task.getId() != 0 || tasks.containsKey(task.getId())) {
            return;
        }

        task.setId(getNextId());
        task.setStatus(task.getStatus() == null ? Status.NEW : task.getStatus());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        if (epic.getId() != 0 || epics.containsKey(epic.getId())) {
            return;
        }

        epic.setId(getNextId());
        epics.put(epic.getId(), epic);

        updateEpicStatus(epic.getId());
    }

    public void addSubtask(Subtask subtask) {
        if (subtask.getId() != 0 || subtasks.containsKey(subtask.getId())) {
            return;
        }
        int mainTaskId = subtask.getMainTaskId();
        if(mainTaskId == 0) {
            return;
        }

        int subtaskId = getNextId();
        subtask.setId(subtaskId);
        subtask.setStatus(subtask.getStatus() == null ? Status.NEW : subtask.getStatus());
        subtasks.put(subtaskId, subtask);
        epics.get(mainTaskId).putSubtaskId(subtaskId);

        updateEpicStatus(mainTaskId); //Обновляем статус епику
    }

    //Получение списка всех задач.
    public HashMap<Integer, Task> getTasks(){
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics(){
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks(){
        return subtasks;
    }

    //Удаление задач
    public void removeEpics(){
        epics = new HashMap<>();
        removeSubtasks();
    }

    public void removeTasks(){
        tasks = new HashMap<>();
    }

    public void removeSubtasks(){
        subtasks = new HashMap<>();

        //Обновляем все эпики
        for (Map.Entry<Integer, Epic> epicData : epics.entrySet()){
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
        if(!tasks.containsKey(newTask.getId())){
            return;
        }

        tasks.replace(tasks.get(newTask.getId()).getId(), newTask);
    }

    public void updateEpic(Epic newEpic) {
        if(!epics.containsKey(newEpic.getId())){
            return;
        }

        Epic epic = epics.get(newEpic.getId());
        epic.setName(newEpic.getName());
        epic.setDesription(newEpic.getDesription());
    }

    public void updateSubtask(Subtask newSubtask) {
        if(!subtasks.containsKey(newSubtask.getId())){
            return;
        }

        Subtask oldSubtask = subtasks.get(newSubtask.getId());
        subtasks.replace(subtasks.get(newSubtask.getId()).getId(), newSubtask);

        updateEpicStatus(newSubtask.getMainTaskId()); //Обновляем статус епику
        updateEpicStatus(oldSubtask.getMainTaskId()); //Обновляем статус епику
    }

    //Удаление по идентификатору
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);

        ArrayList<Integer> subtaskIds = epic.getSubtasksIds();

        for (int subtaskId : subtaskIds) {
            removeSubtaskById(subtaskId);
        }

        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        epics.get(subtasks.get(id).getMainTaskId()).removeSubtaskId(id);
        subtasks.remove(id);
    }

    //Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        Epic epic = getEpicById(id);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        for(int subtaskId : epic.getSubtasksIds()){
            epicSubtasks.add(subtasks.get(subtaskId));
        }

        return epicSubtasks;
    }
//sdds


    //Обновление статуса эпика
    private void updateEpicStatus(int epicId){
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtasksIds();

        if(subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean hasNew = false;
        boolean hasDone = false;
        boolean hasInProgres = false;

        for(int subtaskId : subtaskIds) {
            Status status = subtasks.get(subtaskId).getStatus();

            if(status == Status.NEW) {
                hasNew = true;
            }

            if(status == Status.IN_PROGRESS) {
                hasInProgres = true;
            }

            if(status == Status.DONE) {
                hasDone = true;
            }
        }

        if(hasNew && !hasDone && !hasInProgres){
            epic.setStatus(Status.NEW);
        } else if (hasDone && !hasNew && !hasInProgres) {
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