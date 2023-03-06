package testing;

import API.HttpTaskManager;
import API.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static final String loadUri = "http://localhost:8080/load";

    KVServer kvServer = new KVServer();

    public HttpTaskManagerTest() throws IOException {
    }

    @Override
    void setTaskManager() {
        taskManager = new HttpTaskManager();
    }

    @BeforeEach
    public void startServer() {
        kvServer.start();
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }

    @Test
    public void loadFromServer() throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = HttpTaskManager.loadFromServer(loadUri);

        Task task = new Task("Задача Один", "Описание");
        httpTaskManager.addNewTask(task);
        Epic epic = new Epic("Эпик 1", "Эпик 1");
        httpTaskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1", "01.01.2023 09:00", 60);
        subtask1.setEpicId(epic);
        httpTaskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2", "01.01.2023 10:00", 60);
        subtask2.setEpicId(epic);
        httpTaskManager.addSubtask(subtask2);

        httpTaskManager.getWithIdEpics(2);
        httpTaskManager.getWithIdTask(1);
        httpTaskManager.getWithIdSubtasks(4);
        httpTaskManager.getWithIdSubtasks(4);
        httpTaskManager.getWithIdSubtasks(3);
        httpTaskManager.getWithIdSubtasks(4);

        HttpTaskManager.loadFromServer(loadUri);

        List<Task> allTask = httpTaskManager.getAllTasks();

        assertEquals(4, allTask.size(), "Количество задач не совпадает с файлом.");

        String str = httpTaskManager.stringHistory();

        assertEquals("2,1,3,4", str, "Количество задач не совпадает с файлом.");
    }

    @Test
    public void save() throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager();

        Task task = new Task("Задача Один", "Описание");
        httpTaskManager.addNewTask(task);
        Epic epic = new Epic("Эпик 1", "Эпик 1");
        httpTaskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1", "01.01.2023 09:00", 60);
        subtask1.setEpicId(epic);
        httpTaskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2", "01.01.2023 10:01", 60);
        subtask2.setEpicId(epic);
        httpTaskManager.addSubtask(subtask2);

        httpTaskManager.getWithIdEpics(2);
        httpTaskManager.getWithIdTask(1);
        httpTaskManager.getWithIdSubtasks(4);
        httpTaskManager.getWithIdSubtasks(4);
        httpTaskManager.getWithIdSubtasks(3);
        httpTaskManager.getWithIdSubtasks(4);

        HttpTaskManager.loadFromServer(loadUri);

        List<Task> allTask = httpTaskManager.getAllTasks();

        assertEquals(4, allTask.size(), "Количество задач не совпадает с файлом.");

        String str = httpTaskManager.stringHistory();

        assertEquals("2,1,3,4", str, "Количество задач не совпадает с файлом.");
    }
}
