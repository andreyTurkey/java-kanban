import service.*;
import model.Task;
import model.Epic;
import model.Subtask;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача Один", "Описание");
        Task task2 = new Task("Задача Два", "Описание");
        Task task3 = new Task("Задача Три", "Описание");
        Task task4 = new Task("Задача Четыре", "Описание");
        Task task5 = new Task("Задача Пять", "Описание");
        Task task6 = new Task("Задача Шесть", "Описание");
        Task task7 = new Task("Задача Семь", "Описание");
        Task task8 = new Task("Задача Восемь", "Описание");
        Task task9 = new Task("Задача Девять", "Описание");
        Task task10 = new Task("Задача Десять", "Описание");
        Task task11 = new Task("Задача Одинадцать", "Описание");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.addNewTask(task4);
        taskManager.addNewTask(task5);
        taskManager.addNewTask(task6);
        taskManager.addNewTask(task7);
        taskManager.addNewTask(task8);
        taskManager.addNewTask(task9);
        taskManager.addNewTask(task10);
        taskManager.addNewTask(task11);

        Epic epic1 = new Epic("Эпик Задача Один", "Описание");
        Epic epic2 = new Epic("Эпик Задача Два", "Описание");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача один", "Описание");
        Subtask subtask2 = new Subtask("Подзадача два", "Описание");
        Subtask subtask3 = new Subtask("Подзадача три", "Описание");

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdTask(3);
        taskManager.getWithIdTask(4);
        taskManager.getWithIdTask(5);
        taskManager.getWithIdTask(6);
        taskManager.getWithIdTask(7);
        taskManager.getWithIdTask(8);
        taskManager.getWithIdTask(9);
        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdTask(3);
        taskManager.getWithIdTask(4);
        taskManager.getWithIdTask(5);

        taskManager.getWithIdEpics(12);
        taskManager.getWithIdEpics(13);

        taskManager.getWithIdTask(10);
        taskManager.getWithIdTask(11);

        taskManager.getWithIdSubtasks(14);
        taskManager.getWithIdSubtasks(15);
        taskManager.getWithIdSubtasks(16);


        /*taskManager.removeTask(1);
        taskManager.removeEpic(12);
        taskManager.removeSubtask(14);*/

        taskManager.printHistory();

        System.out.println();

        taskManager.printMap();
    }
}



