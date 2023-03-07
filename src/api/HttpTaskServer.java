package api;

import com.google.gson.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import model.Epic;
import model.Subtask;
import model.Task;

import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

import java.net.InetSocketAddress;
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
    private HttpServer server;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext(TASKSPATH, new TasksHandler());
        taskManager = Managers.getDefault();
    }

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }

    public class TasksHandler implements HttpHandler {
        private Gson gson = new Gson();

        public TasksHandler() {
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod(), httpExchange);

            switch (endpoint) {
                case GET_ALLTASKS: {
                    handleGetAllTasks(httpExchange);
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
                case GET_HISTORY: {
                    handleGetHistory(httpExchange);
                    break;
                }
                case GET_PRIORITIZE: {
                    handleGetPrioritize(httpExchange);
                    break;
                }
                case GET_TASK_HANDLERS: {
                    TaskHandler handler = new TaskHandler(taskManager);
                    handler.taskHandlers(httpExchange);
                    break;
                }
                case GET_EPIC_HANDLERS: {
                    EpicHandler handler = new EpicHandler(taskManager);
                    handler.epicHandlers(httpExchange);
                    break;
                }
                case GET_SUBTASK_HANDLERS: {
                    SubtaskHandler handler = new SubtaskHandler(taskManager);
                    handler.subtaskHandlers(httpExchange);
                    break;
                }
                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
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

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            List<Task> tasksHistory = taskManager.getHistory();
            if (tasksHistory.isEmpty()) {
                writeResponse(exchange, "История отсутсвует.", 400);
            } else {
                String tasksForGson = gson.toJson(tasksHistory);
                writeResponse(exchange, tasksForGson, 200);
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

        private void handleGetPrioritize(HttpExchange exchange) throws IOException {
            Set<Task> prioritize = taskManager.getPrioritizedTasks();
            if (prioritize.size() != 0) {
                String tasksForGson = gson.toJson(prioritize);
                writeResponse(exchange, tasksForGson, 200);
            } else {
                writeResponse(exchange, "Список задач пустой.", 400);
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
                return Endpoint.GET_TASK_HANDLERS;
            }
            if (pathParts.length == 3 && pathParts[2].equals("task") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_TASK_HANDLERS;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.GET_TASK_HANDLERS;
                }
            }
            if ((pathParts.length == 3 && pathParts[2].equals("epic")) && !exchange.getRequestURI().getSchemeSpecificPart().contains("id=")) {
                return Endpoint.GET_EPIC_HANDLERS;
            }
            if (pathParts.length == 3 && pathParts[2].equals("epic") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_EPIC_HANDLERS;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.GET_EPIC_HANDLERS;
                }
            }
            if ((pathParts.length == 3 && pathParts[2].equals("subtask")) && !exchange.getRequestURI().getSchemeSpecificPart().contains("id=")) {
                return Endpoint.GET_SUBTASK_HANDLERS;
            }
            if (pathParts.length == 3 && pathParts[2].equals("subtask") && exchange.getRequestURI().getSchemeSpecificPart()
                    .contains("id=")) {
                if (requestMethod.equals("GET")) {
                    return Endpoint.GET_SUBTASK_HANDLERS;
                }
                if (requestMethod.equals("DELETE")) {
                    return Endpoint.GET_SUBTASK_HANDLERS;
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
                return Endpoint.GET_EPIC_HANDLERS;
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
