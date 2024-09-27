package practicum.yandex;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private final ArrayList<String> TEST_TASKS_NAMES = new ArrayList<>(); //{"Купить хлеб", "Прогуляться", ""}
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task();
        task1.setName("Таск 1");
        task1.setDesription("Описание 1");
        manager.addTask(task1);

        Task task2 = new Task();
        task1.setName("Таск 2");
        task1.setDesription("Описание 2");
        manager.addTask(task2);

        Task task3 = new Task();
        task3.setName("Таск 3");
        task3.setDesription("Описание 3");
        task3.setStatus(Status.IN_PROGRESS);
        manager.addTask(task3);

        System.out.println("Создание тасков");
        for (Map.Entry<Integer, Task> managetTask : manager.getTasks().entrySet()){
            System.out.print(managetTask);
        }

        System.out.println("============================================================== \n ");

        System.out.println("Получение таска по id");

        System.out.print(manager.getTaskById(1));

        System.out.println("============================================================== \n ");

        Task task4 = new Task();
        task4.setId(2);
        task4.setName("Таск 4");
        task4.setDesription("Описание 4");
        task4.setStatus(Status.IN_PROGRESS);

        manager.updateTask(task4);

        System.out.println("Обновление таска 2");
        printTasks(manager);
        System.out.println("============================================================== \n");

        manager.removeTaskById(1);

        System.out.println("Удаление таска 1");
        printTasks(manager);
        System.out.println("============================================================== \n");

        manager.removeTasks();

        System.out.println("Очистка тасков");
        printTasks(manager);
        System.out.println("============================================================== \n");


        Epic epic1 = new Epic();
        epic1.setName("Эпик 1");
        epic1.setDesription("Описание Эпик 1");
        manager.addEpic(epic1);

        Epic epic2 = new Epic();
        epic2.setName("Эпик 2");
        epic2.setDesription("Описание Эпик 2");
        manager.addEpic(epic2);

        System.out.println("Создание эпиков");
        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        Subtask subtask1 = new Subtask();
        subtask1.setName("Сабтаск 1");
        subtask1.setDesription("Описание Сабтаск 1");
        subtask1.setMainTaskId(4);
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setName("Сабтаск 2");
        subtask2.setDesription("Описание Сабтаск 2");
        subtask2.setMainTaskId(4);
        manager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask();
        subtask3.setName("Сабтаск 3");
        subtask3.setDesription("Описание Сабтаск 3");
        subtask3.setMainTaskId(4);
        manager.addSubtask(subtask3);

        Subtask subtask4 = new Subtask();
        subtask4.setName("Сабтаск 4");
        subtask4.setDesription("Описание Сабтаск 4");
        subtask4.setMainTaskId(4);
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

        for(Subtask subtask : manager.getEpicSubtasks(4)){
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

        Epic epic3 = new Epic();
        epic3.setId(5);
        epic3.setName("Эпик 3");
        epic3.setDesription("Описание Эпик 3");
        manager.updateEpic(epic3);

        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");

        System.out.println("Удаление эпиков");

        subtask1.setMainTaskId(5);
        subtask2.setMainTaskId(5);
        subtask3.setMainTaskId(5);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        manager.removeEpics();

        printEpicsWithSubtasks(manager);
        System.out.println("============================================================== \n");
    }

    private static void printTasks(Manager manager){
        for (Map.Entry<Integer, Task> managetTask : manager.getTasks().entrySet()){
            System.out.print(managetTask);
        }
    }

    private static void printSubtasks(Manager manager){
        for (Map.Entry<Integer, Subtask> managetSubtask : manager.getSubtasks().entrySet()){
            System.out.print(managetSubtask);
        }
    }

    private static void printEpicsWithSubtasks(Manager manager){
        for (Map.Entry<Integer, Epic> managerEpic : manager.getEpics().entrySet()){
            Epic epic = managerEpic.getValue();
            System.out.print(epic);

            for(Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.print(subtask);
            }

        }
    }

}
