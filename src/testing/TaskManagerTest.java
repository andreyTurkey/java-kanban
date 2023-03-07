package testing;

import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.TaskManager;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    void setTaskManager() {
    }

    @BeforeEach
    void setUp() {
        setTaskManager();
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test getPrioritizedEpic1", "Test Test getPrioritizedEpic1 description");
        taskManager.addNewEpic(epic);
        Epic epic2 = new Epic("Test getPrioritizedEpic2", "Test Test getPrioritizedEpic2 description");
        taskManager.addNewEpic(epic2);

        Subtask subtask = new Subtask("Test getPrioritizedSubtask1",
                "Test getPrioritizedSubtask1 description");
        Subtask subtask2 = new Subtask("Test getPrioritizedSubtask2",
                "Test getPrioritizedSubtask2 description", "06.02.2023 10:00", 60);
        Subtask subtask3 = new Subtask("Test getPrioritizedSubtask3",
                "Test getPrioritizedSubtask3 description", "06.02.2023 11:00", 45);
        Subtask subtask4 = new Subtask("Test getPrioritizedSubtask4",
                "Test getPrioritizedSubtask4 description", "06.02.2023 08:00", 121);
        Subtask subtask5 = new Subtask("Test getPrioritizedSubtask5",
                "Test getPrioritizedSubtask5 description", "06.02.2023 11:30", 10);

        subtask.setEpicId(epic);
        subtask2.setEpicId(epic);
        subtask3.setEpicId(epic);
        subtask4.setEpicId(epic);
        subtask5.setEpicId(epic);

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        try {
            taskManager.addSubtask(subtask4);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        try {
            taskManager.addSubtask(subtask5);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        taskManager.updateEpic(epic);
        taskManager.updateEpic(epic2);

        Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        prioritizedTasks.stream().forEach(System.out::println);
    }

    @Test
    void timeValidator() throws IOException, IllegalStateException, InterruptedException {
        Task task1 = new Task("Test timeValidator1", "TesttimeValidator1 description",
                "01.01.2000 09:00", 120);
        taskManager.addNewTask(task1);

        Task task2 = new Task("Test timeValidator2", "TesttimeValidator2 description",
                "01.01.2000 08:00", 60);
        taskManager.addNewTask(task2);

        Epic epic = new Epic("Test timeValidator3", "Test timeValidator3 description");
        taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test timeValidator4", "Test timeValidator4 description",
                "01.01.2000 11:00", 60);
        taskManager.addSubtask(subtask);

        Subtask subtask2 = new Subtask("Test timeValidator5", "Test timeValidator5 description",
                "01.01.2000 07:00", 80);
        try {
            taskManager.addSubtask(subtask2);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        Subtask subtask3 = new Subtask("Test timeValidator6", "Test timeValidator6 description",
                "01.01.2000 09:00", 90);
        try {
            taskManager.addSubtask(subtask3);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        Subtask subtask4 = new Subtask("Test timeValidator7", "Test timeValidator7 description",
                "01.01.2000 05:00", 480);
        try {
            taskManager.addSubtask(subtask4);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        List<Subtask> tasks = taskManager.getListSubtask();
        System.out.println(tasks);
    }

    @Test
    void addNewTask() throws IOException, InterruptedException {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addNewTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getWithIdTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task task = new Task("Test updateTask", "Test updateTask description");
        taskManager.addNewTask(task);
        Task task1 = new Task("Test updateTask1", "Test updateTask1 description");
        taskManager.addNewTask(task1);

        task1.setDescription("Changed Task");

        taskManager.updateTask(task1);
        final Task savedTask = taskManager.getWithIdTask(2);

        assertEquals(savedTask.getDescription(), "Changed Task", "Задачи не совпадают.");

    }

    @Test
    void addNewEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addNewEpic(epic);

        final int epicId = epic.getId();

        final Epic savedTask = taskManager.getWithIdEpics(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epicks = taskManager.getListEpic();

        assertNotNull(epicks, "Задачи на возвращаются.");
        assertEquals(1, epicks.size(), "Неверное количество задач.");
        assertEquals(epic, epicks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test updateEpic", "Test updateEpic description");
        taskManager.addNewEpic(epic);

        List<Integer> subtaskId = epic.getSubtaskIds();

        assertEquals(0, subtaskId.size(), "Новый Эпик возвращает не пустой лист Subtask");

        Subtask subtask = new Subtask("Test updateEpic", "Test updateEpic description");
        Subtask subtask2 = new Subtask("Test updateEpic", "Test updateEpic description");

        subtask.setEpicId(epic);
        subtask2.setEpicId(epic);

        int epicId = epic.getId();
        assertEquals(epicId, subtask.getEpicId(), "Неверный ID Epic.");

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        Status statusEpic = epic.getStatus();

        assertEquals(Status.NEW, statusEpic, "Неверный статус задачи.");

        subtask.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        taskManager.updateEpic(epic);

        Status SecondStatusEpic = epic.getStatus();
        assertEquals(Status.DONE, SecondStatusEpic, "Неверный статус задачи.");

        subtask.setStatus(Status.NEW);

        taskManager.updateEpic(epic);

        Status ThirdStatusEpic = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, ThirdStatusEpic, "Неверный статус задачи.");

        subtask.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);

        taskManager.updateEpic(epic);

        Status FourthStatusEpic = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, FourthStatusEpic, "Неверный статус задачи.");
    }

    @Test
    public void addSubtask() throws IOException, InterruptedException {
        Subtask task = new Subtask("Test addSubtask", "Test addSubtask description");
        taskManager.addSubtask(task);
        final int taskId = task.getId();

        final Subtask savedTask = taskManager.getWithIdSubtasks(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Subtask> tasks = taskManager.getListSubtask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }


    @Test
    void getListTask() throws IOException, InterruptedException {
        Task task = new Task("Test getListTask", "Test getListTask description");
        taskManager.addNewTask(task);

        List<Task> tasks = taskManager.getListTask();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getListEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test getListEpic", "Test getListEpic description");
        taskManager.addNewEpic(epic);

        List<Epic> tasks = taskManager.getListEpic();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getListSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Test getListSubtask",
                "Test getListSubtask description");
        taskManager.addSubtask(subtask);

        List<Subtask> tasks = taskManager.getListSubtask();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void clearTasks() throws IOException, InterruptedException {
        Task task = new Task("Test clearTasks", "Test clearTasks description");
        taskManager.addNewTask(task);
        Map<Integer, Task> tasks = taskManager.clearTasks();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    void clearEpics() throws IOException, InterruptedException {
        Epic task = new Epic("Test clearEpics", "Test clearEpics description");
        taskManager.addNewEpic(task);
        Map<Integer, Epic> tasks = taskManager.clearEpics();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    void clearSubtasks() throws IOException, InterruptedException {
        Subtask task = new Subtask("Test clearSubtasks", "Test clearSubtasks description");
        taskManager.addSubtask(task);
        Map<Integer, Subtask> tasks = taskManager.clearSubtasks();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    void getWithIdTask() throws IOException, InterruptedException {
        Task task = new Task("Test getWithIdTask", "Test getWithIdTask description");
        taskManager.addNewTask(task);
        Task task1 = taskManager.getWithIdTask(task.getId());

        assertNotNull(task1, "Задачи на возвращаются.");
        assertEquals(task, task1, "Задачи не совпадают.");
    }

    @Test
    void getWithIdEpics() throws IOException, InterruptedException {
        Epic task = new Epic("Test getWithIdEpics", "Test getWithIdEpics description");
        taskManager.addNewEpic(task);
        Epic task1 = taskManager.getWithIdEpics(task.getId());

        assertNotNull(task1, "Задачи на возвращаются.");
        assertEquals(task, task1, "Задачи не совпадают.");
    }

    @Test
    void getWithIdSubtasks() throws IOException, InterruptedException {
        Subtask task = new Subtask("Test getWithIdSubtasks",
                "Test getWithIdSubtasks description");
        taskManager.addSubtask(task);
        Subtask task1 = taskManager.getWithIdSubtasks(task.getId());

        assertNotNull(task1, "Задачи на возвращаются.");
        assertEquals(task, task1, "Задачи не совпадают.");
    }

    @Test
    void removeTask() throws IOException, InterruptedException {
        Task task = new Task("Test removeTask", "Test removeTask description");
        taskManager.addNewTask(task);

        Map<Integer, Task> tasks = taskManager.removeTask(task.getId());

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    void removeEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test removeEpic", "Test removeEpic description");
        taskManager.addNewEpic(task);

        taskManager.removeEpic(task.getId());

        Map<Integer, Epic> tasks = taskManager.getEpicsStorage();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    void removeSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test getDurationSubtasks", "Test getDurationSubtasks description");
        taskManager.addNewEpic(epic);

        Subtask task = new Subtask("Test removeSubtask", "Test removeSubtask description");
        task.setEpicId(epic);
        taskManager.addSubtask(task);

        taskManager.updateEpic(epic);

        Map<Integer, Subtask> tasks = taskManager.removeSubtask(task.getId());

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }
}