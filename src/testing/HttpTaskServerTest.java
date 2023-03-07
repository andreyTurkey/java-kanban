package testing;

import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private Gson gson = new Gson();
    private HttpClient client;
    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private final Task task1 = new Task("Task 1", "Task 1", "01.01.2023 10:00", 10);
    private final Task task2 = new Task("Task 2", "Task 2", "01.01.2023 11:00", 10);
    private final Epic epic1 = new Epic("Epic 1", "Epic 1");
    private final Subtask subtask1 = new Subtask("SubTask 1", "SubTask 1", "01.01.2023 11:00", 10);

    @BeforeEach
    public void startServer() throws IOException {
        client = HttpClient.newHttpClient();
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void handlePostTask() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task1);
        URI uri = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .header("Accept", "*/*")
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            String str = response.body();
            System.out.println(str);
            assertEquals(response.statusCode(), 201);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetTask() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task2);
        URI uriTest = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/task?id=1";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            String str = response.body();
            System.out.println(str);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleDeleteTask() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task1);
        URI uriTest = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/task?id=1";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            String str = response.body();
            System.out.println(str);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handlePostEpic() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetEpic() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8081/tasks/epic?id=1";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleDeleteEpic() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8081/tasks/epic?id=1";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handlePostSubtask() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        subtask1.setEpicId(1);

        String subtask = gson.toJson(subtask1);
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            int status = response.statusCode();
            String str = response.body();
            System.out.println(str);
            assertEquals(response.statusCode(), 201);
            if (status >= 200 && status <= 299) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetSubtask() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        subtask1.setEpicId(1);

        String subtask = gson.toJson(subtask1);
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String subtaskUrl = "http://localhost:8081/tasks/subtask?id=2";
        URI uri = URI.create(subtaskUrl);
        HttpRequest.Builder requestBuilderGetSubtask = HttpRequest.newBuilder();
        HttpRequest requestSubtask = requestBuilderGetSubtask
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerSubtask = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestSubtask, handlerSubtask);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleDeleteSubtask() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        subtask1.setEpicId(1);

        String subtask = gson.toJson(subtask1);
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String subtaskUrl = "http://localhost:8081/tasks/subtask?id=2";
        URI uri = URI.create(subtaskUrl);
        HttpRequest.Builder requestBuilderGetSubtask = HttpRequest.newBuilder();
        HttpRequest requestSubtask = requestBuilderGetSubtask
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerSubtask = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestSubtask, handlerSubtask);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleDeleteAllModels() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task1);
        URI uriTest = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/task";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetAllTask() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task1);
        URI uriTest = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/task";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetAllEpic() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8081/tasks/epic";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetAllSubtask() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        subtask1.setEpicId(1);

        String subtask = gson.toJson(subtask1);
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String subtaskUrl = "http://localhost:8081/tasks/subtask";
        URI uri = URI.create(subtaskUrl);
        HttpRequest.Builder requestBuilderGetSubtask = HttpRequest.newBuilder();
        HttpRequest requestSubtask = requestBuilderGetSubtask
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerSubtask = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestSubtask, handlerSubtask);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetAllModel() {
        String task = gson.toJson(epic1);
        URI uriTest = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        subtask1.setEpicId(1);

        String subtask = gson.toJson(subtask1);
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String subtaskUrl = "http://localhost:8081/tasks";
        URI uri = URI.create(subtaskUrl);
        HttpRequest.Builder requestBuilderGetSubtask = HttpRequest.newBuilder();
        HttpRequest requestSubtask = requestBuilderGetSubtask
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerSubtask = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestSubtask, handlerSubtask);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetHistory() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task1);
        URI uriTest = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/task?id=1";
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String urlHistory = "http://localhost:8081/tasks/history";
        URI uriHistory = URI.create(urlHistory);
        HttpRequest.Builder requestBuilderHistory = HttpRequest.newBuilder();
        HttpRequest requestHistory = requestBuilderHistory
                .GET()
                .uri(uriHistory)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerHistory = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestHistory, handlerHistory);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetPriority() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String task = gson.toJson(task1);
        URI uriTest = URI.create("http://localhost:8081/tasks/task");
        HttpRequest.Builder requestBuilderTest = HttpRequest.newBuilder();
        HttpRequest requestTest = requestBuilderTest
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .uri(uriTest)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handlerTest = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(requestTest, handlerTest);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Gson gson1 = new GsonBuilder()
                .create();
        String epic = gson1.toJson(epic1);
        URI uri = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(epic))

                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestSubtask = HttpRequest.newBuilder();

        subtask1.setEpicId(2);

        String subtask = gson.toJson(subtask1);
        HttpRequest request1 = requestSubtask
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request1, handler1);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String urlPriority = "http://localhost:8081/tasks/priority";
        URI uri1 = URI.create(urlPriority);
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request2, handler2);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleGetEpicSubtask() {
        Gson gson1 = new GsonBuilder()
                .create();
        String epic = gson1.toJson(epic1);
        URI uri = URI.create("http://localhost:8081/tasks/epic");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(epic))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String url = "http://localhost:8081/tasks/subtask";
        URI uriSubTask = URI.create(url);
        HttpRequest.Builder requestSubtask = HttpRequest.newBuilder();

        subtask1.setEpicId(1);
        Gson gson2 = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        String subtask = gson2.toJson(subtask1);
        HttpRequest request1 = requestSubtask
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .uri(uriSubTask)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request1, handler1);
            String str = response.body();
            System.out.println(str);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String urlEpicsubtask = "http://localhost:8081/tasks/epicsubtask?id=1";
        URI uri1 = URI.create(urlEpicsubtask);
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)

                .build();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request2, handler2);
            String str = response.body();
            System.out.println(str);
            assertEquals(response.statusCode(), 200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


