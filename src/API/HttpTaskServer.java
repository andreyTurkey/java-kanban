package API;

import com.google.gson.*;
import com.google.gson.JsonSyntaxException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import model.Epic;
import model.Subtask;
import model.Task;

import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class HttpTaskServer {
    private static final int PORT = 8081;
    private static final String TASKSPATH = "/tasks";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private TaskManager taskManager = Managers.getDefault();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext(TASKSPATH, new TasksHandler());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        new HttpTaskServer().start();
    }

    public class TasksHandler implements HttpHandler {
        //private TaskManager taskManager = Managers.getDefault();
        //private TaskManager taskManager = HttpTaskManager.loadFromServer(loadUri);

        private Gson gson = new Gson();

        public TasksHandler() {
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod(), httpExchange);

            switch (endpoint) {
                case GET_ALLTASKS: {
                    handleGetAllTasks(httpExchange);
                    handleGetPrioritize(httpExchange);
                    break;
                }
                case DELETE_ALLTASKS: {
                    try {
                        handleDeleteAllTasks(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_TASK: {
                    handleGetTask(httpExchange);
                    break;
                }
                case POST_TASK: {
                    try {
                        handlePostTask(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_TASK: {
                    try {
                        handleDeleteTask(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_TASKS: {
                    handleGetTasks(httpExchange);
                    break;
                }
                case DELETE_TASKS: {
                    try {
                        handleDeleteTasks(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_EPICS: {
                    handleGetEpics(httpExchange);
                    break;
                }
                case GET_EPIC: {
                    handleGetEpic(httpExchange);
                    break;
                }
                case POST_EPIC: {
                    try {
                        handlePostEpic(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_EPIC: {
                    try {
                        handleDeleteEpic(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_ALLEPICS: {
                    try {
                        handleDeleteEpics(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_SUBTASKS: {
                    handleGetSubtasks(httpExchange);
                    break;
                }
                case DELETE_ALLSUBTASKS: {
                    try {
                        handleDeleteSubstasks(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_SUBTASK: {
                    handleGetSubtask(httpExchange);
                    break;
                }
                case POST_SUBTASK: {
                    try {
                        handlePostSubtask(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_SUBTASK: {
                    try {
                        handleDeleteSubtask(httpExchange);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case GET_HISTORY: {
                    handleGetHistory(httpExchange);
                    break;
                }
                case GET_PRIORITIZE: {
                    handleGetPrioritize(httpExchange);
                    break;
                }
                case GET_EPICSUBTASK: {
                    handleGetEpicSubtask(httpExchange);
                    break;
                }
                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        private void handleGetPrioritize(HttpExchange exchange) throws IOException {
            Set<Task> prioritize = taskManager.getPrioritizedTasks();
            if (prioritize.size() != 0) {
                String tasksForGson = gson.toJson(prioritize);
                writeResponse(exchange, tasksForGson, 200);
            } else {
                writeResponse(exchange, "Список задач пустой.", 400);
            }
        }

        private void handleGetAllTasks(HttpExchange exchange) throws IOException {
            String tasksForGson = "";
            String mistake = "Список задач пустой.";
            List<Task> allTasks = new ArrayList<>();
            List<Task> tasks = taskManager.getListTask();
            List<Epic> epics = taskManager.getListEpic();
            List<Subtask> subtask = taskManager.getListSubtask();
            allTasks.addAll(tasks);
            allTasks.addAll(epics);
            allTasks.addAll(subtask);

            if (allTasks.size() != 0) {
                tasksForGson = gson.toJson(allTasks);
                writeResponse(exchange, tasksForGson, 200);
            } else {
                writeResponse(exchange, mistake, 400);
            }
        }

        private int idFromUrl(HttpExchange exchange) {
            String requestPath = exchange.getRequestURI().getSchemeSpecificPart();
            String[] partsParametrs = requestPath.split("=");
            int id = Integer.parseInt(partsParametrs[1]);
            return id;
        }

        private void handleGetTask(HttpExchange exchange) throws IOException {
            int id = idFromUrl(exchange);
            try {
                Task task = taskManager.getWithIdTask(id);
                writeResponse(exchange, gson.toJson(task), 200);
            } catch (InterruptedException | IllegalStateException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void handleGetEpic(HttpExchange exchange) throws IOException {
            int id = idFromUrl(exchange);
            try {
                Epic task = taskManager.getWithIdEpics(id);
                writeResponse(exchange, gson.toJson(task), 200);
            } catch (InterruptedException | IllegalStateException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void handleGetSubtask(HttpExchange exchange) throws IOException {
            int id = idFromUrl(exchange);
            try {
                Subtask task = taskManager.getWithIdSubtasks(id);
                writeResponse(exchange, gson.toJson(task), 200);
            } catch (InterruptedException | IllegalStateException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void handleGetTasks(HttpExchange exchange) throws IOException {
            String tasksForGson = "";
            String mistake = "Список задач пуст.";
            List<Task> tasks = taskManager.getListTask();

            if (tasks.size() != 0) {
                tasksForGson = gson.toJson(tasks);
                writeResponse(exchange, tasksForGson, 200);
            } else {
                writeResponse(exchange, mistake, 400);
            }
        }

        private void handleDeleteTask(HttpExchange exchange) throws IOException, InterruptedException {
            int id = idFromUrl(exchange);
            if (taskManager.getTasksStorages().containsKey(id)) {
                taskManager.removeTask(id);
                writeResponse(exchange, "Задача удалена.", 200);
            } else if (!taskManager.getTasksStorages().containsKey(id)) {
                writeResponse(exchange, String.format("Задачи с номером ID = %d не существует.", id), 400);
            } else {
                writeResponse(exchange, "Ошибка. Не удалось удалить задачу.", 400);
            }
        }

        private void handleDeleteEpic(HttpExchange exchange) throws IOException, InterruptedException {
            int id = idFromUrl(exchange);
            if (taskManager.getEpicsStorage().containsKey(id)) {
                taskManager.removeEpic(id);
                writeResponse(exchange, "Задача удалена.", 200);
            } else if (!taskManager.getEpicsStorage().containsKey(id)) {
                writeResponse(exchange, String.format("Задачи с номером ID = %d не существует.", id), 400);
            } else {
                writeResponse(exchange, "Ошибка. Не удалось удалить задачу.", 400);
            }
        }

        private void handleDeleteSubtask(HttpExchange exchange) throws IOException, InterruptedException {
            int id = idFromUrl(exchange);
            if (taskManager.getSubtaksStorage().containsKey(id)) {
                taskManager.removeSubtask(id);
                writeResponse(exchange, "Задача удалена.", 200);
            } else if (!taskManager.getSubtaksStorage().containsKey(id)) {
                writeResponse(exchange, String.format("Задачи с номером ID = %d не существует.", id), 400);
            } else {
                writeResponse(exchange, "Ошибка. Не удалось удалить задачу.", 400);
            }
        }

        private void handleGetEpics(HttpExchange exchange) throws IOException {
            List<Epic> tasks = taskManager.getListEpic();
            if (tasks.size() != 0) {
                String tasksForGson = gson.toJson(tasks);
                writeResponse(exchange, tasksForGson, 200);
            } else {
                writeResponse(exchange, "Список эпиков пуст.", 400);
            }
        }

        private void handleGetSubtasks(HttpExchange exchange) throws IOException {
            List<Subtask> tasks = taskManager.getListSubtask();
            if (tasks.size() != 0) {
                String tasksForGson = gson.toJson(tasks);
                writeResponse(exchange, tasksForGson, 200);
            } else {
                writeResponse(exchange, "Список подзадач пуст.", 400);
            }
        }

        private void handleDeleteAllTasks(HttpExchange exchange) throws IOException, InterruptedException {
            Map<Integer, Task> tasks = taskManager.clearTasks();
            Map<Integer, Epic> epics = taskManager.clearEpics();
            Map<Integer, Subtask> subtasks = taskManager.clearSubtasks();
            if (tasks.size() == 0 && epics.size() == 0 && subtasks.size() == 0) {
                writeResponse(exchange, "Все задачи успешно удалены.", 200);
            } else {
                writeResponse(exchange, "Все задачи не были удалены. Возникла ошибка.", 400);
            }
        }

        private void handleDeleteTasks(HttpExchange exchange) throws IOException, InterruptedException {
            taskManager.clearTasks();
            Map<Integer, Task> tasks = taskManager.getTasksStorages();
            if (tasks.size() == 0) {
                writeResponse(exchange, "Задачи успешно удалены.", 200);
            } else {
                writeResponse(exchange, "Задачи не были удалены. Возникла ошибка.", 400);
            }
        }

        private void handleDeleteEpics(HttpExchange exchange) throws IOException, InterruptedException {
            taskManager.clearEpics();
            Map<Integer, Epic> tasks = taskManager.getEpicsStorage();
            if (tasks.size() == 0) {
                writeResponse(exchange, "Задачи успешно удалены.", 200);
            } else {
                writeResponse(exchange, "Задачи не были удалены. Возникла ошибка.", 400);
            }
        }

        private void handleDeleteSubstasks(HttpExchange exchange) throws IOException, InterruptedException {
            taskManager.clearSubtasks();
            Map<Integer, Subtask> tasks = taskManager.getSubtaksStorage();
            if (tasks.size() == 0) {
                writeResponse(exchange, "Задачи успешно удалены.", 200);
            } else {
                writeResponse(exchange, "Задачи не были удалены. Возникла ошибка.", 400);
            }
        }

        private void handlePostTask(HttpExchange exchange) throws IOException, InterruptedException {
            Task task;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes());
            try {
                task = gson.fromJson(body, Task.class);
            } catch (JsonSyntaxException exc) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            } catch (RuntimeException exc) {
                writeResponse(exchange, exc.getMessage(), 400);
                return;
            }
            if (task.getName().equals("") || task.getDescription().equals("")) {
                writeResponse(exchange, "Поля задачи не могут быть пустыми", 400);
                return;
            }
            if (task.getId() == 0) {
                try {
                    taskManager.addNewTask(task);
                } catch (IllegalStateException exc) {
                    writeResponse(exchange, "Ошибка добавления задачи в методе addNewTask(task).", 400);
                    return;
                }
            } else if (task.getId() != 0) {
                try {
                    taskManager.updateTask(task);
                } catch (IllegalStateException exc) {
                    writeResponse(exchange, "Ошибка обновления задачи в методе updateTask(task).", 400);
                    return;
                }
            } else {
                taskManager.updateTask(task);
            }
            int id = task.getId();
            writeResponse(exchange, "Задача добавлена " + id, 201);
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException, InterruptedException {
            Epic task;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes());
            try {
                task = gson.fromJson(body, Epic.class);
                //writeResponse(exchange, "Задача добавлена", 200);
            } catch (JsonSyntaxException exc) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            if (task.getName().equals("") || task.getDescription().equals("")) {
                writeResponse(exchange, "Поля задачи не могут быть пустыми", 400);
                return;
            }
            if (task.getId() == 0) {
                taskManager.addNewEpic(task);
            } else {
                taskManager.updateEpic(task);
            }
            int id = task.getId();
            writeResponse(exchange, "Задача добавлена " + id, 201);
        }

        private void handleGetEpicSubtask(HttpExchange exchange) throws IOException {
            int id = idFromUrl(exchange);
            try {
                Epic epic = taskManager.getWithIdEpics(id);
                List<Subtask> epicSubtask = epic.getSubtasks();
                if (!epicSubtask.isEmpty()) {
                    String tasksForGson = gson.toJson(epicSubtask);
                    writeResponse(exchange, tasksForGson, 200);
                } else {
                    writeResponse(exchange, "Список подзадач эпика пустой.", 400);
                }
            } catch (InterruptedException | IllegalStateException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void handlePostSubtask(HttpExchange exchange) throws IOException, InterruptedException {
            Subtask task;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes());
            try {
                task = gson.fromJson(body, Subtask.class);
            } catch (JsonSyntaxException exc) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            if (task.getName().equals("") || task.getDescription().equals("")) {
                writeResponse(exchange, "Поля задачи не могут быть пустыми", 400);
                return;
            }
            int id = task.getEpicIdOrZero();
            if (!taskManager.getEpicsStorage().containsKey(id)) {
                writeResponse(exchange, "Подзадача не может быть добавлена. Эпика не существует", 400);
                return;
            }
            try {
                taskManager.addSubtask(task);
            } catch (IllegalStateException exc) {
                writeResponse(exchange, exc.getMessage(), 400);
                return;
            }
            writeResponse(exchange, "Задача добавлена", 201);
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            List<Task> tasksHistory = taskManager.getHistory();
            if (tasksHistory.isEmpty()) {
                writeResponse(exchange, "История отсутсвует.", 400);
            } else {
                String tasksForGson = gson.toJson(tasksHistory);
                writeResponse(exchange, tasksForGson, 200);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod, HttpExchange exchange) {
            String[] pathParts = requestPath.split("/");
            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_ALLTASKS;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_ALLTASKS;
                }
            }
            if ((pathParts.length == 3 && pathParts[2].equals("task")) && !exchange.getRequestURI().getSchemeSpecificPart().contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_TASKS;
                }
                if (requestMethod.equals("POST")) {
                    return Endpoint.POST_TASK;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_TASKS;
                }
            }
            if (pathParts.length == 3 && pathParts[2].equals("task") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_TASK;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_TASK;
                }
            }
            if ((pathParts.length == 3 && pathParts[2].equals("epic")) && !exchange.getRequestURI().getSchemeSpecificPart().contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_EPICS;
                }
                if (requestMethod.equals("POST")) {
                    return Endpoint.POST_EPIC;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_ALLEPICS;
                }
            }
            if (pathParts.length == 3 && pathParts[2].equals("epic") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_EPIC;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_EPIC;
                }
            }
            if ((pathParts.length == 3 && pathParts[2].equals("subtask")) && !exchange.getRequestURI().getSchemeSpecificPart().contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_SUBTASKS;
                }
                if (requestMethod.equals("POST")) {
                    return Endpoint.POST_SUBTASK;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_ALLSUBTASKS;
                }
            }
            if (pathParts.length == 3 && pathParts[2].equals("subtask") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_SUBTASK;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.DELETE_SUBTASK;
                }
            }
            if (pathParts.length == 3 && pathParts[2].equals("history")) {
                return Endpoint.GET_HISTORY;
            }
            if (pathParts.length == 3 && pathParts[2].equals("priority")) {
                return Endpoint.GET_PRIORITIZE;
            }
            if (pathParts.length == 3 && pathParts[2].equals("epicsubtask") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                return Endpoint.GET_EPICSUBTASK;
            }
            return Endpoint.UNKNOWN;
        }

    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Останавливаем сервер на порту " + PORT);
        server.stop(1);
    }
}
