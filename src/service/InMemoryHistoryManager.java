package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int maxLines = 10;

    public List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == maxLines) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
