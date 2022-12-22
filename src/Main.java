import model.Statuses;
import service.TaskManager;
import model.Task;
import model.Epic;
import model.Subtask;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача Один", "Описание");
        Task task2 = new Task("Задача Два", "Описание");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.printTask();

        task1.setStatuses(Statuses.DONE);
        taskManager.updatingTask(task1);

        System.out.println();

        taskManager.printTask();

        System.out.println();

        Epic epic1 = new Epic("Эпик Задача Один", "Описание");
        Epic epic2 = new Epic("Эпик Задача Два", "Описание");

        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        taskManager.printEpic();

        System.out.println();

        Subtask subtask1 = new Subtask("Подзадача один", "Описание");
        Subtask subtask2 = new Subtask("Подзадача два", "Описание");
        Subtask subtask3 = new Subtask("Подзадача три", "Описание");

        taskManager.addNewSubtaskAndUpdate(subtask1);
        taskManager.addNewSubtaskAndUpdate(subtask2);
        taskManager.addNewSubtaskAndUpdate(subtask3);

        subtask1.setEpicId(3);
        subtask2.setEpicId(4);
        subtask3.setEpicId(4);

        taskManager.printSubtask();

        System.out.println();

        subtask2.setStatuses(Statuses.DONE);
        subtask3.setStatuses(Statuses.DONE);

        taskManager.updatingEpic(epic1);
        taskManager.updatingEpic(epic2);

        taskManager.printEpic();

        System.out.println();

        HashMap<Integer, Task> removedTasks = taskManager.removeTask(1);
        HashMap<Integer, Epic> removedEpics = taskManager.removeEpic(3);
        HashMap<Integer, Subtask> removedSubtasks = taskManager.removeSubtask(5);

        taskManager.printTask();
        taskManager.printEpic();
        taskManager.printSubtask();

        System.out.println();

        taskManager.clearSubtasks();

        taskManager.printSubtask();
        System.out.println();

    }
}



