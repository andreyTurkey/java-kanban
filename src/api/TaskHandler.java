package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TaskHandler {
    private TaskManager taskManager;
    private Gson gson = new Gson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void taskHandlers(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod(), httpExchange);

        switch (endpoint) {
            case GET_TASKS: {
                handleGetTasks(httpExchange);
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
            case DELETE_TASKS: {
                try {
                    handleDeleteTasks(httpExchange);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            default:
                writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
        }
    }

    private int idFromUrl(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getSchemeSpecificPart();
        String[] partsParametrs = requestPath.split("=");
        int id = Integer.parseInt(partsParametrs[1]);
        return id;
    }

    public void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
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

    private void handleGetTask(HttpExchange exchange) throws IOException {
        int id = idFromUrl(exchange);
        try {
            Task task = taskManager.getWithIdTask(id);
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

    private void handleDeleteTasks(HttpExchange exchange) throws IOException, InterruptedException {
        taskManager.clearTasks();
        Map<Integer, Task> tasks = taskManager.getTasksStorages();
        if (tasks.size() == 0) {
            writeResponse(exchange, "Задачи успешно удалены.", 200);
        } else {
            writeResponse(exchange, "Задачи не были удалены. Возникла ошибка.", 400);
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
        return Endpoint.UNKNOWN;
    }
}
