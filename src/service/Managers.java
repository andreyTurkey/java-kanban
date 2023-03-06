package service;

import API.HttpTaskManager;
import API.HttpTaskServer;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /*public static HttpTaskServer getDefaultHttpTaskServer() throws IOException, InterruptedException {
        return new HttpTaskServer();
    }*/

    public static TaskManager getDefaultFileBacked() {
        return new FileBackedTasksManager();
    }
}
