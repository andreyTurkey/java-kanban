import model.Status;
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
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.addNewTask(task4);

        //taskManager.printTask();

        task1.setStatus(Status.DONE);
        //taskManager.updatingTask(task1);

        System.out.println();

        //taskManager.printTask();

        System.out.println();

        Epic epic1 = new Epic("Эпик Задача Один", "Описание");
        Epic epic2 = new Epic("Эпик Задача Два", "Описание");

        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        //taskManager.printEpic();

        System.out.println();

        Subtask subtask1 = new Subtask("Подзадача один", "Описание");
        Subtask subtask2 = new Subtask("Подзадача два", "Описание");
        Subtask subtask3 = new Subtask("Подзадача три", "Описание");

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        subtask1.setEpicId(3);
        subtask2.setEpicId(4);
        //subtask3.setEpicId(4);

        //taskManager.printSubtask();

        System.out.println();

        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);

        //taskManager.updatingEpic(epic1);
        //taskManager.updatingEpic(epic2);

        //taskManager.printEpic();

        System.out.println();

        //HashMap<Integer, Task> removedTasks = taskManager.removeTask(1);
        //HashMap<Integer, Epic> removedEpics = taskManager.removeEpic(3);
        //HashMap<Integer, Subtask> removedSubtasks = taskManager.removeSubtask(5);

        //taskManager.printTask();
        //taskManager.printEpic();
        //taskManager.printSubtask();

        System.out.println();

        //taskManager.clearSubtasks();

        //taskManager.printSubtask();
        //System.out.println();
        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdTask(3);
        taskManager.getWithIdTask(4);
        taskManager.getWithIdEpics(5);
        taskManager.getWithIdEpics(5);
        taskManager.getWithIdSubtasks(7);
        taskManager.getWithIdSubtasks(7);
        taskManager.getWithIdSubtasks(7);
        taskManager.getWithIdSubtasks(7);
        taskManager.getWithIdSubtasks(7);
        taskManager.getWithIdSubtasks(7);

        taskManager.printHistory();


    }
}



