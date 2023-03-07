package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
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

public class EpicHandler {
    private TaskManager taskManager;
    private Gson gson = new Gson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void epicHandlers(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod(), httpExchange);

        switch (endpoint) {
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
            case GET_EPIC_SUBTASK: {
                handleGetEpicSubtask(httpExchange);
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

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
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

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> tasks = taskManager.getListEpic();
        if (tasks.size() != 0) {
            String tasksForGson = gson.toJson(tasks);
            writeResponse(exchange, tasksForGson, 200);
        } else {
            writeResponse(exchange, "Список эпиков пуст.", 400);
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

    private void handleDeleteEpics(HttpExchange exchange) throws IOException, InterruptedException {
        taskManager.clearEpics();
        Map<Integer, Epic> tasks = taskManager.getEpicsStorage();
        if (tasks.size() == 0) {
            writeResponse(exchange, "Задачи успешно удалены.", 200);
        } else {
            writeResponse(exchange, "Задачи не были удалены. Возникла ошибка.", 400);
        }
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

    private Endpoint getEndpoint(String requestPath, String requestMethod, HttpExchange exchange) {
        String[] pathParts = requestPath.split("/");

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

        if (pathParts.length == 3 && pathParts[2].equals("epicsubtask") && exchange.getRequestURI().getSchemeSpecificPart()
                .contains("id=")) {
            return Endpoint.GET_EPIC_SUBTASK;
        }
        return Endpoint.UNKNOWN;
    }
}


