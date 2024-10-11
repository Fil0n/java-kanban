package com.yandex.app;

import com.yandex.app.service.*;
import com.yandex.app.model.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Таск 1", "Описание 1");
        taskManager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        taskManager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        taskManager.addTask(task3);

        System.out.println("Создание тасков");
        for (Task managetTask : taskManager.getTasks()) {
            System.out.print(managetTask);
        }
        System.out.println("============================================================== \n ");

        System.out.println("Получение таска по id");
        System.out.print(taskManager.getTaskById(1));
        System.out.println("============================================================== \n ");

        Task task4 = new Task("Таск 4", "Описание 4");
        task4.setId(2);
        task4.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task4);

        System.out.println("Обновление таска 2");
        printTasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Удаление таска 1");
        taskManager.removeTaskById(1);
        printTasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Очистка тасков");
        taskManager.removeTasks();
        printTasks(taskManager);
        System.out.println("============================================================== \n");


        Epic epic1 = new Epic("Эпик 1", "Описание Эпик 1");
        taskManager.addEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание Эпик 2");
        taskManager.addEpic(epic2);

        System.out.println("Создание эпиков");
        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание Сабтаск 1", 4);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание Сабтаск 2", 4);
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание Сабтаск 3", 4);
        taskManager.addSubtask(subtask3);

        Subtask subtask4 = new Subtask("Сабтаск 4", "Описание Сабтаск 4", 4);
        taskManager.addSubtask(subtask4);


        System.out.println("Создание подтасков");
        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");


        System.out.println("Удаление сабтаска по идентификатору");

        taskManager.removeSubtaskById(8);
        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Смена статуса 1-го сабтаска на IN_PROGRESS");

        subtask4.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask4);

        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Смена статусов всех подзадач эпика на DONE");

        subtask4.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask4);

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);

        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Получение подзадач эпика");

        for (Subtask subtask : taskManager.getEpicSubtasks(4)) {
            System.out.println(subtask);
        }

        System.out.println("============================================================== \n");

        System.out.println("Получение подзадач");
        printSubtasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Получение подзадачи по id");
        System.out.println(taskManager.getSubtaskById(9));
        System.out.println("============================================================== \n");

        System.out.println("Удаление всех подзадач");
        taskManager.removeSubtasks();
        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        System.out.println("Удаление эпика по id");
        taskManager.removeEpicById(4);
        printEpicsWithSubtasksFromManagerMap(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Обновление эпика ");

        Epic epic3 = new Epic("Эпик 3", "Описание Эпик 3");
        epic3.setId(5);
        taskManager.updateEpic(epic3);

        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        System.out.println("Удаление эпиков");

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        taskManager.removeEpics();

        printEpicsWithSubtasks(taskManager);
        System.out.println("============================================================== \n");

        Task[] tasks =  historyManager.getHistory();

        System.out.println("История");
        for(Task task : tasks){
            System.out.println(task);
        }

        System.out.println("============================================================== \n");
    }

    private static void printTasks(TaskManager taskManager) {
        for (Task managetTask : taskManager.getTasks()) {
            System.out.print(managetTask);
        }
    }

    private static void printSubtasks(TaskManager taskManager) {
        for (Subtask managetSubtask : taskManager.getSubtasks()) {
            System.out.print(managetSubtask);
        }
    }

    private static void printEpicsWithSubtasks(TaskManager taskManager) {
        for (Epic epic : taskManager.getEpics()) {
            System.out.print(epic);

            for (Subtask subtask : taskManager.getEpicSubtasks(epic.getId())) {
                System.out.print(subtask);
            }

        }
    }

    private static void printEpicsWithSubtasksFromManagerMap(TaskManager taskManager) {
        for (Epic epic : taskManager.getEpics()) {
            System.out.print(epic);
        }

        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.print(subtask);
        }
    }

}
