package com.yandex.app;

import com.yandex.app.service.*;
import com.yandex.app.model.*;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Таск 1", "Описание 1");
        manager.addTask(task1);

        Task task2 = new Task("Таск 2", "Описание 2");
        manager.addTask(task2);

        Task task3 = new Task("Таск 3", "Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        manager.addTask(task3);

        System.out.println("Создание тасков");
        for (Task managetTask : manager.getTasks()) {
            System.out.print(managetTask);
        }
        System.out.println("============================================================== \n ");

        System.out.println("Получение таска по id");
        System.out.print(manager.getTaskById(1));
        System.out.println("============================================================== \n ");

        Task task4 = new Task("Таск 4", "Описание 4");
        task4.setId(2);
        task4.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task4);

        System.out.println("Обновление таска 2");
        printTasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Удаление таска 1");
        manager.removeTaskById(1);
        printTasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Очистка тасков");
        manager.removeTasks();
        printTasks(manager);
        System.out.println("============================================================== \n");


        Epic epic1 = new Epic("Эпик 1", "Описание Эпик 1");
        manager.addEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание Эпик 2");
        manager.addEpic(epic2);

        System.out.println("Создание эпиков");
        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание Сабтаск 1", 4);
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание Сабтаск 2", 4);
        manager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание Сабтаск 3", 4);
        manager.addSubtask(subtask3);

        Subtask subtask4 = new Subtask("Сабтаск 4", "Описание Сабтаск 4", 4);
        manager.addSubtask(subtask4);


        System.out.println("Создание подтасков");
        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");


        System.out.println("Удаление сабтаска по идентификатору");

        manager.removeSubtaskById(8);
        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Смена статуса 1-го сабтаска на IN_PROGRESS");

        subtask4.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask4);

        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Смена статусов всех подзадач эпика на DONE");

        subtask4.setStatus(Status.DONE);
        manager.updateSubtask(subtask4);

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);

        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);

        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Получение подзадач эпика");

        for (Subtask subtask : manager.getEpicSubtasks(4)) {
            System.out.println(subtask);
        }

        System.out.println("============================================================== \n");

        System.out.println("Получение подзадач");
        printSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Получение подзадачи по id");
        System.out.println(manager.getSubtaskById(9));
        System.out.println("============================================================== \n");

        System.out.println("Удаление всех подзадач");
        manager.removeSubtasks();
        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        System.out.println("Удаление эпика по id");
        manager.removeEpicById(4);
        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Обновление эпика ");

        Epic epic3 = new Epic("Эпик 3", "Описание Эпик 3");
        epic3.setId(5);
        manager.updateEpic(epic3);

        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Удаление эпиков");

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        manager.removeEpics();

        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");
    }

    private static void printTasks(Manager manager) {
        for (Task managetTask : manager.getTasks()) {
            System.out.print(managetTask);
        }
    }

    private static void printSubtasks(Manager manager) {
        for (Subtask managetSubtask : manager.getSubtasks()) {
            System.out.print(managetSubtask);
        }
    }

    private static void printEpicsWithSubtasks(Manager manager) {
        for (Epic epic : manager.getEpics()) {
            System.out.print(epic);

            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.print(subtask);
            }

        }
    }

}
