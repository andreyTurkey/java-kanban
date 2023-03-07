package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import model.Subtask;

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

public class SubtaskHandler {
    private TaskManager taskManager;
    private Gson gson = new Gson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void subtaskHandlers(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod(), httpExchange);

        switch (endpoint) {
            case GET_SUBTASKS: {
                handleGetSubtasks(httpExchange);
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
            case DELETE_ALLSUBTASKS: {
                try {
                    handleDeleteSubstasks(httpExchange);
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

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        int id = idFromUrl(exchange);
        try {
            Subtask task = taskManager.getWithIdSubtasks(id);
            writeResponse(exchange, gson.toJson(task), 200);
        } catch (InterruptedException | IllegalStateException e) {
            writeResponse(exchange, e.getMessage(), 400);
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

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> tasks = taskManager.getListSubtask();
        if (tasks.size() != 0) {
            String tasksForGson = gson.toJson(tasks);
            writeResponse(exchange, tasksForGson, 200);
        } else {
            writeResponse(exchange, "Список подзадач пуст.", 400);
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

    private Endpoint getEndpoint(String requestPath, String requestMethod, HttpExchange exchange) {
        String[] pathParts = requestPath.split("/");
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
        return Endpoint.UNKNOWN;
    }
}
