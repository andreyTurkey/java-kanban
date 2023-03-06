package API;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import model.*;
import service.FileBackedTasksManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private static final String uri = "http://localhost:8080/register";
    private static final String loadUri = "http://localhost:8080/load";
    private final String saveUri = "http://localhost:8080/save";
    private static KVTaskClient kvTaskClient = new KVTaskClient(uri);

    public static void main(String[] args) throws IOException, InterruptedException {

        TaskManager taskManager = Managers.getDefault();

        taskManager.addNewTask(new Task("Задача один", "Описание", "10.01.2023 10:00", 30));
        taskManager.addNewTask(new Task("Задача Два", "Описание", "10.01.2023 10:30", 30));
        taskManager.addNewTask(new Task("Задача Три", "Описание"));

        Epic epic1 = new Epic("Эпик 1", "Эпик 1");
        taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Эпик 2");
        taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаска 1", "Сабтаска 1");
        subtask1.setEpicId(epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаска 2", "Сабтаска 2");
        subtask2.setEpicId(epic1);
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаска 3", "Сабтаска 3", "10.01.2023 09:50", 10);
        subtask3.setEpicId(epic2);
        taskManager.addSubtask(subtask3);

        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdTask(3);
        taskManager.getWithIdEpics(4);
        taskManager.getWithIdEpics(5);

        /*TaskManager newTaskManager = HttpTaskManager.loadFromServer(loadUri);
        List<Task> tasks = newTaskManager.getListTask();
        for (Task task : tasks) {
            System.out.println(task);
        }
        List<Epic> epics = newTaskManager.getListEpic();
        for (Epic task : epics) {
            System.out.println(task);
        }
        List<Subtask> subtasks = newTaskManager.getListSubtask();
        for (Subtask task : subtasks) {
            System.out.println(task);
        }
        System.out.println();
        Set<Task> prioritize = newTaskManager.getPrioritizedTasks();
        for (Task task : prioritize) {
            System.out.println(task);
        }
        System.out.println();
        newTaskManager.printHistory();*/
    }

    public static HttpTaskManager loadFromServer(String loadUri) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager();
        String jsonData = httpTaskManager.kvTaskClient.load(loadUri);
        if (jsonData.length() != 0) {
            Gson gson = new Gson();
            DataForJson savedData = gson.fromJson(jsonData, DataForJson.class);
            String[] tasks = savedData.getTasks();
            String history = savedData.getHistory();
            List<String> tasksFromServer = new ArrayList<>();
            for (int i = 0; i < tasks.length; i++) {
                String task = tasks[i];
                tasksFromServer.add(task);
            }
            httpTaskManager.makingTasks(tasksFromServer);
            List<Integer> historyFromFile = historyFromString(history);
            httpTaskManager.recoveryHistory(historyFromFile);
            List<Task> listTasks = httpTaskManager.getListTask();
            List<Epic> listEpics = httpTaskManager.getListEpic();
            List<Subtask> listSubtaks = httpTaskManager.getListSubtask();
            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(listTasks);
            allTasks.addAll(listEpics);
            allTasks.addAll(listSubtaks);
            Set<Task> prioritizedTasks = httpTaskManager.getPrioritizedTasks();
            for (Task task : allTasks) {
                prioritizedTasks.add(task);
            }
        }
        return httpTaskManager;
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        List<Task> taskToServer = getListTask();
        List<Epic> epicToServer = getListEpic();
        List<Subtask> subtaskToServer = getListSubtask();
        List<String> stringsTasks = new ArrayList<>();
        for (Task task : taskToServer) {
            String taskToString = task.toStringForFile(task);
            stringsTasks.add(taskToString);
        }
        for (Epic task : epicToServer) {
            String taskToString = toStringEpic(task);
            stringsTasks.add(taskToString);
        }
        for (Subtask task : subtaskToServer) {
            String taskToString = toStringSubtask(task);
            stringsTasks.add(taskToString);
        }
        Set<Task> prioritize = getPrioritizedTasks();
        List<String> stringsPrioritize = new ArrayList<>();
        for (Task task : prioritize) {
            String taskToString = task.toStringForFile(task);
            stringsPrioritize.add(taskToString);
        }
        String history = historyToString(inMemoryHistoryManager);
        DataForJson dataForJson = new DataForJson(stringsTasks, history, stringsPrioritize);
        String jsonTaskToHistory = gson.toJson(dataForJson, DataForJson.class);
        kvTaskClient.put(saveUri, jsonTaskToHistory);
    }
}
