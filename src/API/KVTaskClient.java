package API;

import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private long apiKey;

    public KVTaskClient(String uri) {
        client = HttpClient.newHttpClient();
        apiKey = 100; //  apiKey = getApiKey(uri);
    }

    public void put(String saveUri, String json) { // Отправляет текущее состояние менеджера
        String url = saveUri + "/" + apiKey + "?API_TOKEN=" + apiKey;
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            int status = response.statusCode();
            if (status >= 200 && status <= 299) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
                return;
            }
            if (status >= 400 && status <= 499) {
                String str = response.body();
                System.out.println("Сервер сообщил о проблеме с запросом. Код состояния: " + status + " " + str);
                return;
            }
            if (status >= 500 && status <= 599) {
                System.out.println("Сервер сообщил о внутренней проблеме и невозможности обработать запрос." +
                        " Код состояния: " + status);
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String loadUri) {         // Получает состояние менеджера
        String url = loadUri + "/" + apiKey + "?API_TOKEN=" + apiKey;
        URI uri = URI.create(url);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        String json = "";
        try {
            HttpResponse<String> response = client.send(request, handler);
            int status = response.statusCode();
            if (status >= 200 && status <= 299) {
                System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
                json = response.body();
            }
            if (status >= 400 && status <= 499) {
                String str = response.body();
                System.out.println("Сервер сообщил о проблеме с запросом. Код состояния: " + status + " " + str);
            }
            if (status >= 500 && status <= 599) {
                System.out.println("Сервер сообщил о внутренней проблеме и невозможности обработать запрос." +
                        " Код состояния: " + status);
            }
        } catch (IOException | InterruptedException e) {
            // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            e.printStackTrace();
        }
        return json;
    }

    private long getApiKey(String adress) {
        URI url = URI.create(adress);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        long newApi = 0;
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                System.out.println("Тело ответа: " + response.body());
                newApi = jsonElement.getAsLong();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return newApi;
    }
}


